import config
import mllp
import MLLPClient

// Mensajes a enviar
client_messages = ["Hola mundo 1", "Hola mundo 2", "Hola mundo 3"] as String[]
def ArrayList list_mllp_stream = new ArrayList();
for (idx = 0; idx < client_messages.size(); idx++) {
    // Genero los mensajes en formato MLLP
    mllp_message = mllp.create_mllp_message(client_messages[idx])
    mllp_message_list = mllp_message.toList()
    // Lista que contiene el "stream" con todos los mensajes que
    // se van a enviar, es decir: "<SB>Mensaje 1<EB><CR><SB>Mensaje 2 etc...
    list_mllp_stream.addAll(mllp_message_list)
}

// Convierto la lista original a una lista de enteros, que se enviarán como
// bytes a través del socket TCP
def mllp_stream = list_mllp_stream as int[]
def client = new MLLPClient(config.SERVER_PORT, config.SERVER_HOST, mllp_stream)
