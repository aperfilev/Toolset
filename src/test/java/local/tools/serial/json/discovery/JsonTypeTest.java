package local.tools.serial.json.discovery;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonTypeTest {
    @Test
    public void testObjectValueTypes() throws JSONException {
        JSONObject obj = new JSONObject("{\"foo\":42,\"bar\":3.1415,\"baz\":\"Hello World!\",\"baz2\":\"\\n\",\"bonk\":false,\"test\":null,\"obj\":{},\"arr\":[]}");
        assertTrue(obj.get("foo") instanceof Integer);
        assertTrue(obj.get("bar") instanceof Double);
        assertTrue(obj.get("baz") instanceof String);
        assertTrue(obj.get("baz2") instanceof String);
        assertTrue(obj.get("bonk") instanceof Boolean);
        assertTrue(obj.get("test") == null);
        assertTrue(obj.get("obj") instanceof JSONObject);
        assertTrue(obj.get("arr") instanceof JSONArray);
    }

    @Test
    public void testArrayValueTypes() throws JSONException {
        JSONArray arr = new JSONArray("[42,3.1415,\"Hello World!\",false,null,{},[],\"\\n\"]");
        assertTrue(arr.get(0) instanceof Integer);
        assertTrue(arr.get(1) instanceof Double);
        assertTrue(arr.get(2) instanceof String);
        assertTrue(arr.get(3) instanceof Boolean);
        assertTrue(arr.get(4) == null);
        assertTrue(arr.get(5) instanceof JSONObject);
        assertTrue(arr.get(6) instanceof JSONArray);
        assertTrue(arr.get(7) instanceof String);
    }
}