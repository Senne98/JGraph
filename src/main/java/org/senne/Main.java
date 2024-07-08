package org.senne;

import org.senne.graphs.Graph2D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        Graph2D graph = new Graph2D(10d, 10d);
        graph.addGraph(List.of(1d, 2d, 3d, 4d, 5d), List.of(1d, 1d, 3d, 4d, 5d));
        graph.addErrorBar(2.5d, 2.5d, 0.5d, 0.5d);
        graph.logLog();
        //graph.addGraph(List.of(-1f, 2f, 3f, 4f, 5f), List.of(-1f, 2f, 3f, 4f, 5f));
        //graph.addGraph(List.of(-1f, 1f), List.of(-1f, 2f));
        //graph.addPoint(10f, 10f, new Color(255, 0, 174));
        //graph.addPoint(28.5f, 3.5f, new Color(0, 210, 22));
        //graph.setXLimits(0f, 6f);
        //graph.setYLimits(0f, 6f);
        //graph.setShowGrid(false);
        //graph.setAspectRatio(1, 1);
        try {
            graph.plot();
            graph.save("graph");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();

        //if set to 100d, the first 2 x reference points will be messed up!
        for (int i = 0; i < 100d; i++) {
            double valX = ((2d * Math.PI) * (i / 100d));
            x.add(valX);
            y.add(Math.sin(valX));
        }

        Graph2D graph2 = new Graph2D(x, y, "sinus graph");
        //graph2.setYLimits(0, 1);
        graph2.plot();
        graph2.save("sin");

        Graph2D animatedGraph = new Graph2D(10f, 10f);
        animatedGraph.addGraph(List.of(1d, 2d, 3d, 4d, 5d), List.of(1d, 1d, 3d, 4.5d, 5d));
        animatedGraph.setAnimateAxis(true);
        animatedGraph.setAxisAnimationFrames(150);
        animatedGraph.animate(i -> {
            if (i == 0) {
                animatedGraph.setPointState(0, false);
                animatedGraph.setGraphState(1, List.of(1.0), List.of(1.0));
            }
            if (i == 1) {
                animatedGraph.setGraphState(1, List.of(1.0, 2.0), List.of(1.0, 2.0));
            }
            if (i == 2) {
                animatedGraph.setGraphState(1, List.of(1.0, 2.0, 3.0), List.of(1.0, 2.0, 3.0));
            }
            if (i == 3) {
                animatedGraph.setGraphState(1, List.of(1.0, 2.0, 3.0, 4.0), List.of(1.0, 2.0, 3.0, 4.0));
            }
            if (i == 4) {
                animatedGraph.setPointState(0, true);
                animatedGraph.setGraphState(1, List.of(1.0, 2.0, 3.0, 4.0, 5.0), List.of(1.0, 2.0, 3.0, 4.0, 5.0));
            }
        }, 5, 25);

    }
}