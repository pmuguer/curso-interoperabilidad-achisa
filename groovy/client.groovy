import mlpp

class TCPClient {
    def TCPClient(int serverPort, String serverIP)
    {
        // Crea socket de conexion con el servidor,
        // especificando direccion IP y puerto del servidor.
        // "localhost" equivale a la IP 127.0.0.1
        def socket = new Socket(InetAddress.getByName(serverIP), serverPort)
        println "TCPClient: conectado a " + socket.getRemoteSocketAddress()
        def input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        def output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        try
        {
            // Escribe en el socket
            output.writeLine( "Pablo" ) // Agrega \n al final, el server hace readLine.
            output.flush()
            println "TCPClient recibe: " + input.readLine()
        }
        catch (Exception e)
        {
            println e.message
        }
        finally
        {
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
list_mllp = mllp_stream.toList()
for (idx = 0; idx < client_messages.size(); idx++) {
    print client_messages[idx]
    message_stream = mlpp.create_mlpp_message("asdadasdadasd")
    ms = message_stream.toList()
    list_mllp.addAll(ms)
    //for (index = 0; index < message_stream.size(); index++) {
    //    mllp_stream.add(message_stream[index])
    //}
}
