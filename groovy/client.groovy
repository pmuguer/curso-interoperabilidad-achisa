import mllp

import java.lang.reflect.Array

class TCPClient {
    def TCPClient(int serverPort, String serverIP, ArrayList message_stream) {
        // Crea socket de conexion con el servidor,
        // especificando direccion IP y puerto del servidor.
        // "localhost" equivale a la IP 127.0.0.1
        def socket = new Socket(InetAddress.getByName(serverIP), serverPort)
        println "TCPClient: conectado a " + socket.getRemoteSocketAddress()
        def input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        def output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        try {
            // Escribe en el socket
            output.write( message_stream ) // Agrega \n al final, el server hace readLine.
            output.flush()
            received_messages_count = 0;
            while (true) {
                server_data = input.read()
                server_messages = extract_messages_from_stream(server_data)
                //println "TCPClient recibe: " + input.readLine()
                def i;
                for (i = 0; i < server_messages.size(); i ++) {
                    println "Respuesta recibida del servidor:";
                    println server_messages[i];
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
byte[] mllp_stream = []
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
//mllp_stream = list_mllp_stream as byte[]
println(list_mllp_stream.getClass())
def client = new TCPClient(9090, "127.0.0.1", list_mllp_stream)

