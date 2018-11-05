import mllp
import mllpbuffer

class MLLPServer {
    def MLLPServer(int port) {
        // Se usa un buffer para simplificar el procesamiento de los mensajes
        def buffer = new mllpbuffer()
        try {
            def server = new ServerSocket(port)
            println "MLLPServer escuchando en el puerto: " +
                server.getInetAddress() +':'+ server.getLocalPort()

            def mensaje_del_cliente;
            // Mantiene el servidor corriendo entre sucesivos clientes
            while (true) {
                println "MLLPServer hilo servidor: " + Thread.currentThread().getId()
                // Espera hasta que un cliente solicite una conexión
                server.accept() { socket ->
                    // Abre un nuevo hilo de atención por cada cliente nuevo
                    println "MLLPServer cliente conectado, hilo de atencion: " +
                            Thread.currentThread().getId()
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
                            // Leer datos (se bloquea la thread hasta que hayan datos para leer,
                            // es decir, hasta que el cliente envie un mensaje
                            client_data = inp.read()
                            buffer.add_to_buffer(client_data)
                            mensaje_del_cliente = buffer.pop_message()
                            if (mensaje_del_cliente != null) {
                                // El ack del server en respuesta al mensaje del cliente
                                // funciona en el contexto específico del ejercicio:
                                // se esperan mensajes en los que el ultimo caracter
                                // es el numero de mensaje. Se extrae ese ultimo caracter
                                // y se agrega al mensaje de ack correspondiente
                                def message_number = mensaje_del_cliente[mensaje_del_cliente.size() - 1]
                                println "Mensaje recibido: " + mensaje_del_cliente
                                mllp_ack_message = mllp.create_mllp_message("Ok " + message_number)
                                def idx_ack;
                                for (idx_ack = 0; idx_ack < mllp_ack_message.size(); idx_ack++) {
                                    out.write(mllp_ack_message[idx_ack] as int);
                                }
                                out.flush()
                            }
                        } // while mismo cliente

                    } // cuando sale de socket.withStreams cierra los streams
                    println "MLLPServer cerrando conexion con cliente"
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
            println "MLLPServer cerrando servidor"
            try {
                server.close()
            } catch (Exception ex) {
                // Tira un error que no sé como solucionar
            }
        }
    }
}
