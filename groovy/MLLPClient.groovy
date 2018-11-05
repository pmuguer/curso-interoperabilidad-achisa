import mllp
import mllpbuffer

class MLLPClient {
    // Cliente que envía mensajes a un server MLLPServer
    // El server MLLPServer espera que el cliente use el protocolo MLLP.
    // Respecto al formato de los mensajes, el server espera un
    // formato específico (ver la descripción del ejercicio).
    // Cada mensaje debería terminar con un número de un dígito.
    // El server responde con un ACK igual al numero que forma parte
    // del mensaje. Por ejemplo, "Hola mundo 3" recibe como respuesta
    // "Ok 3"
    def MLLPClient(int serverPort, String serverIP, int[] message_stream) {
        // Se usa un buffer para simplificar el procesamiento de las
        // respuestas recibidas del server
        def buffer = new mllpbuffer();
        def mensaje_del_server = ""
        // Crea socket de conexion con el servidor,
        // especificando direccion IP y puerto del servidor.
        // "localhost" equivale a la IP 127.0.0.1
        def socket = new Socket(InetAddress.getByName(serverIP), serverPort)
        // No sé si esto es correcto, creo que el server devuelve integers
        def input_char = 0;
        println "MLLPClient: conectado a " + socket.getRemoteSocketAddress()
        def input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        def output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        try {
            // Escribe en el socket
            def stream_idx = 0;
            for (stream_idx = 0; stream_idx < message_stream.size(); stream_idx++) {
                output.write( message_stream[stream_idx] ) // Agrega \n al final, el server hace readLine.
            }
            output.flush()
            def received_messages_count = 0;
            def String[] server_ack_responses = [];
            while (true) {
                while (input_char != -1) {
                    input_char = input.read()
                    if (input_char != -1) {
                        buffer.add_to_buffer(input_char)
                        mensaje_del_server = buffer.pop_message()
                        if (mensaje_del_server != null) {
                            println "Mensaje del server: " + mensaje_del_server
                            received_messages_count += 1;
                        }
                        if (received_messages_count == 3) {
                            break;
                        } 
                    }
                }
                break; 
            }
        }
        catch (Exception e) {
            println e.message
        }
        finally {
            // Cierra el socket
            if (socket.isConnected() && !socket.isClosed())
            {
                socket.close()
            }
        }
    }
}

