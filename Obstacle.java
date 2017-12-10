import java.util.ArrayList;

/**
 * Created by sayleebhide on 10/31/17.
 */
public class Obstacle {

    ArrayList <PointO> vertices;

    public Obstacle(ArrayList<PointO>_verts)
    {
        int size = _verts.size();
        vertices = new ArrayList<>();

        for ( int i = 0; i < size; i++)
        {
            addVertex(_verts.get(i));
        }
    }

    public void addVertex(PointO point){
        vertices.add(point);
    }
}
