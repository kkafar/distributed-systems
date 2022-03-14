package kkafara.server.data.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Message {
  public enum Type {
    TCP, UDP;
  }

  public static final int ID_SIZE = 32;
  public static final int CONTENT_SIZE = 8;
  public static final int MAX_CONTENT_SIZE = 512;

  private final int mLength;
  private final User mUser;
  private final String mContent;

  @Nullable
  private int mPort;

  @Nullable
  private Type mType;

  public Message(User user, String content) {
    mUser = user;
    mContent = content;
    mLength = mContent.length();
    mType = null;
  }

  public Message (User user, char[] content) {
    mUser = user;
    mContent = new String(content);
    mLength = mContent.length();
    mType = null;
  }

  public Type getType() {
    return mType;
  }

  public void setType(@NotNull Type type) {
    mType = type;
  }

  public int getPort() {
    return mPort;
  }

  public void setPort(int port) {
    mPort = port;
  }

  public User getUser() { return mUser; }

  public String getContent() { return mContent; }

  public int getLength() { return mLength; }

  public static Message parseFromCharArray(char[] message) {
    char[] idBuff = new char[ID_SIZE];
    char[] lengthBuff = new char[CONTENT_SIZE];

    for (int i = 0; i < ID_SIZE; ++i) {
      idBuff[i] = message[i];
    }
    String userName = new String(idBuff).strip();

//    System.out.println(userName);

    for (int i = 0; i < CONTENT_SIZE; ++i) {
      lengthBuff[i] = message[i + ID_SIZE];
    }

    int contentLength = -1;
    try {
      contentLength = Integer.parseInt(new String(lengthBuff).strip());
    } catch (NumberFormatException exception) {
    }

//    System.out.println(contentLength);

    if (contentLength > MAX_CONTENT_SIZE || contentLength < 0) {
      // TODO: handle this case
      System.out.println("Invalid content length");
      return null;
    }

    char[] content = new char[contentLength];
    for (int i = 0; i < contentLength; ++i) {
      content[i] = message[i + ID_SIZE + CONTENT_SIZE];
    }

    return new Message(new User(0, userName), content);
  }

  public static Message parseFromString(String message) {
    return parseFromCharArray(message.toCharArray());
  }

  public String encodeToString() {
    StringBuilder builder = new StringBuilder(ID_SIZE + CONTENT_SIZE + mContent.length());

    builder.append(mUser.getName());

    if (mUser.getName().length() < ID_SIZE) {
      builder.append(" ".repeat(ID_SIZE - mUser.getName().length()));
    }

    String contentLengthAsString = Integer.toString(mContent.length());
    builder.append(contentLengthAsString);

    if (contentLengthAsString.length() < CONTENT_SIZE) {
      builder.append(" ".repeat(CONTENT_SIZE - contentLengthAsString.length()));
    }

    builder.append(mContent);
    return builder.toString();
  }
}
