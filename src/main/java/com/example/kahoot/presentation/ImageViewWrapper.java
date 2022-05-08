package com.example.kahoot.presentation;


import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class ImageViewWrapper {
    private final ImageView imageView;

    private BufferedImage bufferedImage = null;
    private final double screenHeight;

    public ImageViewWrapper(ImageView imageView, double screenHeight) {
        this.imageView = imageView;
        this.screenHeight = screenHeight;
    }

    public boolean hasImage() {
        return bufferedImage != null;
    }

    public void updateImgView(URI uri) {
        bufferedImage = loadImg(uri);
        boolean show = hasImage();
        imageView.setVisible(show);
        if (show) {
            Platform.runLater(() -> imageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null)));
        } else {
            imageView.setFitHeight(0);
            imageView.setFitWidth(0);
        }
        updateImageViewSize();
    }

    private BufferedImage rotateBufferImage(BufferedImage bimg) {
        int angle = 90;
        double sin = Math.abs(Math.sin(Math.toRadians(angle))),
                cos = Math.abs(Math.cos(Math.toRadians(angle)));
        int w = bimg.getWidth();
        int h = bimg.getHeight();
        int newW = (int) Math.floor(w * cos + h * sin),
                newH = (int) Math.floor(h * cos + w * sin);
        BufferedImage rotated = new BufferedImage(newW, newH, bimg.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.translate((newW - w) / 2, (newH - h) / 2);
        graphic.rotate(Math.toRadians(angle), w / 2.0, h / 2.0);
        graphic.drawRenderedImage(bimg, null);
        graphic.dispose();
        return rotated;
    }

    public void rotateImage() {
        bufferedImage = rotateBufferImage(bufferedImage);
        imageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
    }

    /**
     * scale bufferImage and save it in pict folder
     */
    public void saveImg(String imageName, String folder) {
        try {
            File file = new File(folder);
            if (!file.exists())
                file.mkdirs();
            ImageIO.write(scale(), "jpg", new File(folder + "/" + imageName + ".jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        updateImageViewSize();
    }

    public void saveImg(String imageName) {
        saveImg(imageName, "pict");
    }

    public void clear(URI uri) {
        File file = new File(uri);
        if (file.exists())
            file.delete();
        updateImgView(null);
    }

    private BufferedImage scale() {
        int height = (int) (screenHeight / 2.0);
        AffineTransform at = new AffineTransform();
        double scale = (height / (double) bufferedImage.getHeight()) * 2;
        at.scale(scale, scale);
        BufferedImage after = new BufferedImage((int) (bufferedImage.getWidth() * scale), (int) (bufferedImage.getHeight() * scale), BufferedImage.TYPE_3BYTE_BGR);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        return scaleOp.filter(bufferedImage, after);
    }


    private BufferedImage loadImg(URI filename) {
        if (filename == null) return null;
        BufferedImage img;
        try {
            img = ImageIO.read(new File(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return img;
    }


    private void updateImageViewSize() {
        double maxH = screenHeight / 2;
        int height, width;
        height = (int) maxH;
        if (hasImage()) {
            width = getWidth(bufferedImage.getWidth(), bufferedImage.getHeight(), height);
        } else {
            height = 0;
            width = 0;
        }
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
    }

    private int getWidth(double baseWidth, double baseHeight, int height) {
        double proportion = baseHeight / baseWidth;
        return (int) (height / proportion);
    }
}
