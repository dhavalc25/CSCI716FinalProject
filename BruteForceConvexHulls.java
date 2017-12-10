import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Implementation of a Brute force algorithm that takes in
 * a bunch of points in 2D space and gives points that lie
 * on the convex hull as the output.
 *
 * Created by: Dhaval Chauhan (dmc8686@g.rit.edu)
 * Created on: 09/26/2017
 */
public class BruteForceConvexHulls
{
    /**
     * Reads a text file and exports co-ordinate information to
     * a list of String.
     *
     * @param fileName - name of the file without extension
     * @return information regarding points in form of a list
     */
    public ArrayList<String> readDateFromFile(String fileName)
    {
        // Arraylist for input points.
        ArrayList<String> input= new ArrayList<String>();
        String line = null;

        try
        {
            // Using filereader to read the text file.
            FileReader fileReader = new FileReader(fileName);

            // Read the file contents using buffered reader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Read lines one by one and put them into the input list.
            while((line = bufferedReader.readLine()) != null)
            {

                // Add to the list.
                input.add(line);
            }

            // Close the buffered reader.
            bufferedReader.close();
        }

        // File Not Found exception
        catch(FileNotFoundException ex)
        {
            System.out.println(
                    "Error: Couldn't find the file '" +
                            fileName + "'");
        }

        // Input Output error while reading the file.
        catch(IOException ex)
        {
            System.out.println(
                    "Error: Couldn't read the file. '"
                            + fileName + "'");
        }

        // Return input list.
        return input;
    }

    /**
     * takes a list of points in form of a string and converts it
     * into a list of points in double datatype. X and Y co-ordinates
     * are put in separate indices one after another. Hence the list
     * size is twice the number of input points.
     * eg: 0 1 2 3 4 5 6 7 8 9 ....
     *     x y x y x y x y x y ....
     *
     * @param input - points in form of a list of strings.
     * @return points in form of a list of double type.
     */
    public double[] getPoints(ArrayList<String> input)
    {
        // Get the number of points in the file.
        int n = Integer.parseInt(input.get(0));

        // All the points lie on convex hull if there
        // are fewer than three in total.
        if( n <= 3 )
        {
            System.out.println("All the given input points lie on " +
                    "convex hull since they are less than 3.");
            System.exit(0);
        }

        // Continue if more than 3.
        // Create a list of type double of twice the input size.
        double inPoints[] = new double[2*n];

        // Convert to double and store all the points one by one.
        for( int itr = 1; itr <= n ; itr++ )
        {
            // Store X co-ordinate of the point.
            inPoints[2 * itr - 2] = Double.parseDouble((input.get(itr).trim()).split(" ")[0]);

            // Store Y co-ordinate of the point.
            inPoints[2 * itr - 1] = Double.parseDouble((input.get(itr).trim()).split(" ")[1]);
        }

        // Return input points list that's in double form.
        return inPoints;
    }

    /**
     * Runs a Brute Force algorithm that finds all the points lying on the
     * convex hull. This algorithm runs in O(n^3) time. It considers duplicate
     * points lying on the hull and it also considers collinear points that
     * lie on the convex hull.
     *
     * @param inPoints - input points in double type.
     * @return indices (in the string list of input points) of the points
     *          lying on the convex hull.
     */
    public ArrayList<String> getHullPoints(double[] inPoints)
    {
        // Arraylist for indices of hull points
        ArrayList<String> outPoints = new ArrayList<>();

        // edges made by any two points.
        for( int p = 0; p < inPoints.length - 2; p = p + 2)
        {
            for( int q = p + 2; q < inPoints.length; q = q + 2)
            {
                // Whether all the points lie on one side of the line or not
                boolean oneSide = true;

                // To keep track of whether all the points are on the left
                // side of the line or right.
                // negative value is left. 0 is on the line. positive is right.
                String negPos = "doesnt matter";

                // variable to keep the calculated value in memory.
                double value = 0.0;

                // To check for very first iteration and update negpos.
                boolean firstIteration = true;

                // Check with all other points except for the points lying on edges
                for( int r = 0; r < inPoints.length; r = r + 2)
                {
                    // continue if the third point is same as the first two,
                    if( (inPoints[p] == inPoints[r] && inPoints[p + 1] == inPoints[r + 1])
                         || (inPoints[q] == inPoints[r] && inPoints[q + 1] == inPoints[r + 1]) )
                        continue;
                    else
                    {
                        // Check whether the point is on left or right of the line using vector
                        // math formula.
                        value = (inPoints[q] - inPoints[p]) * (inPoints[r + 1] - inPoints[p + 1])
                                - (inPoints[q + 1] - inPoints[p + 1]) * (inPoints[r] - inPoints[p]);

                        // Update the negpos value based on the first value that we find
                        if( firstIteration )
                        {
                            firstIteration = false;
                            if( value < 0 )
                                negPos = "neg";
                            else
                                negPos = "pos";
                        }
                        else
                        {
                            // stop iteration if there's a change in polarity.
                            if( (value < 0 && negPos != "neg") || (value >= 0 && negPos != "pos") )
                            {
                                oneSide = false;
                                break;
                            }
                        }
                    }
                }

                // Set the one side checker to default value true.
                if(!oneSide)
                {
                    oneSide = true;
                }
                else
                {
                    // Put the point index in output list if it doesn't already exist.
                    if( !outPoints.contains( inPoints[p] + " " + inPoints[p+1]))
                        outPoints.add( inPoints[p] + " " + inPoints[p+1] );

                    // Put the point index in output list if it doesn't already exist.
                    if( !outPoints.contains( inPoints[q] + " " + inPoints[q+1]))
                        outPoints.add( inPoints[q] + " " + inPoints[q+1] );
                }

                // Reset the firstiteration boolean variable to default.
                firstIteration = true;
            }
        }

        // Return the list of indices that lie on the convex hull.
        return outPoints;
    }

