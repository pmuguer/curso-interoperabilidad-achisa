import mllp

import java.lang.reflect.Array

class TCPClient {
    def TCPClient(int serverPort, String serverIP, int[] message_stream) {
        // Crea socket de conexion con el servidor,
        // especificando direccion IP y puerto del servidor.
        // "localhost" equivale a la IP 127.0.0.1
        def socket = new Socket(InetAddress.getByName(serverIP), serverPort)
        // No sé si esto es correcto, creo que el server devuelve integers
        def server_data = 0;
        def byte[] server_data_stream;
        def server_data_char_list = [].toList()
        println "TCPClient: conectado a " + socket.getRemoteSocketAddress()
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

                // Tengo que leer la respuesta de a un caracter:


                //while (socket.isConnected() && !socket.isClosed() && server_data != -1)
                //{
                //    // Leer datos (se bloquea hasta que hayan datos para leer)
                //    // se leen datos de a un Int por vez
                //    client_data = inp.read()

                //    println "Leido:"
                //    println client_data
                //    if (client_data == null)
                //    {
                //        println "El cliente ha cerrado la conexion"
                //        break // Sale del while para cerrar la conexion desde el server
                //    }
                //    if (client_data != -1) {
                //        buffered_client_stream.add(client_data)
                //    }
                //} // while mismo cliente

                while (server_data != -1) {
                    server_data = input.read()
                    if (server_data != -1) {
                        println "Recibido:"
                        println server_data
                        server_data_char_list.add(server_data)
                    }
                }
               
                server_data_stream = server_data_char_list.toArray(Byte) 
                server_ack_responses = extract_messages_from_stream(server_data_stream)
                //println "TCPClient recibe: " + input.readLine()
                def i;
                for (i = 0; i < server_ack_responses.size(); i ++) {
                    println "Respuesta recibida del servidor:";
                    println server_ack_responses[i];
                    received_messages_count += 1;
                }
                if (received_messages_count == 3) {
                    break;
                }
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

client_messages = ["Hola mundo 1", "Hola mundo 2", "Hola mundo 3"] as String[]
println client_messages
//byte[] mllp_stream = []
//list_mllp_stream = mllp_stream.toList()
def ArrayList list_mllp_stream = new ArrayList();
for (idx = 0; idx < client_messages.size(); idx++) {
    print client_messages[idx]
    mllp_message = mllp.create_mllp_message(client_messages[idx])
    mllp_message_list = mllp_message.toList()
    list_mllp_stream.addAll(mllp_message_list)
    //for (index = 0; index < message_stream.size(); index++) {
    //    mllp_stream.add(message_stream[index])
    //}
}

//def param = list_mllp_stream as byte[]
def mllp_stream = list_mllp_stream as int[]
//println(list_mllp_stream.getClass())
def client = new TCPClient(9090, "127.0.0.1", mllp_stream)

