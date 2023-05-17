package com.example.english.entities.keys;

import java.io.Serializable;
import java.util.Objects;

public class LikeBlogKey implements Serializable {
  private Long user;
  private Long blog;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LikeBlogKey)) {
      return false;
    }
    LikeBlogKey that = (LikeBlogKey) o;
    return Objects.equals(user, that.user) && Objects.equals(blog, that.blog);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, blog);
  }
}
