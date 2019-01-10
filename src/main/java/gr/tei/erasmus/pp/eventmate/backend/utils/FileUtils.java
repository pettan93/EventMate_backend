package gr.tei.erasmus.pp.eventmate.backend.utils;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

public class FileUtils {

    public static File getFileFromBlob(Blob blob, File tempFile) {
        try {
            InputStream in = blob.getBinaryStream();
            OutputStream out = new FileOutputStream(tempFile);
            byte[] buff = blob.getBytes(1, (int) blob.length());
            out.write(buff);
            out.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        return tempFile;
    }


    public static Blob getFileBlob(File f) {
        try {
            return new SerialBlob(Files.readAllBytes(f.toPath()));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Blob getBlobFromEncodedString(String encoded) {
        byte[] decodedBytes = Base64.getDecoder().decode(encoded);

        try {
            return new SerialBlob(decodedBytes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getEncodedStringFromBlob(Blob blob) {
        int blobLength = 0;
        try {
            blobLength = (int) blob.length();
            byte[] blobAsBytes = blob.getBytes(1, blobLength);
            return new String(Base64.getEncoder().encode(blobAsBytes));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
