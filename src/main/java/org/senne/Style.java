package org.senne;

import java.awt.*;

public class Style {
    static Color backgroundColor = new Color(255, 255, 255);
    static Color gridColor = new Color(0, 0, 0, 50);
    static Color axisColor = new Color(0, 0, 0);
    static Color premierGraphColor = new Color(0, 128, 255);
    static Color secondaryGraphColor = new Color(255, 153, 0);
    static Color tertiaryGraphColor = new Color(0, 210, 22);
    static Color quaternaryGraphColor = new Color(255, 0, 38);
    static String fontName = "Calibri";
    static int fontSize = 30;
    static float lineWidth = 5.0f;
    static int verticalResolution = 1080;
    static float aspectRatio = 16f / 9f;
    static boolean showGrid = true;
    static boolean animateAxis = true;
    static int axisAnimationFrames = 70;
    static int errorBarWidth = 10;
    static Color errorBarColor = new Color(0, 0, 0);

    public static Color getBackgroundColor() {
        return backgroundColor;
    }

    public static void setBackgroundColor(Color backgroundColor) {
        Style.backgroundColor = backgroundColor;
    }

    public static Color getGridColor() {
        return gridColor;
    }

    public static void setGridColor(Color gridColor) {
        Style.gridColor = gridColor;
    }

    public static Color getAxisColor() {
        return axisColor;
    }

    public static void setAxisColor(Color axisColor) {
        Style.axisColor = axisColor;
    }

    public static Color getPremierGraphColor() {
        return premierGraphColor;
    }

    public static void setPremierGraphColor(Color premierGraphColor) {
        Style.premierGraphColor = premierGraphColor;
    }

    public static Color getSecondaryGraphColor() {
        return secondaryGraphColor;
    }

    public static void setSecondaryGraphColor(Color secondaryGraphColor) {
        Style.secondaryGraphColor = secondaryGraphColor;
    }

    public static Color getTertiaryGraphColor() {
        return tertiaryGraphColor;
    }

    public static void setTertiaryGraphColor(Color tertiaryGraphColor) {
        Style.tertiaryGraphColor = tertiaryGraphColor;
    }

    public static Color getQuaternaryGraphColor() {
        return quaternaryGraphColor;
    }

    public static void setQuaternaryGraphColor(Color quaternaryGraphColor) {
        Style.quaternaryGraphColor = quaternaryGraphColor;
    }

    public static String getFontName() {
        return fontName;
    }

    public static void setFontName(String fontName) {
        Style.fontName = fontName;
    }

    public static int getFontSize() {
        return fontSize;
    }

    public static void setFontSize(int fontSize) {
        Style.fontSize = fontSize;
    }

    public static float getLineWidth() {
        return lineWidth;
    }

    public static void setLineWidth(float lineWidth) {
        Style.lineWidth = lineWidth;
    }

    public static int getVerticalResolution() {
        return verticalResolution;
    }

    public static void setVerticalResolution(int verticalResolution) {
        Style.verticalResolution = verticalResolution;
    }

    public static float getAspectRatio() {
        return aspectRatio;
    }

    public static void setAspectRatio(float aspectRatio) {
        Style.aspectRatio = aspectRatio;
    }

    public static boolean isShowGrid() {
        return showGrid;
    }

    public static void setShowGrid(boolean showGrid) {
        Style.showGrid = showGrid;
    }

    public static boolean isAnimateAxis() {
        return animateAxis;
    }

    public static void setAnimateAxis(boolean animateAxis) {
        Style.animateAxis = animateAxis;
    }

    public static int getAxisAnimationFrames() {
        return axisAnimationFrames;
    }

    public static void setAxisAnimationFrames(int axisAnimationFrames) {
        Style.axisAnimationFrames = axisAnimationFrames;
    }

    public static int getErrorBarWidth() {
        return errorBarWidth;
    }

    public static void setErrorBarWidth(int errorBarWidth) {
        Style.errorBarWidth = errorBarWidth;
    }

    public static Color getErrorBarColor() {
        return errorBarColor;
    }

    public static void setErrorBarColor(Color errorBarColor) {
        Style.errorBarColor = errorBarColor;
    }
}
