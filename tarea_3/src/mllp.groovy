public class mllp {
    // Helper class con los caracteres y un metodo
    // de conveniencia para generar mensajes usando MLLP
    static START_BLOCK = 11
    static END_BLOCK = 28
    static CARRIAGE_RETURN = 13

    static byte[] create_mllp_message(String message) {
        // Generar, a partir de un String, un array
        // de caracteres que incluye los separadores
        // requeridos por MLLP
        def mllp_message = []
        def msg_list = message.getBytes().toList()
        mllp_message.addAll([this.START_BLOCK])
        mllp_message.addAll(msg_list)
        mllp_message.addAll([this.END_BLOCK])
        mllp_message.addAll([this.CARRIAGE_RETURN])
        def char_retval = mllp_message.toArray()
        return char_retval as byte[]
    }
}
