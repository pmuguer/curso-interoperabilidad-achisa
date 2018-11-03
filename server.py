#! -*- coding: utf-8 -*-
import socket

from config import SERVER_HOST, SERVER_PORT
from mllp_helpers import START_BLOCK, END_BLOCK, CARRIAGE_RETURN, encode_message

def init_socket():
    """Inicialización del socket"""
    server_sck = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_sck.bind((SERVER_HOST, SERVER_PORT))
    return server_sck

if __name__ == "__main__":
    server_sck = init_socket()
    print "Server listening at: {}:{}".format(SERVER_HOST, SERVER_PORT)
    print ""

    # Esperar conexiones al socket
    server_sck.listen(10)
    
    conn, addr = server_sck.accept()
    print "Un cliente llamó a socket.connect(). Origen: {}".format(addr)
    print ""
    while True:
        print "Server bloqueado esperando mensajes del cliente"
        # Llamada bloqueante a recv(). El server no hará nada hasta
        # que el cliente llame a socket.send().
        client_data = conn.recv(1024)
        if not client_data:
            # El cliente cerró la conexión mediante socket.close(),
            # se debe salir del loop
            break
        # Se recibieron datos del cliente, procesarlos
        client_message = ""
        for idx in range(0, len(client_data)):
            if client_data[idx] == START_BLOCK:
                client_message = ""
            elif client_data[idx] == END_BLOCK and client_data[idx+1] == CARRIAGE_RETURN:
                number = client_message[-1]
                print "Mensaje recibido del cliente '{}'".format(
                    client_message)
                client_message = ""
                 
                server_response = "Ok {}".format(number)
                encoded_server_response = encode_message(server_response)
                print "Enviando respuesta (en formato MLLP) -> '{}'".format(server_response)
                conn.send(encoded_server_response)
            else:
                if client_data[idx] != CARRIAGE_RETURN:
                    client_message = client_message + client_data[idx]
