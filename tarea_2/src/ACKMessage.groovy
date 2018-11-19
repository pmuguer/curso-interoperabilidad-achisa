import ca.uhn.hl7v2.model.v25.message.ACK

import HL7Message

class ACKMessage extends HL7Message {
    def ACKMessage() {
        // Genero una instancia de un mensaje ACK
        this.msg = new ACK()
       
        // Genero el MSH con los atributos que van en todos los mensajes 
        this.initMSH()
    }

    def setMessageControlID(messageControlID) {
        this.msg.getMSH().getMessageControlID().setValue(messageControlID.toString())
    }
}
