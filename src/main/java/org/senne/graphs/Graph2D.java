package org.senne.graphs;

import org.senne.Style;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.senne.utils.ListUtils.reverseList;

public class Graph2D extends Base2D {

    //constructors

    public Graph2D() {}

    public Graph2D(String title) {
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
        graph.setOuterPoints();
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
        point.setOuterPoints();

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

        error.setOuterPoints();

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

    public void addArea(List<Double> pointsX1, List<Double> pointsY1, List<Double> pointsX2, List<Double> pointsY2) {
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

        addArea(pointsX1, pointsY1, pointsX2, pointsY2, color);
    }

    public void addArea(List<Double> pointsX1, List<Double> pointsY1, List<Double> pointsX2, List<Double> pointsY2, Color color) {
        if (pointsX1.size() != pointsY1.size()) throw new IllegalArgumentException("pointsX1 and pointsY1 must have the same size");
        if (pointsX2.size() != pointsY2.size()) throw new IllegalArgumentException("pointsX2 and pointsY2 must have the same size");

        Area area = new Area(pointsX1, pointsY1, pointsX2, pointsY2, color);
        area.setOuterPoints();
        graphElements.add(area);
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

    public Graph2D setErrorBarState(int i, boolean show) {
        if (!(graphElements.get(i) instanceof ErrorBar)) throw new IllegalArgumentException("graph element at position " + i + " is not a valid graph element. Must be a Point.");

        ((Point) graphElements.get(i)).setShow(show);
        return this;
    }

    public Graph2D setAreaState(int i, List<Double> pointsX1, List<Double> pointsY1, List<Double> pointsX2, List<Double> pointsY2) {
        if (pointsX1.size() != pointsY1.size()) throw new IllegalArgumentException("pointsX1 and pointsY1 must have the same size");
        if (pointsX2.size() != pointsY2.size()) throw new IllegalArgumentException("pointsX2 and pointsY2 must have the same size");

        if (!(graphElements.get(i) instanceof Area)) throw new IllegalArgumentException("graph element at position " + i + " is not a valid graph element. Must be a Graph.");

        if (!xLimitsCustom) {
            double xMin = pointsX1.stream().min(Double::compareTo).get();
            double xMax = pointsX1.stream().max(Double::compareTo).get();

            if (xMin < outerPointsX[0]) {
                xLimits[0] = xMin - (outerPointsX[1] - xMin) / 10;
                outerPointsX[0] = xMin;
            }
            if (xMax > outerPointsX[1]) {
                xLimits[1] = xMax + (xMax - outerPointsX[0]) / 10;
                outerPointsX[1] = xMax;
            }

            xMin = pointsX2.stream().min(Double::compareTo).get();
            xMax = pointsX2.stream().max(Double::compareTo).get();

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
            double yMin = pointsY1.stream().min(Double::compareTo).get();
            double yMax = pointsY1.stream().max(Double::compareTo).get();

            if (yMin < outerPointsY[0]) {
                yLimits[0] = yMin - (outerPointsY[1] - yMin) / 10;
                outerPointsY[0] = yMin;
            }
            if (yMax > outerPointsY[1]) {
                yLimits[1] = yMax + (yMax - outerPointsY[0]) / 10;
                outerPointsY[1] = yMax;
            }

            yMin = pointsY2.stream().min(Double::compareTo).get();
            yMax = pointsY2.stream().max(Double::compareTo).get();

            if (yMin < outerPointsY[0]) {
                yLimits[0] = yMin - (outerPointsY[1] - yMin) / 10;
                outerPointsY[0] = yMin;
            }
            if (yMax > outerPointsY[1]) {
                yLimits[1] = yMax + (yMax - outerPointsY[0]) / 10;
                outerPointsY[1] = yMax;
            }
        }

        ((Area) graphElements.get(i)).setPoints(pointsX1, pointsY1, pointsX2, pointsY2);
        return this;
    }

    // |-|  |--\ |-|  |-----|  |-----|  |-----|  |--\ |-|    /---\    |-|
    // | |  | |\\| |    | |    |--|     |_____|  | |\\| |   / /_\ \   | |
    // |_|  |_| \__|    |_|    |_____|  |_| \_\  |_| \__|  /_/   \_\  |____|

    //objects

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

        @Override
        public void setOuterPoints() {
            double xMin = x.stream().min(Double::compareTo).get();
            double xMax = x.stream().max(Double::compareTo).get();

            double yMin = y.stream().min(Double::compareTo).get();
            double yMax = y.stream().max(Double::compareTo).get();

            if (graphElements.size() == 0) {
                outerPointsX[0] = xMin;
                outerPointsX[1] = xMax;
                outerPointsY[0] = yMin;
                outerPointsY[1] = yMax;
                return;
            }

            if (xMin < outerPointsX[0]) {
                outerPointsX[0] = xMin;
            }
            if (xMax > outerPointsX[1]) {
                outerPointsX[1] = xMax;
            }
            if (yMin < outerPointsY[0]) {
                outerPointsY[0] = yMin;
            }
            if (yMax > outerPointsY[1]) {
                outerPointsY[1] = yMax;
            }


            /*if (!xLimitsCustom) {
                double xMin = x.stream().min(Double::compareTo).get();
                double xMax = x.stream().max(Double::compareTo).get();

                if (graphElements.size() == 0) {
                    outerPointsX[0] = xMin;
                    outerPointsX[1] = xMax;
                    if (logX) {
                        xLimits[0] = Math.pow(10, Math.log10(xMin) - (Math.log10(outerPointsX[1]) - Math.log10(xMin)) / 10);
                        xLimits[1] = Math.pow(10, Math.log10(xMax) + (Math.log10(outerPointsX[0]) - Math.log10(xMax)) / 10);
                    } else {
                        xLimits[0] = xMin - (outerPointsX[1] - xMin) / 10;
                        xLimits[1] = xMax + (xMax - outerPointsX[0]) / 10;
                    }
                }

                if (xMin < outerPointsX[0]) {
                    if (logX) {
                        xLimits[0] = Math.pow(10, Math.log10(xMin) - (Math.log10(outerPointsX[1]) - Math.log10(xMin)) / 10);
                    } else {
                        xLimits[0] = xMin - (outerPointsX[1] - xMin) / 10;
                    }
                    outerPointsX[0] = xMin;
                }
                if (xMax > outerPointsX[1]) {
                    if (logX) {
                        xLimits[1] = Math.pow(10, Math.log10(xMax) + (Math.log10(outerPointsX[0]) - Math.log10(xMax)) / 10);
                    } else {
                        xLimits[1] = xMax + (xMax - outerPointsX[0]) / 10;
                    }
                    outerPointsX[1] = xMax;
                }
            }

            if (!yLimitsCustom) {
                double yMin = y.stream().min(Double::compareTo).get();
                double yMax = y.stream().max(Double::compareTo).get();

                if (graphElements.size() == 0) {
                    outerPointsY[0] = yMin;
                    outerPointsY[1] = yMax;
                    if (logX) {
                        yLimits[0] = Math.pow(10, Math.log10(yMin) - (Math.log10(outerPointsY[1]) - Math.log10(yMin)) / 10);
                        yLimits[1] = Math.pow(10, Math.log10(yMax) + (Math.log10(outerPointsY[0]) - Math.log10(yMax)) / 10);
                    } else {
                        yLimits[0] = yMin - (outerPointsY[1] - yMin) / 10;
                        yLimits[1] = yMax + (yMax - outerPointsY[0]) / 10;
                    }
                }

                if (yMin <= outerPointsY[0]) {
                    if (logY) {
                        yLimits[0] = Math.pow(10, Math.log10(yMin) - (Math.log10(outerPointsY[1]) - Math.log10(yMin)) / 10);
                    } else {
                        yLimits[0] = yMin - (outerPointsY[1] - yMin) / 10;
                    }
                    outerPointsY[0] = yMin;
                }
                if (yMax >= outerPointsY[1]) {
                    if (logY) {
                        yLimits[1] = Math.pow(10, Math.log10(yMax) + (Math.log10(outerPointsY[0]) - Math.log10(yMax)) / 10);
                    } else {
                        yLimits[1] = yMax + (yMax - outerPointsY[0]) / 10;
                    }
                    outerPointsY[1] = yMax;
                }
            }*/
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

        @Override
        public void setOuterPoints() {
            if (graphElements.size() == 0) {
                outerPointsX[0] = x;
                outerPointsX[1] = x;
                outerPointsY[0] = y;
                outerPointsY[1] = y;
                return;
            }

            if (x < outerPointsX[0]) {
                outerPointsX[0] = x;
            }
            if (x > outerPointsX[1]) {
                outerPointsX[1] = x;
            }
            if (y < outerPointsY[0]) {
                outerPointsY[0] = y;
            }
            if (y > outerPointsY[1]) {
                outerPointsY[1] = y;
            }
            /*if (!xLimitsCustom) {
                if (graphElements.size() == 0) {
                    outerPointsX[0] = x;
                    outerPointsX[1] = x;
                    if (logX) {
                        xLimits[0] = Math.pow(10, Math.log10(x) - 1);
                        xLimits[1] = Math.pow(10, Math.log10(x) + 1);
                    } else {
                        xLimits[0] = x - 1;
                        xLimits[1] = x + 1;
                    }
                }

                if (x < outerPointsX[0]) {

                    if (logX) {
                        xLimits[0] = Math.pow(10, Math.log10(x) - (Math.log10(outerPointsX[1]) - Math.log10(x)) / 10);
                    } else {
                        xLimits[0] = x - (outerPointsX[1] - x) / 10;
                    }
                    outerPointsX[0] = x;
                } else if (x > outerPointsX[1]) {
                    if (logX) {
                        xLimits[1] = Math.pow(10, Math.log10(x) + (Math.log10(outerPointsX[0]) - Math.log10(x)) / 10);
                    } else {
                        xLimits[1] = x + (x - outerPointsX[0]) / 10;
                    }
                    outerPointsX[1] = x;
                }
            }

            if (!yLimitsCustom) {

                if (graphElements.size() == 0) {
                    outerPointsY[0] = y;
                    outerPointsY[1] = y;
                    if (logX) {
                        yLimits[0] = Math.pow(10, Math.log10(y) - 1);
                        yLimits[1] = Math.pow(10, Math.log10(y) + 1);
                    } else {
                        yLimits[0] = y - 1;
                        yLimits[1] = y + 1;
                    }
                    //adapt for non log
                }

                if (y < outerPointsY[0]) {

                    if (logY) {
                        yLimits[0] = Math.pow(10, Math.log10(y) - (Math.log10(outerPointsY[1]) - Math.log10(y)) / 10);
                    } else {
                        yLimits[0] = y - (outerPointsY[1] - y) / 10;
                    }
                    outerPointsY[0] = y;
                } else if (y > outerPointsY[1]) {
                    if (logY) {
                        yLimits[1] = Math.pow(10, Math.log10(y) + (Math.log10(outerPointsY[0]) - Math.log10(y)) / 10);
                    } else {
                        yLimits[1] = y + (y - outerPointsY[0]) / 10;
                    }
                    outerPointsY[1] = y;
                }
            }*/
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

    class Area extends GraphElement {

        List<Double> x1;
        List<Double> y1;
        List<Double> x2;
        List<Double> y2;

        public Area(List<Double> x1, List<Double> y1, List<Double> x2, List<Double> y2, Color color) {
            super(color);
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        @Override
        public void draw(Graphics2D g) {
            g.setPaint(color);

            List<Integer> xScreen = new ArrayList<>();
            x1.forEach(d -> xScreen.add((int) graphCoordsToScreenCoords(d, 0)[0]));
            reverseList(x2).forEach(d -> xScreen.add((int) graphCoordsToScreenCoords((Double) d, 0)[0]));

            List<Integer> yScreen = new ArrayList<>();
            y1.forEach(d -> yScreen.add((int) graphCoordsToScreenCoords(0, d)[1]));
            reverseList(y2).forEach(d -> yScreen.add((int) graphCoordsToScreenCoords(0, (Double) d)[1]));

            g.fillPolygon(new Polygon(xScreen.stream().mapToInt(i->i).toArray(), yScreen.stream().mapToInt(i->i).toArray(), x1.size() + x2.size()));
        }

        public Area setPoints(List<Double> x1, List<Double> y1, List<Double> x2, List<Double> y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            return this;
        }

        @Override
        public void setOuterPoints() {
            List<Double> x = new ArrayList<>();
            x.addAll(x1);
            x.addAll(x2);
            double xMin = x.stream().min(Double::compareTo).get();
            double xMax = x.stream().max(Double::compareTo).get();

            List<Double> y = new ArrayList<>();
            y.addAll(y1);
            y.addAll(y2);
            double yMin = y.stream().min(Double::compareTo).get();
            double yMax = y.stream().max(Double::compareTo).get();

            if (graphElements.size() == 0) {
                outerPointsX[0] = xMin;
                outerPointsX[1] = xMax;
                outerPointsY[0] = yMin;
                outerPointsY[1] = yMax;
                return;
            }

            if (xMin < outerPointsX[0]) {
                outerPointsX[0] = xMin;
            }
            if (xMax > outerPointsX[1]) {
                outerPointsX[1] = xMax;
            }
            if (yMin < outerPointsY[0]) {
                outerPointsY[0] = yMin;
            }
            if (yMax > outerPointsY[1]) {
                outerPointsY[1] = yMax;
            }

            /*
            List<Double> x = new ArrayList<>();
            x.addAll(x1);
            x.addAll(x2);

            List<Double> y = new ArrayList<>();
            y.addAll(y1);
            y.addAll(y2);

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
            }*/
        }
    }
}
