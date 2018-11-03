#! -*- coding: utf-8 -*-
import socket

from config import SERVER_HOST, SERVER_PORT
from mllp_helpers import START_BLOCK, END_BLOCK, CARRIAGE_RETURN, \
    create_mllp_message, extract_messages_from_stream

if __name__ == "__main__":
    client_messages = ["Hola mundo 1", "Hola mundo 2", "Hola mundo 3"]
    mllp_stream = ""
    for client_message in client_messages:
        mllp_stream = mllp_stream + create_mllp_message(client_message)

    client_sck = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_sck.connect((SERVER_HOST, SERVER_PORT))
   
    print "Enviando tres mensajes en formato MLLP"
    client_sck.send(mllp_stream)

    received_messages_count = 0
    while True:
        # La llamada a recv() es bloqueante, significa que el proceso
        # del cliente quedará bloqueado hasta que reciba una respuesta
        # del server
        server_data = client_sck.recv(1024)
        # Se recibio respuesta del server al mensaje enviado
        # continua la ejecucion del cliente
        server_messages = extract_messages_from_stream(server_data)
        for server_message in server_messages:
            print "Respuesta recibida del server: '{}'".format(server_message)
            received_messages_count += 1
        if received_messages_count == 3:
            # Se recibió el ack del 3er mensaje, salgo del ciclo
            break

    client_sck.close()
