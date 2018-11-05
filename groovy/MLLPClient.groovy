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
        // message_stream: es una lista de int's que corresponden
        // a los mensajes que se van a enviar, incluidos los separadores
        // de MLLP
  
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
                // Se escribe de a un caracter el "stream" correspondiente a los
                // mensajes que se envian usando el protocolo MLLP
                output.write( message_stream[stream_idx] )
            }
            output.flush()
            def received_messages_count = 0;
            def String[] server_ack_responses = [];
            while (true) {
                while (input_char != -1) {
                    // Se espera que para cada mensaje enviado el server
                    // devuelva una respuesta; por lo tanto se debe leer
                    // del stream 'input'
                    input_char = input.read()
                    if (input_char != -1) {
                        // Se agrega al buffer la respuesta recibida
                        // caracter por caracter
                        buffer.add_to_buffer(input_char)
                        // Si hay un mensaje completo, pop_message() lo
                        // devuelve como un String
                        mensaje_del_server = buffer.pop_message()
                        if (mensaje_del_server != null) {
                            println "Mensaje del server: " + mensaje_del_server
                            received_messages_count += 1;
                        }
                        // Como el ejercicio especifica que se envian 3 mensajes,
                        // cuando se reciba el 3er ack se debe cerrar la conexion
                        // con el server
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

