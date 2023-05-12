package local.tools.serial.json.discovery;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class ModifyTest {
    @Test
    public void changeStringTest() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":42,\"aval\":[2,2,4],\"oval\":{\"foo\":9}}";
        JSONObject obj = new JSONObject(str);
        assertEquals(obj.getString("foo"), "bar");
        obj.put("foo", "Hello World");
        assertEquals(obj.getString("foo"), "Hello World");
        assertEquals(obj.toString(), "{\"foo\":\"Hello World\",\"baz\":42,\"aval\":[2,2,4],\"oval\":{\"foo\":9}}");
    }

    @Test
    public void addStringTest() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":42}";
        JSONObject obj = new JSONObject(str);
        obj.put("test", "Hello World");
        assertEquals(obj.getString("test"), "Hello World");
        assertEquals(obj.getString("foo"), "bar");
        assertEquals(obj.toString(), "{\"foo\":\"bar\",\"baz\":42,\"test\":\"Hello World\"}");
    }

    @Test
    public void addObjectTest() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":42}";
        JSONObject root = new JSONObject();
        JSONObject obj = new JSONObject(str);
        root.put("test", obj);
        assertEquals(root.toString(), "{\"test\":{\"foo\":\"bar\",\"baz\":42}}");
        assertNotNull(obj.keys());
    }

    @Test
    public void addObjectByKeysTest() throws JSONException {
        String str = "{\"foo\":{\"bar\":9}}";
        JSONObject root = new JSONObject();
        JSONObject obj = new JSONObject(str);
        root.put("test", obj.get("foo"));
        assertNotNull(obj.toString());
    }

    @Test
    public void addObjectArray() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":42}";
        JSONArray root = new JSONArray();
        JSONObject obj = new JSONObject(str);
        root.put(obj);
        assertEquals(root.toString(), "[{\"foo\":\"bar\",\"baz\":42}]");
    }

    @Test
    public void addComplexStringTest() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":42}";
        JSONObject obj = new JSONObject(str);
        obj.put("test", "Hello \n\t\r\b\"\\\f World");
        assertEquals(obj.getString("test"), "Hello \n\t\r\b\"\\\f World");
        assertEquals(obj.getString("foo"), "bar");
        assertEquals(obj.toString(), "{\"foo\":\"bar\",\"baz\":42,\"test\":\"Hello \\n\\t\\r\\b\\\"\\\\\\f World\"}");
    }

    @Test
    public void testModifyKeys() throws JSONException {
        String str = "{}";
        JSONObject obj = new JSONObject(str);
        obj.put("foo", 9);
        obj.put("bar", 10);
        // obj.put("baz",11);
        Iterator<String> it = obj.keys();
        assertTrue(it.hasNext());
        assertEquals("foo", it.next());
        assertTrue(it.hasNext());
        assertEquals("bar", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testRemoveKeys() throws JSONException {
        String str = "{\"test\":42}";
        JSONObject obj = new JSONObject(str);
        obj.put("foo", 9);
        obj.put("bar", 10);
        obj.remove("test");
        // obj.put("baz",11);
        Iterator<String> it = obj.keys();
        assertTrue(it.hasNext());
        assertEquals("foo", it.next());
        assertTrue(it.hasNext());
        assertEquals("bar", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void buildObjectTest() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("foo", "bar");
        obj.put("baz", -42);
        obj.put("lval", 99l);
        obj.put("dval", 3.1415);
        obj.put("floatval", 3.1415f);
        obj.put("fval", false);
        obj.put("tval", true);
        obj.put("test", "Hello World");
        obj.put("nval", (Object)null);
        assertEquals(obj.getString("test"), "Hello World");
        assertEquals(obj.getInt("baz"), -42);
        assertEquals(obj.getLong("baz"), -42l);
        assertEquals(obj.getDouble("dval"), 3.1415, 0.0001);
        assertEquals(obj.getBoolean("fval"), false);
        assertEquals(obj.getBoolean("tval"), true);
        assertEquals(obj.getString("foo"), "bar");
    }

    @Test
    public void buildObjectFromObjectsTest() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("foo", (Object) "bar");
        obj.put("baz", (Integer) 42);
        obj.put("lval", (Long) 99l);
        obj.put("dval", (Double) 3.1415);
        obj.put("floatval", (Float) 3.1415f);
        obj.put("fval", (Boolean) false);
        obj.put("tval", (Boolean) true);
        obj.put("test", "Hello World");
        obj.put("nval", (Object)null);
        assertEquals(obj.getString("test"), "Hello World");
        assertEquals(obj.getInt("baz"), 42);
        assertEquals(obj.getLong("baz"), 42l);
        assertEquals(obj.getDouble("dval"), 3.1415, 0.0);
        assertEquals(obj.getBoolean("fval"), false);
        assertEquals(obj.getBoolean("tval"), true);
        assertEquals(obj.getString("foo"), "bar");
    }

    @Test
    public void buildArrayTest() throws JSONException {
        JSONArray arr = new JSONArray();
        arr.put("foo");
        arr.put(42);
        arr.put(99l);
        arr.put(3.1415f);
        arr.put(2.9);
        arr.put(true);
        arr.put(false);
        arr.put(new JSONArray("[2,2,4]"));
        arr.put(new JSONObject("{\"foo\":42}"));
        arr.put((Object)null);
        assertEquals(arr.getString(0), "foo");
        assertEquals(arr.getInt(1), 42);
        assertEquals(arr.getLong(2), 99l);
        assertEquals(arr.getDouble(3), 3.1415f, 0.0);
        assertEquals(arr.getDouble(4), 2.9, 0.0);
        assertEquals(arr.getBoolean(5), true);
        assertEquals(arr.getBoolean(6), false);
    }

    @Test
    public void serializeArrayTest() throws JSONException {
        JSONArray arr = new JSONArray("[null,{\"foo\":3},[2,2,4]]");
        arr.put(42);
        assertEquals(arr.toString(), "[null,{\"foo\":3},[2,2,4],42]");
    }

    @Test
    public void insertArrayTest() throws JSONException {
        JSONArray arr = new JSONArray("[0,1,2,3,4,5,6,7,8,9,10]");
        arr.add(0, "foo");
        arr.add(1, 42);
        arr.add(2, 99l);
        arr.add(3, 3.1415f);
        arr.add(4, 2.9);
        arr.add(5, true);
        arr.add(6, false);
        arr.add(7, new JSONArray("[2,2,4]"));
        arr.add(8, new JSONObject("{\"foo\":42}"));
        // arr.put(JSONObject.NULL);
        assertEquals(arr.getString(0), "foo");
        assertEquals(arr.getInt(1), 42);
        assertEquals(arr.getLong(2), 99l);
        assertEquals(arr.getDouble(3), 3.1415f, 0.0001);
        assertEquals(arr.getDouble(4), 2.9, 0.0001);
        assertEquals(arr.getBoolean(5), true);
        assertEquals(arr.getBoolean(6), false);
    }

    @Test
    public void removeKeyTest() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":{\"foo\":9}}";
        JSONObject obj = new JSONObject(str);
        obj.remove("baz");
        assertFalse(obj.containsKey("baz"));
        assertEquals(obj.getString("foo"), "bar");
    }

    @Test
    public void removeFirstKeyTest() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":{\"foo\":9}}";
        JSONObject obj = new JSONObject(str);
        obj.remove("foo");
        assertFalse(obj.containsKey("foo"));
        assertTrue(obj.containsKey("baz"));
    }

    @Test
    public void cleanArrayToObject() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":[9,2,4]}";
        JSONObject obj = new JSONObject(str);
        obj.put("test", obj.getJSONArray("baz"));
        assertEquals(obj.getJSONArray("test").getInt(0), 9);
    }

    @Test
    public void cleanArrayToArray() throws JSONException {
        String str = "[\"bar\",[9,2,4]]";
        JSONArray arr = new JSONArray(str);
        arr.put(arr.getJSONArray(1));
        assertEquals(arr.getJSONArray(2).getInt(0), 9);
    }

    @Test
    public void cleanObjectToArray() throws JSONException {
        String str = "[\"bar\",[9,2,4],{\"foo\":42}]";
        JSONArray arr = new JSONArray(str);
        arr.put(arr.getJSONObject(2));
        assertEquals(arr.getJSONObject(3).getInt("foo"), 42);
    }

    @Test
    public void cleanArrayToArrayIndex() throws JSONException {
        String str = "[\"bar\",[9,2,4]]";
        JSONArray arr = new JSONArray(str);
        arr.add(0, arr.getJSONArray(1));
        assertEquals(arr.getJSONArray(0).getInt(0), 9);
    }

    @Test
    public void cleanObjectToArrayIndex() throws JSONException {
        String str = "[\"bar\",[9,2,4],{\"foo\":42}]";
        JSONArray arr = new JSONArray(str);
        arr.add(0, arr.getJSONObject(2));
        assertEquals(arr.getJSONObject(0).getInt("foo"), 42);
    }

    @Test
    public void dirtyArrayToObject() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":[9,2,4]}";
        JSONObject obj = new JSONObject(str);
        obj.getJSONArray("baz").put(42);
        obj.put("test", obj.getJSONArray("baz"));
        assertEquals(obj.getJSONArray("test").getInt(3), 42);
    }

    @Test
    public void differentBufArrayToObject() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":[9,2,4]}";
        JSONObject obj = new JSONObject(str);
        String str2 = "[3,3,6,42]";
        obj.put("test", new JSONArray(str2));
        assertEquals(obj.getJSONArray("test").getInt(3), 42);
    }

    @Test
    public void differentBufDirtyArrayToObject() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":[9,2,4]}";
        JSONObject obj = new JSONObject(str);
        String str2 = "[3,3,6,42]";
        JSONArray arr = new JSONArray(str2);
        arr.put(0);
        obj.put("test", arr);
        assertEquals(obj.getJSONArray("test").getInt(3), 42);
    }

    @Test
    public void cleanObjectToObject() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":{\"foo\":9}}";
        JSONObject obj = new JSONObject(str);
        obj.put("test", obj.getJSONObject("baz"));
        assertEquals(obj.getJSONObject("test").getInt("foo"), 9);
    }

    @Test
    public void dirtyObjectToObjectSameBuf() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":{\"foo\":9}}";
        JSONObject obj = new JSONObject(str);
        obj.getJSONObject("baz").put("foo", 10);
        obj.put("test", obj.getJSONObject("baz"));
        assertEquals(obj.getJSONObject("test").getInt("foo"), 10);
    }

    @Test
    public void separateBufObjectToObject() throws JSONException {
        String str1 = "{\"foo\":\"bar\"}";
        String str2 = "{\"baz\":{\"foo\":9}}";
        JSONObject obj1 = new JSONObject(str1);
        JSONObject obj2 = new JSONObject(str2);

        obj1.put("test", obj2.getJSONObject("baz"));
        assertEquals(obj1.getJSONObject("test").getInt("foo"), 9);
    }
}