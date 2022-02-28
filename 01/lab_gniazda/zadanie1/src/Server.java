import javax.naming.ldap.SortKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {
  public static void main(String[] args) throws IOException {
    System.out.println("JAVA UDP SERVER");
    DatagramSocket socket = null;
    int portNumber = 9008;

    try {
      socket = new DatagramSocket(portNumber);
      byte[] receiveBuffer = new byte[1024];

      while (true) {
        Arrays.fill(receiveBuffer, (byte)0);
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        socket.receive(receivePacket);
        String msg = new String(receivePacket.getData());
        System.out.println("received msg: " + msg);

        // send response (echo response for simplicity)
        DatagramPacket sendPacket = new DatagramPacket(
            receiveBuffer,
            receiveBuffer.length,
            receivePacket.getAddress(),
            receivePacket.getPort()
        );

        socket.send(sendPacket);
        System.out.println("send msg: " + msg);
      }
    } catch (Exception e){
      e.printStackTrace();
    } finally {
      if (socket != null) {
        socket.close();
      }
    }
  }
}
