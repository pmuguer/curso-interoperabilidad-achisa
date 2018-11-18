import mllp
import mllpbuffer

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
                    // Passes the Socket's InputStream and OutputStream to the closure.
                    clientSocket.withStreams({ input, output ->
                        def reader = input.newReader()
                        def inputString = reader.getText()
                        buffer.add_string_to_buffer(inputString)
                        def clientMessage = buffer.pop_message()
                        println(clientMessage)
                        output << "Se recibio mensaje\n"
                    })
                    println("Saliendo de la thread")
                })
            }
        } finally {
            println("Hello")
        }
    }
}