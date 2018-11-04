import mllp
import mllpbuffer

def byte_list_to_int_list(byte_list) {
    // Convertir la lista de bytes a ints
    def retval = []
    for (i = 0; i < byte_list.size(); i++) {
        retval.add(byte_list[i] as int)
    }
    return retval
}

msg = "Pruebas de conversion de tipos de datos"
println "Clase de 'msg': " + msg.getClass()
msg_char_list = msg.toList()
println "Clase de 'msg_char_list': " + msg_char_list.getClass()
println msg_char_list
msg_byte_list = msg.getBytes()
//msg_int_list = msg_char_list.toArray(Byte)
//println msg.getBytes()
println "Clase de 'msg_byte_list': " + msg_byte_list.getClass()
println msg_byte_list

int_list = byte_list_to_int_list(msg_byte_list)
println "Clase de 'int_list': " + int_list.getClass()
println int_list
println "Clase un elemento de 'int_list': " + int_list[0].getClass()

def buf = new mllpbuffer()
mensaje1 = "Hola mundo 1"
def mllp_message1 = mllp.create_mllp_message(mensaje1)
mensaje2 = "Hola mundo 1"
def mllp_message2 = mllp.create_mllp_message(mensaje1)
mensaje3 = "Hola mundo 1"
def mllp_message3 = mllp.create_mllp_message(mensaje1)

int_list1 = byte_list_to_int_list(mllp_message1)
println "int_list1 = " + int_list1

for (j = 0; j < int_list1.size(); j++) {
    buf.add_to_buffer(int_list1[j])
}

mensaje_del_buffer = buf.pop_message()
print mensaje_del_buffer
//buf.add_to_buffer(10)
//buf.pop_message()
//buf.add_to_buffer(mllp.START_BLOCK)
//buf.pop_message()
//buf.add_to_buffer(25)
//buf.pop_message()
//buf.add_to_buffer(100)
//buf.add_to_buffer(mllp.END_BLOCK)
//buf.add_to_buffer(mllp.CARRIAGE_RETURN)
//buf.add_to_buffer(30)
//res = buf.pop_message()
//print res
//buf.add_to_buffer(50)
//buf.add_to_buffer(50)
//buf.add_to_buffer(50)
//res = buf.pop_message()
//print res
