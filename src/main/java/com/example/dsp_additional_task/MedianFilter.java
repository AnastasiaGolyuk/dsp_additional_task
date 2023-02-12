package com.example.dsp_additional_task;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.imageio.*;
import java.io.File;

public class MedianFilter {

    private static class HSB {
        private final float h;
        private final float s;
        private final float b;

        HSB(float h, float s, float b) {
            this.h = h;
            this.s = s;
            this.b = b;
        }

        public float getH() {
            return h;
        }

        public float getS() {
            return s;
        }

        public float getB() {
            return b;
        }
    }


    public Image medianFilter(BufferedImage bufferedImage, int coreSize) throws Throwable {

        float[] H = new float[coreSize * coreSize];
        float[] S = new float[coreSize * coreSize];
        float[] B = new float[coreSize * coreSize];
        WritableImage wr = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
        HSB[][] hsbArr = new HSB[bufferedImage.getWidth()][bufferedImage.getHeight()];
        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                float[] hsb = new float[3];
                Color color = new Color(bufferedImage.getRGB(x, y));
                Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
                HSB hsbColor = new HSB(hsb[0], hsb[1], hsb[2]);
                hsbArr[x][y] = hsbColor;
            }
        }
        PixelWriter pw = wr.getPixelWriter();
        File output = new File("output.jpg");
        HSB[] window = new HSB[coreSize * coreSize];
        int edgex = (coreSize - 1) / 2;
        int edgey = (coreSize - 1) / 2;
        for (int x = edgex; x < bufferedImage.getWidth() - edgex; x++) {
            for (int y = edgey; y < bufferedImage.getHeight() - edgey; y++) {
                int i = 0;
                for (int dx = 0; dx < coreSize; dx++) {
                    for (int dy = 0; dy < coreSize; dy++) {
                        window[i] = hsbArr[x + dx - edgex][y + dy - edgey];
                        i++;
                    }
                }
                for (int k = 0; k < coreSize * coreSize; k++) {
                    H[k] = window[k].getH();
                    S[k] = window[k].getS();
                    B[k] = window[k].getB();
                }
                Arrays.sort(H);
                Arrays.sort(S);
                Arrays.sort(B);
                int index = coreSize * coreSize / 2;
                int clr = Color.HSBtoRGB(H[index], S[index], B[index]);
                Color color = new Color(clr);
                pw.setArgb(x, y, color.getRGB());
                bufferedImage.setRGB(x, y, color.getRGB());
            }
        }

        ImageIO.write(bufferedImage, "jpg", output);
        return new ImageView(wr).getImage();
    }
}
