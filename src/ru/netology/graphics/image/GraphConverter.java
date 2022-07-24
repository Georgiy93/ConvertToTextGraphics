package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;


public class GraphConverter implements TextGraphicsConverter {
    private int width;
    private int height;
    private double maxRatio;
    private TextColorSchema schema;


    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        int widthTrue = img.getWidth();
        int heightTrue = img.getHeight();
        double ratioWidthToHeight = widthTrue / heightTrue;
        double ratioHeightToWidth = heightTrue / widthTrue;
        int newWidth;
        int newHeight;
        // Если максимально допустимое соотношение сторон не задано, то используем сумму соотношений
        //cторон в качестве максимально допустимое соотношения
        if (maxRatio == 0) {
            maxRatio = ratioWidthToHeight + ratioHeightToWidth;
        }
        if (ratioWidthToHeight > maxRatio) {
            throw new BadImageSizeException(ratioWidthToHeight, maxRatio);
        }
        if (ratioHeightToWidth > maxRatio) {
            throw new BadImageSizeException(ratioHeightToWidth, maxRatio);
        }
        if (width == 0) {
            width = widthTrue;
        }
        if (widthTrue > width) {
            newWidth = widthTrue / (widthTrue / width);
            if (newWidth > width) {
                newWidth = width;
            }
        } else {
            newWidth = widthTrue;
        }
        if (height == 0) {
            height = heightTrue;
        }
        if (heightTrue > height) {
            newHeight = heightTrue / (heightTrue / height);
            if (newHeight > height) {
                newHeight = height;
            }
        } else {
            newHeight = heightTrue;
        }
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();
        char[][] outCharArray = new char[bwRaster.getWidth()][bwRaster.getHeight()];
        for (int w = 0; w < bwRaster.getWidth(); w++) {
            for (int h = 0; h < bwRaster.getHeight(); h++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                outCharArray[w][h] = c;

                // ??? //запоминаем символ c, например, в двумерном массиве или как-то ещё на ваше усмотрение
            }
        }

        StringBuilder outputPicture = new StringBuilder();
        for (int j = 0; j < bwRaster.getHeight(); j++) {
            for (int i = 0; i < bwRaster.getWidth(); i++) {

                outputPicture.append(outCharArray[i][j]);
                outputPicture.append(outCharArray[i][j]);
            }
            outputPicture = outputPicture.append("\n");
        }

        return outputPicture.toString();

    }


    @Override
    public void setMaxWidth(int width) {
        if (width <= 0) {
            return;
        }
        this.width = width;
    }

    @Override
    public void setMaxHeight(int height) {
        if (height <= 0) {
            return;
        }
        this.height = height;
    }


    @Override
    public void setMaxRatio(double maxRatio) {
        if (maxRatio <= 0) {
            return;
        }
        this.maxRatio = maxRatio;

    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }


}

