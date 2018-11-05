import mllp
import mllpbuffer

class MLLPServer {
    def MLLPServer(int port) {
        // Server que espera mensajes utilizando el protocolo MLLP
        // El formato de los mensajes es específico para el ejercicio;
        // Se supone que los mensajes terminan con un número, que el
        // server usará para construir un ACK que corresponde a ese mismo
        // número de mensaje; por ejemplo "Hola mundo 2" recibirá como
        // respuesta "Ok 2"

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
                    def input_byte = ""
                    // Crea un InputObjectStream y un OutputObjectStream desde el socket
                    // y se los pasa a la clausura
                    // Los streams se cierran cuando se retorna de la clausura, incluso
                    // si se lanza una excepción.
                    socket.withStreams { input, output ->
                        def out = new BufferedWriter(new OutputStreamWriter(output))
                        def inp = new BufferedReader(new InputStreamReader(input))

                        // Procesamiento de la comunicación con el cliente conectado
                        // El cliente puede cerrar el socket
                        while (socket.isConnected() && !socket.isClosed() && input_byte != -1)
                        {
                            // Leer datos (se bloquea la thread hasta que hayan datos para leer,
                            // es decir, hasta que el cliente envie un mensaje).
                            // Se usa el método read(), que lee de a un byte por vez;
                            // por lo tanto el "while" se ejecuta una vez por cada byte
                            // enviado por el cliente

                            // Leo un byte
                            input_byte = inp.read()
                            // Se agrega el byte recibido al buffer
                            buffer.add_to_buffer(input_byte)
                            // Si hay un mensaje completo en el buffer, el método
                            // lo devuelve (en formato String, sin los delimitadores
                            // de MLLP)
                            mensaje_del_cliente = buffer.pop_message()
                            // La siguiente condición se cumple sólo si hay un mensaje
                            // completo en el buffer para extraer
                            if (mensaje_del_cliente != null) {
                                // El ack del server en respuesta al mensaje del cliente
                                // funciona en el contexto específico del ejercicio:
                                // se esperan mensajes en los que el ultimo caracter
                                // es el numero de mensaje. Se extrae ese ultimo caracter
                                // y se agrega al mensaje de ack correspondiente
                                def message_number = mensaje_del_cliente[mensaje_del_cliente.size() - 1]
                                println "Mensaje recibido: " + mensaje_del_cliente
                                // Crear el mensaje MLLP con el ack en respuesta al mensaje
                                // del cliente
                                mllp_ack_message = mllp.create_mllp_message("Ok " + message_number)
                                def idx_ack;
                                for (idx_ack = 0; idx_ack < mllp_ack_message.size(); idx_ack++) {
                                    // Escribir de a un byte el mensaje de respuesta
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
