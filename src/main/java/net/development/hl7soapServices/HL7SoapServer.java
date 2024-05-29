package net.development.hl7soapServices;

import jakarta.xml.ws.Endpoint;
import jakarta.xml.ws.soap.SOAPBinding;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HL7SoapServer {
    private static final Logger logger = LoggerFactory.getLogger(HL7SoapServer.class);
    private static final String HL7_SOAP_HOST = System.getenv("MY_POD_IP");
//    private static final String HL7_SOAP_HOST = "192.168.0.17";
    private static final int HL7_SOAP_PORT = 8080;
    private static final int ENDPOINT_PUBLISHED_PORT = 8090;

    public static void main(String[] args) {
        try {
            // Create Jetty server
            logger.info("Creating Jetty server...");
            Server server = new Server();

            // Create ServerConnector with the desired host and port
            logger.info("Creating ServerConnector...");
            ServerConnector connector = new ServerConnector(server);
            connector.setHost(HL7_SOAP_HOST);
            connector.setPort(HL7_SOAP_PORT);

            // Add the ServerConnector to the server
            logger.info("Adding ServerConnector to the server...");
            server.addConnector(connector);

            // Create and map the servlet context
            logger.info("Creating servlet context...");
            ServletContextHandler context = new ServletContextHandler();
            context.setContextPath("/");
            server.setHandler(context);

            // Start the Jetty server
            logger.info("Starting the Jetty server...");
            server.start();

            // Create and publish the SOAP endpoint with SOAP 1.2 binding
            logger.info("Creating and publishing SOAP endpoint...");
            HL7SoapServiceImplementation serviceImpl = new HL7SoapServiceImplementation();
            Endpoint endpoint = Endpoint.create(SOAPBinding.SOAP12HTTP_BINDING, serviceImpl);
            endpoint.publish("http://"+HL7_SOAP_HOST+":"+ENDPOINT_PUBLISHED_PORT+"/services/hl7-soap");
            logger.info("SOAP endpoint published at http://"+HL7_SOAP_HOST+":"+ENDPOINT_PUBLISHED_PORT+"/services/hl7-soap");

            // Join the server thread to keep the server running
            logger.info("Joining the server thread...");
            server.join();
        } catch (Exception e) {
            logger.error("Error starting HL7 SOAP service: {}", e.getMessage(), e);
        }
    }
}
