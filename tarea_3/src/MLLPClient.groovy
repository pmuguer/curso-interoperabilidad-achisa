import java.net.SocketException
import ca.uhn.hl7v2.model.v25.message.ACK
import mllp
import mllpbuffer

class MLLPClient {
    def MLLPClient(String serverIP, int serverPort, controlID, message) {
        // Constructor de MLLPClient
        // serverIP: ip en la que escucha el server MLLP
        // serverPort: puerto en el que escucha el server MLLP
        // controlID: id de control del mensaje que se va a enviar al server
        // message: mensaje a enviar al server (String en formato ER7)

        // Buffer ad-hoc para procesar el protocolo MLLP
        def buffer = new mllpbuffer()
        def mllpMessage = mllp.create_mllp_message(message)
        // Creo una instancia de ACK para poder chequear si el id de control coicide
        // con el del mensaje
        def hapiMessage = new ACK()
        def String serverACK

        // Inicio conexión con el server
        def socket = new Socket(InetAddress.getByName(serverIP), serverPort);
        try {
            // Obtengo los streams de input y output
            socket.withStreams { input, output ->
                // Envio el mensaje al server
                output.write(mllpMessage)
                output.flush()

                // Proceso la respuesta del server
                input.eachByte { inputbyte ->
                    buffer.add_to_buffer(inputbyte)
                    serverACK = buffer.pop_message()
                    if (serverACK != null) {
                        println("\nACK recibido del server:")
                        hapiMessage.parse(serverACK.toString())
                        println(hapiMessage.toString().normalize())
                        println "Control ID del ACK: " + hapiMessage.getMSH().getMessageControlID().toString()

                        if (hapiMessage.getMSH().getMessageControlID().toString() == controlID) {
                            println "El id del ack recibido coincide con el del mensaje; cerrando la conexión"
                            socket.close()
                        }
                    }
                }
            }
        } catch(SocketException ex) {
            println "Conexión cerrada por el cliente"
        }
    }
}
