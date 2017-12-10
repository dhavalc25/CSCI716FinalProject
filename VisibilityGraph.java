import java.lang.reflect.Array;
import java.util.ArrayList;

public class VisibilityGraph
{
    public Graph makeVisibilityGraph(ArrayList<String> allInflatedConvexPoints, double src_x,double src_y,double dst_x,double dst_y)
    {
        int numberOfObstacles = allInflatedConvexPoints.size();
        ArrayList<double[]> allObstacleVertices = new ArrayList<>();
        String coords[];
        double intCoords[];

        Graph vGraph = new Graph();

        Vertex start = new Vertex(new PointO(src_x,src_y),"s");
        Vertex destination = new Vertex(new PointO(dst_x,dst_y),"d");

        vGraph.addVertex(start);
        vGraph.addVertex(destination);

        //Make Vertices for Graph and add them
        for( int i = 0; i < numberOfObstacles; i++ )
        {
            coords = (allInflatedConvexPoints.get(i).trim()).split(" ");
            intCoords = new double[coords.length];
            for( int j = 0; j < coords.length; j++ )
            {
                intCoords[j] = Double.parseDouble(coords[j]);
            }

            allObstacleVertices.add(intCoords);

            //Make Vertices of 1 obstacle
            for( int j = 0; j < coords.length; j+=2 )
            {
                vGraph.addVertex(new Vertex(new PointO(intCoords[j],intCoords[j+1]), "s"));
            }
        }

        //Connect neighbors
        for( int i = 0; i < numberOfObstacles; i++ )
        {
            coords = (allInflatedConvexPoints.get(i).trim()).split(" ");
            intCoords = new double[coords.length];
            for( int j = 0; j < coords.length; j++ )
            {
                intCoords[j] = Double.parseDouble(coords[j]);
            }
            System.out.println(i+"!!!!!!!!!!!!");
            makeIntraPolygonVisibilityGraph(intCoords, vGraph);
        }

        makeInterPolygonVisibilityGraph(allObstacleVertices,vGraph,start,destination);

        return vGraph;
    }

    private void makeIntraPolygonVisibilityGraph(double intCoords[], Graph graph)
    {
        int size = intCoords.length;
        Vertex v_left;
        Vertex v_itself;
        Vertex v_right;

        for( int j = 0; j < size; j+=2 )
        {
            System.out.println(j+"!!");
            v_itself = graph.getVertex(intCoords[j],intCoords[j+1]);

            if( j == 0 )
            {
                v_left = graph.getVertex(intCoords[size-2],intCoords[size-1]);
                v_right = graph.getVertex(intCoords[j+2],intCoords[j+3]);
            }
            else if ( j == size - 2)
            {
                v_left = graph.getVertex(intCoords[j-2],intCoords[j-1]);
                v_right = graph.getVertex(intCoords[0],intCoords[1]);
            }
            else
            {
                v_left = graph.getVertex(intCoords[j-2],intCoords[j-1]);
                v_right = graph.getVertex(intCoords[j+2],intCoords[j+3]);
            }

            v_itself.addVisibleVertex(v_left);
            v_itself.addVisibleVertex(v_right);
        }
    }

