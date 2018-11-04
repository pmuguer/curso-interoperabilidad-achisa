public class mlpp {
    static START_BLOCK = 11
    static END_BLOCK = 28
    static CARRIAGE_RETURN = 13

    static int[] create_mlpp_message(message) {
        def mlpp_message = []
        def msg_list = message.getBytes().toList()
        mlpp_message.addAll([this.START_BLOCK])
        mlpp_message.addAll(msg_list)
        mlpp_message.addAll([this.END_BLOCK])
        mlpp_message.addAll([this.CARRIAGE_RETURN])
        def char_retval = mlpp_message.toArray()
        println char_retval
        return char_retval
        //return 10
        //return 10
    }

    static String[] extract_messages_from_stream(stream) {
        def messages = []
        def new_message = ""
    
        if (stream.size() < 3) {
            throw new Exception("El formato del mensaje es incorrecto")
        }
        def idx = 0
        for (idx=0; idx < stream.size(); idx++) {
            def msg_byte = stream[idx]
    
            if (msg_byte == mlpp.START_BLOCK) {
                new_message = ""
            } else if (msg_byte == mlpp.END_BLOCK) {
                if (idx == stream.length - 1) {
                    throw Exception("El formato del mensaje es incorrecto")
                }
                if (stream[idx + 1] == mlpp.CARRIAGE_RETURN) {
                    messages.add(new_message)
                } else {
                    throw Exception("El formato es incorrecto")
                }
            } else if (msg_byte == mlpp.CARRIAGE_RETURN) {
                // No hay que hacer nada
            } else {
                new_message += msg_byte
            }
        }
        return messages
    }
}

//msg_mlpp = create_mlpp_message("Hola mundo")
//println(msg_mlpp)
//messages_list = extract_messages_from_stream(msg_mlpp)
//println(messages_list)
