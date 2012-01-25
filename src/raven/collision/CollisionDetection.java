package raven.collision;

import java.util.LinkedList;

import raven.collision.datastructure.EndPointQueue;
import raven.collision.datastructure.MathFactory;
import raven.collision.datastructure.SweepLine;

public class CollisionDetection {
	private LinkedList<EndPoint> collisions = new LinkedList<EndPoint>();
	public EndPointQueue epQ = new EndPointQueue();
	public SweepLine slQ = new SweepLine();
	/*
	 * Step 1 order line segment end points Top to bottom into Queue
	 * Step 2 pop top end point from queue
	 * 		-if start EP, then call insertLinesegment function
	 *      -if end EP, then call removeLinesegment function
	 *      -if intersection end point then add to intersection ep list
	 */
	public void solve(LineSegment segments[]){
		collisions.clear();
		print("Insert Segment End Points into queue");
		epQ.clear();
		slQ.clear();
		for(int i=0;i<segments.length;i++){
			LineSegment seg = segments[i];
			epQ.addEndPoint(seg.start);
			epQ.addEndPoint(seg.currstop);
		}
		
		print("Pop queue elements one at a time and insert segment into segment queue");
		while(epQ.queuecount>0){
			EndPoint ep = epQ.pop();
			//print("EP x "+ep.x+" y "+ep.y);
			if(ep instanceof raven.collision.CollisionPoint){
				print("\nCollision Point detected");
			}else{
				EndPoint ordered[] = MathFactory.getInstance().getSweepLineOrdered(ep.seg);
				if(ep == ordered[0]){
					//print("\nStart EP detected");
					this.insertLinesegment(ep);
					
				}else{
					//print("\nStop EP detected");
					this.removeLinesegment(ep);
					
				}
			}
		}
	}
	
	/*
	 * Step 1: Insert segment into sweepline L-R position based on EP
	 * Step 2: Once inserted check left and right neighbor for intersection points
	 * Step 3: Insert any collision points into EP-Queue
	 */
	private void insertLinesegment(EndPoint ep){
		slQ.addLineSegment(ep.seg);
		System.out.println("");
		slQ.printEntireStructure();
		System.out.println("");
		//check left
		LineSegment leftSeg=slQ.getLeftSegment(ep.seg);
		if(leftSeg==null){
			print("Segment "+ep.x+","+ep.y+" has no left");
		}else{
			print("Segment "+ep.x+","+ep.y+" has a left node "+leftSeg.start.x+","+leftSeg.start.y);
		}
		
		//check right
		LineSegment rightSeg=slQ.getRightSegment(ep.seg);
		if(rightSeg==null){
			print("Segment "+ep.x+","+ep.y+" has no right");
		}else{
			print("Segment "+ep.x+","+ep.y+" has a right node "+rightSeg.start.x+","+rightSeg.start.y);
		}
	}
	
	/*
	 * Step 1: Remove Line Segment from the sweep line (LS-Queue)
	 * Step 2: Remove any EP-Collisions associated with this line segment from the 
	 * EP-Queue
	 * Step 3: Determine collision from left and right segments (if they exist)
	 */
	private void removeLinesegment(EndPoint ep){
		slQ.removeLineSegment(ep.seg);
	    slQ.printEntireStructure();
	}
	
	public void print(String msg){
		System.out.println(msg);
	}
}
