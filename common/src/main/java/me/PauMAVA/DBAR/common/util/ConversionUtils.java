package me.PauMAVA.DBAR.common.util;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ConversionUtils {

    public static byte[] longToBytes(long l) {
        return new byte[]{
                (byte) ((l >> 56) & 0xff),
                (byte) ((l >> 48) & 0xff),
                (byte) ((l >> 40) & 0xff),
                (byte) ((l >> 32) & 0xff),
                (byte) ((l >> 24) & 0xff),
                (byte) ((l >> 16) & 0xff),
                (byte) ((l >> 8) & 0xff),
                (byte) ((l) & 0xff),
        };
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }

    public static byte[] concatArrays(byte[]... arrays) {
        byte[] destination = new byte[0];
        for (byte[] array: arrays) {
            if (array != null) {
                destination = concatArrays(destination, array);
            }
        }
        return destination;
    }

    public static byte[] concatArrays(byte[] destination, byte[] append) {
        byte[] finalArray = new byte[destination.length + append.length];
        int i = 0;
        for (byte value: destination) {
            finalArray[i] = value;
            i++;
        }
        for (byte value: append) {
            finalArray[i] = value;
            i++;
        }
        return finalArray;
    }

    public static byte[] subArray(byte[] array, int beginIndex, int endIndex) {
        byte[] newArray = new byte[endIndex - beginIndex + 1];
        int j = 0;
        for (int i = beginIndex; i <= endIndex; i++) {
            newArray[j] = array[i];
            j++;
        }
        return newArray;
    }

    public static long crc32(byte[] data) {
        Checksum checksum = new CRC32();
        checksum.update(data);
        return checksum.getValue();
    }

    public static String byteToBinaryString(byte b) {
        StringBuilder sb = new StringBuilder("00000000");
        for (int bit = 0; bit < 8; bit++) {
            if (((b >> bit) & 1) > 0) {
                sb.setCharAt(7 - bit, '1');
            }
        }
        return sb.toString();
    }

    public static String bytesToBinaryString(byte[] bytes) {
        StringBuilder bytesString = new StringBuilder();
        for (byte b: bytes) {
            bytesString.append(byteToBinaryString(b));
        }
        return bytesString.toString();
    }

    public static String bytesToPrettyBinaryString(byte [] bytes) {
        int j = 0;
        StringBuilder sb = new StringBuilder();
        String binaryString = bytesToBinaryString(bytes);
        for (int i = 0; i < binaryString.length(); i++) {
            sb.append(binaryString.charAt(i));
            j++;
            if (j == 8) {
                sb.append(" ");
                j = 0;
            }
        }
        return sb.toString();
    }

}
