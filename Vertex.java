/**
 * Created by sayleebhide on 11/30/17.
 */
import java.util.HashMap;

public class Vertex
{
    PointO point;
    HashMap visibleVertices;
    String label;

    public Vertex(PointO _point, String _label)
    {
        point = _point;
        visibleVertices = new HashMap();
        label =_label;
    }

    public void addVisibleVertex(Vertex vert)
    {
        visibleVertices.put(vert, Math.sqrt( (this.point.x - vert.point.x)*(this.point.x - vert.point.x) + (this.point.y - vert.point.y)*(this.point.y - vert.point.y) ));
    }

    public HashMap getAllVisibleVertices()
    {
        return visibleVertices;
    }

    public Double getDistance(Vertex vert)
    {
        return (Double)visibleVertices.get(vert);
    }
}
