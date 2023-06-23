package com.yoxel.aurinko.bean;

import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public interface AurNativePropertiesSupport {
  Map<String, Object> getNativeProperties();

  void setNativeProperties(Map<String, Object> nativeProperties);

  @SuppressWarnings("unchecked")
  default <T> T getNativeProperty(Class<T> type, String name) {
    if (getNativeProperties() == null) {
      return null;
    }
    final var obj = getNativeProperties().get(name);
    if (obj == null || com.google.api.client.util.Data.isNull(obj)) {
      return null;
    }

    if (Integer.class.isAssignableFrom(type)) {
      return type.cast(((BigDecimal) obj).intValue());
    }

    if (Long.class.isAssignableFrom(type)) {
      return type.cast(((BigDecimal) obj).longValue());
    }

    if (Double.class.isAssignableFrom(type)) {
      return type.cast(((BigDecimal) obj).doubleValue());
    }

    if (!type.isInstance(obj)) {
      throw new IllegalArgumentException(
          "Wrong type for property" + name + ". Expected " + type.getName() + ", got " + obj.getClass().getName() + ": " + obj
      );
    }

    return (T) obj;
  }

  @SuppressWarnings("unchecked")
  default Map<String, Object> getNativePropertyObject(String name) {
    return (Map<String, Object>) getNativeProperty(Map.class, name);
  }

  @SuppressWarnings("unchecked")
  default List<Object> getNativePropertyArray(String name) {
    return (List<Object>) getNativeProperty(List.class, name);
  }

  default void setNativeProperty(String name, Object value) {
    Validate.notNull(name, "name");

    if (getNativeProperties() == null) {
      setNativeProperties(new HashMap<>());
    }

    getNativeProperties().put(name, value);
  }
}
