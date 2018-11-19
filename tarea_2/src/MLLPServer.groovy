import java.net.SocketException
import mllp
import mllpbuffer
import ca.uhn.hl7v2.model.v25.message.ADT_A01

import ACKMessage
import ADTMessage

class MLLPServer {
    def MLLPServer(int port) {
        // Server que recibe y procesa mensajes utilizando el protocolo MLLP
        def serverSocket = new ServerSocket(port)
        println("Server corriendo")

        // Mantiene el servidor corriendo entre sucesivos clientes
        while (true) {
            // Espera hasta que un cliente solicite una conexión
            // Accepts a connection and passes the resulting Socket to the closure
            // which runs in a new Thread.
            //
            // En este caso, el "resulting Socket" es clientSocket
            serverSocket.accept({ clientSocket ->
                try {
                    def buffer = new mllpbuffer()
                    def clientMessage = ""
                    //def hapiMessage = new ADT_A01()
                    // Instancia que se va a inicializar con los datos
                    // recibidos del cliente
                    def clientADTMessage = new ADTMessage()
                    def ackMessage = new ACKMessage()
                    def ackEncodedMessage = ""
                    def String messageCode = ""
                    def String messageEvent = ""
                    // Passes the Socket's InputStream and OutputStream to the closure.
                    clientSocket.withStreams({ input, output ->
                        def reader = input.newReader()
                        // Traverse through each byte of the specified stream.
                        input.eachByte { inputbyte ->
                            buffer.add_to_buffer(inputbyte)
                            clientMessage = buffer.pop_message()
                            if (clientMessage != null) {
                                //println(clientMessage.toString().normalize())
                                //hapiMessage.parse(clientMessage.toString())
                                clientADTMessage.initFromER7Message(clientMessage.toString())

                                println("\nMensaje recibido del cliente:")
                                println(clientADTMessage.getPrintableFormat())
                                //def messageControlID = hapiMessage.getMSH().getMessageControlID()
                                def messageControlID = clientADTMessage.getMessageControlID()
                                println("Message control ID: " + messageControlID)
                                //println("Contenido del mensaje:")
                                //messageCode = hapiMessage.getMSH().getMessageType().getMessageCode().toString()
                                //messageEvent = hapiMessage.getMSH().getMessageType().getTriggerEvent().toString()
                                messageCode = clientADTMessage.getMessageCode()
                                messageEvent = clientADTMessage.getMessageEvent()
                                ackMessage.setMessageControlID(messageControlID.toString())
                                // Se esperan mensajes de tipo "ADT^A01"
                                if ((messageCode != "ADT") || (messageEvent != "A01")) {
                                    println("ERROR: el mensaje recibido no es de tipo ADT^A01")
                                    // Indico que no hubo error ("AE" = Error)
                                    ackMessage.setAcknowledgementCode("AE")
                                }
                                // Indico que no hubo error ("AA" = Accepted)
                                ackMessage.setAcknowledgementCode("AA")
                                ackEncodedMessage = ackMessage.er7Encode()

                                println("Respondiendo al cliente con ACK")
                                // Agrego los separadores de MLLP
                                def mllpACKMessage = mllp.create_mllp_message(ackEncodedMessage)
                                // Respondo con el ACK al cliente
                                output.write(mllpACKMessage)
                                output.flush()

                            }
                        }
                    })
                    println("\nSaliendo del thread")
                } catch (SocketException ex) {
                    println "Conexión cerrada por el cliente"
                }
           })
        }
    }
}
