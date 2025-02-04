package project;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QrGenerator {
    public static void generateQR(String data, String filePath) throws WriterException, IOException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 300, 300);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static void main(String[] args) {
        try {
            // Update the file path to save the QR code in the specified directory
            String filePath = "C:\\javaprograms\\project\\app\\src\\main\\resources\\qr_subject_&.png";
            for(int i=1;i<=5;i++){
                generateQR(Integer.toString(i), filePath.replace("&",Integer.toString(i) ));
        }
            System.out.println("QR Code Generated at: " + filePath);
        } catch (WriterException e) {
            System.err.println("Error generating QR Code: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
