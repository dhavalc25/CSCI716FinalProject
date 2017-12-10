import java.util.ArrayList;

/**
 * Created by sayleebhide on 11/30/17.
 */
public class Dijkstras {

    public ArrayList<PointO>shortestVerticesToPlot = new ArrayList<>();

    public int nearestVertex(ArrayList<Double>shortestDistances, ArrayList<Boolean>shortestPathVertices){

        // Initialize min value
        Double minValue = Double.MAX_VALUE;
        int minimum_index=-1;

        for (int v = 0; v < shortestDistances.size(); v++) {
            if (!shortestPathVertices.get(v)&& shortestDistances.get(v) <= minValue) {
                minValue = shortestDistances.get(v);
                minimum_index = v;
            }
        }
        return minimum_index;
    }

    public void printSolution(ArrayList<Double>shortestDistances, int n, ArrayList<Integer>parents, Graph graph){

        int src = 0;
        System.out.println("Vertex\t\t  Distance\t\t\tPath");
        for (int i = 1; i < n; i++)
        {
            if(graph.vertices.get(i).label.equals("d")) {
                System.out.print("\n " + src + " -> " + i + " \t\t" + shortestDistances.get(i) + "\t\t " + src + "\t\t");
                shortestVerticesToPlot.add(graph.vertices.get(src).point);
                printPath(parents, i, graph);
            }
        }
        System.out.println();
    }

    public void printPath(ArrayList<Integer> parent, int index, Graph graph){

        // Base Case : If j is source
        if (parent.get(index)==-1)
            return;

        //recurse till we get src
        printPath(parent, parent.get(index), graph);
        shortestVerticesToPlot.add(graph.vertices.get(index).point);

       System.out.print(index + "\t\t");
    }

    public ArrayList<PointO> shortestPath(int size, Graph graph){

        //hold shortest distances
        ArrayList<Double>shortestDistances = new ArrayList<>();

        //hold vertices included in shortest path
        ArrayList<Boolean> shortestPathVertices = new ArrayList<>();

        ArrayList<Integer>parents = new ArrayList<>();

        //initialize all distances as infinity first and all false
        for(int i = 0; i < size ; i++){
            parents.add(0,-1);
            shortestDistances.add(i, Double.MAX_VALUE);
            shortestPathVertices.add(i,false);
        }

        //distanc eof source vertex from itself is 0
        shortestDistances.set(0,0.0);

        //find shortest path for all vertices
        for(int i = 0 ; i < size -1 ; i ++){

            //// Pick the minimum distance vertex from the set of vertices
            // not yet processed. u is always equal to src in first
            // iteration.

            int nearestVertex_index = nearestVertex(shortestDistances,shortestPathVertices);

            //if(graph.vertices.get(i).label.equals("d"))
                //break;

            shortestPathVertices.set(nearestVertex_index,true);

            // Update dist value of the adjacent vertices of the
            // picked vertex.
            for (int adjvert_index = 0; adjvert_index < size ; adjvert_index++) {

                // Update vertex at index v only if is not in the set which has the shortest path, there is an
                // edge from current nearest vertex  to v, and total weight of path from src to
                // v through u is smaller than current value of vertex at index v
                if (!shortestPathVertices.get(adjvert_index) && graph.vertices.get(nearestVertex_index).getAllVisibleVertices().containsKey(graph.vertices.get(adjvert_index)) &&
                        shortestDistances.get(nearestVertex_index) != Double.MAX_VALUE &&
                        shortestDistances.get(nearestVertex_index) + graph.vertices.get(nearestVertex_index).getDistance(graph.vertices.get(adjvert_index)) < shortestDistances.get(adjvert_index))
                {
                    //set parent of current vertex as the nearest vertex which we are considering
                    parents.set(adjvert_index,nearestVertex_index);

                    //Set the distance to adj vertex as sum of dist from src to nearestvertex considered an distance from nearest vertex to adjacent vertex
                    shortestDistances.set(adjvert_index, shortestDistances.get(nearestVertex_index) + graph.vertices.get(nearestVertex_index).getDistance(graph.vertices.get(adjvert_index)));
                }
            }
        }

        printSolution(shortestDistances, size, parents, graph);
        return shortestVerticesToPlot;
    }
}
