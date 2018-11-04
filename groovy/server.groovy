import mllp
import mllpbuffer

class TCPServer {
    def TCPServer(int port) {
        def buffer = new mllpbuffer()
        try {
            def server = new ServerSocket(port)
            println "TCPServer escuchando en el puerto: " +
                server.getInetAddress() +':'+ server.getLocalPort()

            def buffered_client_stream = [].toList()
            def idx_client_data = 0;
            def mensaje_del_cliente;
            // Mantiene el servidor corriendo entre sucesivos clientes
            while (true) {
                println "TCPServer hilo servidor: " + Thread.currentThread().getId()
                // Espera hasta que un cliente solicite una conexión
                server.accept() { socket ->
                    // Abre un nuevo hilo de atención por cada cliente nuevo
                    println "TCPServer cliente conectado, hilo de atencion: " +
                            Thread.currentThread().getId()
                    // Variables disponibles para la atención de un cliente
                    def String[] messages = [];
                    def byte[] mllp_ack_message = [];
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
                        while (socket.isConnected() && !socket.isClosed() && client_data != -1)
                        {
                            // Leer datos (se bloquea hasta que hayan datos para leer)
                            // se leen datos de a un Int por vez
                            client_data = inp.read()
                            buffer.add_to_buffer(client_data)
                            mensaje_del_cliente = buffer.pop_message()
                            if (mensaje_del_cliente != null) {
                                println "Mensaje recibido:" + mensaje_del_cliente
                                mllp_ack_message = mllp.create_mllp_message("Ok")
                                //conn.send(mllp_ack_message)
                                def idx_ack;
                                for (idx_ack = 0; idx_ack < mllp_ack_message.size(); idx_ack++) {
                                    out.write(mllp_ack_message[idx_ack] as int);
                                }
                                out.flush()
                            }
 

                            //println "Leido:"
                            //println client_data
                            if (client_data == null)
                            {
                                println "El cliente ha cerrado la conexion"
                                break // Sale del while para cerrar la conexion desde el server
                            }
                            //if (client_data != -1) {
                            //    buffered_client_stream.add(client_data)
                            //}
                        } // while mismo cliente

                        //println "Stream:........."
                        //println(buffered_client_stream)
                        ////for (idx_client_data = 0; idx_client_data < client_data.)
                        ////buffered_client_stream
                        //def byte[] buffered_char_client_stream = buffered_client_stream.toArray(Byte)
                        ////messages = mllp.extract_messages_from_stream(client_data)
                        //messages = mllp.extract_messages_from_stream(buffered_char_client_stream)
                        ////println "TCPServer recibe: " + datos_recibidos

                        //def i = 0;
                        //def idx_ack = 0;
                        //for (i = 0; i < messages.size(); i++) {
                        //    println "Mensaje recibido del cliente:";
                        //    println messages[i];
                        //    //msg_number = message[-1]
                        //    //ack_message = "Ok {}".format(msg_number)
                        //    mllp_ack_message = mllp.create_mllp_message("Ok")
                        //    //conn.send(mllp_ack_message)
                        //    for (idx_ack = 0; idx_ack < mllp_ack_message.size(); idx_ack++) {
                        //        out.write(mllp_ack_message[idx_ack] as int);
                        //    }
                        //    out.flush()
                        //}

                    } // cuando sale de socket.withStreams cierra los streams
                    println "TCPServer cerrando conexion con cliente"
                    if (socket.isConnected() && !socket.isClosed()) socket.close()
                }
                break
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
            try {
                server.close()
            } catch (Exception ex) {
                // Tira un error que no sé como solucionar
            }
        }
    }
}

def server = new TCPServer(9090)