    /**
     * Takes in a bunch of unordered points lying on a convex hull and
     * sorts them in counter clockwise order.
     * @param unorderedOutPoints - list of unordered points in string form
     * @return counterClockOutPoints - list of points in CCW order
     */
    public ArrayList<String> counterClockwiseOutPoints(ArrayList<String> unorderedOutPoints)
    {
        // Define an arraylist for holding sorted points.
        ArrayList<String> counterClockOutPoints = new ArrayList<>();

        // fill this arraylist with unsorted points of the hull.
        for( int itr = 0; itr < unorderedOutPoints.size(); itr++)
        {
            counterClockOutPoints.add( itr, unorderedOutPoints.get(itr));
        }

        // initialize variables for X and Y coords of the centroid of the hull.
        double centroidX = 0.0;
        double centroidY = 0.0;

        // Caldulate centroid of the convex hull.
        for( int itr = 0; itr < unorderedOutPoints.size() ; itr++ )
        {
            centroidX += Double.parseDouble((unorderedOutPoints.get(itr).trim()).split(" ")[0]);
            centroidY += Double.parseDouble((unorderedOutPoints.get(itr).trim()).split(" ")[1]);
        }
        centroidX = centroidX/unorderedOutPoints.size();
        centroidY = centroidY/unorderedOutPoints.size();

        // Define few temporary variables to use while sorting
        double polarAngle1;
        double polarAngle2;
        String temp;

        // Sort using bubble sort method. It doesn't matter which sorting algorithm
        // we use as long as the running time of that sort algorithm is less than
        // O(n^3).
        for( int i = 0; i < unorderedOutPoints.size() - 1; i++)
        {
            for (int j = 0; j < unorderedOutPoints.size() - 1 - i; j++)
            {
                // Compute polar angles of the points with respect to the centroid of the hull.
                polarAngle1 = Math.atan2( Double.parseDouble((counterClockOutPoints.get(j).trim()).split(" ")[1])
                        - centroidY ,
                        Double.parseDouble((counterClockOutPoints.get(j).trim()).split(" ")[0]) - centroidX);
                polarAngle2 = Math.atan2( Double.parseDouble((counterClockOutPoints.get(j+1).trim()).split(" ")[1])
                        - centroidY ,
                        Double.parseDouble((counterClockOutPoints.get(j+1).trim()).split(" ")[0]) - centroidX);

                // Swap values to sort.
                if( polarAngle1 > polarAngle2 )
                {
                    temp = counterClockOutPoints.get(j);
                    counterClockOutPoints.set(j , counterClockOutPoints.get(j + 1));
                    counterClockOutPoints.set(j + 1 , temp);
                }
            }
        }
        // Return sorted arraylist.
        return counterClockOutPoints;
    }

    /**
     * Program execution starts here. Main Method
     */
    public ArrayList<ArrayList<String>> getAllConvexHullPoints(ArrayList<Obstacle> obstacleList)
    {
        System.out.println("Getting Convex Hull of the obstacles\n");

        int numberOfObstacles = obstacleList.size();
        ArrayList<String> outPoints = new ArrayList<>();
        ArrayList<ArrayList<String>> obstaclesOutPoints = new ArrayList<>();
        double inPoints[];
        Obstacle currentObstacle;
        int numberOfVertices;
        ArrayList<PointO> vertices;

        for ( int i = 0; i < numberOfObstacles; i++ )
        {

            currentObstacle = obstacleList.get(i);
            vertices = currentObstacle.vertices;
            numberOfVertices = vertices.size();
            inPoints = new double[numberOfVertices*2];
            for ( int j = 0; j < numberOfVertices*2 ; j = j + 2 )
            {
                inPoints[j] = vertices.get(j/2).x;
                inPoints[j+1] = vertices.get(j/2).y;
            }

            // Find the points that lie on the convex hull. Brute force method.
            outPoints = getHullPoints(inPoints);

            // Sort the points in counter clockwise direction with respect to
            // the centroid of the convex hull.
            outPoints = counterClockwiseOutPoints(outPoints);

            obstaclesOutPoints.add(outPoints);
        }

        return obstaclesOutPoints;
    }
}
