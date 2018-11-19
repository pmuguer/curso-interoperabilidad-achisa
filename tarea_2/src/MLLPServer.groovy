import mllp
import mllpbuffer
import ca.uhn.hl7v2.model.v25.message.ADT_A01

// Se importan las clases necesarias para generar los mensajes
import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.parser.Parser

import ACKMessage

class MLLPServer {
    def MLLPServer(int port) {
        // Server que recibe y procesa mensajes utilizando el protocolo MLLP
        try {
            def serverSocket = new ServerSocket(port)
            println("Server corriendo")
            //println "Escuchando en el puerto: " +
            //        server.getInetAddress() + ':' + server.getLocalPort()

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
                    def hapiMessage = new ADT_A01()
                    def ackMessage = new ACKMessage()
                    def ackEncodedMessage = ""
                    // Passes the Socket's InputStream and OutputStream to the closure.
                    clientSocket.withStreams({ input, output ->
                        def reader = input.newReader()
                        // Traverse through each byte of the specified stream.
                        input.eachByte { inputbyte ->
                            //print(inputbyte)
                            buffer.add_to_buffer(inputbyte)
                            clientMessage = buffer.pop_message()
                            if (clientMessage != null) {
                                println("\nMensaje recibido del cliente:")
                                //println(clientMessage.toString().normalize())
                                hapiMessage.parse(clientMessage.toString())
                                def messageControlID = hapiMessage.getMSH().getMessageControlID()
                                println("Message control ID: " + messageControlID)
                                println("Contenido del mensaje:")
                                this.printADTMessageFields(hapiMessage)

                                //this.initMSH(hapiACKMessage)
                                // Genero el ACK para el mensaje recibido

                                ackMessage.setMessageControlID(messageControlID.toString())
                                //hapiACKMessage.getMSH().getMessageControlID().setValue(messageControlID.toString())

                                //HapiContext context = new DefaultHapiContext();
                                //Parser parser = context.getPipeParser();
                                //String ackEncodedMessage = parser.encode(hapiACKMessage);
                                ackEncodedMessage = ackMessage.encodeER7()

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
    //def initMSH(msg) {
    //    // Inicialización del header, común para todos los mensajes
    //    def mshSegment = msg.getMSH()
    //    mshSegment.getFieldSeparator().setValue("|")
    //    mshSegment.getEncodingCharacters().setValue("^~\\&")

    //    // Se indica que la versión de HL7 es 2.5
    //    mshSegment.getVersionID().getVersionID().setValue("2.5")
    //    // Indico el tipo de mensaje (Cap 02, página 99)
    //    mshSegment.getMessageType().getMessageCode().setValue("ADT")
    //    // Indico el evento (Cap 02, página 101)
    //    mshSegment.getMessageType().getTriggerEvent().setValue("A01")
    //    // Indico la estructura del mensaje (Cap 02, página 102)
    //    mshSegment.getMessageType().getMessageStructure().setValue("ADT_A01")

    //    // Se registran los datos de la aplicación que genera el mensaje
    //    mshSegment.getSendingApplication().getNamespaceID().setValue("1")
    //    mshSegment.getSendingApplication().getUniversalID().setValue("1")

    //    // Se registran los datos de la aplicación a la que está destinada el mensaje
    //    mshSegment.getReceivingApplication().getNamespaceID().setValue("2")
    //    mshSegment.getReceivingApplication().getUniversalID().setValue("2")

    //    // Indico el id de procesamiento (this field is used to decide whether to process the
    //    // message as defined in HL7 Application (level 7) Processing rules)
    //    // Represents an HL7 PT (Processing Type) data type. This type consists
    //    // of the following components:
    //    //
    //    //  * Processing ID (ID)
    //    //  * Processing Mode (ID)

    //    // Processing ID ("T" corresponde a "training")
    //    // http://hl7-definition.caristix.com:9010/Default.aspx?version=HL7%20v2.5.1&table=0103
    //    mshSegment.getProcessingID().getProcessingID().setValue("T")

    //    // Processing Mode ("T" corresponde a "Current processing")
    //    // http://hl7-definition.caristix.com:9010/Default.aspx?version=HL7%20v2.5.1&table=0207
    //    mshSegment.getProcessingID().getProcessingMode().setValue("T")
    //}


    def printADTMessageFields(ADT_A01 message) {
        // Dado un mensaje ADT, imprimir todos los campos que lo componen
        println message.getMSH().getMessageControlID()
        println message.getPID().getPatientName()
        println message.getPV1().getAdmitDateTime()
    }
}
