package data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public abstract class AbstractBaseEntity {

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}
