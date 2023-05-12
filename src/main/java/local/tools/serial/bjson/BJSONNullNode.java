package local.tools.serial.bjson;

public class BJSONNullNode implements BJSONNode {
    
    public static final BJSONNode INSTANCE = new BJSONNullNode();

    private BJSONNullNode() {
    }

    @Override
    public BJSONType getType() {
        return BJSONType.NULL;
    }

    @Override
    public int getSize() {
        return getType().getSize();
    }
    
    @Override
    public String toString() {
        return "null";
    }
}
