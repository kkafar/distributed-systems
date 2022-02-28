from encodings import utf_8
import socket

serverIP = "127.0.0.1"
serverPort = 9008

msg = 'żółta gęś'

msg_bytes = bytes(msg, 'utf-8')

print('PYTHON UDP CLIENT')
client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
client.sendto(msg_bytes, (serverIP, serverPort))







