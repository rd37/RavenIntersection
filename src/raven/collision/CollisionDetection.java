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
	
	public void populateEventQ(LineSegment segments[]){
		for(int i=0;i<segments.length;i++){
			LineSegment seg = segments[i];
			epQ.addEndPoint(seg.start);
			epQ.addEndPoint(seg.currstop);
		}
	}
	/*
	 * Step 1 order line segment end points Top to bottom into Queue
	 * Step 2 pop top end point from queue
	 * 		-if start EP, then call insertLinesegment function
	 *      -if end EP, then call removeLinesegment function
	 *      -if intersection end point then add to intersection ep list
	 */
	public void solve(){
		
		while(epQ.queuecount>0){
			EndPoint ep = epQ.pop();
			if( !(ep instanceof raven.collision.CollisionPoint) ){
				print("Popped EP belonging to segment "+ep.seg.name+" at Y:"+ep.y+","+ep.x);
			}else{
				print("Popped EP Collision belongs to segments "+((CollisionPoint)ep).seg1.name+" and "+((CollisionPoint)ep).seg2.name+" at Y:"+ep.y+","+ep.x);
			}
			
			if(ep instanceof raven.collision.CollisionPoint){
				print("Collision Point detected so flip them "+((CollisionPoint) ep).x+","+((CollisionPoint) ep).y+" between "+((CollisionPoint)ep).seg1.name+" and "+((CollisionPoint)ep).seg2.name);
				collisions.add((CollisionPoint) ep);
				
				//print("before collision point switch");
				//slQ.printEntireStructure();
				slQ.updateLineSegments((CollisionPoint) ep);//basically switch node positions
				//print("after collision point switch");
				//slQ.printEntireStructure();
				//check for collisions with each neighbour so two collision checks here
				LineSegment seg1 = ((CollisionPoint)ep).seg1;
				LineSegment seg2 = ((CollisionPoint)ep).seg2;
				
				EndPoint array[] = new EndPoint[1];array[0]=ep;
				
				LineSegment seg1Left = slQ.getLeftSegment(seg1);
				LineSegment seg1Right = slQ.getRightSegment(seg1);
				
				LineSegment seg2Left = slQ.getLeftSegment(seg2);
				LineSegment seg2Right = slQ.getRightSegment(seg2);
				
				//check seg collisions
				if(seg1Left==seg2){//then compare seg1 to seg1Right and set2 to seg2 left
					print("Check for collision between "+seg1.name+" and "+seg1Right+"  also   "+seg2.name+" and "+seg2Left);
					if(seg1Right!=null){
						CollisionPoint cp1 = MathFactory.getInstance().getIntersection(seg1, seg1Right);
						if(cp1!=null && slQ.sweepLineCheck(cp1)){
							print("New Collision found after previous collision at point "+cp1.x+","+cp1.y+" between "+seg1.name+" and its righ "+seg1Right.name);
							this.epQ.addEndPoint(cp1);
						}
					}
					if(seg2Left!=null){
						CollisionPoint cp2 = MathFactory.getInstance().getIntersection(seg2, seg2Left);
						if(cp2!=null && slQ.sweepLineCheck(cp2)){
							print("New Collision found after previous collision at point "+cp2.x+","+cp2.y+" between "+seg2.name+" and its left "+seg2Left.name);
							this.epQ.addEndPoint(cp2);
						}
					}
				}else{
					print("Check for collision between "+seg1.name+" and "+seg1Left+"  also   "+seg2.name+" and "+seg2Right);
					if(seg1Left!=null){
						CollisionPoint cp1 = MathFactory.getInstance().getIntersection(seg1, seg1Left);
						if(cp1!=null && slQ.sweepLineCheck(cp1)){
							print("New Collision found after previous collision at point "+cp1.x+","+cp1.y+" between "+seg1.name+" and its left "+seg1Left.name);
							
							this.epQ.addEndPoint(cp1);
						}
					}
					if(seg2Right!=null){
						CollisionPoint cp2 = MathFactory.getInstance().getIntersection(seg2, seg2Right);
						if(cp2!=null && slQ.sweepLineCheck(cp2)){
							print("New Collision found after previous collision at point "+cp2.x+","+cp2.y+" between "+seg2.name+" its Right seg "+seg2Right.name);
							
							this.epQ.addEndPoint(cp2);
						}
					}
				}
				
			}else{
				EndPoint ordered[] = MathFactory.getInstance().getSweepLineOrdered(ep.seg);
				if(ep == ordered[0]){
					this.insertLinesegment(ep);
				}else{
					this.removeLinesegment(ep);
				}
			}
			print("New Sweep Line Level Y: "+slQ.sweepLinePosition.y+" , x:"+slQ.sweepLinePosition.x);
		}
	}
	
	/*
	 * Step 1: Insert segment into sweepline L-R position based on EP
	 * Step 2: Once inserted check left and right neighbor for intersection points
	 * Step 3: Insert any collision points into EP-Queue
	 */
	public void insertLinesegment(EndPoint ep){
		slQ.addSegment(ep.seg,null);
		LineSegment leftSeg=slQ.getLeftSegment(ep.seg);
		if(leftSeg!=null){
			CollisionPoint cp = MathFactory.getInstance().getIntersection(ep.seg, leftSeg);
			if(cp!=null && slQ.sweepLineCheck(cp)){
				print("Collision Point created at "+cp.x+","+cp.y+" between "+cp.seg1.name+" and "+cp.seg2.name);
				epQ.addEndPoint(cp);
			}
		}
		
		//check right
		LineSegment rightSeg=slQ.getRightSegment(ep.seg);
		if(rightSeg!=null){
			CollisionPoint cp = MathFactory.getInstance().getIntersection(ep.seg, rightSeg);
			if(cp!=null && slQ.sweepLineCheck(cp) ){
				print("Collision Point created at "+cp.x+","+cp.y+" between "+cp.seg1.name+" and "+cp.seg2.name);
				epQ.addEndPoint(cp);
			}
		}
	}
	
	/*
	 * Step 1: Remove Line Segment from the sweep line (LS-Queue)
	 * 
	 * Step 3: Determine collision from left and right segments (if they exist)
	 */
	public void removeLinesegment(EndPoint ep){
		LineSegment seg1Left = slQ.getLeftSegment(ep.seg);
		LineSegment seg1Right = slQ.getRightSegment(ep.seg);
		slQ.removeSegment(ep.seg,null);
		
		if(seg1Left!=null && seg1Right!=null){
			CollisionPoint cp1 = MathFactory.getInstance().getIntersection(seg1Left, seg1Right);
			if(cp1!=null && slQ.sweepLineCheck(cp1)){
				print("Collision Detected add to EP queue: "+ep.seg.name+" was removed and caused collision between "+seg1Left.name+" and "+seg1Right.name);
				epQ.addEndPoint(cp1);
			}
		}
	}
	
	public void showEPLeftRight(LineSegment seg){
		LineSegment seg1Left = slQ.getLeftSegment(seg);
		LineSegment seg1Right = slQ.getRightSegment(seg);
		if(seg1Left!=null){
			if(seg1Right!=null){
				print(seg.name+" L:"+seg1Left.name+" R:"+seg1Right.name);
			}else{
				print(seg.name+" L:"+seg1Left.name+" R:");
			}
		}else{
			if(seg1Right!=null){
				print(seg.name+" L:"+" R:"+seg1Right.name);
			}else{
				print(seg.name+" L:"+" R:");
			}
		}
	}
	
	
	public void print(String msg){
		//System.out.println("CollisionDetection:"+msg);
	}
}
