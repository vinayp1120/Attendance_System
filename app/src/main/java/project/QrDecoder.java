package project;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class QrDecoder {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java QrDecoder <path to QR code image>");
            return;
        }

        String filePath = args[0];
        try {
            BufferedImage image = ImageIO.read(new File(filePath));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);
            System.out.println("Decoded QR Code Data: " + result.getText());
        } catch (Exception e) {
            System.err.println("Error decoding QR Code: " + e.getMessage());
        }
    }
}
