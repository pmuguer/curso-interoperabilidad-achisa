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
                    // Instancia que se va a inicializar con los datos
                    // recibidos del cliente
                    def clientADTMessage = new ADTMessage()
                    // Instancia que se va a usar para devolver un ACK al cliente
                    def ackMessage = new ACKMessage()
                    def ackEncodedMessage = ""
                    def String messageCode = ""
                    def String messageEvent = ""
                    // Passes the Socket's InputStream and OutputStream to the closure.
                    clientSocket.withStreams({ input, output ->
                        def reader = input.newReader()
                        // Traverse through each byte of the specified stream.
                        input.eachByte { inputbyte ->
                            // Agrego cada byte del stream de entrada al buffer mllp
                            buffer.add_to_buffer(inputbyte)
                            // Trato de obtener un mensaje
                            clientMessage = buffer.pop_message()
                            if (clientMessage != null) {
                                // Parseo el mensaje y lo uso para inicializar una instancia de ADTMessage
                                clientADTMessage.initFromER7Message(clientMessage.toString())
                                // Uso un método de conveniencia para mostrar los contenidos del mensaje
                                println("\nMensaje recibido del cliente:")
                                println(clientADTMessage.getPrintableFormat())
                                def messageControlID = clientADTMessage.getMessageControlID()
                                println("Message control ID: " + messageControlID)
                                // Obtengo el código de mensaje y el tipo de evento, para chequear que
                                // sean los esperados (ADT^A01)
                                messageCode = clientADTMessage.getMessageCode()
                                messageEvent = clientADTMessage.getMessageEvent()
                                // Seteo el messageControlID del mensaje de ACK para indicar
                                // que el ACK corresponde a un determinado mensaje enviado por el cliente
                                ackMessage.setMessageControlID(messageControlID.toString())
                                // Se esperan mensajes de tipo "ADT^A01"
                                if ((messageCode != "ADT") || (messageEvent != "A01")) {
                                    println("ERROR: el mensaje recibido no es de tipo ADT^A01")
                                    // Indico al cliente que hubo error ("AE" = Error)
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
