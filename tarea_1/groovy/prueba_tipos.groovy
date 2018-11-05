a = [1,2,3]
b = [1,2,6]

byte[] z = [100, 100]
byte[] pq = [23, 24]

a.addAll(b)
println a
n = z.toList()
o = pq.toList()
println n
n.addAll(pq)
println(n)

n.add(77)
println n
n.addAll([55,55])
println(n)

def array_bytes = n.toArray(Byte)
println(array_bytes)


listado_de_enteros = [65,66]
println "listado_de_enteros: " + listado_de_enteros
noseque_de_bytes = listado_de_enteros.toArray(Byte)
println "noseque_de_bytes: " + noseque_de_bytes
byte[] array_de_bytes = noseque_de_bytes
println "array_de_bytes_de_bytes: " + array_de_bytes
string_convertido = new String(array_de_bytes)
println "string_convertido: " + string_convertido

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


