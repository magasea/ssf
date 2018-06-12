package com.shellshellfish.aaas.zhongzhengapi.util;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

/**
 * Created by chenwei on 2018- 四月 - 26
 */

public class  ZZDeserializer<T> implements JsonDeserializer<T>{

  @Override
  public T deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    {
      // Get the "content" element from the parsed JSON
      JsonElement content = jsonElement.getAsJsonObject().get("data");

      // Deserialize it. You use a new instance of Gson to avoid infinite recursion
      // to this deserializer
      return new Gson().fromJson(content, type);

    }
  }
}
