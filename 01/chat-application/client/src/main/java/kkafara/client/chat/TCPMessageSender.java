package kkafara.client.chat;

import kkafara.data.model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.io.PrintWriter;

public class TCPMessageSender implements MessageSender {
  private final PrintWriter mWriter;
  private final Logger mLogger;

  public TCPMessageSender(OutputStream outputStream) {
    mWriter = new PrintWriter(outputStream, true);
    mLogger = LogManager.getLogger(TCPMessageSender.class);
  }

  @Override
  public boolean send(Message message) {
    mWriter.println(message.encodeToString());
    return true;
  }
}
