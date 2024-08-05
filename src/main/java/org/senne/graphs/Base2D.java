package org.senne.graphs;

import org.senne.Style;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public abstract class Base2D {

    List<GraphElement> graphElements = new ArrayList<>();
    double[] xLimits = new double[2];
    double[] outerPointsX = new double[2];
    boolean xLimitsCustom = false;
    double[] yLimits = new double[2];
    double[] outerPointsY = new double[2];
    boolean yLimitsCustom = false;
    String title = "";
    float lineWidth = Style.getLineWidth();
    int verticalResolution = Style.getVerticalResolution();
    double aspectRatio = Style.getAspectRatio();
    BufferedImage image;
    int borderSize = (int) ((verticalResolution * aspectRatio / 1.2) * 0.1);
    int fontSize = Style.getFontSize();
    boolean showGrid = Style.isShowGrid();
    boolean animateAxis = Style.isAnimateAxis();
    int delay;
    int axisAnimationFrames = Style.getAxisAnimationFrames();
    boolean logX = false;
    boolean logY = false;

    //graph settings
    public void setXLimits(double min, double max) {
        this.xLimits = new double[] {min, max};
        xLimitsCustom = true;
    }

    public void setYLimits(double min, double max) {
        this.yLimits = new double[] {min, max};
        yLimitsCustom = true;
    }

    public void setLineWith(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setVerticalResolution(int verticalResolution) {
        this.verticalResolution = verticalResolution;
    }

    public void setAspectRatio(int x, int y) {
        this.aspectRatio = ((float) x) / ((float) y);

        if (aspectRatio >= 1) {
            borderSize = (int) ((verticalResolution * aspectRatio / 1.2) * 0.1);
        } else {
            borderSize = (int) ((verticalResolution / 1.2) * 0.1);
        }
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }

    public void logX() {
        logX = true;

        if (!xLimitsCustom) {
            xLimits[0] = Math.pow(10, Math.log10(outerPointsX[0]) - (Math.log10(outerPointsX[1] - outerPointsX[0])) / 10);
            xLimits[1] = Math.pow(10, Math.log10(outerPointsX[1]) + (Math.log10(outerPointsX[1] - outerPointsX[0])) / 10);
        }
    }

    public void logY() {
        logY = true;

        if (!yLimitsCustom) {
            yLimits[0] = Math.pow(10, Math.log10(outerPointsY[0]) - (Math.log10(outerPointsY[1] - outerPointsY[0])) / 10);
            yLimits[1] = Math.pow(10, Math.log10(outerPointsY[1]) + (Math.log10(outerPointsY[1] - outerPointsY[0])) / 10);
        }
    }

    public void logLog() {
        logX();
        logY();
    }

    //plotting

    public void plot() {
        calculateLimits();

        int width = (int) Math.ceil(verticalResolution * aspectRatio);
        int height = verticalResolution;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        drawGraph(g);

        Plot plot = new Plot(new PlotPanel());
        plot.draw();
    }

    public void save(String fileName) throws IOException {
        if (image == null) throw new RuntimeException("No image to save. Call plot() first.");
        ImageIO.write(image, "png", new File(fileName + ".png"));
    }

    public void animate(GraphAnimation2D animation, int frames, int delay) throws InterruptedException {
        calculateLimits();

        this.delay = delay;

        int width = (int) Math.ceil(verticalResolution * aspectRatio);
        int height = verticalResolution;

        Plot plot = new Plot(new PlotPanel());
        plot.draw();

        if (animateAxis) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setFont(new Font("Calibri", Font.BOLD, fontSize));
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            AxisAnimation(g, width, height, axisAnimationFrames, 0);

            plot.repaint();
            sleep(delay);

            for (int i = 1; i < axisAnimationFrames; i++) {
                image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                g = image.createGraphics();
                g.setFont(new Font("Calibri", Font.BOLD, fontSize));
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, width, height);
                AxisAnimation(g, width, height, axisAnimationFrames, i);
                plot.repaint();
                sleep(delay);
            }
        }

        for (int i = 0; i < frames; i++) {
            animation.animateGraph(i);

            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            drawGraph(g);
            plot.repaint();
            sleep(delay);
        }
    }

    public void saveAnimationFrames(GraphAnimation2D animation, int frames, String name) throws IOException {
        calculateLimits();

        int width = (int) Math.ceil(verticalResolution * aspectRatio);
        int height = verticalResolution;

        File directory = new File(name);
        if (!directory.exists()) {
            directory.mkdir();
        }

        if (animateAxis) {
            for (int i = 0; i < axisAnimationFrames; i++) {
                image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setFont(new Font("Calibri", Font.BOLD, fontSize));
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, width, height);
                AxisAnimation(g, width, height, axisAnimationFrames, i);
                ImageIO.write(image, "png", new File(name + "/" + i + ".png"));
            }
        }

        for (int i = 0; i < frames; i++) {
            animation.animateGraph(i);

            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            drawGraph(g);
            int num = axisAnimationFrames + 1;
            ImageIO.write(image, "png", new File(name + "/" + num + ".png"));
        }
    }

    //animations

    public Base2D setAnimateAxis(boolean animateAxis) {
        this.animateAxis = animateAxis;
        return this;
    }

    public Base2D setAxisAnimationFrames(int frames) {
        this.axisAnimationFrames = frames;
        return this;
    }

    // |-|  |--\ |-|  |-----|  |-----|  |-----|  |--\ |-|    /---\    |-|
    // | |  | |\\| |    | |    |--|     |_____|  | |\\| |   / /_\ \   | |
    // |_|  |_| \__|    |_|    |_____|  |_| \_\  |_| \__|  /_/   \_\  |____|

    void drawGraph(Graphics2D g) {
        double[] xLimits;
        if (this.xLimits[0] < this.xLimits[1]) {
            xLimits = this.xLimits;
        } else {
            xLimits = new double[] {0, 10};
        }

        int width = (int) Math.ceil(verticalResolution * aspectRatio);
        int height = verticalResolution;

        if (borderSize < (int) (g.getFontMetrics().stringWidth(String.valueOf(xLimits[1])) * 2.5 + 50)) {
            borderSize = (int) (g.getFontMetrics().stringWidth(String.valueOf(xLimits[1])) * 2.5 + 50);
        }

        g.setPaint(new Color(255, 255, 255));
        g.fillRect(0, 0, width, height);

        g.setStroke(new BasicStroke(lineWidth));

        for (GraphElement graph : graphElements) {
            graph.draw(g);
        }

        g.setPaint(new Color(0, 0, 0));
        g.setStroke(new BasicStroke(lineWidth));
        g.setFont(new Font("Calibri", Font.BOLD, fontSize));
        drawAxis(g, width, height);
    }

    double[] axisStartDist(double[] limits) {
        BigDecimal[] bDLimits;
        if (limits[0] < limits[1]) {
            bDLimits = new BigDecimal[] {new BigDecimal(String.valueOf(limits[0])), new BigDecimal(String.valueOf(limits[1]))};
        } else {
            bDLimits = new BigDecimal[] {new BigDecimal("0"), new BigDecimal("10")};
        }

        BigDecimal spaceBetweenNumbers = new BigDecimal("1");
        BigDecimal distance = new BigDecimal(String.valueOf(limits[1] - limits[0]));

        while (distance.compareTo(BigDecimal.TEN) == 1) {
            spaceBetweenNumbers = spaceBetweenNumbers.multiply(new BigDecimal("5"));
            distance = distance.divide(new BigDecimal("5"));
        }

        while (distance.compareTo(BigDecimal.ONE) == -1) {
            spaceBetweenNumbers = spaceBetweenNumbers.divide(new BigDecimal("2"));
            distance = distance.multiply(new BigDecimal("2"));
        }

        return new double[] {(bDLimits[0].add((spaceBetweenNumbers.subtract(bDLimits[0].remainder(spaceBetweenNumbers))).remainder(spaceBetweenNumbers))).doubleValue(), spaceBetweenNumbers.doubleValue()};
    }

    double[] axisStartDistLog(double[] limits) {
        BigDecimal[] bDLimits;
        if (limits[0] < limits[1]) {
            bDLimits = new BigDecimal[] {new BigDecimal(String.valueOf(Math.log10(limits[0]))), new BigDecimal(String.valueOf(Math.log10(limits[1])))};
        } else {
            bDLimits = new BigDecimal[] {new BigDecimal("1"), new BigDecimal("10")};
        }

        BigDecimal spaceBetweenNumbers = new BigDecimal("1");
        BigDecimal distance = bDLimits[1].subtract(bDLimits[0]);

        while (distance.compareTo(BigDecimal.TEN) == 1) {
            spaceBetweenNumbers = spaceBetweenNumbers.multiply(new BigDecimal("5"));
            distance = distance.divide(new BigDecimal("5"));
        }

        while (distance.compareTo(BigDecimal.ONE) == -1) {
            spaceBetweenNumbers = spaceBetweenNumbers.divide(new BigDecimal("2"));
            distance = distance.multiply(new BigDecimal("2"));
        }

        return new double[] {(bDLimits[0].add((spaceBetweenNumbers.subtract(bDLimits[0].remainder(spaceBetweenNumbers))).remainder(spaceBetweenNumbers))).doubleValue(), spaceBetweenNumbers.doubleValue()};
    }

    void drawAxis(Graphics2D g, int width, int height) {
        AxisAnimation(g, width, height, 2, 1);
    }

    BufferedImage AxisAnimation(Graphics2D g, int width, int height, double axisAnimationFrames, int i) {
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(lineWidth));

        double distancePercentage = (-(Math.cos(Math.PI * (i / (axisAnimationFrames - 1))) - 1) / 2);

        int[] graphLocationX = {borderSize, (int) ((width - borderSize * 2) * distancePercentage + borderSize)};
        int[] graphLocationY = {(int) ((height - 2 * borderSize) * (1 - distancePercentage) + borderSize), height - borderSize};

        //x, y axis
        g.drawLine(graphLocationX[0], graphLocationY[0], graphLocationX[0], graphLocationY[1]);
        g.drawLine(graphLocationX[0], graphLocationY[1], graphLocationX[1], graphLocationY[1]);

        if (logX) {
            drawXAxisLog(g, graphLocationX, graphLocationY);
        } else {
            drawXAxisNormal(g, graphLocationX, graphLocationY);
        }

        if (logY) {
            drawYAxisLog(g, graphLocationX, graphLocationY);
        } else {
            drawYAxisNormal(g, graphLocationX, graphLocationY);
        }

        g.drawLine(graphLocationX[0], borderSize, graphLocationX[1], borderSize);
        g.drawLine(width - borderSize, graphLocationY[1], width - borderSize, graphLocationY[0]);
        g.setStroke(new BasicStroke(lineWidth));

        //title
        g.setColor(Color.BLACK);
        g.setFont(new Font("Calibri", Font.BOLD, fontSize * 2));
        g.drawString(title, width / 2 - g.getFontMetrics().stringWidth(title) / 2, borderSize / 2 + fontSize);
        g.setFont(new Font("Calibri", Font.BOLD, fontSize));

        return image;
    }

    void drawXAxisNormal(Graphics2D g, int[] graphLocationX, int[] graphLocationY) {
        double[] xLimits;
        if (this.xLimits[0] < this.xLimits[1]) {
            xLimits = this.xLimits;
        } else {
            xLimits = new double[] {0, 10};
        }

        double[] yLimits;
        if (this.yLimits[0] < this.yLimits[1]) {
            yLimits = this.yLimits;
        } else {
            yLimits = new double[] {0, 10};
        }

        //axis numbers
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(lineWidth));
        double[] axisStartDistX = axisStartDist(xLimits);
        double currentPointX = axisStartDistX[0];
        double spaceBetweenNumbersX = axisStartDistX[1];

        while (currentPointX <= xLimits[1]) {
            double[] screenCoords = graphCoordsToScreenCoords(currentPointX, yLimits[0]);
            if (screenCoords[0] > graphLocationX[1]) break;
            g.drawLine((int) screenCoords[0], graphLocationY[1] - 10, (int) screenCoords[0], graphLocationY[1] + 10);
            g.drawString(String.valueOf(currentPointX), (int) screenCoords[0] - g.getFontMetrics().stringWidth(String.valueOf(currentPointX)) / 2, graphLocationY[1] + 15 + fontSize);
            currentPointX += spaceBetweenNumbersX;
        }

        //grid
        if (showGrid) {
            g.setPaint(new Color(0, 0, 0, 50));
            g.setStroke(new BasicStroke(lineWidth/3));

            currentPointX = axisStartDistX[0];
            spaceBetweenNumbersX = axisStartDistX[1];

            while (currentPointX < xLimits[1]) {
                double[] screenCoords = graphCoordsToScreenCoords(currentPointX, yLimits[0]);
                g.drawLine((int) screenCoords[0], graphLocationY[0], (int) screenCoords[0], graphLocationY[1]);
                currentPointX += spaceBetweenNumbersX;
            }
        }
    }

    void drawXAxisLog(Graphics2D g, int[] graphLocationX, int[] graphLocationY) {
        double[] xLimits;
        if (this.xLimits[0] < this.xLimits[1]) {
            xLimits = this.xLimits;
        } else {
            xLimits = new double[] {1, 10};
        }

        double[] yLimits;
        if (this.yLimits[0] < this.yLimits[1]) {
            yLimits = this.yLimits;
        } else {
            yLimits = new double[] {1, 10};
        }

        //axis numbers
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(lineWidth));
        double[] axisStartDistX = axisStartDistLog(xLimits);
        double currentPowerX = axisStartDistX[0];
        double spaceBetweenPowersX = axisStartDistX[1];


        while (Math.pow(10, currentPowerX) <= xLimits[1]) {
            double[] screenCoords = graphCoordsToScreenCoords(Math.pow(10, currentPowerX), yLimits[0]);
            if (screenCoords[0] > graphLocationX[1]) break;
            g.drawLine((int) screenCoords[0], graphLocationY[1] - 10, (int) screenCoords[0], graphLocationY[1] + 10);
            g.drawString(String.valueOf(Math.pow(10, currentPowerX)), (int) screenCoords[0] - g.getFontMetrics().stringWidth(String.valueOf(Math.pow(10, currentPowerX))) / 2, graphLocationY[1] + 15 + fontSize);
            currentPowerX += spaceBetweenPowersX;
        }

        //grid
        if (showGrid) {
            g.setPaint(new Color(0, 0, 0, 50));
            g.setStroke(new BasicStroke(lineWidth/3));

            currentPowerX = axisStartDistX[0];
            spaceBetweenPowersX = axisStartDistX[1];

            while (Math.pow(10, currentPowerX) < xLimits[1]) {
                double[] screenCoords = graphCoordsToScreenCoords(Math.pow(10, currentPowerX), yLimits[0]);
                g.drawLine((int) screenCoords[0], graphLocationY[0], (int) screenCoords[0], graphLocationY[1]);
                currentPowerX += spaceBetweenPowersX;
            }
        }
    }

    void drawYAxisNormal(Graphics2D g, int[] graphLocationX, int[] graphLocationY) {
        double[] xLimits;
        if (this.xLimits[0] < this.xLimits[1]) {
            xLimits = this.xLimits;
        } else {
            xLimits = new double[] {0, 10};
        }

        double[] yLimits;
        if (this.yLimits[0] < this.yLimits[1]) {
            yLimits = this.yLimits;
        } else {
            yLimits = new double[] {0, 10};
        }

        //axis numbers
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(lineWidth));
        double[] axisStartDistY = axisStartDist(yLimits);
        double currentPointY = axisStartDistY[0];
        double spaceBetweenNumbersY = axisStartDistY[1];

        while (currentPointY <= yLimits[1]) {
            double[] screenCoords = graphCoordsToScreenCoords(xLimits[0], currentPointY);
            if (screenCoords[1] < graphLocationY[0]) break;
            g.drawLine(graphLocationX[0] - 10, (int) screenCoords[1], graphLocationX[0] + 10, (int) screenCoords[1]);
            g.drawString(String.valueOf(currentPointY), graphLocationX[0] - 25 - g.getFontMetrics().stringWidth(String.valueOf(currentPointY)), (int) screenCoords[1] + 5);
            currentPointY += spaceBetweenNumbersY;
        }

        //grid
        if (showGrid) {
            g.setPaint(new Color(0, 0, 0, 50));
            g.setStroke(new BasicStroke(lineWidth/3));

            currentPointY = axisStartDistY[0];
            spaceBetweenNumbersY = axisStartDistY[1];

            while (currentPointY < yLimits[1]) {
                double[] screenCoords = graphCoordsToScreenCoords(xLimits[0], currentPointY);
                g.drawLine(graphLocationX[0], (int) screenCoords[1], graphLocationX[1], (int) screenCoords[1]);
                currentPointY += spaceBetweenNumbersY;
            }
        }
    }

    void drawYAxisLog(Graphics2D g, int[] graphLocationX, int[] graphLocationY) {
        double[] xLimits;
        if (this.xLimits[0] < this.xLimits[1]) {
            xLimits = this.xLimits;
        } else {
            xLimits = new double[] {1, 10};
        }

        double[] yLimits;
        if (this.yLimits[0] < this.yLimits[1]) {
            yLimits = this.yLimits;
        } else {
            yLimits = new double[] {1, 10};
        }

        //axis numbers
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(lineWidth));
        double[] axisStartDistY = axisStartDistLog(yLimits);
        double currentPowY = axisStartDistY[0];
        double spaceBetweenPowY = axisStartDistY[1];

        while (Math.pow(10, currentPowY) <= yLimits[1]) {
            double[] screenCoords = graphCoordsToScreenCoords(xLimits[0], Math.pow(10, currentPowY));
            if (screenCoords[1] < graphLocationY[0]) break;
            g.drawLine(graphLocationX[0] - 10, (int) screenCoords[1], graphLocationX[0] + 10, (int) screenCoords[1]);
            g.drawString(String.valueOf(Math.pow(10, currentPowY)), graphLocationX[0] - 25 - g.getFontMetrics().stringWidth(String.valueOf(Math.pow(10, currentPowY))), (int) screenCoords[1] + 5);
            currentPowY += spaceBetweenPowY;
        }

        //grid
        if (showGrid) {
            g.setPaint(new Color(0, 0, 0, 50));
            g.setStroke(new BasicStroke(lineWidth/3));

            currentPowY = axisStartDistY[0];
            spaceBetweenPowY = axisStartDistY[1];

            while (Math.pow(10, currentPowY) < yLimits[1]) {
                double[] screenCoords = graphCoordsToScreenCoords(xLimits[0], Math.pow(10, currentPowY));
                g.drawLine(graphLocationX[0], (int) screenCoords[1], graphLocationX[1], (int) screenCoords[1]);
                currentPowY += spaceBetweenPowY;
            }
        }
    }

    double[] graphCoordsToScreenCoords(double x, double y) {

        double[] xLimits;
        if (this.xLimits[0] < this.xLimits[1]) {
            xLimits = this.xLimits;
        } else {
            if (logX) {
                xLimits = new double[] {1,10};
            } else {
                xLimits = new double[]{0, 10};
            }
        }

        double[] yLimits;
        if (this.yLimits[0] < this.yLimits[1]) {
            yLimits = this.yLimits;
        } else {
            if (logX) {
                yLimits = new double[] {1,10};
            } else {
                yLimits = new double[]{0, 10};
            }
        }

        int width = (int) Math.ceil(verticalResolution * aspectRatio);
        int height = verticalResolution;

        double percentageX = (float) width / (width - 2 * borderSize);
        double percentageY = (float) height / (height - 2 * borderSize);

        double xLim0;
        double xLim1;
        if (logX) {
            xLim0 = Math.log10(xLimits[0]);
            xLim1 = Math.log10(xLimits[1]);
            x = Math.log10(x);
        } else {
            xLim0 = xLimits[0];
            xLim1 = xLimits[1];
        }

        double yLim0;
        double yLim1;
        if (logY) {
            yLim0 = Math.log10(yLimits[0]);
            yLim1 = Math.log10(yLimits[1]);
            y = Math.log10(y);
        } else {
            yLim0 = yLimits[0];
            yLim1 = yLimits[1];
        }

        double lengthX = xLim1 - xLim0;;
        double screenX = borderSize + ((x - xLim0) / lengthX) * (width / percentageX);;


        double lengthY = yLim1 - yLim0;
        double screenY = borderSize + (1 - ((y - yLim0) / lengthY)) * (height / percentageY);

        return new double[] {screenX, screenY};
    }

    void calculateLimits() {
        if (graphElements.size() == 0) {
            if (logX) {
                xLimits[0] = 1;
                xLimits[1] = 100;
            } else {
                xLimits[0] = 0;
                xLimits[1] = 10;
            }
        } else if (outerPointsX[0] == outerPointsX[1]) {
            if (logX) {
                xLimits[0] = outerPointsX[0] / 10;
                xLimits[1] = outerPointsX[1] * 10;
            } else {
                xLimits[0] = outerPointsX[0] - 1;
                xLimits[1] = outerPointsX[1] + 1;
            }
        } else {
            if (logX) {
                xLimits[0] = Math.pow(10, Math.log10(outerPointsX[0]) - (Math.log10(outerPointsX[1]) - Math.log10(outerPointsX[0])) / 10);
                xLimits[1] = Math.pow(10, Math.log10(outerPointsX[1]) + (Math.log10(outerPointsX[0]) - Math.log10(outerPointsX[1])) / 10);
            } else {
                xLimits[0] = outerPointsX[0] - (outerPointsX[1] - outerPointsX[0]) / 10;
                xLimits[1] = outerPointsX[1] + (outerPointsX[1] - outerPointsX[0]) / 10;
            }
        }

        if (graphElements.size() == 0) {
            if (logY) {
                yLimits[0] = 1;
                yLimits[1] = 100;
            } else {
                yLimits[0] = 0;
                yLimits[1] = 10;
            }
        } else if (outerPointsY[0] == outerPointsY[1]) {
            if (logY) {
                yLimits[0] = outerPointsY[0] / 10;
                yLimits[1] = outerPointsY[1] * 10;
            } else {
                yLimits[0] = outerPointsY[0] - 1;
                yLimits[1] = outerPointsY[1] + 1;
            }
        } else {
            if (logY) {
                yLimits[0] = Math.pow(10, Math.log10(outerPointsY[0]) - (Math.log10(outerPointsY[1]) - Math.log10(outerPointsY[0])) / 10);
                yLimits[1] = Math.pow(10, Math.log10(outerPointsY[1]) + (Math.log10(outerPointsY[1]) - Math.log10(outerPointsY[0])) / 10);
            } else {
                yLimits[0] = outerPointsY[0] - (outerPointsY[1] - outerPointsY[0]) / 10;
                yLimits[1] = outerPointsY[1] + (outerPointsY[1] - outerPointsY[0]) / 10;
            }
        }
    }

    //objects

    class Plot extends JFrame {
        PlotPanel component;

        public Plot(PlotPanel component) {
            this.component = component;
            setTitle(title);
            setSize((int) Math.ceil(500 * aspectRatio), 500);
            setPreferredSize(new Dimension((int) Math.ceil(500 * aspectRatio), 500));
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            setVisible(true);
            setLayout(new GridBagLayout());
            setResizable(false);
        }

        public void draw() {
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 1;
            c.weighty = 1;

            add(component, c);
            this.pack();
        }
    }

    class PlotPanel extends JPanel {

        public PlotPanel() {
            setName(title);
            setSize((int) Math.ceil(500 * aspectRatio), 500);
            setPreferredSize(new Dimension((int) Math.ceil(500 * aspectRatio), 500));
            setVisible(true);
            setLayout(new GridBagLayout());
        }

        public void draw() {
            JComponent component = new JComponent() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
                }
            };

            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 1;
            c.weighty = 1;

            add(component, c);
        }

        @Override
        public void repaint() {
            removeAll();
            JComponent component = new JComponent() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
                }
            };

            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 1;
            c.weighty = 1;

            add(component, c);
        }
    }

    abstract class GraphElement {
        Color color;

        public GraphElement(Color color) {
            this.color = color;
        }

        public void draw(Graphics2D g) {}

        public void setOuterPoints() {}
    }

    public interface GraphAnimation2D {
        public void animateGraph(int i);
    }

}
