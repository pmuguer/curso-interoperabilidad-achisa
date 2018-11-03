#! -*- coding: utf-8 -*-
import socket

SERVER_HOST = "127.0.0.1"
SERVER_PORT = 9090

ASCII_11 = b'\x0B'
ASCII_28 = b'\x1C'
ASCII_13 = b'\x0D'

SB = ASCII_11
EB = ASCII_28
CR = ASCII_13

if __name__ == "__main__":
    server_sck = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_sck.bind((SERVER_HOST, SERVER_PORT))
    print "Server listening at: {}:{}".format(SERVER_HOST, SERVER_PORT)
    print ""
    server_sck.listen(10)
    
    def encode_message(msg):
        retval = SB + msg + EB + CR
        return retval
    
    conn, addr = server_sck.accept()
    print "connected by: {}".format(addr)
    print ""
    while True:
        client_data = conn.recv(1024)
        if not client_data:
            break
        client_message = ""
        for idx in range(0, len(client_data)):
            if client_data[idx] == SB:
                client_message = ""
            elif client_data[idx] == EB and client_data[idx+1] == CR:
                number = client_message[-1]
                print "Mensaje recibido del cliente '{}'".format(
                    client_message)
                client_message = ""
               
                 
                server_response = "Ok {}".format(number)
                encoded_server_response = encode_message(server_response)
                print "Enviando respuesta (en formato MLLP) -> '{}'".format(server_response)
                conn.send(encoded_server_response)
            else:
                if client_data[idx] != CR:
                    client_message = client_message + client_data[idx]
