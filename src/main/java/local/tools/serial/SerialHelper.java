package local.tools.serial;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import local.tools.encode.Base64Helper;

public class SerialHelper<Type extends Serializable> {

    /*
     * Serialization process as follows
     * Object -> byte[] -> [Zip] -> Base64 -> String
     */
    public String SerializeInBase64(Type item) throws Exception {
        byte[] data = serialize(item);
        //data = ZipHelper.gZipData(data);
        return Base64Helper.encode(data);
    }
    
    /*
     * Serialization process as follows
     * Object -> byte[] -> [Zip] -> Base64 -> String
     */
    public static String SerializeInBase64(byte[] data) {
        //data = ZipHelper.gZipData(data);
        return Base64Helper.encode(data);
    }

    /*
     * Deserialization process
     * String -> Base64 -> [UnZip] -> byte[] - Object
     */
    public Type DeserializeFromBase64(String base64data) throws Exception {
        byte[] data = Base64Helper.decode(base64data);
        //data = ZipHelper.unGZipData(data);
        return deserialize(data);
    }

    public byte[] serialize(Type object) throws Exception {
        try {
            ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
            try (ObjectOutput out = new ObjectOutputStream(byteOS)) {
                out.writeObject(object);
                out.flush();
            }

            return byteOS.toByteArray();
        } catch(Exception e) {
            throw new Exception("Unable to serialize data. " + e.getMessage());
        }
    }

    public Type deserialize(byte[] data) throws Exception {
        try {
            ByteArrayInputStream byteIS = new ByteArrayInputStream(data);
            Type typeObj;
            try (ObjectInputStream ois = new ObjectInputStream(byteIS)) {
                Object obj = ois.readObject();
                typeObj = (Type) obj;
            }
            return typeObj;
        } catch (IOException | ClassNotFoundException e) {
            throw new Exception("Unable to deserialize data. " + e.getMessage());
        }
    }
}
