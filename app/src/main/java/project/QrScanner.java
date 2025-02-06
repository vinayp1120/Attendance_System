package project;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.BorderLayout;

public class QrScanner {
    private static final int MAX_ATTEMPTS = 10; // Maximum attempts for scanning

    public static void displayWebcam(JFrame frame) {
        Webcam webcam = Webcam.getDefault();
        if (webcam == null) {
            JOptionPane.showMessageDialog(frame, "No webcam detected!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        webcam.open();
        JLabel label = new JLabel();
        frame.add(label, BorderLayout.CENTER);

        new Thread(() -> {
            while (true) {
                BufferedImage image = webcam.getImage();
                if (image != null) {
                    label.setIcon(new ImageIcon(image));
                }
            }
        }).start();
    }

    public static String scanQRCode() {
        Webcam webcam = Webcam.getDefault();
        if (webcam == null) {
            return "No webcam detected!";
        }

        webcam.open();
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            BufferedImage image = webcam.getImage();
            if (image == null) {
                attempts++;
                continue; // Skip if no image is captured
            }

            try {
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                Result result = new MultiFormatReader().decode(bitmap);
                webcam.close(); // Close the webcam when QR code is detected
                return result.getText(); // Extract QR Data (Subject ID)
            } catch (NotFoundException e) {
                attempts++;
                // QR code not detected, continue scanning
            }
        }

        webcam.close(); // Close the webcam after max attempts
        return "QR Code not detected after " + MAX_ATTEMPTS + " attempts.";
    }

    public static void main(String[] args) {
        // Start the webcam display in a separate thread
        JFrame frame = new JFrame("Webcam Feed");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        displayWebcam(frame);
        frame.setVisible(true);
        
        System.out.println("Scanning...");
        String scannedData = scanQRCode();
        System.out.println("Scanned QR Data: " + scannedData);
    }
}
