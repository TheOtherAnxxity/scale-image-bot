package io.github.theotheranxxity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.awt.RenderingHints.*;

public class ScaleImage {
    public static BufferedImage scaleImage(BufferedImage sourceImage, int newWidth, int newHeight) {
        if (sourceImage.getWidth() * sourceImage.getHeight() <= 450 * 450) {
            if (newWidth * newHeight <= 900 * 900) {
                BufferedImage destinationImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D imageGraphics = destinationImage.createGraphics();
                imageGraphics.setRenderingHint(KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_QUALITY);
                imageGraphics.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                imageGraphics.setRenderingHint(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_QUALITY);
                imageGraphics.drawImage(sourceImage, 0, 0, newWidth, newHeight, null);
                imageGraphics.dispose();
                return destinationImage;
            } else throw new IllegalArgumentException("Scale factor requested was too large");
        } else throw new IllegalArgumentException("Image supplied was too large");
    }

    public static BufferedImage scaleImage(BufferedImage sourceImage, int newWidth, int newHeight, Object interpolationType) {
        if (
                interpolationType == VALUE_INTERPOLATION_NEAREST_NEIGHBOR ||
                        interpolationType == VALUE_INTERPOLATION_BILINEAR ||
                        interpolationType == VALUE_INTERPOLATION_BICUBIC
        )
            if (sourceImage.getWidth() * sourceImage.getHeight() <= 450 * 450)
                if (newWidth * newHeight <= 900 * 900) {
                    BufferedImage destinationImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D imageGraphics = destinationImage.createGraphics();
                    imageGraphics.setRenderingHint(KEY_INTERPOLATION, interpolationType);
                    imageGraphics.setRenderingHint(KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_QUALITY);
                    imageGraphics.setRenderingHint(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_QUALITY);
                    imageGraphics.drawImage(sourceImage, 0, 0, newWidth, newHeight, null);
                    imageGraphics.dispose();
                    return destinationImage;
                } else throw new IllegalArgumentException("Scale factor requested was too large");
            else throw new IllegalArgumentException("Image supplied was too large");
        else throw new IllegalArgumentException("Interpolation type supplied was not a valid interpolation type");
    }

    public static String getFileNameScaled(String fileName) {
        List<String> fileNameList = Arrays.asList(fileName.split("\\."));
        LinkedList<String> fileNameListNoExt = new LinkedList<>(fileNameList);
        fileNameListNoExt.removeLast();
        return String.join(".", fileNameListNoExt).concat("_scaled.").concat(new LinkedList<>(fileNameList).getLast());
    }
}
