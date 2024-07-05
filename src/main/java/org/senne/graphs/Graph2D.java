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

import static java.lang.Thread.onSpinWait;
import static java.lang.Thread.sleep;

public class Graph2D {

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

    //constructors

    public Graph2D(double x, double y) {
        graphElements.add(new Point(x, y, Style.getPremierGraphColor()));

        xLimits[0] = x - 1;
        xLimits[1] = x + 1;
        outerPointsX[0] = x;
        outerPointsX[1] = x;

        yLimits[0] = y - 1;
        yLimits[1] = y + 1;
        outerPointsY[0] = y;
        outerPointsY[1] = y;
    }

    public Graph2D(double x, double y, Color color) {
        graphElements.add(new Point(x, y, color));

        xLimits[0] = x - 1;
        xLimits[1] = x + 1;
        outerPointsX[0] = x;
        outerPointsX[1] = x;

        yLimits[0] = y - 1;
        yLimits[1] = y + 1;
        outerPointsY[0] = y;
        outerPointsY[1] = y;
    }

    public Graph2D(double x, double y, String title) {
        graphElements.add(new Point(x, y, Style.getPremierGraphColor()));

        this.title = title;

        xLimits[0] = x - 1;
        xLimits[1] = x + 1;
        outerPointsX[0] = x;
        outerPointsX[1] = x;

        yLimits[0] = y - 1;
        yLimits[1] = y + 1;
        outerPointsY[0] = y;
        outerPointsY[1] = y;
    }

    public Graph2D(double x, double y, Color color, String title) {
        graphElements.add(new Point(x, y, color));

        this.title = title;

        xLimits[0] = x - 1;
        xLimits[1] = x + 1;
        outerPointsX[0] = x;
        outerPointsX[1] = x;

        yLimits[0] = y - 1;
        yLimits[1] = y + 1;
        outerPointsY[0] = y;
        outerPointsY[1] = y;
    }

    public Graph2D(List<Double> pointsX, List<Double> pointsY) {
        if (pointsX.size() != pointsY.size()) throw new IllegalArgumentException("pointsX and pointsY must have the same size");

        graphElements.add(new Graph(pointsX, pointsY, Style.getPremierGraphColor()));

        double xMin = pointsX.stream().min(Double::compareTo).get();
        double xMax = pointsX.stream().max(Double::compareTo).get();

        xLimits[0] = xMin - (xMax - xMin) / 10;
        xLimits[1] = xMax + (xMax - xMin) / 10;
        outerPointsX[0] = xMin;
        outerPointsX[1] = xMax;

        double yMin = pointsY.stream().min(Double::compareTo).get();
        double yMax = pointsY.stream().max(Double::compareTo).get();

        yLimits[0] = yMin - (yMax - yMin) / 10;
        yLimits[1] = yMax + (yMax - yMin) / 10;
        outerPointsY[0] = yMin;
        outerPointsY[1] = yMax;
    }

    public Graph2D(List<Double> pointsX, List<Double> pointsY, Color color) {
        if (pointsX.size() != pointsY.size()) throw new IllegalArgumentException("pointsX and pointsY must have the same size");

        graphElements.add(new Graph(pointsX, pointsY, color));

        double xMin = pointsX.stream().min(Double::compareTo).get();
        double xMax = pointsX.stream().max(Double::compareTo).get();

        xLimits[0] = xMin - (xMax - xMin) / 10;
        xLimits[1] = xMax + (xMax - xMin) / 10;
        outerPointsX[0] = xMin;
        outerPointsX[1] = xMax;

        double yMin = pointsY.stream().min(Double::compareTo).get();
        double yMax = pointsY.stream().max(Double::compareTo).get();

        yLimits[0] = yMin - (yMax - yMin) / 10;
        yLimits[1] = yMax + (yMax - yMin) / 10;
        outerPointsY[0] = yMin;
        outerPointsY[1] = yMax;
    }

