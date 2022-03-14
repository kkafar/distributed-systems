package kkafara.server.chat;

import kkafara.server.data.model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.CharBuffer;

public class TCPMessageReceiver implements MessageReceiver {
  private final CharBuffer mBuffer;
  private final BufferedReader mReader;

  private final Logger mLogger;

  public TCPMessageReceiver(int bufferSize, InputStream inputStream) {
    mBuffer = CharBuffer.allocate(bufferSize);
    mReader = new BufferedReader(new InputStreamReader(inputStream));
    mLogger = LogManager.getLogger(TCPMessageReceiver.class);
  }

  @Override
  public Message receive() {
    mBuffer.clear();
    try {
      int bytesRead = mReader.read(mBuffer);
      if (bytesRead > mBuffer.capacity()) {
          mLogger.warn("Buffer size exceeded. Some data was most likely lost");
      } else if (bytesRead == -1) {
        mLogger.warn("No more data in input stream. Most likely client disconnected");
        return null;
      }
      return Message.parseFromCharArray(mBuffer.array());
    } catch (IOException exception) {
      if (exception.getMessage() != null) {
        mLogger.warn(exception.getMessage());
      }
      exception.printStackTrace();
      return null;
    }
  }
}
