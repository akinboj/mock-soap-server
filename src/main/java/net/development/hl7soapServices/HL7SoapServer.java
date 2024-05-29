package net.development.hl7soapServices;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.http.HTTPTransportFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HL7SoapServer {
    private static final Logger logger = LoggerFactory.getLogger(HL7SoapServer.class);

    public static void main(String[] args) {
        try {
            // Create Jetty server
            logger.info("Creating Jetty server...");
            Server server = new Server();

            // Create ServerConnector with the desired host and port
            logger.info("Creating ServerConnector...");
            ServerConnector connector = new ServerConnector(server);
            connector.setHost("192.168.0.17");
            connector.setPort(8080);

            // Add the ServerConnector to the server
            logger.info("Adding ServerConnector to the server...");
            server.addConnector(connector);

            // Create CXF servlet without Spring
            logger.info("Creating CXFNonSpringServlet...");
            CXFNonSpringServlet cxfServlet = new CXFNonSpringServlet();

            // Register and map the CXF servlet
            logger.info("Registering and mapping the CXF servlet...");
            ServletHolder servletHolder = new ServletHolder(cxfServlet);
            ServletContextHandler context = new ServletContextHandler();
            context.setContextPath("/");
            context.addServlet(servletHolder, "/*");
            server.setHandler(context);

            // Start the Jetty server
            logger.info("Starting the Jetty server...");
            server.start();

            // Initialize the CXF bus
            logger.info("Initializing CXF bus...");
            Bus bus = BusFactory.getDefaultBus();
            logger.info("CXF bus initialized.");

            // Configure the HTTP transport
            logger.info("Configuring HTTP transport...");
            HTTPTransportFactory transportFactory = new HTTPTransportFactory();
            bus.setExtension(transportFactory, HTTPTransportFactory.class);
            logger.info("HTTP transport configured.");

            // Create and publish the SOAP endpoint
            logger.info("Creating and publishing SOAP endpoint...");
            HL7SoapServiceImplementation serviceImpl = new HL7SoapServiceImplementation();
            EndpointImpl endpoint = (EndpointImpl) Endpoint.create(serviceImpl);
            endpoint.setBus(bus);
            endpoint.publish("/hl7-soap-service");
            logger.info("SOAP endpoint published.");

            logger.info("HL7 SOAP service started at http://192.168.0.17:8080/hl7-soap-service");

            // Join the server thread to keep the server running
            logger.info("Joining the server thread...");
            server.join();
        } catch (Exception e) {
            logger.error("Error starting HL7 SOAP service: {}", e.getMessage(), e);
        }
    }
}
