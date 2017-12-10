import java.util.ArrayList;
import java.math.*;

public class InflateConvexPolygon
{
    public ArrayList<String> getInflatedPoints(ArrayList<ArrayList<String>> inputObstacles,int radius)
    {
        int numberOfObstacles = inputObstacles.size();
        double newRadius = radius/2 + 2;                      //I did it.
        ArrayList<String> outputInflatedPoints = new ArrayList<>();

        for ( int n = 0; n < numberOfObstacles; n++ )
        {
            ArrayList<String> inputPoints = inputObstacles.get(n);
            int sides = inputPoints.size();
            ArrayList<Double> outputCoeffs = new ArrayList<>();


            for (int i = 0; i < sides; i++) {
                double x1 = 0.0;
                double y1 = 0.0;
                double x2 = 0.0;
                double y2 = 0.0;
                double coeffA = 0.0;
                double coeffB = 0.0;
                double newcoeffB = 0.0;

                double theta = 0.0;

                x1 = Double.parseDouble((inputPoints.get(i).trim()).split(" ")[0]);
                y1 = Double.parseDouble((inputPoints.get(i).trim()).split(" ")[1]);
                if (i == (sides - 1)) {
                    x2 = Double.parseDouble((inputPoints.get(0).trim()).split(" ")[0]);
                    y2 = Double.parseDouble((inputPoints.get(0).trim()).split(" ")[1]);
                } else {
                    x2 = Double.parseDouble((inputPoints.get(i + 1).trim()).split(" ")[0]);
                    y2 = Double.parseDouble((inputPoints.get(i + 1).trim()).split(" ")[1]);
                }

                coeffA = (y2 - y1) / (x2 - x1);
                coeffB = -1 * coeffA * x1 + y1;
                theta = Math.atan(coeffA);
                if (x1 < x2) {
                    newcoeffB = coeffB + (newRadius / (Math.cos(theta)));
                } else {
                    newcoeffB = coeffB - (newRadius / (Math.cos(theta)));
                }
                outputCoeffs.add(coeffA);
                outputCoeffs.add(newcoeffB);
            }

            outputInflatedPoints.add(getIntersectionPoints(outputCoeffs));

        }
        return outputInflatedPoints;

    }

    private String getIntersectionPoints(ArrayList<Double> outputCoeffs)
    {
        int vertices = outputCoeffs.size()/2;

        double x1 = 0.0;
        double y1 = 0.0;
        double x2 = 0.0;
        double y2 = 0.0;

        double x3 = 0.0;
        double y3 = 0.0;
        double x4 = 0.0;
        double y4 = 0.0;

        double ix = 0.0;
        double iy = 0.0;

        String outputPoints = "";

        for( int i = 0; i<2*vertices; i=i+2 )
        {
            x1 = -1*outputCoeffs.get(i+1)/outputCoeffs.get(i);
            y1 = 0;
            x2 = 0;
            y2 = outputCoeffs.get(i+1);

            if(i == 2*vertices-2)
            {
                x3 = -1 * outputCoeffs.get(1) / outputCoeffs.get(0);
                y3 = 0;
                x4 = 0;
                y4 = outputCoeffs.get(1);
            }
            else
            {
                x3 = -1 * outputCoeffs.get(i + 3) / outputCoeffs.get(i + 2);
                y3 = 0;
                x4 = 0;
                y4 = outputCoeffs.get(i + 3);
            }

            ix =  ((x1*y2 - y1*x2)*(x3 - x4) - (x1 - x2)*(x3*y4 - y3*x4))/((x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4));
            iy =  ((x1*y2 - y1*x2)*(y3 - y4) - (y1 - y2)*(x3*y4 - y3*x4))/((x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4));

            if( i == 0 )
                outputPoints = outputPoints + Double.toString(ix)+ " " +Double.toString(iy);
            else
                outputPoints = outputPoints + " " + Double.toString(ix)+ " " +Double.toString(iy);

        }

        outputPoints = outputPoints.replaceAll("NaN", " ");
        outputPoints = outputPoints.replaceAll(" +", " ");
        return outputPoints;
    }
}
