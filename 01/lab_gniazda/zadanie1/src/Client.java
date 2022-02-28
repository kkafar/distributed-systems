import javax.xml.crypto.Data;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class Client {
  public static void main(String[] args) {
    System.out.println("JAVA UDP CLIENT");
    DatagramSocket socket = null;
    int portNumber = 9008;

    try {
      socket = new DatagramSocket();
      InetAddress address = InetAddress.getByName("localhost");
      byte[] sendBuffer = "Ping Java Udp".getBytes();
      byte[] receiveBuffer = new byte[1024];
      Arrays.fill(receiveBuffer, (byte)0);

      DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, portNumber);
      socket.send(sendPacket);

      // await server response
      DatagramPacket serverResponsePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length, address, portNumber);
      socket.receive(serverResponsePacket);
      String serverResponse = new String(serverResponsePacket.getData());

      System.out.println("received msg: " + serverResponse);
    } catch(Exception e){
      e.printStackTrace();
    } finally {
      if (socket != null) {
        socket.close();
      }
    }
  }
}
