

package org.net.demo.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class QRCodeGenerator {

    public static Image generateQRCodeImage(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            // Tạo một WritableImage trực tiếp của JavaFX
            WritableImage writableImage = new WritableImage(width, height);
            PixelWriter pixelWriter = writableImage.getPixelWriter();

            // Tự vẽ các điểm pixel Đen/Trắng trực tiếp lên ảnh của JavaFX mà không cần thông qua Swing/AWT
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
                    pixelWriter.setColor(x, y, color);
                }
            }

            return writableImage;

        } catch (Exception e) {
            System.err.println("Lỗi khi tạo mã QR: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}