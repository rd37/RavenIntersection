package raven.collision;

import java.util.LinkedList;

import raven.collision.datastructure.EndPointQueue;
import raven.collision.datastructure.MathFactory;
import raven.collision.datastructure.SweepLine;

public class CollisionDetection {
	public LinkedList<CollisionPoint> collisions = new LinkedList<CollisionPoint>();
	public EndPointQueue epQ = new EndPointQueue();
	public SweepLine slQ = new SweepLine();
	
	public void clear(){
		collisions.clear();
		epQ = new EndPointQueue();
		slQ = new SweepLine();
	}
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
				collisions.add((CollisionPoint) ep);
				slQ.updateLineSegments((CollisionPoint) ep);//basically switch node positions
				
				//check for collisions with each neighbor so two collision checks here
				LineSegment seg1 = ((CollisionPoint)ep).seg1;
				LineSegment seg2 = ((CollisionPoint)ep).seg2;
				
				LineSegment seg1Left = slQ.getLeftSegment(seg1);
				LineSegment seg1Right = slQ.getRightSegment(seg1);
				
				LineSegment seg2Left = slQ.getLeftSegment(seg2);
				LineSegment seg2Right = slQ.getRightSegment(seg2);
				CollisionPoint cp1=null;
				CollisionPoint cp2=null;
				//do segment 1 first
				if(seg1Left==seg2){//then check for collision between seg1 and seg1Right
					if(seg1Right!=null)
						cp1 = MathFactory.getInstance().getIntersection(seg1, seg1Right);
					
				}else{//then check for collision between seg1 and seg1Left
					if(seg1Left!=null)
						cp1 = MathFactory.getInstance().getIntersection(seg1, seg1Left);
				}
				if(cp1!=null){
					//print("After colllsion point flip There is a left collision between these segments");
					epQ.addEndPoint(cp1);
				}else{
					//print("After collsion point flip There is no left collsion between these segments");
				}
				//do segment 2 now
				if(seg2Left==seg1){//then check for collision between seg2 and seg2Right
					if(seg2Right!=null)
						cp2 = MathFactory.getInstance().getIntersection(seg2, seg2Right);
				}else{//then check for collision between seg2 and seg2Left
					if(seg2Left!=null)
						cp2 = MathFactory.getInstance().getIntersection(seg2, seg2Left);
				}
				if(cp2!=null){
					//print("After collison point flip There is a right collision between these segments");
					epQ.addEndPoint(cp2);
				}else{
					//print("After collision point flip There is no right collsion between these segments");
				}
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
		//DataStructureNode leftNode = slQ.getLeftSegment(ep.seg);
		LineSegment leftSeg=slQ.getLeftSegment(ep.seg);
		if(leftSeg==null){
			//print("Segment "+ep.x+","+ep.y+" has no left");
		}else{
			//print("Segment "+ep.x+","+ep.y+" has a left node "+leftSeg.start.x+","+leftSeg.start.y);
			//check for collision and insert collision into epQueue
			CollisionPoint cp = MathFactory.getInstance().getIntersection(ep.seg, leftSeg);
			if(cp!=null){
				//print("After insert segment There is a Left collision between these segments");
				epQ.addEndPoint(cp);
			}else{
				//print("After Insert segment There is no Left collsion between these segments");
			}
		}
		
		//check right
		LineSegment rightSeg=slQ.getRightSegment(ep.seg);
		if(rightSeg==null){
			//print("Segment "+ep.x+","+ep.y+" has no right");
		}else{
			//print("Segment "+ep.x+","+ep.y+" has a right node "+rightSeg.start.x+","+rightSeg.start.y);
			CollisionPoint cp = MathFactory.getInstance().getIntersection(ep.seg, rightSeg);
			if(cp!=null){
				//print("After insert segment There is a right collision between these segments");
				epQ.addEndPoint(cp);
			}else{
				//print("After insert segment There is no right collsion between these segments");
			}
		}
	}
	
	/*
	 * Step 1: Remove Line Segment from the sweep line (LS-Queue)
	 * 
	 * Step 3: Determine collision from left and right segments (if they exist)
	 */
	private void removeLinesegment(EndPoint ep){
		LineSegment seg1Left = slQ.getLeftSegment(ep.seg);
		LineSegment seg1Right = slQ.getRightSegment(ep.seg);
		slQ.removeLineSegment(ep.seg);
		if(seg1Left!=null && seg1Right!=null){
			CollisionPoint cp1 = MathFactory.getInstance().getIntersection(seg1Left, seg1Right);
			if(cp1!=null){
				//print("After remove line segment , There is a collision between LR segments");
				epQ.addEndPoint(cp1);
			}else{
				//print("After remove line segment , There is no collsion between LR segments");
			}
		}
	    slQ.printEntireStructure();
	}
	
	
	public void print(String msg){
		System.out.println(msg);
	}
}
