import mllp

class TCPServer {
    def TCPServer(int port) {
        try {
            def server = new ServerSocket(port)
            println "TCPServer escuchando en el puerto: " +
                server.getInetAddress() +':'+ server.getLocalPort()

            // Mantiene el servidor corriendo entre sucesivos clientes
            while (true) {
                println "TCPServer hilo servidor: " + Thread.currentThread().getId()
                // Espera hasta que un cliente solicite una conexión
                server.accept() { socket ->
                    // Abre un nuevo hilo de atención por cada cliente nuevo
                    println "TCPServer cliente conectado, hilo de atencion: " +
                            Thread.currentThread().getId()
                    // Variables disponibles para la atención de un cliente
                    def client_data = ""
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
                            client_data = inp.read()
                            if (client_data == null)
                            {
                                println "El cliente ha cerrado la conexion"
                                break // Sale del while para cerrar la conexion desde el server
                            }
                            messages = mllp.extract_messages_from_stream(client_data)
                            //println "TCPServer recibe: " + datos_recibidos

                            def i = 0;
                            
                            for (i = 0; i < messages.size(); i++) {
                                println "Mensaje recibido del cliente:";
                                println messages[i];
                                //msg_number = message[-1]
                                //ack_message = "Ok {}".format(msg_number)
                                mllp_ack_message = mllp.create_mllp_message("Ok")
                                //conn.send(mllp_ack_message)
                                out.write(mllp_ack_message);
                                out.flush()
                            }
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
