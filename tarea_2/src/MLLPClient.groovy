import mllp
import mllpbuffer

class MLLPClient {
    def MLLPClient(message) {
        //def buffer = new mllpbuffer()
        def mllpMessage = mllp.create_mllp_message(message)
        //println("El cliente va a escribir el siguiente mensaje")
        //println(mllpMessage)
        def s = new Socket("localhost", 8181);
        s.withStreams { input, output ->
            //output << mllpMessage
            output.write(mllpMessage)
            //buffer = input.newReader().readLine()
            //println "response = $buffer"
        }
    }
}