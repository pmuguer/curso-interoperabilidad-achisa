import mllp
import mllpbuffer
import ca.uhn.hl7v2.model.v25.message.ADT_A01

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
                                println(clientMessage.toString().normalize())
                                hapiMessage.parse(clientMessage.toString())
                                def fechaHora = hapiMessage.getPV1().getAdmitDateTime()
                                println("fecha y hora " + fechaHora)
                                println("Hola mundo")
                            }
                        }
                        //def inputString = reader.getText()
                        //println("Input string")
                        //println(inputString)
                        //buffer.add_string_to_buffer(inputString)
                        //def clientMessage = buffer.pop_message()
                        //println(clientMessage)
                        //output << "Se recibio mensaje\n"
                    })
                    println("\nSaliendo del thread")
                })
            }
        } finally {
            println("Hello")
        }
    }
}
