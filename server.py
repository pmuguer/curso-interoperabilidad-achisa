import socket

server_sck = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

server_sck.bind(("127.0.0.1", 9090))
server_sck.listen(10)

ASCII_11 = b'\x0B'
ASCII_28 = b'\x1C'
ASCII_13 = b'\x0D'

SB = ASCII_11
EB = ASCII_28
CR = ASCII_13

def encode_message(msg):
    retval = [11] + [char for char in msg] + [28] + [13]
    return bytearray(retval)


conn, addr = server_sck.accept()
print "connected by: {}".format(addr)
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
            print client_message
            client_message = ""
            server_response = "Ok " + str(number)
            encoded_server_response = encode_message(server_response)
            #server_response = SB + "Ok " + str(number) + EB + CR
            conn.send(encoded_server_response)
        else:
            if client_data[idx] != CR:
                client_message = client_message + client_data[idx]
