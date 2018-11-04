import mllp

public class mllpbuffer {
    // Clase para facilitar el procesamiento del stream de datos
    // entre el cliente y el server MLLP
    // Se guarda en un buffer cada byte recibido, y cuando se
    // llama a pop_message, si en el buffer hay un mensaje que cumple
    // con los criterios de validación, se devuelve el mensaje.
    public List buffer = [];
    public add_to_buffer(int value) {
        // Agregar un byte al buffer
        this.buffer.add(value)
    }
    public pop_message() {
        // Si existe un mensaje, devolverlo y quitar los
        // datos correspondientes del buffer
        def i = 0;
        def retval = [];
        for (i = 0; i < this.buffer.size(); i++) {
            if (this.buffer[i] == mllp.START_BLOCK) {
                def j = i;
                for (j = i; j < this.buffer.size(); j++) {
                    if (this.buffer[j] == mllp.CARRIAGE_RETURN) {
                        if (j == 0) {
                            // CR no puede ser el primer caracter
                            throw Exception("Mensaje mal formado")
                        }
                        if (this.buffer[j - 1] != mllp.END_BLOCK) {
                            // Antes de CR siempre tiene que venir EB
                            throw Exception("Mensaje mal formado")
                        }
                        // El mensaje no debe incluir los separadores,
                        // por eso uso los índices i+1 y j-1
                        retval = this.buffer.subList(i+1, j-1)
                        def sublist = [];
                        def sublist1 = [];
                        def sublist2 = [];
                        sublist1 = this.buffer.subList(0,i)
                        sublist2 = this.buffer.subList(j+1, this.buffer.size())
                        sublist.addAll(sublist1)
                        sublist.addAll(sublist2)
                        this.buffer = sublist
                    }
                }
            }
        }
        return retval
    }
}
