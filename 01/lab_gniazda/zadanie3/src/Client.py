from encodings import utf_8
import socket

print('PYTHON UDP CLIENT')

serverIP = "127.0.0.1"
serverPort = 9008

msg_bytes = (300).to_bytes(4, byteorder='little')

client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
client.sendto(msg_bytes, (serverIP, serverPort))

received_bytes, address = client.recvfrom(1024)

# print(received_bytes)

received_int = int.from_bytes(received_bytes, byteorder='little')

print(received_int)