/*******************************************************************************
 *  Copyright (c) 2017 Uber Technologies, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 *******************************************************************************/
package com.uber.cherami.example;

import java.nio.ByteBuffer;

/**
 * Value type that represents the message that's published or consumed from
 * Cherami.
 *
 * @author venkat
 */
public class AppData {

    private static int HEADER_SIZE = 12;

    /** integer representing the data identifier. */
    public final long id;
    /** byte array representing the opaque payload. */
    public final byte[] payload;

    /**
     * Constructs an AppData object.
     *
     * @param id
     *            long representing the id
     * @param payload
     *            byte array representing the payload.
     */
    public AppData(long id, byte[] payload) {
        this.id = id;
        this.payload = payload;
    }

    /**
     * Serialize converts this object into a byte array.
     *
     * @return Byte array containing the serialized bytes
     */
    public byte[] serialize() {
        ByteBuffer buf = ByteBuffer.allocate(HEADER_SIZE + payload.length);
        buf.putLong(id);
        buf.putInt(payload.length);
        buf.put(payload);
        buf.flip();
        return buf.array();
    }

    /**
     * Converts the given byte array into an AppData object.
     *
     * @param bytes
     *            Byte array which needs to be deserialized.
     * @return Deserialized AppData object
     */
    public static AppData deserialize(byte[] bytes) {
        if (bytes.length < HEADER_SIZE) {
            throw new RuntimeException("Packet size too small");
        }
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        long id = buf.getLong();
        int length = buf.getInt();
        byte[] payload = new byte[length];
        buf.get(payload);
        return new AppData(id, payload);
    }
}
