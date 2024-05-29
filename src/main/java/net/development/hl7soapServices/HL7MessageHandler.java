package net.development.hl7soapServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HL7MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(HL7MessageHandler.class);

    public String processHL7Message(String hl7Message) {
        try {
            // Log the message for now
            logger.info("Processing HL7 message: {}", hl7Message);
            // Generate a simple ACK response
            String ackResponse = "ACK for message: " + hl7Message.substring(0, Math.min(50, hl7Message.length())) + "...";
            logger.info("Generated ACK response: {}", ackResponse);
            return ackResponse;
        } catch (Exception e) {
            logger.error("Error processing HL7 message: {}", e.getMessage(), e);
            return "Error processing HL7 message: " + e.getMessage();
        }
    }
}