    public Graph2D(List<Double> pointsX, List<Double> pointsY, String title) {
        if (pointsX.size() != pointsY.size()) throw new IllegalArgumentException("pointsX and pointsY must have the same size");

        graphElements.add(new Graph(pointsX, pointsY, Style.getPremierGraphColor()));

        double xMin = pointsX.stream().min(Double::compareTo).get();
        double xMax = pointsX.stream().max(Double::compareTo).get();

        xLimits[0] = xMin - (xMax - xMin) / 10;
        xLimits[1] = xMax + (xMax - xMin) / 10;
        outerPointsX[0] = xMin;
        outerPointsX[1] = xMax;

        double yMin = pointsY.stream().min(Double::compareTo).get();
        double yMax = pointsY.stream().max(Double::compareTo).get();

        yLimits[0] = yMin - (yMax - yMin) / 10;
        yLimits[1] = yMax + (yMax - yMin) / 10;
        outerPointsY[0] = yMin;
        outerPointsY[1] = yMax;

        this.title = title;
    }

    public Graph2D(List<Double> pointsX, List<Double> pointsY, Color color, String title) {
        if (pointsX.size() != pointsY.size()) throw new IllegalArgumentException("pointsX and pointsY must have the same size");

        graphElements.add(new Graph(pointsX, pointsY, color));

        double xMin = pointsX.stream().min(Double::compareTo).get();
        double xMax = pointsX.stream().max(Double::compareTo).get();

        xLimits[0] = xMin - (xMax - xMin) / 10;
        xLimits[1] = xMax + (xMax - xMin) / 10;
        outerPointsX[0] = xMin;
        outerPointsX[1] = xMax;

        double yMin = pointsY.stream().min(Double::compareTo).get();
        double yMax = pointsY.stream().max(Double::compareTo).get();

        yLimits[0] = yMin - (yMax - yMin) / 10;
        yLimits[1] = yMax + (yMax - yMin) / 10;
        outerPointsY[0] = yMin;
        outerPointsY[1] = yMax;

        this.title = title;
    }

    //graph elements

    public void addGraph(List<Double> pointsX, List<Double> pointsY) {
        Color color;
        if ((graphElements.size() + 1) % 4 == 0) {
            color = Style.getQuaternaryGraphColor();
        } else if ((graphElements.size() + 1) % 3 == 0) {
            color = Style.getTertiaryGraphColor();
        } else if ((graphElements.size() + 1) % 2 == 0) {
            color = Style.getSecondaryGraphColor();
        } else {
            color = Style.getPremierGraphColor();
        }

        addGraph(pointsX, pointsY, color);
    }

    public void addGraph(List<Double> pointsX, List<Double> pointsY, Color color) {
        if (pointsX.size() != pointsY.size()) throw new IllegalArgumentException("pointsX and pointsY must have the same size");

        Graph graph = new Graph(pointsX, pointsY, color);
        graph.setGraphLimits();
        graphElements.add(graph);
    }

    public void addPoint(double x, double y) {
        Color color;
        if ((graphElements.size() + 1) % 4 == 0) {
            color = Style.getQuaternaryGraphColor();
        } else if ((graphElements.size() + 1) % 3 == 0) {
            color = Style.getTertiaryGraphColor();
        } else if ((graphElements.size() + 1) % 2 == 0) {
            color = Style.getSecondaryGraphColor();
        } else {
            color = Style.getPremierGraphColor();
        }

        addPoint(x, y, color);
    }

    public void addPoint(double x, double y, Color color) {
        Point point = new Point(x, y, color);
        point.setGraphLimits();

        graphElements.add(point);
    }

    public void addPointList(List<Double> x, List<Double> y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException("x and y must have the same size");
        }

        Color color;
        if ((graphElements.size() + 1) % 4 == 0) {
            color = Style.getQuaternaryGraphColor();
        } else if ((graphElements.size() + 1) % 3 == 0) {
            color = Style.getTertiaryGraphColor();
        } else if ((graphElements.size() + 1) % 2 == 0) {
            color = Style.getSecondaryGraphColor();
        } else {
            color = Style.getPremierGraphColor();
        }

