class Globals {
    static START_BLOCK = 11
    static END_BLOCK = 28
    static CARRIAGE_RETURN = 13
}

def create_mlpp_message(message) {
    mlpp_message = []
    msg_list = message.getBytes().toList()
    mlpp_message.addAll([Globals.START_BLOCK])
    mlpp_message.addAll(msg_list)
    mlpp_message.addAll([Globals.END_BLOCK])
    mlpp_message.addAll([Globals.CARRIAGE_RETURN])

    char_retval = mlpp_message.toArray()
    return char_retval
}

def extract_messages_from_stream(stream) {
    messages = []
    new_message = ""

    if (stream.size() < 3) {
        throw new Exception("El formato del mensaje es incorrecto")
    }
    for (idx=0; idx < stream.size(); idx++) {
        msg_byte = stream[idx]

        if (msg_byte == Globals.START_BLOCK) {
            new_message = ""
        } else if (msg_byte == Globals.END_BLOCK) {
            if (idx == stream.length - 1) {
                throw Exception("El formato del mensaje es incorrecto")
            }
            if (stream[idx + 1] == Globals.CARRIAGE_RETURN) {
                messages.add(new_message)
            } else {
                throw Exception("El formato es incorrecto")
            }
        } else if (msg_byte == Globals.CARRIAGE_RETURN) {
            // No hay que hacer nada
        } else {
            new_message += msg_byte
        }
    }
    return messages
}

msg_mlpp = create_mlpp_message("Hola mundo")
println(msg_mlpp)
messages_list = extract_messages_from_stream(msg_mlpp)
println(messages_list)
