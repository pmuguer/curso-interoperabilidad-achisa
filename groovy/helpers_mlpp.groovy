def create_mlpp_message(message) {
    START_BLOCK = [11];
    END_BLOCK = [28];
    CARRIAGE_RETURN = [13];

    mlpp_message = []
    msg_list = message.getBytes().toList()
    mlpp_message.addAll(START_BLOCK)
    mlpp_message.addAll(msg_list)
    mlpp_message.addAll(END_BLOCK)
    mlpp_message.addAll(CARRIAGE_RETURN)

    char_retval = mlpp_message.toArray()
    return char_retval
}

msg_mlpp = create_mlpp_message("Hola mundo")
println(msg_mlpp)