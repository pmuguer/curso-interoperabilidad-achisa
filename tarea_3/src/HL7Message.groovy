// Se importan las clases necesarias para generar los mensajes
import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.v25.segment.PID
import ca.uhn.hl7v2.model.v25.segment.PV1
import ca.uhn.hl7v2.parser.Parser

import ca.uhn.hl7v2.model.AbstractMessage

class HL7Message {
    AbstractMessage msg

    def initMSH() {
        // Inicialización del header, común para todos los mensajes
        def mshSegment = this.msg.getMSH()
        mshSegment.getFieldSeparator().setValue("|")
        mshSegment.getEncodingCharacters().setValue("^~\\&")
        
        // Se indica que la versión de HL7 es 2.3.1 (la requerida por DCM4CHEE 2)
        mshSegment.getVersionID().getVersionID().setValue("2.3.1")
        // Indico que se usa el charset Latin-1 (ISO 8859/1)
        mshSegment.getCharacterSet(0).setValue("8859/1")

        // Indico el id de procesamiento (this field is used to decide whether to process the
        // message as defined in HL7 Application (level 7) Processing rules)
        // Represents an HL7 PT (Processing Type) data type. This type consists
        // of the following components:
        //
        //  * Processing ID (ID)
        //  * Processing Mode (ID)

        // Processing ID ("T" corresponde a "training")
        // http://hl7-definition.caristix.com:9010/Default.aspx?version=HL7%20v2.5.1&table=0103
        mshSegment.getProcessingID().getProcessingID().setValue("T")
        
        // Processing Mode ("T" corresponde a "Current processing")
        // http://hl7-definition.caristix.com:9010/Default.aspx?version=HL7%20v2.5.1&table=0207
        mshSegment.getProcessingID().getProcessingMode().setValue("T")
    }



    def er7Encode() {
        HapiContext context = new DefaultHapiContext();
        Parser parser = context.getPipeParser();
        String er7EncodedMessage = parser.encode(this.msg);
        return er7EncodedMessage
    }

    def setMessageControlID(messageControlID) {
        // messageControlID: string con id que identificará el mensaje
        this.msg.getMSH().getMessageControlID().setValue(messageControlID.toString())
    }

    def getMessageControlID() {
        return this.msg.getMSH().getMessageControlID().toString()
    }

    def setDateTimeOfMessage(dateTimeOfMessage) {
        // Registro el timestamp del mensaje
        // dateTimeOfMessage: string en formato "YYYYMMDDHHMMSS"
        this.msg.getMSH().getDateTimeOfMessage().getTime().setValue(dateTimeOfMessage)
    }

    def getDateTimeOfMessage() {
        // Consulto el timestamp del mensaje
        return this.msg.getMSH().getDateTimeOfMessage().getTime().toString()
    }

    def getMessageCode() {
        return this.msg.getMSH().getMessageType().getMessageCode().toString()
    }

    def getMessageEvent() {
        return this.msg.getMSH().getMessageType().getTriggerEvent().toString()
    }

    def setSendingApplication(nameSpaceID, sendingApplicationID) {
        // Indicar cuál es la aplicación que genera el mensaje
        this.msg.getMSH().getSendingApplication().getNameSpaceID().setValue(nameSpaceID)
        this.msg.getMSH().getSendingApplication().getUnversalID().setValue(sendingApplicationID)
    }

    def setSendingFacility(nameSpaceID, sendingFacilityID) {
        // Indicar cuál es el servicio que genera el mensaje
        this.msg.getMSH().getSendingFacility().getNameSpaceID().setValue(nameSpaceID)
        this.msg.getMSH().getSendingFacility().getUnversalID().setValue(sendingFacilityID)
    }

    def setReceivingApplication(nameSpaceID, receivingApplicationID) {
        // Indicar cuál es la aplicación a la que esta destinado el mensaje
        this.msg.getMSH().getReceivingApplication().getNameSpaceID().setValue(nameSpaceID)
        this.msg.getMSH().getReceivingApplication().getUnversalID().setValue(receivingApplicationID)

    }

    def setReceivingFacility(nameSpaceID, receivingFacilityID) {
        // Indicar cuál es el servicio al que está destinado el mensaje
        this.msg.getMSH().getReceivingFacility().getNameSpaceID().setValue(nameSpaceID)
        this.msg.getMSH().getReceivingFacility().getUnversalID().setValue(receivingFacilityID)

    }
}
