package project;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.awt.image.BufferedImage;

public class QrScanner {
    public static String scanQRCode() {
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        BufferedImage image = webcam.getImage();
        webcam.close();

        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText(); // Extract QR Data (Subject ID)
        } catch (NotFoundException e) {
            return "QR Code not detected!";
        }
    }

    public static void main(String[] args) {
        System.out.println("Scanning...");
        String scannedData = scanQRCode();
        System.out.println("Scanned QR Data: " + scannedData);
    }
}
