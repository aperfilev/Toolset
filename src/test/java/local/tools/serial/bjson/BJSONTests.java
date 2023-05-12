package local.tools.serial.bjson;

import local.tools.io.File;
import local.tools.io.FilePath;
import local.tools.logs.Logger;
import local.tools.serial.json.discovery.JSONArray;
import local.tools.serial.json.discovery.JSONObject;
import local.tools.serial.primitives.*;
import local.tools.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BJSONTests {

    //TODO Plans:
    // - [x] Make more convenient access methods for Int## - like types
    // - [x] Make JSON - to/from - BJSON converter
    // - [x] Try saving Strings as "abc\0" with EOL terminating symbol instead of prefix length

    // - [ ] Try not create redundant objects:
    // - [ ] Check readers to read using cached byte[] values
    // - [ ] Resolve memory overuse:
    // - [ ] Try not using wrapper objects Float64
    // - [ ] Try not using wrapper objects BJSONNodeFloat64
    // - [ ] Try create ArrayLists with exact size
    // - [ ] Try reduce number complexity if possible - float64 -> float32 its a single accuracy value
    // - [ ] Try reduce number complexity if possible - int64 -> int32 its a single accurecy value
    // - [ ] Try find better name

    @Test
    public void testBasicJavaTypes() throws Exception {
        Logger.printf(new Object(){}.getClass().getEnclosingMethod().getName());
        BJSONObject original = new BJSONObject();
        original.put("null", (Object)null);
        original.put("string1", "=123ТЕС=");
        original.put("string2", "1234567890abcABC");
        original.put("string3", "1234567890абвгдАБВГД");
        original.put("minByte", Byte.MIN_VALUE);
        original.put("maxByte", Byte.MAX_VALUE);
        original.put("minShort", Short.MIN_VALUE);
        original.put("maxShort", Short.MAX_VALUE);
        original.put("minInt", Integer.MIN_VALUE);
        original.put("maxInt", Integer.MAX_VALUE);
        original.put("minLong", Long.MIN_VALUE);
        original.put("maxLong", Long.MAX_VALUE);
        original.put("minFloat", Float.MIN_VALUE);
        original.put("maxFloat", Float.MAX_VALUE);
        original.put("floatPInf", Float.POSITIVE_INFINITY);
        original.put("floatNInf", Float.NEGATIVE_INFINITY);
        original.put("floatNaN", Float.NaN);
        original.put("minDouble", Double.MIN_VALUE);
        original.put("maxDouble", Double.MAX_VALUE);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BJSONWriter writer = new BJSONWriter(output);
        writer.writeBJSONNode(original);
        writer.close();

//        writer = new BJSONWriter("bjson.bin");
//        writer.writeBJSONNode(original);
//        writer.close();

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        BJSONReader reader = new BJSONReader(input);
        BJSONObject duplicate = (BJSONObject) reader.readBJSONNode();
        reader.close();

        assertEquals(original.get("null"), duplicate.get("null"));
        assertEquals(original.getString("string1"), duplicate.getString("string1"));
        assertEquals(original.getString("string2"), duplicate.getString("string2"));
        assertEquals(original.getString("string3"), duplicate.getString("string3"));
        assertEquals(original.getByte("minByte"), duplicate.getByte("minByte"));
        assertEquals(original.getByte("maxByte"), duplicate.getByte("maxByte"));
        assertEquals(original.getShort("minShort"), duplicate.getShort("minShort"));
        assertEquals(original.getShort("maxShort"), duplicate.getShort("maxShort"));
        assertEquals(original.getInt("minInt"), duplicate.getInt("minInt"));
        assertEquals(original.getInt("maxInt"), duplicate.getInt("maxInt"));
        assertEquals(original.getLong("minLong"), duplicate.getLong("minLong"));
        assertEquals(original.getLong("maxLong"), duplicate.getLong("maxLong"));
        assertEquals(original.getFloat("minFloat"), duplicate.getFloat("minFloat"));
        assertEquals(original.getFloat("maxFloat"), duplicate.getFloat("maxFloat"));
        assertEquals(original.getFloat("floatPInf"), duplicate.getFloat("floatPInf"));
        assertEquals(original.getFloat("floatNInf"), duplicate.getFloat("floatNInf"));
        assertEquals(original.getFloat("floatNaN"), duplicate.getFloat("floatNaN"));
        assertEquals(original.getDouble("minDouble"), duplicate.getDouble("minDouble"));
        assertEquals(original.getDouble("maxDouble"), duplicate.getDouble("maxDouble"));
        Logger.print(" OK");
    }

    @Test
    public void testSignedPrimitives() throws FileNotFoundException, IOException, Exception {
        Logger.printf(new Object(){}.getClass().getEnclosingMethod().getName());
        BJSONObject original = new BJSONObject();
        original.put("minInt8",  Int8.MIN_VALUE);
        original.put("maxInt8",  Int8.MAX_VALUE);
        original.put("minInt16",  Int16.MIN_VALUE);
        original.put("maxInt16",  Int16.MAX_VALUE);
        original.put("minInt32",  Int32.MIN_VALUE);
        original.put("maxInt32",  Int32.MAX_VALUE);
        original.put("minInt64",  Int64.MIN_VALUE);
        original.put("maxInt64",  Int64.MAX_VALUE);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BJSONWriter writer = new BJSONWriter(output);
        writer.writeBJSONNode(original);
        writer.close();

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        BJSONReader reader = new BJSONReader(input);
        BJSONObject duplicate = (BJSONObject) reader.readBJSONNode();
        reader.close();

        assertEquals(original.getByte("minInt8"), duplicate.getByte("minInt8"));
        assertEquals(original.getByte("maxInt8"), duplicate.getByte("maxInt8"));
        assertEquals(original.getShort("minInt16"), duplicate.getShort("minInt16"));
        assertEquals(original.getShort("minInt16"), duplicate.getShort("minInt16"));
        assertEquals(original.getInt("minInt32"), duplicate.getInt("minInt32"));
        assertEquals(original.getInt("minInt32"), duplicate.getInt("minInt32"));
        assertEquals(original.getLong("minInt64"), duplicate.getLong("minInt64"));
        assertEquals(original.getLong("minInt64"), duplicate.getLong("minInt64"));
        Logger.print(" OK");
    }

    @Test
    public void testUnsignedPrimitives() throws FileNotFoundException, IOException, Exception {
        Logger.printf(new Object(){}.getClass().getEnclosingMethod().getName());
        BJSONObject original = new BJSONObject();
        original.put("minUInt8",  new UInt8(UInt8.MIN_VALUE));
        original.put("maxUInt8",  new UInt8(UInt8.MAX_VALUE));
        original.put("minUInt16",  new UInt16(UInt16.MIN_VALUE));
        original.put("maxUInt16",  new UInt16(UInt16.MAX_VALUE));
        original.put("minUInt32",  new UInt32(UInt32.MIN_VALUE));
        original.put("maxUInt32",  new UInt32(UInt32.MAX_VALUE));
        original.put("minUInt64",  new UInt64(UInt64.MIN_VALUE));
        original.put("maxUInt64",  new UInt64(UInt64.MAX_VALUE));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BJSONWriter writer = new BJSONWriter(output);
        writer.writeBJSONNode(original);
        writer.close();

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        BJSONReader reader = new BJSONReader(input);
        BJSONObject duplicate = (BJSONObject) reader.readBJSONNode();
        reader.close();

        assertEquals(original.getUInt8("minUInt8"), duplicate.getUInt8("minUInt8"));
        assertEquals(original.getUInt8("minUInt8"), duplicate.getUInt8("minUInt8"));
        assertEquals(original.getUInt16("minUInt16"), duplicate.getUInt16("minUInt16"));
        assertEquals(original.getUInt16("minUInt16"), duplicate.getUInt16("minUInt16"));
        assertEquals(original.getUInt32("minUInt32"), duplicate.getUInt32("minUInt32"));
        assertEquals(original.getUInt32("minUInt32"), duplicate.getUInt32("minUInt32"));
        assertEquals(original.getUInt64("minUInt64"), duplicate.getUInt64("minUInt64"));
        assertEquals(original.getUInt64("minUInt64"), duplicate.getUInt64("minUInt64"));

        Logger.print(" OK");
    }

    @Test
    public void testBJSONArray() throws FileNotFoundException, IOException, Exception {
        Logger.printf(new Object(){}.getClass().getEnclosingMethod().getName());
        BJSONArray original = new BJSONArray();
        original.put("=123ТЕС=");
        original.put("1234567890abcABC");
        original.put("1234567890абвгдАБВГД");
        original.put(Byte.MIN_VALUE);
        original.put(Byte.MAX_VALUE);
        original.put(Short.MIN_VALUE);
        original.put(Short.MAX_VALUE);
        original.put(Integer.MIN_VALUE);
        original.put(Integer.MAX_VALUE);
        original.put(Long.MIN_VALUE);
        original.put(Long.MAX_VALUE);
        original.put(Float.MIN_VALUE);
        original.put(Float.MAX_VALUE);
        original.put(Float.POSITIVE_INFINITY);
        original.put(Float.NEGATIVE_INFINITY);
        original.put(Float.NaN);
        original.put(Double.MIN_VALUE);
        original.put(Double.MAX_VALUE);
        original.put((Object)null);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BJSONWriter writer = new BJSONWriter(output);
        writer.writeBJSONNode(original);
        writer.close();

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        BJSONReader reader = new BJSONReader(input);
        BJSONArray duplicate = (BJSONArray) reader.readBJSONNode();
        reader.close();

        assertEquals(original.getString(0), duplicate.getString(0));
        assertEquals(original.getString(1), duplicate.getString(1));
        assertEquals(original.getString(2), duplicate.getString(2));
        assertEquals(original.getByte(3), duplicate.getByte(3));
        assertEquals(original.getByte(4), duplicate.getByte(4));
        assertEquals(original.getShort(5), duplicate.getShort(5));
        assertEquals(original.getShort(6), duplicate.getShort(6));
        assertEquals(original.getInt(7), duplicate.getInt(7));
        assertEquals(original.getInt(8), duplicate.getInt(8));
        assertEquals(original.getLong(9), duplicate.getLong(9));
        assertEquals(original.getLong(10), duplicate.getLong(10));
        assertEquals(original.getFloat(11), duplicate.getFloat(11));
        assertEquals(original.getFloat(12), duplicate.getFloat(12));
        assertEquals(original.getFloat(13), duplicate.getFloat(13));
        assertEquals(original.getFloat(14), duplicate.getFloat(14));
        assertEquals(original.getFloat(15), duplicate.getFloat(15));
        assertEquals(original.getDouble(16), duplicate.getDouble(16));
        assertEquals(original.getDouble(17), duplicate.getDouble(17));
        assertEquals(original.get(18), duplicate.get(18));
        Logger.print(" OK");
    }

    @Test
    public void testSubObject() throws FileNotFoundException, IOException, Exception {
        Logger.printf(new Object(){}.getClass().getEnclosingMethod().getName());
        BJSONObject original = new BJSONObject();
        BJSONObject subobject = new BJSONObject();
        original.put("subobject", subobject);
        subobject.put("string1", "=123ТЕС=");
        subobject.put("string2", "1234567890abcABC");
        subobject.put("string3", "1234567890абвгдАБВГД");
        subobject.put("minByte", Byte.MIN_VALUE);
        subobject.put("maxByte", Byte.MAX_VALUE);
        subobject.put("minShort", Short.MIN_VALUE);
        subobject.put("maxShort", Short.MAX_VALUE);
        subobject.put("minInt", Integer.MIN_VALUE);
        subobject.put("maxInt", Integer.MAX_VALUE);
        subobject.put("minLong", Long.MIN_VALUE);
        subobject.put("maxLong", Long.MAX_VALUE);
        subobject.put("minFloat", Float.MIN_VALUE);
        subobject.put("maxFloat", Float.MAX_VALUE);
        subobject.put("floatPInf", Float.POSITIVE_INFINITY);
        subobject.put("floatNInf", Float.NEGATIVE_INFINITY);
        subobject.put("floatNaN", Float.NaN);
        subobject.put("minDouble", Double.MIN_VALUE);
        subobject.put("maxDouble", Double.MAX_VALUE);
        original.put("field", "AFTERFIELD");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BJSONWriter writer = new BJSONWriter(output);
        writer.writeBJSONNode(original);
        writer.close();

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        BJSONReader reader = new BJSONReader(input);
        BJSONObject duplicate = (BJSONObject) reader.readBJSONNode();
        reader.close();
        BJSONObject dup_subobject = duplicate.getJSONObject("subobject");

        assertEquals(subobject.getString("string1"), dup_subobject.getString("string1"));
        assertEquals(subobject.getString("string2"), dup_subobject.getString("string2"));
        assertEquals(subobject.getString("string3"), dup_subobject.getString("string3"));
        assertEquals(subobject.getByte("minByte"), dup_subobject.getByte("minByte"));
        assertEquals(subobject.getByte("maxByte"), dup_subobject.getByte("maxByte"));
        assertEquals(subobject.getShort("minShort"), dup_subobject.getShort("minShort"));
        assertEquals(subobject.getShort("maxShort"), dup_subobject.getShort("maxShort"));
        assertEquals(subobject.getInt("minInt"), dup_subobject.getInt("minInt"));
        assertEquals(subobject.getInt("maxInt"), dup_subobject.getInt("maxInt"));
        assertEquals(subobject.getLong("minLong"), dup_subobject.getLong("minLong"));
        assertEquals(subobject.getLong("maxLong"), dup_subobject.getLong("maxLong"));
        assertEquals(subobject.getFloat("minFloat"), dup_subobject.getFloat("minFloat"));
        assertEquals(subobject.getFloat("maxFloat"), dup_subobject.getFloat("maxFloat"));
        assertEquals(subobject.getFloat("floatPInf"), dup_subobject.getFloat("floatPInf"));
        assertEquals(subobject.getFloat("floatNInf"), dup_subobject.getFloat("floatNInf"));
        assertEquals(subobject.getFloat("floatNaN"), dup_subobject.getFloat("floatNaN"));
        assertEquals(subobject.getDouble("minDouble"), dup_subobject.getDouble("minDouble"));
        assertEquals(subobject.getDouble("maxDouble"), dup_subobject.getDouble("maxDouble"));
        assertEquals(original.getString("field"), duplicate.getString("field"));
        Logger.print(" OK");
    }

    @Test
    public void testSubArray() throws FileNotFoundException, IOException, Exception {
        Logger.printf(new Object(){}.getClass().getEnclosingMethod().getName());
        BJSONArray original = new BJSONArray();
        BJSONArray subarray = new BJSONArray();
        original.put(subarray);
        subarray.put("=123ТЕС=");
        subarray.put("1234567890abcABC");
        subarray.put("1234567890абвгдАБВГД");
        subarray.put(Byte.MIN_VALUE);
        subarray.put(Byte.MAX_VALUE);
        subarray.put(Short.MIN_VALUE);
        subarray.put(Short.MAX_VALUE);
        subarray.put(Integer.MIN_VALUE);
        subarray.put(Integer.MAX_VALUE);
        subarray.put(Long.MIN_VALUE);
        subarray.put(Long.MAX_VALUE);
        subarray.put(Float.MIN_VALUE);
        subarray.put(Float.MAX_VALUE);
        subarray.put(Float.POSITIVE_INFINITY);
        subarray.put(Float.NEGATIVE_INFINITY);
        subarray.put(Float.NaN);
        subarray.put(Double.MIN_VALUE);
        subarray.put(Double.MAX_VALUE);
        original.put("AFTERFIELD");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BJSONWriter writer = new BJSONWriter(output);
        writer.writeBJSONNode(original);
        writer.close();

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        BJSONReader reader = new BJSONReader(input);
        BJSONArray duplicate = (BJSONArray) reader.readBJSONNode();
        BJSONArray sub_duplicate = duplicate.getBJSONArray(0);
        reader.close();

        assertEquals(subarray.getString(0), sub_duplicate.getString(0));
        assertEquals(subarray.getString(1), sub_duplicate.getString(1));
        assertEquals(subarray.getString(2), sub_duplicate.getString(2));
        assertEquals(subarray.getByte(3), sub_duplicate.getByte(3));
        assertEquals(subarray.getByte(4), sub_duplicate.getByte(4));
        assertEquals(subarray.getShort(5), sub_duplicate.getShort(5));
        assertEquals(subarray.getShort(6), sub_duplicate.getShort(6));
        assertEquals(subarray.getInt(7), sub_duplicate.getInt(7));
        assertEquals(subarray.getInt(8), sub_duplicate.getInt(8));
        assertEquals(subarray.getLong(9), sub_duplicate.getLong(9));
        assertEquals(subarray.getLong(10), sub_duplicate.getLong(10));
        assertEquals(subarray.getFloat(11), sub_duplicate.getFloat(11));
        assertEquals(subarray.getFloat(12), sub_duplicate.getFloat(12));
        assertEquals(subarray.getFloat(13), sub_duplicate.getFloat(13));
        assertEquals(subarray.getFloat(14), sub_duplicate.getFloat(14));
        assertEquals(subarray.getFloat(15), sub_duplicate.getFloat(15));
        assertEquals(subarray.getDouble(16), sub_duplicate.getDouble(16));
        assertEquals(subarray.getDouble(17), sub_duplicate.getDouble(17));
        assertEquals(original.getString(1), duplicate.getString(1));
        Logger.print(" OK");
    }

    @ParameterizedTest
    @ValueSource(strings = {"big_obj.json"})
    public void testJSONObjectToBJSONConverter(String filename) throws FileNotFoundException, IOException, Exception {
        FilePath fp = FilePath.parse(filename);
        long filesize = File.length(filename) / (1024 * 1024);

        Logger.info("Testing speed comparison for '%s' file with size %d Mb.", filename, filesize);
        StopWatch sw = new StopWatch();
        sw.start();
        JSONObject jsonObj = new JSONObject(File.readAllText(filename));
        sw.stop();
        Logger.info("JSON parsing %d millis.", sw.peek());

        sw.reset();
        sw.start();
        BJSONObject bJsonObj = new BJSONObject(jsonObj);
        sw.stop();
        Logger.info("BJSON copy building %d millis.", sw.peek());

        sw.reset();
        sw.start();
        try (BJSONWriter writer = new BJSONWriter(fp.filepath("bjson"))) {
            writer.writeBJSONNode(bJsonObj);
        }
        sw.stop();
        Logger.info("BJSON writing %d millis.", sw.peek());

        sw.reset();
        sw.start();
        try (BJSONReader reader = new BJSONReader(fp.filepath("bjson"))) {
            BJSONObject testedBJSONObject = (BJSONObject) reader.readBJSONNode();
        }
        sw.stop();
        Logger.info("BJSON reading %d millis.", sw.peek());
        Logger.print(" OK");
    }

    @ParameterizedTest
    @ValueSource(strings = {"big_array.json"})
    public void testJSONArraytoBJSONConverter(String filename) throws FileNotFoundException, IOException, Exception {
        FilePath fp = FilePath.parse(filename);
        long filesize = File.length(filename) / (1024 * 1024);

        Logger.info("Testing speed comparison for '%s' file with size %d Mb.", filename, filesize);
        StopWatch sw = new StopWatch();
        sw.start();
        JSONArray jsonObj = new JSONArray(File.readAllText(filename));
        sw.stop();
        Logger.info("JSON parsing %d millis.", sw.peek());

        sw.reset();
        sw.start();
        BJSONArray bJsonObj = new BJSONArray(jsonObj);
        sw.stop();
        Logger.info("BJSON copy building %d millis.", sw.peek());

        sw.reset();
        sw.start();
        try (BJSONWriter writer = new BJSONWriter(fp.filepath("bjson"))) {
            writer.writeBJSONNode(bJsonObj);
        }
        sw.stop();
        Logger.info("BJSON writing %d millis.", sw.peek());

        sw.reset();
        sw.start();
        try (BJSONReader reader = new BJSONReader(fp.filepath("bjson"))) {
            BJSONArray testedBJSONObject = (BJSONArray) reader.readBJSONNode();
        }
        sw.stop();
        Logger.info("BJSON reading %d millis.", sw.peek());
        Logger.print(" OK");
    }

    public static void main(String[] args) throws Exception {
        BJSONTests tests = new BJSONTests();
        tests.testJSONArraytoBJSONConverter("studyareas_4M.json");
        tests.testJSONArraytoBJSONConverter("studyareas_50M.json");
        tests.testJSONArraytoBJSONConverter("studyareas_100M.json");
    }
}
