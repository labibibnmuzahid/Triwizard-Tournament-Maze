import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A SimpleCanvas represents a window on the screen that can be drawn on.
 */
public class SimpleCanvas {
    private JFrame frame;
    private Graphics2D onscreenGraphics, offscreenGraphics;
    private BufferedImage onscreenImage, offscreenImage;
    //private Color penColor;
    private int height, width;
    private Color bgColor = Color.WHITE;
    private int lastMouseClickX = 0, lastMouseClickY = 0;

    /**
     * Creates a new SimpleCanvas of the specified width and height.
     */
    public SimpleCanvas(int width, int height) {
        this(width, height, "SimpleCanvas");
    }

    /**
     * Creates a new SimpleCanvas of the specified width and height, with the specified title.
     */
    public SimpleCanvas(int width, int height, String title) {
        this.height = height;
        this.width = width;
        //penColor = Color.BLACK;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame(title);
                frame.setVisible(false);
                onscreenImage = new BufferedImage(2 * width, 2 * height, BufferedImage.TYPE_INT_ARGB);
                offscreenImage = new BufferedImage(2 * width, 2 * height, BufferedImage.TYPE_INT_ARGB);
                onscreenGraphics = onscreenImage.createGraphics();
                offscreenGraphics = offscreenImage.createGraphics();
                onscreenGraphics.scale(2, 2);

                // add antialiasing
                RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                offscreenGraphics.addRenderingHints(hints);

                // clear screen
                offscreenGraphics.setColor(bgColor);
                offscreenGraphics.fillRect(0, 0, width, height);
                //System.out.println("painted BG");
                offscreenGraphics.setColor(Color.BLACK);

                // frame stuff
                RetinaImageIcon icon = new RetinaImageIcon(onscreenImage);
                JLabel draw = new JLabel(icon);
                draw.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        lastMouseClickX = e.getX();
                        lastMouseClickY = e.getY();
                    }
                });
                frame.setContentPane(draw);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.pack();
                //frame.repaint();
                //System.out.println("done constructor");
            }
        });
    }

    private static class MouseWaiter extends MouseAdapter {
        public void mouseReleased(MouseEvent e) {
            synchronized (this) {
                notifyAll();
            }
        }
    }

    /**
     * Pauses the main program until the mouse is clicked on the canvas.  The mouse click position
     * can be retrieved later.
     */
    public void waitForClick() {
        MouseWaiter mw = new MouseWaiter();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    frame.getContentPane().addMouseListener(mw);
                }
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
        try {
            synchronized (mw) {
                mw.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //System.out.println("wait for click done");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.getContentPane().removeMouseListener(mw);
            }
        });
    }

    /**
     * Retrieves the x-coordinate of the most recent mouse click.
     */
    public int getMouseClickX() {
        return lastMouseClickX;
    }

    /**
     * Retrieves the y-coordinate of the most recent mouse click.
     */
    public int getMouseClickY() {
        return lastMouseClickY;
    }


    /**
     * Clears the canvas with the current background color.
     */
    public void clear() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Color saveColor = offscreenGraphics.getColor();
                offscreenGraphics.setColor(bgColor);
                offscreenGraphics.fillRect(0, 0, width, height);
                offscreenGraphics.setColor(saveColor);
            }
        });
    }

    /**
     * Draws a circle on the canvas with the center at (centerX, centerY) and the specified radius.
     */
    public void drawCircle(int centerX, int centerY, int radius) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                offscreenGraphics.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
                //frame.repaint();
            }
        });
    }

    /**
     * Draws an oval on the canvas with the center at (centerX, centerY) and the specified radii in the
     * x and y directions.
     */
    public void drawOval(int centerX, int centerY, int radiusX, int radiusY) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                offscreenGraphics.drawOval(centerX - radiusX, centerY - radiusY, radiusX * 2, radiusY * 2);
                //frame.repaint();
            }
        });
    }

    /**
     * Draws a rectangle on the canvas with the left corner at (topLeftX, topLeftY) and the specified
     * width and height.
     */
    public void drawRectangle(int topLeftX, int topLeftY, int width, int height) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                offscreenGraphics.drawRect(topLeftX, topLeftY, width, height);
                //frame.repaint();
            }
        });
    }

    public void drawPolygon(int[] xPoints, int[] yPoints) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                offscreenGraphics.drawPolygon(xPoints, yPoints, xPoints.length);
                //frame.repaint();
            }
        });
    }

    public void drawFilledPolygon(int[] xPoints, int[] yPoints) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                offscreenGraphics.fillPolygon(xPoints, yPoints, xPoints.length);
                //frame.repaint();
            }
        });
    }

    /**
     * Draws a circle on the canvas with the center at (centerX, centerY) and the specified radius, filled
     * with the current pen color.
     */
    public void drawFilledCircle(int centerX, int centerY, int radius) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                offscreenGraphics.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
                //onscreenImage.
                //frame.repaint();
            }
        });
    }

    /**
     * Draws an oval on the canvas with the center at (centerX, centerY) and the specified radii in the
     * x and y directions, filled with the current pen color.
     */
    public void drawFilledOval(int centerX, int centerY, int radiusX, int radiusY) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                offscreenGraphics.fillOval(centerX - radiusX, centerY - radiusY, radiusX * 2, radiusY * 2);
                //onscreenImage.
                //frame.repaint();
            }
        });
    }

    /**
     * Draws a rectangle on the canvas with the left corner at (topLeftX, topLeftY) and the specified
     * width and height, filled with the current pen color.
     */
    public void drawFilledRectangle(int topLeftX, int topLeftY, int width, int height) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                offscreenGraphics.fillRect(topLeftX, topLeftY, width, height);
                //onscreenImage.
                //frame.repaint();
            }
        });
    }

    /**
     * Draws a line on the canvas from (x1, y1) to (x2, y2).
     */
    public void drawLine(int x1, int y1, int x2, int y2) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                offscreenGraphics.drawLine(x1, y1, x2, y2);
            }
        });
    }

    /**
     * Draws the specified text on the canvas, with the lower left corner of the text at
     * the point (x, y).
     */
    public void drawString(int x, int y, String text) {
        drawString(x, y, text, 12);
    }

    /**
     * Draws the specified text on the canvas, with the center of the text at the point (x, y).
     */
    public void drawStringCentered(int x, int y, String text) {
        drawStringCentered(x, y, text, 12);
    }

    /**
     * Draws the specified text on the canvas, with the lower left corner of the text at
     * the point (x, y), and in the specified font size.
     */
    public void drawString(int x, int y, String text, int fontSize) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Font currentFont = offscreenGraphics.getFont();
                Font newFont = new Font(currentFont.getFontName(), currentFont.getStyle(), fontSize);
                offscreenGraphics.setFont(newFont);
                offscreenGraphics.drawString(text, x, y);
            }
        });
    }

    /**
     * Draws the specified text on the canvas, with the center of the text at the point (x, y),
     * and in the specified font size.
     */
    public void drawStringCentered(int x, int y, String text, int fontSize) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Font currentFont = offscreenGraphics.getFont();
                Font newFont = new Font(currentFont.getFontName(), currentFont.getStyle(), fontSize);
                offscreenGraphics.setFont(newFont);

                FontMetrics metrics = offscreenGraphics.getFontMetrics(newFont);
                int stringWidth = metrics.stringWidth(text);
                int stringHeight = metrics.getHeight();
                //System.out.println(stringWidth + " " + stringHeight);

                offscreenGraphics.drawString(text, x - stringWidth / 2, y + stringHeight / 2 - metrics.getAscent() / 2);
            }
        });
    }

    /**
     * Sets the thickness of the lines drawn for the borders of shapes.
     */
    public void setLineThickness(int size) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                offscreenGraphics.setStroke(new BasicStroke(size));
            }
        });
    }

    /**
     * Draws the specified image on the canvas with the top left corner of the image at the point (x, y).
     * The image string can be a URL or a filename, and must be either a .jpg or .png, though other times
     * might work too.
     */
    public void drawImage(int x, int y, String filename) {
        Image image = getImage(filename);
        // int ws = image.getWidth();    // can call only if image is a BufferedImage
        // int hs = image.getHeight();
        int ws = image.getWidth(null);
        int hs = image.getHeight(null);
        int xs = x * 2;
        int ys = y * 2;
        if (ws < 0 || hs < 0) throw new IllegalArgumentException("image " + filename + " is corrupt");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //offscreenGraphics.drawImage(image, (int) Math.round(xs - ws/2.0), (int) Math.round(ys - hs/2.0), null);
                offscreenGraphics.drawImage(image, x, y, null);
                //frame.repaint();
            }
        });
    }

    /**
     * Sets the current pen color on the canvas.  All future drawing will take place
     * using this color until changed.
     */
    public void setPenColor(Color c) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                offscreenGraphics.setColor(c);
            }
        });
    }

    /**
     * Sets the canvas background color.  Must call clear() to take effect.
     */
    public void setBackgroundColor(Color c) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                bgColor = c;
            }
        });
    }

    /**
     * Returns the color of the pixel at location (x, y) on the canvas.
     */
    public Color getPixelColor(int x, int y) {
        final Color[] answer = new Color[1];
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    answer[0] = new Color(offscreenImage.getRGB(2 * x, 2 * y), true);
                }
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return answer[0];
    }

    /**
     * Sets the pixel color at location (x, y) on the canvas.
     */
    public void setPixelColor(int x, int y, Color c) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                offscreenImage.setRGB(2 * x, 2 * y, c.getRGB());
                //frame.repaint();
            }
        });
    }

    public void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the canvas window on the screen, if it's currently hidden.  No effect if it's already visible.
     */
    public void show() {
        update();
        /*SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //System.out.println("EDT: setvisible true from show");
                frame.setVisible(true);
            }
        });*/
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    frame.setVisible(true);
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hides the canvas window on the screen, if it's currently shown.  No effect if it's already hidden.
     */
    public void hide() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(false);
            }
        });
    }

    /**
     * Returns the height of the canvas in pixels.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the width of the canvas in pixels.
     */
    public int getWidth() {
        return width;
    }

    /**
     * If any drawing operations have taken place, update() actually draws them to the canvas.
     * Shapes are not normally drawn instantaneously; instead, they are not drawn until this
     * method is called.  Therefore, this method can be used to have a number of new shapes all
     * appear simultaneously on the screen.
     */
    public void update() {
        /*try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    System.out.println("EDT: repaint from update");
                    frame.repaint();
                }
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }*/
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //System.out.println("EDT: repaint from update");
                onscreenGraphics.drawImage(offscreenImage, 0, 0, null);
                //frame.repaint();
                frame.repaint();
            }
        });
    }

    private static class RetinaImageIcon extends ImageIcon {

        public RetinaImageIcon(Image image) {
            super(image);
        }

        public int getIconWidth() {
            return super.getIconWidth() / 2;
        }

        /**
         * Gets the height of the icon.
         *
         * @return the height in pixels of this icon
         */
        public int getIconHeight() {
            return super.getIconHeight() / 2;
        }

        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.scale(0.5, 0.5);
            super.paintIcon(c, g2, x * 2, y * 2);
            g2.dispose();
        }
    }

    private static Image getImage(String filename) {
        if (filename == null) throw new IllegalArgumentException();

        // to read from file
        ImageIcon icon = new ImageIcon(filename);

        // try to read from URL
        if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
            try {
                URL url = new URL(filename);
                icon = new ImageIcon(url);
            } catch (MalformedURLException e) {
                /* not a url */
            }
        }

        // in case file is inside a .jar (classpath relative to StdDraw)
        if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
            URL url = SimpleCanvas.class.getResource(filename);
            if (url != null)
                icon = new ImageIcon(url);
        }

        // in case file is inside a .jar (classpath relative to root of jar)
        if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
            URL url = SimpleCanvas.class.getResource("/" + filename);
            if (url == null) throw new IllegalArgumentException("image " + filename + " not found");
            icon = new ImageIcon(url);
        }

        return icon.getImage();
    }
}