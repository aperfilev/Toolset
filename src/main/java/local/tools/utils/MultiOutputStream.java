package local.tools.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MultiOutputStream extends OutputStream {

    /**
     * Output streams.
     */
    private List<OutputStream> outputs;

    /**
     * Default constructor. Constructs a multiple output stream with no
     * underlying output streams. Data written to this stream is ignored until
     * an underlying output stream is established by calling
     * {@link #addOutput addOutput()}.
     */
    public MultiOutputStream() {
    }

    /**
     * Constructor. Contructs a multiple output stream with a single underlying
     * output stream.
     *
     * @param	out An output stream, to which all the output of this object is
     * written.
     */
    public MultiOutputStream(OutputStream out) {
        addOutput(out);
    }

    /**
     * Constructor. Contructs a multiple output stream with two underlying
     * output streams.
     *
     * @param	out1 An output stream, to which all the output of this object is
     * written.
     *
     * @param	out2 Another output stream, to which all the output of this object
     * is written.
     */
    public MultiOutputStream(OutputStream out1, OutputStream out2) {
        addOutput(out1);
        addOutput(out2);
    }

    /**
     * Add an output stream to this object.
     *
     * @param	out An output stream, to which all the output of this object is
     * written.
     *
     * @return The index that the specified output stream (<tt>out</tt>) has
     * within this object. Note that the first output stream added has an index
     * of 0.
     *
     * @throws	NullPointerException (unchecked) Thrown if <tt>out</tt> is null.
     */
    public synchronized int addOutput(OutputStream out) {
        // Sanity check
        if (out == null) {
            throw new NullPointerException("Null output stream");
        }

        // Add an output stream to the list of outputs
        if (outputs == null) {
            outputs = new ArrayList<>(4);
        }

        outputs.add(out);
        return outputs.size() - 1;
    }

    /**
     * Retrieve an output stream from this object.
     *
     * @param	n Index of the underlying output stream to retrieve. The index
     * number is the value returned by a previous call to
     * {@link #addOutput addOutput()}.
     *
     * @return The index that the specified output stream (<tt>out</tt>) has
     * within this object. Note that the first output stream added has an index
     * of 0.
     *
     * @throws	ArrayIndexOutOfBoundsException (unchecked) Thrown if <tt>n</tt>
     * does not specify a valid output stream index.
     */
    public OutputStream getOutput(int n) {
        // Range check
        if (outputs == null || n < 0 || n >= outputs.size()) {
            throw new ArrayIndexOutOfBoundsException("Bad output stream index: " + n);
        }

        // Get an output stream from the list of outputs
        return outputs.get(n);
    }

    /**
     * Flush all the output streams of this object. All the underlying output
     * streams are flushed, forcing any pending output data to be written.
     *
     * @throws	IOException Thrown if flushing any of the underlying output
     * streams causes an
     * <tt>IOException</tt> to be thrown. If this happens, the first exception
     * thrown is re-thrown by this method after all the underlying streams
     * are flushed.
     */
    @Override
    public void flush() throws IOException {
        if (outputs == null) return;

        IOException err = null;
        for (OutputStream output : outputs) {
            try {
                output.flush();
            } catch (IOException ex) {
                if (err == null) {
                    err = ex;
                }
            }
        }

        // Handle any caught exceptions
        if (err != null) {
            throw err;
        }
    }

    /**
     * Close all the output streams of this object. All the underlying output
     * streams are flushed and closed, and then forgotten, so this object will
     * no longer have any output streams assigned to it. Any subsequent writes
     * to this object will therefore be ignored.
     *
     * @throws	IOException Thrown if closing any of the underlying output
     * streams causes an
     * <tt>IOException</tt> to be thrown. If this happens, the first exception
     * thrown is re-thrown by this method after all the underlying streams
     * are closed.
     */
    @Override
    public void close() throws IOException {
        if (outputs == null) return;

        IOException err = null;
        // Close all the output streams in the list of outputs
        for (OutputStream output : outputs) {
            try {
                output.close();
            } catch (IOException ex) {
                if (err == null) {
                    err = ex;
                }
            }
        }

        // Discard the list of outputs
        outputs = null;

        // Handle any caught exceptions
        if (err != null) {
            throw err;
        }
    }

    /**
     * Write a byte to the output streams of this object.
     *
     * @param	b An output byte.
     *
     * @throws	IOException Thrown if any of the underlying output streams throws
     * this exception.
     */
    @Override
    public void write(int b) throws IOException {
        if (outputs == null) return;

        IOException err = null;
        for (OutputStream output : outputs) {
            try {
                output.write(b);
            } catch (IOException ex) {
                if (err == null) {
                    err = ex;
                }
            }
        }

        // Handle any caught exceptions
        if (err != null) {
            throw err;
        }
    }

    /**
     * Write a set of bytes to the output streams of this object. This is
     * identical to the call:
     * <pre>
     *    {@link #write(byte[], int, int) write}(buf, 0, buf.length)</pre>
     *
     * @param	buf An array of bytes to be written.
     *
     * @throws	IOException Thrown if any of the underlying output streams throws
     * this exception.
     */
    @Override
    public void write(byte[] buf) throws IOException {
        write(buf, 0, buf.length);
    }

    /**
     * Write a set of bytes to the output streams of this object.
     *
     * @param	buf An array of bytes to be written.
     *
     * @param	off Index of the first byte in <tt>buf</tt> to write.
     *
     * @param	len Number of bytes in <tt>buf</tt> to write.
     *
     * @throws	IOException Thrown if any of the underlying output streams throws
     * this exception.
     */
    @Override
    public void write(byte[] buf, int off, int len) throws IOException {
        if (outputs == null) return;

        IOException err = null;
        for (OutputStream output : outputs) {
            try {
                output.write(buf, off, len);
            } catch (IOException ex) {
                if (err == null) {
                    err = ex;
                }
            }
        }

        // Handle any caught exceptions
        if (err != null) {
            throw err;
        }
    }
}
