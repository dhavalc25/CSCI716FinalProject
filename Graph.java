/**
 * Created by sayleebhide on 11/30/17.
 */
import java.util.ArrayList;

public class Graph
{
    ArrayList<Vertex> vertices = new ArrayList<>();
    int numberOfVertices;

    public Graph()
    {
        numberOfVertices = 0;
    }

    public Vertex addVertex(Vertex vert)
    {
        if( getVertex(vert.point.x,vert.point.y) == null )
        {
            vertices.add(vert);
            numberOfVertices++;
            return vert;
        }
        else
            return getVertex(vert.point.x,vert.point.y);
    }

    public Vertex getVertex(double x, double y)
    {
        Vertex p;
        String coordinates[] = new String[4];
        double coordDouble[] = new double[4];

        for(int i = 0; i < vertices.size(); i++)
        {
            p = vertices.get(i);

            coordinates[0] = Double.toString(x);
            coordinates[1] = Double.toString(y);
            coordinates[2] = Double.toString(p.point.x);
            coordinates[3] = Double.toString(p.point.y);

            for( int s = 0; s < 4; s++)
            {
                if(coordinates[s].length() > 6)
                {
                    coordinates[s] = coordinates[s].substring(0,8);
                }
                coordDouble[s] = Double.parseDouble(coordinates[s]);
            }

            if( coordDouble[0] == coordDouble[2]
                    && coordDouble[1] == coordDouble[3] )
                return p;
        }
        return null;
    }

    public ArrayList<Vertex> getAllVertices()
    {
        return vertices;
    }
}
