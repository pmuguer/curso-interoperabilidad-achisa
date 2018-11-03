#
"""Helpers para server y cliente MLLP
"""

ASCII_11 = b'\x0B'
ASCII_28 = b'\x1C'
ASCII_13 = b'\x0D'

START_BLOCK = ASCII_11
END_BLOCK = ASCII_28
CARRIAGE_RETURN = ASCII_13

def encode_message(msg):
    retval = START_BLOCK + msg + END_BLOCK + CARRIAGE_RETURN
    return retval
