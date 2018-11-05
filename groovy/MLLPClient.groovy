import mllp
import mllpbuffer

class MLLPClient {
    def MLLPClient(int serverPort, String serverIP, int[] message_stream) {
        def buffer = new mllpbuffer();
        def mensaje_del_server = ""
        // Crea socket de conexion con el servidor,
        // especificando direccion IP y puerto del servidor.
        // "localhost" equivale a la IP 127.0.0.1
        def socket = new Socket(InetAddress.getByName(serverIP), serverPort)
        // No sé si esto es correcto, creo que el server devuelve integers
        def server_data = 0;
        def byte[] server_data_stream;
        def server_data_char_list = [].toList()
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
                while (server_data != -1) {
                    server_data = input.read()
                    if (server_data != -1) {
                        server_data_char_list.add(server_data)
                        buffer.add_to_buffer(server_data)
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