    private void makeInterPolygonVisibilityGraph( ArrayList<double[]> allObstacleVertices, Graph graph, Vertex start, Vertex destination)
    {
        ArrayList<Vertex> completelist = graph.getAllVertices();
        int stopPoints[] = new int[allObstacleVertices.size() + 1];
        double intCoords[];
        stopPoints[0] = 2;

        for( int i = 0; i < allObstacleVertices.size(); i++ )
        {
            stopPoints[i+1] = stopPoints[i] + allObstacleVertices.get(i).length/2;
        }

        int numberOfVertices = completelist.size();
        int previousStopPoint = 0;
        int currentStopPoint = 2;
        int stopPointCounter = 0;

        Vertex v1;
        Vertex v2;
        double x3 = 0.0;
        double y3 = 0.0;
        double x4 = 0.0;
        double y4 = 0.0;
        String coordinates[] = new String[8];
        double coordDouble[] = new double[8];

        for( int i = 0; i < numberOfVertices; i++ )
        {
            for( int j = 0; j < numberOfVertices; j++ )
            {
                chooseNextTwoPoints:
                if( i!= j)
                {
                    v1 = completelist.get(i);
                    v2 = completelist.get(j);
                    if(i == currentStopPoint)
                    {
                        previousStopPoint = currentStopPoint;
                        stopPointCounter++;
                        currentStopPoint = stopPoints[stopPointCounter];
                    }
                    if(i >= previousStopPoint && i < currentStopPoint && j >= previousStopPoint && j < currentStopPoint && previousStopPoint != 0)
                    {
                        //Do nothing
                    }
                    else
                    {
                        for( int k = 0; k < allObstacleVertices.size(); k++ )
                        {
                            intCoords = allObstacleVertices.get(k);
                            for( int n = 0; n < intCoords.length; n+=2 )
                            {
                                if(n == 0)
                                {
                                    x3 = intCoords[intCoords.length-2];
                                    y3 = intCoords[intCoords.length-1];
                                    x4 = intCoords[n];
                                    y4 = intCoords[n+1];
                                }
                                else
                                {
                                    x3 = intCoords[n-2];
                                    y3 = intCoords[n-1];
                                    x4 = intCoords[n];
                                    y4 = intCoords[n+1];
                                }

                                coordinates[0] = Double.toString(v1.point.x);
                                coordinates[1] = Double.toString(v1.point.y);
                                coordinates[2] = Double.toString(v2.point.x);
                                coordinates[3] = Double.toString(v2.point.y);
                                coordinates[4] = Double.toString(x3);
                                coordinates[5] = Double.toString(y3);
                                coordinates[6] = Double.toString(x4);
                                coordinates[7] = Double.toString(y4);

                                for( int s = 0; s < 8; s++)
                                {
                                    if(coordinates[s].length() > 6)
                                    {
                                        coordinates[s] = coordinates[s].substring(0,8);
                                    }
                                    coordDouble[s] = Double.parseDouble(coordinates[s]);
                                }

                                if(((coordDouble[0] != coordDouble[4]) || (coordDouble[1] != coordDouble[5]))
                                        && ((coordDouble[2] != coordDouble[6]) || (coordDouble[3] != coordDouble[7]))
                                        && ((coordDouble[0] != coordDouble[6]) || (coordDouble[1] != coordDouble[7]))
                                        && ((coordDouble[2] != coordDouble[4]) || (coordDouble[3] != coordDouble[5])))
                                    if(checkIntersection(coordDouble[0],coordDouble[1],coordDouble[2],coordDouble[3],
                                            coordDouble[4],coordDouble[5],coordDouble[6],coordDouble[7]))
                                        break chooseNextTwoPoints;
                            }
                        }
                        v1.addVisibleVertex(v2);
                    }
                }
            }
        }
    }

    public boolean checkIntersection(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4)
    {
        double ix;
        double iy;
        double p1;
        double p2;
        double p3;
        double p4;

        ix =  ((x1*y2 - y1*x2)*(x3 - x4) - (x1 - x2)*(x3*y4 - y3*x4))/((x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4));
        iy =  ((x1*y2 - y1*x2)*(y3 - y4) - (y1 - y2)*(x3*y4 - y3*x4))/((x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4));

        if(x1 > x2)
        {
            p1 = x2;
            p2 = x1;
        }
        else
        {
            p1 = x1;
            p2 = x2;
        }
        if(x3 > x4)
        {
            p3 = x4;
            p4 = x3;
        }
        else
        {
            p3 = x3;
            p4 = x4;
        }

        if(ix>p1 && ix>p3 && ix<p2 && ix<p4)
            return true;
        return false;
    }
}
