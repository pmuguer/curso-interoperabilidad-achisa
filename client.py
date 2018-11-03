import socket

client_sck = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
#client_sck.connect(("127.0.0.1", 9090))
client_sck.connect(("127.0.0.1", 9090))


ASCII_11 = b'\x0B'
ASCII_28 = b'\x1C'
ASCII_13 = b'\x0D'

SB = ASCII_11
EB = ASCII_28
CR = ASCII_13

def encode_message(msg):
    retval = SB + msg + EB + CR
    return retval

if __name__ == "__main__":
    client_messages = ["Hola mundo 1", "Hola mundo 2", "Hola mundo 3"]
    
    for client_message in client_messages:
        encoded_message = encode_message(client_message)
        print "Enviando mensaje (en formato MLLP): -> '{}'".format(client_message)
        client_sck.send(encoded_message)
        server_data = client_sck.recv(1024)
        server_message = ""
        for idx in range(0, len(server_data)):
            if server_data[idx] == SB:
                server_message = ""
            elif server_data[idx] == EB and server_data[idx+1] == CR:
                print "Respuesta recibida del server: '{}'".format(
                    server_message)
            else:
                if server_data[idx] != CR:
                    server_message = server_message + server_data[idx]
    
    client_sck.close()
