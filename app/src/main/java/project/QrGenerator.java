package project;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Files;

public class QrGenerator {
    public static void generateQR(String data, String filePath) throws WriterException, IOException {
        // Validate the file path
        Path path = FileSystems.getDefault().getPath(filePath);
        if (!Files.exists(path.getParent())) {
            throw new IOException("Directory does not exist: " + path.getParent());
        }

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 300, 300);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static void main(String[] args) {
        try {
            // Update the file path to save the QR code in the specified directory
            String filePath = "C:\\javaprograms\\project\\app\\src\\main\\resources\\qr_subject_&.png";
            for (int i = 101; i <= 105; i++) {
                generateQR(Integer.toString(i), filePath.replace("&", Integer.toString(i)));
            }
            System.out.println("QR Codes Generated at: " + filePath);
        } catch (WriterException e) {
            System.err.println("Error generating QR Code: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
