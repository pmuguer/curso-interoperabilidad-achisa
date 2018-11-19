import mllp
import mllpbuffer
import ca.uhn.hl7v2.model.v25.message.ADT_A01

// Se importan las clases necesarias para generar los mensajes
import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.parser.Parser

import ACKMessage
import ADTMessage

class MLLPServer {
    def MLLPServer(int port) {
        // Server que recibe y procesa mensajes utilizando el protocolo MLLP
        try {
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
                                println("\nMensaje recibido del cliente:")
                                //println(clientMessage.toString().normalize())
                                //hapiMessage.parse(clientMessage.toString())
                                clientADTMessage.initFromER7Message(clientMessage.toString())
                                //def messageControlID = hapiMessage.getMSH().getMessageControlID()
                                def messageControlID = clientADTMessage.getMessageControlID()
                                println("Message control ID: " + messageControlID)
                                //println("Contenido del mensaje:")
                                //messageCode = hapiMessage.getMSH().getMessageType().getMessageCode().toString()
                                //messageEvent = hapiMessage.getMSH().getMessageType().getTriggerEvent().toString()
                                messageCode = clientADTMessage.getMessageCode()
                                messageEvent = clientADTMessage.getMessageEvent()
                                // Se esperan mensajes de tipo "ADT^A01"
                                if ((messageCode != "ADT") || (messageEvent != "A01")) {
                                    println("ERROR: el mensaje recibido no es de tipo ADT^A01")
                                }
                                //this.printADTMessageFields(hapiMessage)

                                ackMessage.setMessageControlID(messageControlID.toString())
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
                })
            }
        } finally {
            println("Hello")
        }
    }

    //def printADTMessageFields(ADT_A01 message) {
    //    // Dado un mensaje ADT, imprimir todos los campos que lo componen
    //    println message.getMSH().getMessageControlID()
    //    println message.getPID().getPatientName()
    //    println message.getPV1().getAdmitDateTime()
    //}
}
