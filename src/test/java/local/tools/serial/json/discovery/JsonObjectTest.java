package local.tools.serial.json.discovery;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.Assert.*;

public class JsonObjectTest {

    @Test
    public void keysetTest() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":42}";
        JSONObject obj = new JSONObject(str);
        Set<String> keys = obj.keySet();
        assertTrue(keys.contains("foo"));
        assertTrue(keys.contains("baz"));
        assertFalse(keys.contains("bar"));
    }

    @Test
    public void keysetWithNullTest() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":42,\"test\":null}";
        JSONObject obj = new JSONObject(str);
        Set<String> keys = obj.keySet();
        assertTrue(keys.contains("foo"));
        assertTrue(keys.contains("baz"));
        assertFalse(keys.contains("bar"));
    }

    @Test
    public void testMissingFields() throws JSONException {
        String str = "{\"foo\":42}";
        JSONObject obj = new JSONObject(str);
        obj.getString("bar");
    }

    @Test
    public void objectGet() throws JSONException {
        String str = "{\"foo\":9,\"bar\":true,\"baz\":3.1415,\"sval\":\"hello world\",\"fval\":false,\"nval\":null,\"aval\":[2,2,4],\"oval\":{\"foo\":42},\"eval\":\"\\n\"}";
        JSONObject obj = new JSONObject(str);
        assertTrue(obj.get("foo") instanceof Integer);
        assertTrue(obj.get("bar") instanceof Boolean);
        assertTrue(obj.get("baz") instanceof Double);
        assertTrue(obj.get("sval") instanceof String);
        assertTrue(obj.get("fval") instanceof Boolean);
        assertEquals(obj.get("nval"), null);
        assertTrue(obj.get("aval") instanceof JSONArray);
        assertTrue(obj.get("oval") instanceof JSONObject);
        assertTrue(obj.get("eval") instanceof String);
    }

    @Test
    public void objectOpt() throws JSONException {
        String str = "{\"foo\":9,\"bar\":true,\"baz\":3.1415,\"sval\":\"hello world\",\"fval\":false,\"nval\":null,\"aval\":[2,2,4],\"oval\":{\"foo\":42},\"eval\":\"\\n\"}";
        JSONObject obj = new JSONObject(str);
        assertTrue(obj.get("foo") instanceof Integer);
        assertTrue(obj.get("bar") instanceof Boolean);
        assertTrue(obj.get("baz") instanceof Double);
        assertTrue(obj.get("sval") instanceof String);
        assertTrue(obj.get("fval") instanceof Boolean);
        assertEquals(obj.get("nval"), null);
        assertTrue(obj.get("aval") instanceof JSONArray);
        assertTrue(obj.get("oval") instanceof JSONObject);
        assertTrue(obj.get("eval") instanceof String);
        assertNull(obj.get("does-not-exist"));
    }

    @Test
    public void testRyansSample() throws JSONException {
        String str = "{\"data\":{\"blah\":9},\"header\":{}}";
        JSONObject obj = new JSONObject(str);
        assertTrue(obj.containsKey("header"));
        assertTrue(obj.containsKey("data"));
        assertEquals(obj.toString(), str);
    }

    @Test
    public void testDeepNesting() throws JSONException {
        String str = "{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":42}}}}}}}}}}}}}}}}";
        JSONObject obj = new JSONObject(str);
        for (int i = 0; i < 15; i++) {
            obj = obj.getJSONObject("foo");
            assertNotNull(obj);
        }
        assertEquals(42, obj.getInt("foo"));
    }

    @Test
    public void testHas() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":{\"key\":42}}";
        JSONObject obj = new JSONObject(str);
        assertTrue(obj.containsKey("foo"));
        assertFalse(obj.containsKey("bar"));
        assertTrue(obj.containsKey("baz"));
        assertFalse(obj.containsKey("key"));
        assertFalse(obj.containsKey("random"));
    }

    @Test
    public void testKeys() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":{\"key\":42}}";
        JSONObject obj = new JSONObject(str);
        Iterator<String> it = obj.keys();
        assertTrue(it.hasNext());
        assertEquals("foo", it.next());
        assertTrue(it.hasNext());
        assertEquals("baz", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testEmptyKeys() throws JSONException {
        String str = "{}";
        JSONObject obj = new JSONObject(str);
        Iterator<String> it = obj.keys();
        assertFalse(it.hasNext());
    }

    @Test
    public void testNoMoreKeys() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":{\"key\":42}}";
        JSONObject obj = new JSONObject(str);
        Iterator<String> it = obj.keys();
        assertTrue(it.hasNext());
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            it.next();
            it.next();
            it.next();
            it.next();
        });
    }

    @Test
    public void testLength() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":{\"key\":42}}";
        JSONObject obj = new JSONObject(str);
        assertEquals(2, obj.size());
        assertEquals(2, obj.size());
        str = "{}";
        obj = new JSONObject(str);
        assertEquals(0, obj.size());
    }

    @Test
    public void testStringFields() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":\"\",\"bonk\":\"\\t\\b\\r\\n\\\\\\\"\\f\"}";
        JSONObject obj = new JSONObject(str);
        String value = obj.getString("foo");
        assertNotNull(value);
        assertEquals(value, "bar");
        value = obj.getString("baz");
        assertNotNull(value);
        assertEquals(value, "");
        assertEquals(obj.getString("bonk"), "\t\b\r\n\\\"\f");
    }

    @Test
    public void testIntegerFields() throws JSONException {
        String str = "{\"foo\":999,\"bar\":0,\"baz\":42,\"bonk\":-378}";
        JSONObject obj = new JSONObject(str);
        assertEquals(999, obj.getInt("foo"));
        assertEquals(0, obj.getInt("bar"));
        assertEquals(42, obj.getInt("baz"));
        assertEquals(-378, obj.getInt("bonk"));
    }

    @Test
    public void testLongFields() throws JSONException {
        String str = "{\"foo\":999,\"bar\":0,\"baz\":42,\"bonk\":-378,\"crazy\":12147483647}";
        JSONObject obj = new JSONObject(str);
        assertEquals(999, obj.getLong("foo"));
        assertEquals(0, obj.getLong("bar"));
        assertEquals(42, obj.getLong("baz"));
        assertEquals(-378, obj.getLong("bonk"));
        assertEquals(12147483647l, obj.getLong("crazy"));

    }

    @Test
    public void testDoubleFields() throws JSONException {
        String str = "{\"foo\":3.1415,\"bar\":0.0,\"baz\":1.2345e+1,\"bonk\":-3.78}";
        JSONObject obj = new JSONObject(str);
        assertEquals(3.1415d, obj.getDouble("foo"), 0);
        assertEquals(0.0, obj.getDouble("bar"), 0);
        assertEquals(12.345, obj.getDouble("baz"), 0);
        assertEquals(-3.78, obj.getDouble("bonk"), 0);
    }

    @Test
    public void testBooleanFields() throws JSONException {
        String str = "{\"foo\":false,\"bar\":true}";
        JSONObject obj = new JSONObject(str);
        assertEquals(false, obj.getBoolean("foo"));
        assertEquals(true, obj.getBoolean("bar"));
    }

    @Test
    public void testNonBooleanFields() throws JSONException {
        String str = "{\"foo\":false,\"bar\":42}";
        JSONObject obj = new JSONObject(str);
        obj.getBoolean("bar");
    }

    @Test
    public void testObjectSpaces() throws JSONException {
        String str = " {    \"foo\" :\"bar\" ,   \"baz\":  42}   ";
        JSONObject obj = new JSONObject(str);
        assertEquals("bar", obj.getString("foo"));
        assertEquals(42, obj.getInt("baz"));
    }

    @Test
    public void testObjectTabs() throws JSONException {
        String str = "\t{\t\"foo\"\t:\"bar\"\t,\t\t\"baz\":\t42\t}\t";
        JSONObject obj = new JSONObject(str);
        assertEquals("bar", obj.getString("foo"));
        assertEquals(42, obj.getInt("baz"));
    }

    @Test
    public void testNestedObjects() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":{\"test\":9}}";
        JSONObject obj = new JSONObject(str);
        obj = obj.getJSONObject("baz");
        assertNotNull(obj);
        assertEquals(9, obj.getInt("test"));
    }

    @Test
    public void testDeepNestedObjects() throws JSONException {
        String str = "{\"foo\":\"bar\",\"baz\":{\"test\":9,\"test2\":{\"id\":100},\"second\":33}}";
        JSONObject obj = new JSONObject(str);
        obj = obj.getJSONObject("baz");
        assertNotNull(obj);
        assertEquals(9, obj.getInt("test"));
        obj = obj.getJSONObject("test2");
        assertNotNull(obj);
        assertEquals(100, obj.getInt("id"));
    }

    @Test
    public void testJSONOrgSample1() throws JSONException {
        String str = "{\n    \"glossary\": {\n        \"title\": \"example glossary\",\n        \"GlossDiv\": {\n            \"title\": \"S\",\n            \"GlossList\": {\n                \"GlossEntry\": {\n                    \"ID\": \"SGML\",\n                    \"SortAs\": \"SGML\",\n                    \"GlossTerm\": \"Standard Generalized Markup Language\",\n                    \"Acronym\": \"SGML\",\n                    \"Abbrev\": \"ISO 8879:1986\",\n                    \"GlossDef\": {\n                        \"para\": \"A meta-markup language, used to create markup languages such as DocBook.\",\n                        \"GlossSeeAlso\": [\"GML\", \"XML\"]\n                    },\n                    \"GlossSee\": \"markup\"\n                }\n            }\n        }\n    }}";
        JSONObject obj = new JSONObject(str);
        JSONObject glo = obj.getJSONObject("glossary");
        assertNotNull(glo);
        assertEquals("example glossary", glo.getString("title"));
    }

    @Test
    public void testJSONOrgSample2() throws JSONException {
        String str = "{\"menu\": {\n  \"id\": \"file\",\n  \"value\": \"File\",\n  \"popup\": {\n    \"menuitem\": [\n      {\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"},      {\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},\n      {\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}\n    ]\n  }\n}}";
        JSONObject obj = new JSONObject(str);
        JSONObject m = obj.getJSONObject("menu");
        assertNotNull(m);
        assertEquals("file", m.getString("id"));
        m = m.getJSONObject("popup");
        assertNotNull(m);
        JSONArray a = m.getJSONArray("menuitem");
        assertNotNull(a);
        JSONObject o = a.getJSONObject(1);
        assertNotNull(o);
        assertEquals("Open", o.getString("value"));
    }

    @Test
    public void testComplexObject() throws Exception {
        String str = "{" +
                "\"Type\":\"rating\"," +
                "\"IsDisabled\":false," +
                "\"EventID\":\"deadbeef-dead-beef-dead-beef00000001\"," +
                "\"Record\":{" +
                "\"Item\":{" +
                "\"ID\":2983980," +
                "\"Rating\":5," +
                "\"Type\":null" +
                "}," +
                "\"User\":{" +
                "\"ID\":478830012," +
                "\"First\":\"Ben\"," +
                "\"Last\":\"Boolean\"," +
                "\"Email\":\"foo@test.local\"," +
                "\"Title\":\"Chief Blame Officer\"," +
                "\"Company\":\"DoubleDutch\"," +
                "\"Department\":null" +
                "}" +
                "}" +
                "}";

        JSONObject obj = new JSONObject(str);
        JSONObject record = obj.getJSONObject("Record");
        assertNotNull(record);
        JSONObject item = record.getJSONObject("Item");
        assertEquals(item.getInt("ID"), 2983980);
        assertEquals("rating", obj.getString("Type"));
        JSONObject user = record.getJSONObject("User");
        assertNotNull(user);
        assertEquals("Ben", user.getString("First"));
        assertEquals("DoubleDutch", user.getString("Company"));
        assertTrue(user.containsKey("Department"));
    }
}