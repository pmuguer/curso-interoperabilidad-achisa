import ca.uhn.hl7v2.model.v25.message.ACK
import mllp
import mllpbuffer

class MLLPClient {
    def MLLPClient(message) {
        def buffer = new mllpbuffer()
        def mllpMessage = mllp.create_mllp_message(message)
        def hapiMessage = new ACK()
        def String serverACK

        def socket = new Socket("localhost", 8181);
        socket.withStreams { input, output ->
            output.write(mllpMessage)
            output.flush()

            input.eachByte { inputbyte ->
                buffer.add_to_buffer(inputbyte)
                serverACK = buffer.pop_message()
                if (serverACK != null) {
                    println("\nACK recibido del server:")
                    hapiMessage.parse(serverACK.toString())
                    println(hapiMessage.getMSH().getMessageControlID())
                }
            }
        }
    }
}
