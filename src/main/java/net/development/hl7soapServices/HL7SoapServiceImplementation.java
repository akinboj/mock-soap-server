package net.development.hl7soapServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public class HL7SoapServiceImplementation {
    private static final Logger logger = LoggerFactory.getLogger(HL7SoapServiceImplementation.class);

    @WebMethod
    public String receiveHL7Message(String hl7Message) {
        HL7MessageHandler messageHandler = new HL7MessageHandler();
        try {
            logger.info("Received HL7 message: {}", hl7Message);

            // Construct the MLLP-wrapped ACK message
            String ackMessage = messageHandler.generateACKMessage(hl7Message);

            // Log the ACK message as a whole
            String logAckMessage = ackMessage.replace((char) 0x0B, '\n')
                                             .replace((char) 0x1C, '\n')
                                             .replace("\r", "\n");
            logger.info("=**=>Sent ACK message to client:\n{}", logAckMessage);

            // Replace \r with actual new line characters for proper display
            String formattedAckMessage = ackMessage.replace("\r", "\n");

            return formattedAckMessage; // Return the formatted ACK message to the client
        } catch (Exception e) {
            logger.error("Error processing HL7 message: {}", e.getMessage(), e);
            return "Error processing HL7 message: " + e.getMessage();
        }
    }
}
