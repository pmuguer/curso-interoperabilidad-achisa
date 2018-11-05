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
println "Mensaje: " + mensaje_del_buffer
