import socket

from config import SERVER_HOST, SERVER_PORT
from mllp_helpers import START_BLOCK, END_BLOCK, CARRIAGE_RETURN, encode_message

if __name__ == "__main__":
    client_sck = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_sck.connect((SERVER_HOST, SERVER_PORT))
   
    client_messages = ["Hola mundo 1", "Hola mundo 2", "Hola mundo 3"]
    for client_message in client_messages:
        encoded_message = encode_message(client_message)
        print "Enviando mensaje (en formato MLLP): -> '{}'".format(client_message)
        client_sck.send(encoded_message)
        server_data = client_sck.recv(1024)
        server_message = ""
        for idx in range(0, len(server_data)):
            if server_data[idx] == START_BLOCK:
                server_message = ""
            elif server_data[idx] == END_BLOCK and server_data[idx+1] == CARRIAGE_RETURN:
                print "Respuesta recibida del server: '{}'".format(
                    server_message)
            else:
                if server_data[idx] != CARRIAGE_RETURN:
                    server_message = server_message + server_data[idx]
    
    client_sck.close()
