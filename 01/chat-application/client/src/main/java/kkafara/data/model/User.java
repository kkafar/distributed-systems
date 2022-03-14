package kkafara.data.model;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class User {
  private final long mID;

  @NotNull
  private final String mName;

  public User(long id, @NotNull String name) {
    mID = id;
    mName = name;
  }

  public long getID() { return mID; }

  public String getName() { return mName; }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return mName.equals(user.mName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mName);
  }
}
