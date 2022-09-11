package CGHW1;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class Images {

    private static int firstX;
    private static int firstY;
    private static int n = 0;

    public static void gradientSetRaster(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        WritableRaster raster = img.getRaster();
        int[] pixel = new int[3]; //RGB

        for (int y = 0; y < height; y++) {
            int val = (int) (y * 255f / height);
            for (int shift = 1; shift < 3; shift++) {
                pixel[shift] = val;
            }

            for (int x = 0; x < width; x++) {
                raster.setPixel(x, y, pixel);
            }
        }

    }

    public static void bresenham(BufferedImage img, Graphics g, int x1, int y1, int x2, int y2) {
        int x, y, e, incX, incY, inc1, inc2;
        int dx = x2 - x1;
        int dy = y2 - y1;
        if (dx < 0) {
            dx = -dx;
        }
        if (dy < 0) {
            dy = -dy;
        }
        if (x2 < x1) {
            incX = -1;
        } else {
            incX = 1;
        }
        if (y2 < y1) {
            incY = -1;
        } else {
            incY = 1;
        }
        x = x1;
        y = y1;
        if (dx > dy) {
            putPixel(img, g, x, y);
            e = 2 * dy - dx;
            inc1 = 2 * (dy - dx);
            inc2 = 2 * dy;
            for (int j = 0; j < dx; j++) {
                if (e >= 0) {
                    y += incY;
                    e += inc1;
                } else {
                    e += inc2;
                }
                x += incX;
                putPixel(img, g, x, y);
            }
        } else {
            putPixel(img, g, x, y);
            e = 2 * dx - dy;
            inc1 = 2 * (dx - dy);
            inc2 = 2 * dx;
            for (int j = 0; j < dy; j++) {
                if (e >= 0) {
                    x += incX;
                    e += inc1;
                } else {
                    e += inc2;
                }
                y += incY;
                putPixel(img, g, x, y);
            }
        }
    }

    public static void putPixel(BufferedImage img, Graphics g, int x, int y) {
        int[] pixel = {255, 255, 255};
        img.getRaster().setPixel(x, y, pixel);
        g.drawImage(img, 0, 0, (img1, infoflags, xx, yy, width, height) -> false);
    }


    public static void main(String... args) {
        Frame w = new Frame("Raster");  //window
        final int imageWidth = 500;
        final int imageHeight = 500;

        w.setSize(imageWidth, imageHeight);
        w.setLocation(100, 100);
        w.setVisible(true);

        Graphics g = w.getGraphics();

        BufferedImage img = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        gradientSetRaster(img);
        g.drawImage(img, 0, 0, (img1, infoflags, x, y, width, height) -> false);  //draw the image. You can think of this as the display method.

        w.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    n++;
                    if (n == 1) {
                        firstX = e.getX();
                        firstY = e.getY();
                    }
                    int[] pixel = {255, 255, 255};
                    img.getRaster().setPixel(firstX, firstY, pixel);
                    g.drawImage(img, 0, 0, (img1, infoflags, x, y, width, height) -> false);
                    if (n == 2) {
                        bresenham(img, g, firstX, firstY, e.getX(), e.getY());
                        n = 0;
                    }
                }
            }
        });


        w.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                w.dispose();
                g.dispose();
                System.exit(0);
            }
        });
    }
    /* REFERENCE: Abdul Bari's Bresenham's Line Drawing Algorithm video -> https://www.youtube.com/watch?v=RGB-wlatStc*/
    /* Modified to work for all octants.  */
}