        addPointList(x, y, color);
    }

    public void addPointList(List<Double> x, List<Double> y, Color color) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException("x and y must have the same size");
        }

        for (int i = 0; i < x.size(); i++) {
            addPoint(x.get(i), y.get(i), color);
        }
    }

    public void addErrorBar(double x, double y, double errorX, double errorY) {
        addErrorBar(x, y, Style.getErrorBarColor(), errorX, errorY);
    }

    public void addErrorBar(double x, double y, Color color, double errorX, double errorY) {

        if (errorX == 0 && errorY == 0) {
            throw new IllegalArgumentException("Error bar must have at least one non-zero error value");
        }

        ErrorBar error = new ErrorBar(x, y, color);
        if (errorX != 0) error.setErrorX(errorX);
        if (errorY != 0) error.setErrorY(errorY);

        error.setGraphLimits();

        graphElements.add(error);
    }

    public void addErrorBarList(List<Double> x, List<Double> y, List<Double> errorX, List<Double> errorY) {
        if (x.size() != y.size() || x.size() != errorX.size() || x.size() != errorY.size()) {
            throw new IllegalArgumentException("x, y, errorX and errorY must have the same size");
        }

        for (int i = 0; i < x.size(); i++) {
            addErrorBar(x.get(i), y.get(i), Style.getErrorBarColor(), errorX.get(i), errorY.get(i));
        }
    }

    public void addErrorBarList(List<Double> x, List<Double> y, Color color, List<Double> errorX, List<Double> errorY) {
        if (x.size() != y.size() || x.size() != errorX.size() || x.size() != errorY.size()) {
            throw new IllegalArgumentException("x, y, errorX and errorY must have the same size");
        }

        for (int i = 0; i < x.size(); i++) {
            addErrorBar(x.get(i), y.get(i), color, errorX.get(i), errorY.get(i));
        }
    }

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
        recalculateBoundaries();
    }

    public void logY() {
        logY = true;
        recalculateBoundaries();
    }

    public void logLog() {
        logX = true;
        logY = true;
        recalculateBoundaries();
    }

    //plotting

    public void plot() {

        //print the limits
        System.out.println("xLimits: " + xLimits[0] + ", " + xLimits[1]);
        System.out.println("yLimits: " + yLimits[0] + ", " + yLimits[1]);

        //print log plot
        System.out.println("logX: " + logX);
        System.out.println("logY: " + logY);

        int width = (int) Math.ceil(verticalResolution * aspectRatio);
        int height = verticalResolution;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        drawGraph(g);

        Plot plot = new Plot(new PlotPanel());
        plot.draw();
    }

    public void save(String fileName) throws IOException {
        ImageIO.write(image, "png", new File(fileName + ".png"));
    }

    public void animate(GraphAnimation2D animation, int frames, int delay) throws InterruptedException {
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
    public Graph2D setGraphState(int i, List<Double> pointsX, List<Double> pointsY) {
        if (pointsX.size() != pointsY.size()) throw new IllegalArgumentException("pointsX and pointsY must have the same size");

        if (!(graphElements.get(i) instanceof Graph)) throw new IllegalArgumentException("graph element at position " + i + " is not a valid graph element. Must be a Graph.");

        if (!xLimitsCustom) {
            double xMin = pointsX.stream().min(Double::compareTo).get();
            double xMax = pointsX.stream().max(Double::compareTo).get();

            if (xMin < outerPointsX[0]) {
                xLimits[0] = xMin - (outerPointsX[1] - xMin) / 10;
                outerPointsX[0] = xMin;
            }
            if (xMax > outerPointsX[1]) {
                xLimits[1] = xMax + (xMax - outerPointsX[0]) / 10;
                outerPointsX[1] = xMax;
            }
        }

        if (!yLimitsCustom) {
            double yMin = pointsY.stream().min(Double::compareTo).get();
            double yMax = pointsY.stream().max(Double::compareTo).get();

            if (yMin < outerPointsY[0]) {
                yLimits[0] = yMin - (outerPointsY[1] - yMin) / 10;
                outerPointsY[0] = yMin;
            }
            if (yMax > outerPointsY[1]) {
                yLimits[1] = yMax + (yMax - outerPointsY[0]) / 10;
                outerPointsY[1] = yMax;
            }
        }

        ((Graph) graphElements.get(i)).setPoints(pointsX, pointsY);
        return this;
    }

    public Graph2D setPointState(int i, boolean show) {
        if (!(graphElements.get(i) instanceof Point)) throw new IllegalArgumentException("graph element at position " + i + " is not a valid graph element. Must be a Point.");

        ((Point) graphElements.get(i)).setShow(show);
        return this;
    }

    public Graph2D setAnimateAxis(boolean animateAxis) {
        this.animateAxis = animateAxis;
        return this;
    }

    public Graph2D setAxisAnimationFrames(int frames) {
        this.axisAnimationFrames = frames;
        return this;
    }

    // |-|  |--\ |-|  |-----|  |-----|  |-----|  |--\ |-|    /---\    |-|
    // | |  | |\\| |    | |    |--|     |_____|  | |\\| |   / /_\ \   | |
    // |_|  |_| \__|    |_|    |_____|  |_| \_\  |_| \__|  /_/   \_\  |____|

    void drawGraph(Graphics2D g) {
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
        BigDecimal bDLimits[] = new BigDecimal[] {new BigDecimal(String.valueOf(limits[0])), new BigDecimal(String.valueOf(limits[1]))};

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
        BigDecimal bDLimits[] = new BigDecimal[] {new BigDecimal(String.valueOf(Math.log10(limits[0]))), new BigDecimal(String.valueOf(Math.log10(limits[1])))};

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

    /*BufferedImage AxisAnimation(Graphics2D g, int width, int height, double axisAnimationFrames, int i) {
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(lineWidth));

        double distancePercentage = (-(Math.cos(Math.PI * (i / (axisAnimationFrames - 1))) - 1) / 2);

        int[] graphLocationX = {borderSize, (int) ((width - borderSize * 2) * distancePercentage + borderSize)};
        int[] graphLocationY = {(int) ((height - 2 * borderSize) * (1 - distancePercentage) + borderSize), height - borderSize};

        //x, y axis
        g.drawLine(graphLocationX[0], graphLocationY[0], graphLocationX[0], graphLocationY[1]);
        g.drawLine(graphLocationX[0], graphLocationY[1], graphLocationX[1], graphLocationY[1]);

        //axis numbers
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

            currentPointX = axisStartDistX[0];
            spaceBetweenNumbersX = axisStartDistX[1];

            while (currentPointX < xLimits[1]) {
                double[] screenCoords = graphCoordsToScreenCoords(currentPointX, yLimits[0]);
                g.drawLine((int) screenCoords[0], graphLocationY[0], (int) screenCoords[0], graphLocationY[1]);
                currentPointX += spaceBetweenNumbersX;
            }

            currentPointY = axisStartDistY[0];
            spaceBetweenNumbersY = axisStartDistY[1];

            while (currentPointY < yLimits[1]) {
                double[] screenCoords = graphCoordsToScreenCoords(xLimits[0], currentPointY);
                g.drawLine(graphLocationX[0], (int) screenCoords[1], graphLocationX[1], (int) screenCoords[1]);
                currentPointY += spaceBetweenNumbersY;
            }

            g.drawLine(graphLocationX[0], borderSize, graphLocationX[1], borderSize);
            g.drawLine(width - borderSize, graphLocationY[1], width - borderSize, graphLocationY[0]);
            g.setStroke(new BasicStroke(lineWidth));
        }

        //title
        g.setColor(Color.BLACK);
        g.setFont(new Font("Calibri", Font.BOLD, fontSize * 2));
        g.drawString(title, width / 2 - g.getFontMetrics().stringWidth(title) / 2, borderSize / 2 + fontSize);
        g.setFont(new Font("Calibri", Font.BOLD, fontSize));

        return image;
    }*/

    double[] graphCoordsToScreenCoords(double x, double y) {

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

    /*double[] graphCoordsToScreenCoords(double x, double y) {

        int width = (int) Math.ceil(verticalResolution * aspectRatio);
        int height = verticalResolution;

        double percentageX = (float) width / (width - 2 * borderSize);
        double percentageY = (float) height / (height - 2 * borderSize);

        double lengthX;
        double screenX;
        if (logX) {
            x = Math.log10(x);
            lengthX = Math.log10(xLimits[1]) - Math.log10(xLimits[0]);
            screenX = borderSize + ((Math.log10(x) - Math.log10(xLimits[0])) / lengthX) * (width / percentageX);
        } else {
            lengthX = xLimits[1] - xLimits[0];
            screenX = borderSize + ((x - xLimits[0]) / lengthX) * (width / percentageX);
        }

        double lengthY;
        double screenY;
        if (logY) {
            y = Math.log10(y);
            lengthY = Math.log10(yLimits[1]) - Math.log10(yLimits[0]);
            screenY = borderSize + (1 - ((Math.log10(y) - Math.log10(yLimits[0])) / lengthY)) * (height / percentageY);
        } else {
            lengthY = yLimits[1] - yLimits[0];
            screenY = borderSize + (1 - ((y - yLimits[0]) / lengthY)) * (height / percentageY);
        }

        return new double[] {screenX, screenY};
    }*/

    void recalculateBoundaries() {
        if (xLimitsCustom && yLimitsCustom) return;

        for (GraphElement graph : graphElements) {
            graph.setGraphLimits();
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

    private abstract class GraphElement {
        Color color;

        public GraphElement(Color color) {
            this.color = color;
        }

        public void draw(Graphics2D g) {}

        public void setGraphLimits() {}
    }

    class Graph extends GraphElement {

        List<Double> x;
        List<Double> y;

        public Graph(List<Double> x, List<Double> y, Color color) {
            super(color);
            this.x = x;
            this.y = y;
        }

        @Override
        public void draw(Graphics2D g) {
            g.setPaint(color);

            for (int i = 0; i < x.size() - 1; i++) {
                double[] points = {x.get(i), y.get(i), x.get(i + 1), y.get(i + 1)};

                //outside of the graph
                if ((points[0] <= xLimits[0] && points[2] <= xLimits[0]) || (points[1] <= yLimits[0] && points[3] <= yLimits[0])) continue;
                if ((points[0] >= xLimits[1] && points[2] >= xLimits[1]) || (points[1] >= yLimits[1] && points[3] >= yLimits[1])) continue;

                //part in the graph
                if (points[0] < xLimits[0]){
                    double rico = (points[3] - points[1]) / (points[2] - points[0]);
                    double b = points[1] - rico * points[0];
                    double newY = rico * xLimits[0] + b;

                    if (newY < yLimits[0] || newY > yLimits[1]){
                        continue;
                    }

                    points[0] = xLimits[0];
                    points[1] = newY;
                }
                if (points[2] < xLimits[0]){
                    double rico = (points[3] - points[1]) / (points[2] - points[0]);
                    double b = points[1] - rico * points[0];
                    double newY = rico * xLimits[0] + b;

                    if (newY < yLimits[0] || newY > yLimits[1]){
                        continue;
                    }

                    points[2] = xLimits[0];
                    points[3] = newY;
                }
                if (points[0] > xLimits[1]){
                    double rico = (points[3] - points[1]) / (points[2] - points[0]);
                    double b = points[1] - rico * points[0];
                    double newY = rico * xLimits[1] + b;

                    if (newY < yLimits[0] || newY > yLimits[1]){
                        continue;
                    }

                    points[0] = xLimits[1];
                    points[1] = newY;
                }
                if (points[2] > xLimits[1]){
                    double rico = (points[3] - points[1]) / (points[2] - points[0]);
                    double b = points[1] - rico * points[0];
                    double newY = rico * xLimits[1] + b;

                    if (newY < yLimits[0] || newY > yLimits[1]){
                        continue;
                    }

                    points[2] = xLimits[1];
                    points[3] = newY;
                }

                if (points[1] < yLimits[0]){
                    double rico = (points[3] - points[1]) / (points[2] - points[0]);
                    double b = points[1] - rico * points[0];
                    double newX = (yLimits[0] - b) / rico;

                    if (newX < xLimits[0] || newX > xLimits[1]){
                        continue;
                    }

                    points[0] = newX;
                    points[1] = yLimits[0];
                }
                if (points[3] < yLimits[0]){
                    double rico = (points[3] - points[1]) / (points[2] - points[0]);
                    double b = points[1] - rico * points[0];
                    double newX = (yLimits[0] - b) / rico;

                    if (newX < xLimits[0] || newX > xLimits[1]){
                        continue;
                    }

                    points[2] = newX;
                    points[3] = yLimits[0];
                }
                if (points[1] > yLimits[1]){
                    double rico = (points[3] - points[1]) / (points[2] - points[0]);
                    double b = points[1] - rico * points[0];
                    double newX = (yLimits[1] - b) / rico;

                    if (newX < xLimits[0] || newX > xLimits[1]){
                        continue;
                    }

                    points[0] = newX;
                    points[1] = yLimits[1];
                }
                if (points[3] > yLimits[1]){
                    double rico = (points[3] - points[1]) / (points[2] - points[0]);
                    double b = points[1] - rico * points[0];
                    double newX = (yLimits[1] - b) / rico;

                    if (newX < xLimits[0] || newX > xLimits[1]){
                        continue;
                    }

                    points[2] = newX;
                    points[3] = yLimits[1];
                }

                double[] screenCoords1 = graphCoordsToScreenCoords(points[0], points[1]);
                double[] screenCoords2 = graphCoordsToScreenCoords(points[2], points[3]);
                g.drawLine((int) screenCoords1[0], (int) screenCoords1[1], (int) screenCoords2[0], (int) screenCoords2[1]);
            }
        }

        public Graph setPoints(List<Double> x, List<Double> y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public void setGraphLimits() {
            if (!xLimitsCustom) {
                double xMin = x.stream().min(Double::compareTo).get();
                double xMax = x.stream().max(Double::compareTo).get();

                if (xMin < outerPointsX[0]) {
                    if (logX) {
                        xLimits[0] = Math.pow(Math.log10(xMin) - (Math.log10(outerPointsX[1]) - Math.log10(xMin)) / 10, 10);
                    } else {
                        xLimits[0] = xMin - (outerPointsX[1] - xMin) / 10;
                    }
                    outerPointsX[0] = xMin;
                }
                if (xMax > outerPointsX[1]) {
                    if (logX) {
                        xLimits[1] = Math.pow(Math.log10(xMax) + (Math.log10(outerPointsX[0]) - Math.log10(xMax)) / 10, 10);
                    } else {
                        xLimits[1] = xMax + (xMax - outerPointsX[0]) / 10;
                    }
                    outerPointsX[1] = xMax;
                }
            }

            if (!yLimitsCustom) {
                double yMin = y.stream().min(Double::compareTo).get();
                double yMax = y.stream().max(Double::compareTo).get();

                if (yMin < outerPointsY[0]) {
                    if (logX) {
                        yLimits[0] = Math.pow(Math.log10(yMin) - (Math.log10(outerPointsY[1]) - Math.log10(yMin)) / 10, 10);
                    } else {
                        yLimits[0] = yMin - (outerPointsY[1] - yMin) / 10;
                    }
                    outerPointsY[0] = yMin;
                }
                if (yMax > outerPointsY[1]) {
                    if (logX) {
                        yLimits[1] = Math.pow(Math.log10(yMax) + (Math.log10(outerPointsY[0]) - Math.log10(yMax)) / 10, 10);
                    } else {
                        yLimits[1] = yMax + (yMax - outerPointsY[0]) / 10;
                    }
                    outerPointsY[1] = yMax;
                }
            }
        }
    }

    class Point extends GraphElement {

        double x;
        double y;
        boolean show = true;

        public Point(double x, double y, Color color) {
            super(color);
            this.x = x;
            this.y = y;
        }

        @Override
        public void draw(Graphics2D g) {
            if (!show) return;
            g.setPaint(color);

            if (x < xLimits[0] || x > xLimits[1] || y < yLimits[0] || y > yLimits[1]) return;

            g.fillOval((int) (graphCoordsToScreenCoords(x, y)[0] - 1.5f * lineWidth), (int) (graphCoordsToScreenCoords(x, y)[1] - 1.5f * lineWidth), (int) (lineWidth * 3), (int) (lineWidth * 3));
        }

        public Point setShow(boolean show) {
            this.show = show;
            return this;
        }

        public void setGraphLimits() {
            if (!xLimitsCustom) {
                if (x < outerPointsX[0]) {
                    if (logX) {
                        xLimits[0] = Math.exp(Math.log10(x) - (Math.log10(outerPointsX[1]) - Math.log10(x)) / 10);
                    } else {
                        xLimits[0] = x - (outerPointsX[1] - x) / 10;
                    }
                    outerPointsX[0] = x;
                } else if (x > outerPointsX[1]) {
                    if (logX) {
                        xLimits[1] = Math.exp(Math.log10(x) + (Math.log10(outerPointsX[0]) - Math.log10(x)) / 10);
                    } else {
                        xLimits[1] = x + (x - outerPointsX[0]) / 10;
                    }
                    outerPointsX[1] = x;
                }
            }

            if (!yLimitsCustom) {
                if (y < outerPointsY[0]) {
                    if (logX) {
                        yLimits[0] = Math.exp(Math.log10(y) - (Math.log10(outerPointsY[1]) - Math.log10(y)) / 10);
                    } else {
                        yLimits[0] = y - (outerPointsY[1] - y) / 10;
                    }
                    outerPointsY[0] = y;
                } else if (y > outerPointsY[1]) {
                    if (logX) {
                        yLimits[1] = Math.exp(Math.log10(y) + (Math.log10(outerPointsY[0]) - Math.log10(y)) / 10);
                    } else {
                        yLimits[1] = y + (y - outerPointsY[0]) / 10;
                    }
                    outerPointsY[1] = y;
                }
            }
        }
    }

    class ErrorBar extends Point {

        boolean errorXBool = false;
        boolean errorYBool = false;
        double errorX;
        double errorY;

        public ErrorBar(double x, double y, Color color, double errorX, double errorY) {
            super(x, y, color);

            errorXBool = true;
            this.errorX = errorX;

            errorYBool = true;
            this.errorY = errorY;
        }

        public ErrorBar(double x, double y, Color color) {
            super(x, y, color);
        }

        public void setErrorX(double errorX) {
            errorXBool = true;
            this.errorX = errorX;
        }

        public void setErrorY(double errorY) {
            errorYBool = true;
            this.errorY = errorY;
        }

        @Override
        public void draw(Graphics2D g) {
            if (!show) return;
            g.setPaint(color);

            if (x < xLimits[0] || x > xLimits[1] || y < yLimits[0] || y > yLimits[1]) return;

            g.fillOval((int) (graphCoordsToScreenCoords(x, y)[0] - 1.5f * lineWidth), (int) (graphCoordsToScreenCoords(x, y)[1] - 1.5f * lineWidth), (int) (lineWidth * 3), (int) (lineWidth * 3));

            //error bars
            double[] screenCoordsXMin = graphCoordsToScreenCoords(x - errorX, y);
            double[] screenCoordsXMax = graphCoordsToScreenCoords(x + errorX, y);

            double[] screenCoordsXMinDraw;
            double[] screenCoordsXMaxDraw;

            if (screenCoordsXMin[0] < borderSize) {
                screenCoordsXMinDraw = graphCoordsToScreenCoords(x, y);
                screenCoordsXMaxDraw = graphCoordsToScreenCoords(x + errorX, y);
            } else if (screenCoordsXMax[0] > (int) Math.ceil(verticalResolution * aspectRatio) - borderSize) {
                screenCoordsXMinDraw = graphCoordsToScreenCoords(x - errorX, y);
                screenCoordsXMaxDraw = graphCoordsToScreenCoords(x, y);
            } else {
                screenCoordsXMinDraw = screenCoordsXMin;
                screenCoordsXMaxDraw = screenCoordsXMax;
            }

            double[] screenCoordsYMin = graphCoordsToScreenCoords(x, y - errorY);
            double[] screenCoordsYMax = graphCoordsToScreenCoords(x, y + errorY);

            double[] screenCoordsYMinDraw;
            double[] screenCoordsYMaxDraw;

            if (screenCoordsYMin[1] < borderSize) {
                screenCoordsYMinDraw = graphCoordsToScreenCoords(x, y);
                screenCoordsYMaxDraw = graphCoordsToScreenCoords(x, y + errorY);
            } else if (screenCoordsYMax[1] > verticalResolution - borderSize) {
                screenCoordsYMinDraw = graphCoordsToScreenCoords(x, y - errorY);
                screenCoordsYMaxDraw = graphCoordsToScreenCoords(x, y);
            } else {
                screenCoordsYMinDraw = screenCoordsYMin;
                screenCoordsYMaxDraw = screenCoordsYMax;
            }

            if (errorXBool) {
                g.drawLine((int) screenCoordsXMinDraw[0], (int) screenCoordsXMinDraw[1], (int) screenCoordsXMaxDraw[0], (int) screenCoordsXMaxDraw[1]);

                if (screenCoordsXMin == screenCoordsXMinDraw) {
                    g.drawLine((int) screenCoordsXMinDraw[0], (int) screenCoordsXMinDraw[1] - Style.getErrorBarWidth(), (int) screenCoordsXMinDraw[0], (int) screenCoordsXMinDraw[1] + Style.getErrorBarWidth());
                }
                if (screenCoordsXMax == screenCoordsXMaxDraw) {
                    g.drawLine((int) screenCoordsXMaxDraw[0], (int) screenCoordsXMaxDraw[1] - Style.getErrorBarWidth(), (int) screenCoordsXMaxDraw[0], (int) screenCoordsXMaxDraw[1] + Style.getErrorBarWidth());
                }
            }

            if (errorYBool) {
                g.drawLine((int) screenCoordsYMinDraw[0], (int) screenCoordsYMinDraw[1], (int) screenCoordsYMaxDraw[0], (int) screenCoordsYMaxDraw[1]);

                if (screenCoordsYMin == screenCoordsYMinDraw) {
                    g.drawLine((int) screenCoordsYMinDraw[0] - Style.getErrorBarWidth(), (int) screenCoordsYMinDraw[1], (int) screenCoordsYMinDraw[0] + Style.getErrorBarWidth(), (int) screenCoordsYMinDraw[1]);
                }
                if (screenCoordsYMax == screenCoordsYMaxDraw) {
                    g.drawLine((int) screenCoordsYMaxDraw[0] - Style.getErrorBarWidth(), (int) screenCoordsYMaxDraw[1], (int) screenCoordsYMaxDraw[0] + Style.getErrorBarWidth(), (int) screenCoordsYMaxDraw[1]);
                }
            }
        }

        public Point setShow(boolean show) {
            this.show = show;
            return this;
        }
    }

    public interface GraphAnimation2D {
        public void animateGraph(int i);
    }

}
