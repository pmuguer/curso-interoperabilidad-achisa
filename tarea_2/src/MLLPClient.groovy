import ca.uhn.hl7v2.model.v25.message.ACK
import mllp
import mllpbuffer

class MLLPClient {
    def MLLPClient(message) {
        def buffer = new mllpbuffer()
        def mllpMessage = mllp.create_mllp_message(message)
        def hapiMessage = new ACK()
        def String serverACK
        //println("El cliente va a escribir el siguiente mensaje")
        //println(mllpMessage)
        def socket = new Socket("localhost", 8181);
        socket.withStreams { input, output ->
            //output << mllpMessage
            output.write(mllpMessage)
            output.flush()
            //buffer = input.newReader().readLine()
            //println "response = $buffer"




            input.eachByte { inputbyte ->
                buffer.add_to_buffer(inputbyte)
                serverACK = buffer.pop_message()
                if (serverACK != null) {
                    println("\nACK recibido del server:")
                    //println(serverACK.toString().normalize())
                    hapiMessage.parse(serverACK.toString())
                    println(hapiMessage.getMSH().getMessageControlID())
                    //def messsageControlID = hapiMessage.getMSH().getMessageControlID()
                    //println("Message control ID: " + messsageControlID)
                    //println("Contenido del mensaje:")
                    //this.printADTMessageFields(hapiMessage)
                }
            }
        }
    }
}
