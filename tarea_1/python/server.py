#! -*- coding: utf-8 -*-
import socket

from config import SERVER_HOST, SERVER_PORT
from mllp_helpers import START_BLOCK, END_BLOCK, CARRIAGE_RETURN, \
    create_mllp_message, extract_messages_from_stream

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
        # Llamada bloqueante a recv(). El server no hará nada hasta
        # que el cliente llame a socket.send().
        client_data = conn.recv(1024)
        if not client_data:
            # El cliente cerró la conexión mediante socket.close(),
            # se debe salir del loop
            break
        messages = extract_messages_from_stream(client_data)
        for message in messages:
            print "Mensaje recibido del cliente: '{}'; " + \
                "respondiendo con ACK".format(message)
            msg_number = message[-1]
            ack_message = "Ok {}".format(msg_number)
            mllp_ack_message = create_mllp_message(ack_message)
            conn.send(mllp_ack_message)
