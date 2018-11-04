import mlpp

class TCPServer {
    // Constructor
    def TCPServer(int port)
    {
        try
        {
            // http://docs.oracle.com/javase/6/docs/api/java/net/ServerSocket.html
            def server = new ServerSocket(port)
            // Si la IP es 0.0.0.0 el socket no tiene bind a una IP, entonces
            // escucha en todas las interfaces del equipo.
            println "TCPServer escuchando en el puerto: " +
                server.getInetAddress() +':'+ server.getLocalPort()
            // Mantiene el servidor corriendo entre sucesivos clientes
            while (true)
            {
                println "TCPServer hilo servidor: " + Thread.currentThread().getId()
                // Espera hasta que un cliente solicite una conexión
                server.accept() { socket ->
                    // Abre un nuevo hilo de atención por cada cliente nuevo
                    println "TCPServer cliente conectado, hilo de atencion: " +
                            Thread.currentThread().getId()
                    // Variables disponibles para la atención de un cliente
                    def datos_recibidos = ""
                    // Crea un InputObjectStream y un OutputObjectStream desde el socket
                    // y se los pasa a la clausura
                    // Los streams se cierran cuando se retorna de la clausura, incluso
                    // si se lanza una excepción.
                    socket.withStreams { input, output ->
                        def out = new BufferedWriter(new OutputStreamWriter(output))
                        def inp = new BufferedReader(new InputStreamReader(input))

                        // Procesamiento de la comunicación con el cliente conectado
                        // El cliente puede cerrar el socket
                        while (socket.isConnected() && !socket.isClosed())
                        {
                            // Leer datos (se bloquea hasta que hayan datos para leer)
                            datos_recibidos = inp.readLine()
                            if (datos_recibidos == null)
                            {
                                println "El cliente ha cerrado la conexion"
                                break // Sale del while para cerrar la conexion desde el server
                            }
                            println "TCPServer recibe: " + datos_recibidos
                            messages = mlpp.extract_messages_from_stream(datos_recibidos)
                            println messages
                            // Envía respuesta
                            out.writeLine("Hola " + datos_recibidos)
                            out.flush()
                        } // while mismo cliente
                    } // cuando sale de socket.withStreams cierra los streams
                    println "TCPServer cerrando conexion con cliente"
                    if (socket.isConnected() && !socket.isClosed()) socket.close()
                }
            } // while clientes
        }
        catch (Exception e)
        {
            // Si es un error "Address already in use: JVM_Bind", cambiar el numero de puerto.
            println "Ha ocurrido un error: " + e.getMessage()
        }
        finally
        {
            println "TCPServer cerrando servidor"
            server.close()
        }
    }
}

def server = new TCPServer(9090)
