import config
import MLLPClient

client_messages = ["Hola mundo 1", "Hola mundo 2", "Hola mundo 3"]
def client = new MLLPClient(config.SERVER_PORT, config.SERVER_HOST, client_messages)
