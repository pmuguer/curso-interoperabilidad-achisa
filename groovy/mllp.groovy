public class mllp {
    static START_BLOCK = 11
    static END_BLOCK = 28
    static CARRIAGE_RETURN = 13

    static byte[] create_mllp_message(String message) {
        def mllp_message = []
        def msg_list = message.getBytes().toList()
        mllp_message.addAll([this.START_BLOCK])
        mllp_message.addAll(msg_list)
        mllp_message.addAll([this.END_BLOCK])
        mllp_message.addAll([this.CARRIAGE_RETURN])
        def char_retval = mllp_message.toArray()
        return char_retval as byte[]
    }

    static String[] extract_messages_from_stream(byte[] stream) {
        def messages = []
        def new_message = ""
    
        if (stream.size() < 3) {
            throw new Exception("El formato del mensaje es incorrecto")
        }
        def idx = 0
        for (idx=0; idx < stream.size(); idx++) {
            def msg_byte = stream[idx]
    
            if (msg_byte == mllp.START_BLOCK) {
                new_message = ""
            } else if (msg_byte == mllp.END_BLOCK) {
                if (idx == stream.length - 1) {
                    throw Exception("El formato del mensaje es incorrecto")
                }
                if (stream[idx + 1] == mllp.CARRIAGE_RETURN) {
                    messages.add(new_message)
                } else {
                    throw Exception("El formato es incorrecto")
                }
            } else if (msg_byte == mllp.CARRIAGE_RETURN) {
                // No hay que hacer nada
            } else {
                new_message += new String(msg_byte)
            }
        }
        return messages
    }
}
