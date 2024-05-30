package net.development.hl7soapServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HL7MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(HL7MessageHandler.class);

    public String generateACKMessage(String hl7Message) {
        DateFormatter hl7DateTime = new DateFormatter();

        // Ensure hl7Message is not null or empty
        if (hl7Message == null || hl7Message.isEmpty()) {
            logger.error("Received an empty or null HL7 message.");
            throw new IllegalArgumentException("HL7 message cannot be null or empty.");
        }
        
        // Extract the MSH segment and split it into fields
        String[] segments = hl7Message.split("\r");
        String mshSegment = segments[0];
        String[] mshFields = mshSegment.split("\\|");

        // Validate that we have at least the required fields
        if (mshFields.length < 11) {
            logger.error("Invalid MSH segment: {}", mshSegment);
            throw new IllegalArgumentException("MSH segment does not contain enough fields");
        }

        // Construct the ACK message
        StringBuilder ackMessage = new StringBuilder();
        ackMessage.append((char) 0x0B); // Start of block
        ackMessage.append("MSH|^~\\&|").append(mshFields[3]).append("|").append(mshFields[4])
                   .append("|").append(mshFields[5]).append("||").append(hl7DateTime.hl7AckTimeFormat())
                   .append("||ACK^").append(mshFields[8]).append("|").append(mshFields[9])
                   .append("|P|2.3|\r")
                   .append("MSA|AA|").append(mshFields[9]).append("\r");
        ackMessage.append((char) 0x1C); // End of block
        ackMessage.append((char) 0x0D); // Carriage return

        return ackMessage.toString();
    }
}
