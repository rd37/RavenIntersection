package raven.collision;

import java.util.LinkedList;

public class CollisionDetection {
	private LinkedList<EndPoint> collisions = new LinkedList<EndPoint>();
	
	/*
	 * Step 1 order line segment end points Top to bottom into Queue
	 * Step 2 pop top end point from queue
	 * 		-if start EP, then call insertLinesegment function
	 *      -if end EP, then call removeLinesegment function
	 *      -if intersection end point then add to intersection ep list
	 */
	public void solve(LineSegment segments[]){
		collisions.clear();
		//System.out.println("Solve new segments");
		
	}
	
	/*
	 * Step 1: Insert segment into sweepline L-R position based on EP
	 * Step 2: Once inserted check left and right neighbor for intersection point
	 * Step 3: Insert any collision points into EP-Queue
	 * Step 4: determine collisions with left and right segments
	 */
	private void insertLinesegment(EndPoint ep){
		
	}
	
	/*
	 * Step 1: Remove Line Segment from the sweep line (LS-Queue)
	 * Step 2: Remove any EP-Collisions associated with this line segment from the 
	 * EP-Queue
	 * Step 3: Determine collision from left and right segments (if they exist)
	 */
	private void removeLinesegment(EndPoint ep){
		
	}
}
