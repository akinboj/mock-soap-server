package net.development.hl7soapServices;

import javax.jws.WebMethod;
import javax.jws.WebService;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebService
public class HL7SoapServiceImplementation {
    private static final Logger logger = LoggerFactory.getLogger(HL7SoapServiceImplementation.class);

    @WebMethod
    public String receiveHL7Message(String messageForm) {
        HL7MessageHandler messageHandler = new HL7MessageHandler();
        try {
            // Unescape HTML entities to get the original HL7 message
            String hl7Message = StringEscapeUtils.unescapeHtml4(messageForm);
            logger.info("Received HL7 message: {}", hl7Message);
            
            String ackResponse = messageHandler.processHL7Message(hl7Message);
            logger.info("Sent ACK response: {}", ackResponse);
            
            // Escape the ACK response for XML compliance
            return StringEscapeUtils.escapeHtml4(ackResponse);
        } catch (Exception e) {
            logger.error("Error processing HL7 message: {}", e.getMessage(), e);
            return "Error processing HL7 message: " + e.getMessage();
        }
    }
}
