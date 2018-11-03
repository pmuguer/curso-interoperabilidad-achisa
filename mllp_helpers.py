#
"""Helpers para server y cliente MLLP
"""

ASCII_11 = b'\x0B'
ASCII_28 = b'\x1C'
ASCII_13 = b'\x0D'

START_BLOCK = ASCII_11
END_BLOCK = ASCII_28
CARRIAGE_RETURN = ASCII_13

class MLLPException(Exception):
    pass

def create_mllp_message(message):
    """Crear un mensaje MLLP agregando los caracteres delimitadores
    """
    mllp_message = START_BLOCK + message + \
        END_BLOCK + CARRIAGE_RETURN
    return mllp_message

def extract_messages_from_stream(stream):
    messages = []
    new_message = ""
    if len(stream) < 3:
        raise MLLPException("asdads")
    for idx in range(0, len(stream)):
        byte = stream[idx]
        if byte == START_BLOCK:
            new_message = ""   
        elif byte == END_BLOCK:
            if idx == len(stream) - 1:
                raise MLLPException("asdasd")
            if stream[idx + 1] == CARRIAGE_RETURN:
                messages.append(new_message)
        elif byte == CARRIAGE_RETURN:
            pass
        else:
            new_message += byte
    return messages
