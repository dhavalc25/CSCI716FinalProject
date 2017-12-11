=======================================================================================
I. Introduction:															=======================================================================================
Project Title:		Euclidean Shortest Path Problem (CSCI-716)		 
Team Members:		Dhaval Chauhan (dmc8686@rit.edu),    
			Anuja Vane (asv1612@rit.edu),                               	
			Saylee Bhide (smb6390@rit.edu)	                                  
				Computer Science Department	   				
				Rochester Institute of Technology  				
Developed in: 		Java								   					
Environment: 		IntelliJ							   					
Additional Toolkits: 	Java Swing for developing the Graphical User Interface	                  
=======================================================================================


=======================================================================================
II. Files Included:															
=======================================================================================
PointO.java  
	Class for storing the 2D Point																		
Obstacle.java		
	Class for storing an obstacle which is a list of 2D Points	
	
learnSwing.java		
	Main Class that implements the GUI and calls the other java files	
	
BruteForceConvexHulls.java														
	Class that finds the Convex Hull for each polygon created by the User in the 
	GUI	
	
InflateConvexPolygon.java														
	Class for Inflating the polygon created by the User in the GUI	
	
Vertex.java		
	Class for storing 2D Point Vertices of Visibility Graph		
	
Graph.java	
	Class for storing the Visibility Graph attributes and helper functions	
	
VisibilityGraph.java															
	Class for finding the VisibilityGraph 		
	
Dijkstras.java	
	Class to find Shortest Path between vertices using Dijkstras Shortest Path 	
	Algorithm																	
=======================================================================================


=======================================================================================
III. Assumptions:															
=======================================================================================
1. User must plot the points in an Anti-Clockwise order.						
				
2. User must not plot the start or destination point in one of the Obstacles.	
=======================================================================================


=======================================================================================
IV. Validations: 
=======================================================================================
1. User cannot create an Obstacle of two points. The minimum number of points 	
	required for the Obstacle to be created is at-least three. 												
2. The map should contain at-least two obstacles. User should create at-least 	
	two Obstacles in the map before clicking the Finish button. 												
3. User cannot find the shortest path until the Obstacles have been created and 
	the start and destination points have been plotted.							
=======================================================================================


=======================================================================================
V. How to run the Project:														
=======================================================================================
Main Class : learnSwing.java	

#1. Compile and Run the file learnSwing.java. It will display the User Interface 
	for the User to customize his map by drawing Obstacles and selecting the 	
	start and destination point on the Canvas. Please read the Guidelines 		
	mentioned on \the GUI before proceeding. 														
#2. To draw an Obstacle, plot points in an anti-clockwise order on the yellow	 
	canvas using the click action of the mouse. To finish the obstacle click the 
	Make button to join the last plotted point with the first plotted point.
	
#3. Once all the obstacles have been plotted, click the Finish button to plot 	
	the start and destination point.
	
#4. The default radius of the start and destination point is 20 but to select 	
	your own radius, select from the list by scrolling down and selecting the 	
	desired radius.																		
#5. To plot the start and destination point, press the mouse at the location in 
	the canvas where you want your start point to be at and release it by 		
	dragging the mouse to the location you want your destination point to be at.										
#6. Once you are done plotting the start and destination points, click Find 	
	Shortest Path button to find the shortest path between the start and 		
	destination point. 
	
#7. If you want to find the shortest path for a new set of Obstacles, press the 
	Reset button to erase the Canvas and repeat the above mentioned steps. 		
=======================================================================================


=======================================================================================
VI. Failure Scenarios:															
=======================================================================================
1. The Obstacles in the map are in close vicinity of each other.

2. The obstacles contain sharp edges.	

3. The radius of the moving point does not fit in the path that leads to the 	
	destination point.			
	
4. The start or the destination point lies within an obstacle.					
=======================================================================================






