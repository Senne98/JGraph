# JGraph
JGraph is a free and open source graphing library for Java, build with Java Swing.

## How to use

### Java version
The library is written with Java 17 and should work from Java 17 and upwards.

### Graph types
#### Graph2D
Graph2D is the most basic graph type.
![image](https://github.com/Senne98/JGraph/assets/94780127/6d03b4ee-e873-4d70-9dd8-9684f56cec9f)

<details>
<summary>Code</summary>
<br>
  
```java
List<Double> x = new ArrayList<>();
List<Double> y = new ArrayList<>();

for (int i = 0; i < 100d; i++) {
  double valX = ((2d * Math.PI) * (i / 100d));
  x.add(valX);
  y.add(Math.sin(valX));
}

Graph2D graph2 = new Graph2D(x, y, "sinus graph");
graph2.plot();
graph2.save("sin");
```
</details>

<details>
<summary><h4> Create a Graph2D <h4></summary>
<br>
  
When you create a Graph2D you have to add a point or line from the start.
If you want to start with a point you use this constuctor:
  
```
public Graph2D(double x, double y)
```
optionally you can add a color to this point and/or a title to the graph:
```
public Graph2D(double x, double y, Color color)
public Graph2D(double x, double y, String title)
public Graph2D(double x, double y, Color color, String title)
```
It works the same if you want to add a line, but instead of giving two point you give two lists of point that deffine the line.
```
public Graph2D(List<Double> pointsX, List<Double> pointsY)
public Graph2D(List<Double> pointsX, List<Double> pointsY, Color color)
public Graph2D(List<Double> pointsX, List<Double> pointsY, String title)
public Graph2D(List<Double> pointsX, List<Double> pointsY, Color color, String title)
```
</details>

<details>
<summary><h4> Add more elements to a graph <h4></summary>
<br>
If you want to add more elements you can do this with the following functions:
  
* Point:
  ```
    public void addPoint(double x, double y)
    public void addPointList(List<Double> x, List<Double> y)
  ```
* ErrorBar:
  ```
    public void addErrorBar(double x, double y, double errorX, double errorY)
    public void addErrorBarList(List<Double> x, List<Double> y, List<Double> errorX, List<Double> errorY)
  ```
* Graph:
  ```
  public void addGraph(List<Double> pointsX, List<Double> pointsY)
  ```
Once again you can alway optionally give a color for the element.
</details>
