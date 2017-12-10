/**
 * Created by sayleebhide on 10/31/17.
 */


/**
 * Validations
 * #1. More than two points required to make an obstacle (DONE)
 * #2. More than two obstacles required to plot start and end point (DONE)
 * #3. Overlapping Obstacles (Not Done)
 * #4. Concave Obstacles(Not Done)
 *
 *
 * For Integration, I suggest check the drawObstacles function. When createAnimationButton(Find shortest path) is
 * clicked, we can run the next steps in that action event.
 */

import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class learnSwing {

    //Main Frame
    public JFrame frame;

    //Drawing Panel
    public JPanel panel;

    //Bottom Panel having all the buttons
    public JPanel bottom_panel;

    //Side Panel - Panel containing Information on how to run the Module
    public JPanel side_panel;

    //Make Obstacle Button - Finishes the obstacle by joining the first point with the last clicked point
    public JButton makeButton;

    //Finish Button - Clicked after all the obstacles have been drawn. On click, plot the start and Destination Points
    public JButton finishButton;

    //Reset Button - Reset the panel
    public JButton resetButton;

    //Label to indicate to user to select the radius
    public JLabel selectRadiusLabel;

    //Label for displaying information window on how to run the module
    public JLabel infoLabel;

    //Find Shortest Path -  runs the Animation Algorithm and creates an animation that traces the shortest path
    public JButton createAnimationButton;

    //List that stores radiuses
    DefaultListModel<Integer>radiusList = new DefaultListModel<>();
    JList<Integer>JRadiusList;
    JScrollPane RadiusScrollpane;

    //Default radius
    public int selectedRadius = 20;

    //Arraylist of obstacles
    public static ArrayList<Obstacle> obstacleArrayList = new ArrayList<Obstacle>();

    //Temporary storage of points that make an obstacle
    public ArrayList<PointO> randList = new ArrayList<PointO>();
    PointO point;

    //Start and Destination Points
    PointO start;
    PointO destination;

    //Flag to turn off mouse events
    boolean allowMouseEvents = true;

    //Flag to indicate that process is still remaining. This is to completely disable mouse events
    boolean remaining = true;

    //Hardcoded to emulate the final result for animation
    public ArrayList<PointO> sampleOutputList = new ArrayList<PointO>();


    BruteForceConvexHulls ch;
    InflateConvexPolygon icp;
    VisibilityGraph vg;
    Dijkstras dijk;



    /**
     * Function that creates the Swing Components
     */
    public void createWindow(){
        try {
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (Exception ex) {
        }

       //create new JFrame
        frame = new JFrame("Euclidean Shortest Path Problem");
        frame.setLayout(new BorderLayout());

        //set width and height
        frame.setSize(1540,840);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       //create panel in south position for storing buttons
        panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.orange);
        panel.setBorder(BorderFactory.createBevelBorder(1,Color.pink,Color.BLACK));

        bottom_panel = new JPanel(new FlowLayout(FlowLayout.CENTER,30,3));
        bottom_panel.setBackground(Color.lightGray);
        bottom_panel.setBorder(BorderFactory.createBevelBorder(1));

        side_panel = new JPanel(new GridLayout(0,1));


       //Buttons of bottom panel
        finishButton = new JButton("Finish");
        finishButton.setFont(new Font("Times Roman", Font.ITALIC,14));


        makeButton = new JButton("Make ");
        makeButton.setFont(new Font("Times Roman", Font.ITALIC,14));

        createAnimationButton = new JButton("Find Shortest Path");
        createAnimationButton.setFont(new Font("Times Roman",Font.ITALIC,14));

        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Times Roman",Font.BOLD,14));

        selectRadiusLabel = new JLabel("Radius Of Moving Point (Default : 20) ");
        selectRadiusLabel.setFont(new Font("Times Roman",Font.ITALIC,14));


        //Radius list
        for(int i = 2 ; i < 100 ; i+=2 ){
            radiusList.addElement(i);
        }

        JRadiusList = new JList<>(radiusList);
        JRadiusList.setVisibleRowCount(4);
        JRadiusList.setSelectedIndex(0);
        JRadiusList.setFont(new Font("Times Roman",Font.ITALIC,12));

        //only one item can be selected at a time
        JRadiusList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JRadiusList.setValueIsAdjusting(false);

        RadiusScrollpane = new JScrollPane(JRadiusList);
        RadiusScrollpane.setLayout(new ScrollPaneLayout());



        //add buttons to bottom panel
        bottom_panel.add(makeButton);
        bottom_panel.add(finishButton);
        bottom_panel.add(selectRadiusLabel);
        bottom_panel.add(RadiusScrollpane);
        bottom_panel.add(createAnimationButton);
        bottom_panel.add(resetButton);

        //add information panel to the side
        infoLabel = new JLabel("<html>Guidelines to run the Module :  <BR> 1. Click on Orange Panel to create shape of obstacles and " +
                "click Make to Finish an Obstacle. <BR> 2. After creating all the obstacles click Finish. <BR> 3. Set radius of start " +
                "and destination point by selecting a value from the list <BR>" + " 4. To Find the Eucledian Shortest path" +
                " Click Find Shortest Path Button <BR>" + "5. Note** Please Plot points in ANTI CLOCKWISE ORDER </html>");
        infoLabel.setFont(new Font("Courier New", Font.BOLD, 12));
        side_panel.add(infoLabel);




        //add panel to frame
        frame.add(panel);
        frame.add(bottom_panel,BorderLayout.SOUTH);
        frame.add(side_panel,BorderLayout.NORTH);

       //add bottom panel to main panel
        //panel.add(bottom_panel, BorderLayout.SOUTH);

        frame.setVisible(true);

   }

    public void fillObstacle(){

        Graphics g = panel.getGraphics();

        int [] xpoints = new int[randList.size()];
        int [] ypoints = new int[randList.size()];

        for(int i = 0 ; i < randList.size() ; i ++){
            xpoints[i] = (int)randList.get(i).x;
            ypoints[i] = (int)randList.get(i).y;
        }

        g.setColor(Color.blue);
        g.fillPolygon(xpoints,ypoints,randList.size());

    }

    /**
     * Function to plot obstacle points
     * @param p point recorded by mouse click
     */
    public void plotPoint(PointO p){

        Graphics g = panel.getGraphics();

        //make the rectangle
        g.drawRect((int)p.x,(int)p.y,4,4);
        g.setColor(Color.BLACK);
        g.fillRect((int)p.x,(int)p.y,4,4);

    }

    /**
     * Function to plot start point
     * @param p start point recorded by mouse pressed
     */
    public void plotStartPoint(PointO p){

        Graphics g = panel.getGraphics();

        //make the oval
        g.drawOval((int)(p.x - selectedRadius/2),(int)(p.y - selectedRadius/2),selectedRadius,selectedRadius);
        g.setColor(Color.GREEN);
        g.fillOval((int)(p.x - selectedRadius/2),(int)(p.y - selectedRadius/2),selectedRadius,selectedRadius);

    }

    /**
     * Function to plot points along the animation
     * @param p Interpolated point found by interpolation function called in animation function
     */
    public void animationPoint(PointO p){

        Graphics g = panel.getGraphics();

        //make the oval
        g.setColor(Color.black);
        //g.fillRoundRect((int)p.x,(int)p.y,15,15,(int)p.x,(int)p.y);
        //g.fill3DRect((int)p.x,(int)p.y,15,15,true);
        g.fillOval((int)(p.x - selectedRadius/2),(int)(p.y - selectedRadius/2),selectedRadius,selectedRadius);


    }

    /**
     * Function to plot Destination point
     * @param p Destination point recorded by mouse released
     */
    public void plotDestPoint(PointO p){

        Graphics g = panel.getGraphics();

        //make the rectangle
        g.drawOval((int)(p.x - selectedRadius/2),(int)(p.y - selectedRadius/2),selectedRadius,selectedRadius);
        g.setColor(Color.RED);
        g.fillOval((int)(p.x - selectedRadius/2),(int)(p.y - selectedRadius/2),selectedRadius,selectedRadius);

    }

    /**
     * Function to draw lines between obstacle points recorded by mouse click
     * @param list list of points recorded where mouse is clicked by user
     */
    public void drawLines(ArrayList<PointO> list){

        Graphics g = panel.getGraphics();

        //draw lines between each of the points
        for(int i = 0; i < list.size()-1; i ++ ){
            g.setColor(Color.BLUE);
            g.drawLine((int)list.get(i).x, (int)list.get(i).y , (int)list.get(i+1).x , (int)list.get(i+1).y);
        }
    }

    /**
     * Function that draws the obstacles - Contains validations and action listeners
     */
    public void drawObstacles(){

        //whereever mouse is clicked, record that as a point and add to the func list.
        //This funclist defines the obstacle shape
        //when mouse click, draw an oval on the panel there showing point
        panel.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(allowMouseEvents && remaining) {
                    //record point - first create new point
                    point = new PointO(e.getX(), e.getY());
                    Graphics g = panel.getGraphics();
                    //g.drawString("(" + point.x + "," + point.y + ")",((Double)point.x).intValue(),((Double)point.y).intValue());
                    //add this point to functionlist
                    randList.add(point);

                    //plot this point on the panel
                    plotPoint(point);

                    //check if points are getting stored in array list
                    for (PointO p : randList) {
                        System.out.println("points are" + " " + p.x + " " + p.y);
                    }
                    System.out.println("-----");

                    //start drawing lines
                    drawLines(randList);


                    finishButton.setEnabled(false);
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

                if(!allowMouseEvents && remaining) {
                    start = new PointO(e.getX(), e.getY());
                    plotStartPoint(start);

                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                if(!allowMouseEvents && remaining) {
                    destination = new PointO(e.getX(), e.getY());
                    plotDestPoint(destination);
                    remaining=false;

                    System.out.println("start is " + start.x + " " + start.y);
                    System.out.println("Destination is" + destination.x + " " + destination.y);


                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        //if make button is clicked - if yes, join the polygon
        makeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //check if button was clicked
                System.out.println("button was clicked");

                if(randList.size()>2) {

                    //join the last two points
                    Graphics g = panel.getGraphics();

                    g.setColor(Color.BLUE);

                    g.drawLine((int) randList.get(randList.size() - 1).x, (int) randList.get(randList.size() - 1).y, (int) randList.get(0).x, (int) randList.get(0).y);

                    //create the new obstacle with the following set of vertices
                    Obstacle obstacle = new Obstacle(randList);

                    //add obstacle to array list
                    obstacleArrayList.add(obstacle);

                    //fill obstacle
                    fillObstacle();
                    //clear randlist for new obstacle
                    randList.clear();

                    //clear points
                    point = null;

                    finishButton.setEnabled(true);

                    System.out.println("No of obstacles are " + obstacleArrayList.size());
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Please Plot more than two points");
                }
            }
        });

        //If finish button is clicked, time to select the starting and ending point
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(obstacleArrayList.size()>1) {
                    System.out.println("Finish button was clicked");
                    finishButton.setEnabled(false);
                    JOptionPane.showMessageDialog(null, "Now select the starting point and destination point by \n " +
                            "press the mouse for plotting start point and drag and release the mouse for plotting destination point");

                    //first set allow mouse events to false and set starting and ending points
                    allowMouseEvents = false;

                    //JOptionPane.showMessageDialog(null, "Starting point is" + start.x + " " + start.y);
                }
                else
                    JOptionPane.showMessageDialog(null, "Please draw more than one Obstacle(s)");

            }
        });

        createAnimationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (obstacleArrayList.size() > 1 && start != null && destination != null) {
                    System.out.println("find shortest path button was clicked");
                    createAnimationButton.setEnabled(false);


                    sampleOutputList.add(start);
                    sampleOutputList.add(new PointO(100, 100));
                    sampleOutputList.add(new PointO(150, 100));
                    sampleOutputList.add(new PointO(200, 200));
                    sampleOutputList.add(new PointO(250, 200));
                    sampleOutputList.add(new PointO(390, 200));
                    sampleOutputList.add(new PointO(400, 600));
                    sampleOutputList.add(destination);


                    ////////////////////////////////////////////////////////////////////////////////////////////////////

                    ArrayList<ArrayList<String>> allConvexPoints = new ArrayList<>();
                    ArrayList<String> intermidAL;
                    Obstacle obs;


                    for( int i = 0; i < obstacleArrayList.size(); i++)
                    {
                        obs = obstacleArrayList.get(i);
                        intermidAL = new ArrayList<>();
                        for( int j = 0; j<obs.vertices.size();j++)
                        {
                            intermidAL.add( obs.vertices.get(j).x + " " + obs.vertices.get(j).y );
                        }

                        //intermidAL = ch.counterClockwiseOutPoints(intermidAL);

                        allConvexPoints.add(intermidAL);
                    }

                    ArrayList<String> allInflatedConvexPoints = icp.getInflatedPoints(allConvexPoints,selectedRadius);
                    Graph g = vg.makeVisibilityGraph(allInflatedConvexPoints,start.x,start.y,destination.x,destination.y);

                    sampleOutputList = dijk.shortestPath(g.vertices.size(),g);

                    ////////////////////////////////////////////////////////////////////////////////////////////////////


                    animation();
                }
                else
                    JOptionPane.showMessageDialog(null, " Missing either \n 1. Obstacles \n 2. Start and Destination Points");
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allowMouseEvents =true;
                remaining = true;
                finishButton.setEnabled(true);
                createAnimationButton.setEnabled(true);
                panel.revalidate();
                panel.repaint();
                clearAll();
            }
        });

        JRadiusList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                boolean adjust = e.getValueIsAdjusting();
                if (!adjust) {
                    selectedRadius = JRadiusList.getSelectedValue();
                    //System.out.println("selected radius is" + selectedRadius);
                }
            }
        });

    }

    public void clearAll(){
        randList.clear();
        obstacleArrayList.clear();
        sampleOutputList.clear();
        start = null;
        destination=null;
        ch=new BruteForceConvexHulls();
        icp = new InflateConvexPolygon();
        vg = new VisibilityGraph();
        dijk = new Dijkstras();
    }

    /**
     * Function that does linear interpolation between two points for animation
     * @param start point 1
     * @param end point 2
     * @param fraction time step
     * @return interpolated point
     */
    public PointO interpolate(PointO start, PointO end, double fraction) {
        int dx = (int)(end.x - start.x);
        int dy = (int)(end.y - start.y);

        int newX = (int) (start.x + dx * fraction);
        int newY = (int) (start.y + dy * fraction);

        return new PointO(newX, newY);
    }

    /**
     * Function for tracing the shortesT path
     */
    public void animation(){
        while (sampleOutputList.size()>1) {
            // Change the speed by changing the delta value
            for (double i = 0.0; i <= 1.0; i += 0.01) {
                if(sampleOutputList.size()>1) {
                    PointO p = interpolate(sampleOutputList.get(0) , sampleOutputList.get(1), i);

                    animationPoint(p);

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(sampleOutputList.size()>1) {
                sampleOutputList.remove(0);
            }
        }
        plotStartPoint(sampleOutputList.get(0));
    }



    public static void main(String[]args){

        learnSwing ls = new learnSwing();
        ls.ch = new BruteForceConvexHulls();
        ls.icp = new InflateConvexPolygon();
        ls.vg = new VisibilityGraph();
        ls.dijk = new Dijkstras();

        //create the window or canvas for drawing
        ls.createWindow();

        //draw the obstacle
        ls.drawObstacles();

       //animation in java to move on these points
       // ls.animation();


    }

}
