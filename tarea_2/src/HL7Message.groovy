// Se importan las clases necesarias para generar los mensajes
import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.parser.Parser

import ca.uhn.hl7v2.model.AbstractMessage

class HL7Message {
    AbstractMessage msg

    def initMSH() {
        // Inicialización del header, común para todos los mensajes
        def mshSegment = this.msg.getMSH()
        mshSegment.getFieldSeparator().setValue("|")
        mshSegment.getEncodingCharacters().setValue("^~\\&")
        
        // Se indica que la versión de HL7 es 2.5
        mshSegment.getVersionID().getVersionID().setValue("2.5")
        // Indico el tipo de mensaje (Cap 02, página 99)
        mshSegment.getMessageType().getMessageCode().setValue("ADT")
        // Indico el evento (Cap 02, página 101)
        mshSegment.getMessageType().getTriggerEvent().setValue("A01")
        // Indico la estructura del mensaje (Cap 02, página 102)
        mshSegment.getMessageType().getMessageStructure().setValue("ADT_A01")

        // Se registran los datos de la aplicación que genera el mensaje
        mshSegment.getSendingApplication().getNamespaceID().setValue("1")
        mshSegment.getSendingApplication().getUniversalID().setValue("1")

        // Se registran los datos de la aplicación a la que está destinada el mensaje
        mshSegment.getReceivingApplication().getNamespaceID().setValue("2")
        mshSegment.getReceivingApplication().getUniversalID().setValue("2")
        
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

    def getMessageCode() {
        return this.msg.getMSH().getMessageType().getMessageCode().toString()
    }

    def getMessageEvent() {
        return this.msg.getMSH().getMessageType().getTriggerEvent().toString()
    }
}
