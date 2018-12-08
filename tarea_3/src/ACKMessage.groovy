import ca.uhn.hl7v2.model.v25.message.ACK

import HL7Message

class ACKMessage extends HL7Message {
    def ACKMessage() {
        // Genero una instancia de un mensaje ACK
        this.msg = new ACK()

        // Genero el MSH con los atributos que van en todos los mensajes 
        this.initMSH()
    }

    def initMSH() {
        // Inicialización del header; primero uso la inicialización de la clase
        // base (HL7Message)        
        super.initMSH()
        // A continuación agrego los campos específicos para mensajes ACK
        def mshSegment = this.msg.getMSH()
        // Indico el tipo de mensaje (Cap 02, página 99)
        mshSegment.getMessageType().getMessageCode().setValue("ACK")
        // Indico el evento (Cap 02, página 101)
        mshSegment.getMessageType().getTriggerEvent().setValue("A01")
        // Indico la estructura del mensaje (Cap 02, página 102)
        mshSegment.getMessageType().getMessageStructure().setValue("ADT_A01")
    }

    def setAcknowledgementCode(acknowledgmentCode) {
        // Agregar el ack code al segmento MSA; AA = aceptado, AE = error
        this.msg.getMSA().getAcknowledgmentCode().setValue(acknowledgmentCode)
    }

}
