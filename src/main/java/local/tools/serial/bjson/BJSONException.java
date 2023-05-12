package local.tools.serial.bjson;

public final class BJSONException extends RuntimeException {
    public BJSONException(String message) {
        super(message);
    }

    public BJSONException(Throwable t) {
        super(t.getMessage(), t);
    }
}
