package com.yoxel.aurinko;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.AurContact;
import com.yoxel.aurinko.bean.AurNativePropertiesSupport;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class ContactNativePropsTest {
  private static final JsonObjectParser JSON_PARSER = new JsonObjectParser(Utils.getDefaultJsonFactory());

  @Test
  public void testNativePropParsing() throws IOException {
    final var rawJson = "{\n" +
        "   \"nativeProperties\": {\n" +
        "      \"stringProp\": \"stringValue\",\n" +
        "      \"stringPropEmpty\": \"\",\n" +
        "      \"intProp\": 123,\n" +
        "      \"nullProp\": null,\n" +
        "      \"boolProp\": false,\n" +
        "      \"objProp\": {\n" +
        "         \"f1\": \"v1\",\n" +
        "         \"f2\": \"v2\"\n" +
        "      }\n" +
        "   }\n" +
        "}";

    final var parsed =
        JSON_PARSER.parseAndClose(new ByteArrayInputStream(rawJson.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8, TestObject.class);

    assertThat(parsed.getNativeProperty(String.class, "stringProp"), equalTo("stringValue"));
    assertThat(parsed.getNativeProperty(String.class, "stringPropEmpty"), equalTo(""));
    assertThat(parsed.getNativeProperty(Integer.class, "intProp"), equalTo(123));
    assertThat(parsed.getNativeProperty(String.class, "nullProp"), nullValue());
    assertThat(parsed.getNativeProperty(Boolean.class, "boolProp"), equalTo(false));

    assertThat(parsed.getNativePropertyObject("objProp").get("f1"), equalTo("v1"));
    assertThat(parsed.getNativePropertyObject("objProp").get("f2"), equalTo("v2"));
    assertThat(parsed.getNativePropertyObject("objPropNonEx"), nullValue());
  }

  public static class TestObject implements AurNativePropertiesSupport {
    @Key
    @Getter
    @Setter
    private Map<String, Object> nativeProperties;
  }
}
