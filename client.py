import socket

client_sck = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_sck.connect(("127.0.0.1", 9090))

ASCII_11 = b'\x0B'
ASCII_28 = b'\x1C'
ASCII_13 = b'\x0D'

SB = ASCII_11
EB = ASCII_28
CR = ASCII_13

def encode_message(msg):
    retval = [11] + [char for char in msg] + [28] + [13]
    return bytearray(retval)

client_messages = ["Hola mundo 1", "Hola mundo 2", "Hola mundo 3"]

class EndOfMessage(Exception):
    pass

class Buffer(object):
    def __init__(self, data):
        self.idx = 0
        self.data = data

    def extract_message(self):
        if self.idx == len(self.data):
            raise EndOfMessage
        if self.data[self.idx] != SB:
            raise Exception, "Error de formato; el mensaje no empieza con <SB>"
        start_idx = self.idx + 1
        extracted_message = ""
        for idx in range(start_idx, len(self.data)):
            self.idx = idx
            if self.data[idx] == EB:
                if self.data[idx+1] == CR:
                    break
                else:
                    raise Exception, "Error de formato; el mensaje no termina con <EB><CR>"
            else:
                extracted_message += self.data[idx]
        if self.idx == len(self.data):
            raise Exception, "Error de formato; el mensaje no termina con <EB><CR>"
        else:
            self.idx += 1
    

for client_message in client_messages:
    encoded_message = encode_message(client_message)
    client_sck.send(encoded_message)
    server_data = client_sck.recv(1024)
    server_message = ""
    for idx in range(0, len(server_data)):
        if server_data[idx] == SB:
            server_message = ""
        elif server_data[idx] == EB and server_data[idx+1] == CR:
            print server_message
        else:
            if server_data[idx] != CR:
                server_message = server_message + server_data[idx]
#for client_message in client_messages:
#    encoded_message = encode_message(client_message)
#    client_sck.send(encoded_message)
#    # Recibo respuesta del server al mensaje
#    server_data = client_sck.recv(1024)
#    
#    #server_message = ""
#    #for idx in range(0, len(server_data)):
#    #    if server_data[idx] == SB:
#    #        server_message = ""
#    #    elif server_data[idx] == EB and server_data[idx+1] == CR:
#    #        print server_message
#    #    else:
#    #        if server_data[idx] != CR:
#    #            server_message = server_message + server_data[idx]
#    buf = Buffer(server_data)
#    while True:
#        try:
#            server_message = buf.extract_message()
#        except EndOfMessage:
#            print server_message

client_sck.close()
