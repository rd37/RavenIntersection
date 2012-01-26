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
	
	public void solve2(LineSegment segments[]){
		for(int i=0;i<segments.length-1;i++){
			for(int j=i+1;j<segments.length;j++){
				CollisionPoint cp = MathFactory.getInstance().getIntersection(segments[i], segments[j]);
				if(cp!=null){
					collisions.add(cp);
				}
			}
		}
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
		System.out.print(".");
		//print("Insert Segment End Points into queue");
		epQ.clear();
		slQ.clear();
		for(int i=0;i<segments.length;i++){
			LineSegment seg = segments[i];
			epQ.addEndPoint(seg.start);
			epQ.addEndPoint(seg.currstop);
		}
		
		//print("Pop queue elements one at a time and insert segment into segment queue");
		while(epQ.queuecount>0){
			EndPoint ep = epQ.pop();
			//print("EP x "+ep.x+" y "+ep.y);
			if(ep instanceof raven.collision.CollisionPoint){
				print("\nCollision Point detected so flip them "+((CollisionPoint) ep).x+","+((CollisionPoint) ep).y);
				collisions.add((CollisionPoint) ep);
				//slQ.printEntireStructure();
				//print("\nCollision Point detected so flip them at "+ep.x+","+ep.y);
				slQ.updateLineSegments((CollisionPoint) ep);//basically switch node positions
				//slQ.printEntireStructure();System.out.println("");
				/*try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				//check for collisions with each neighbor so two collision checks here
				LineSegment seg1 = ((CollisionPoint)ep).seg1;
				LineSegment seg2 = ((CollisionPoint)ep).seg2;
				
				EndPoint array[] = new EndPoint[1];array[0]=ep;
				LineSegment seg1Left = slQ.getLeftSegment(seg1,array);
				LineSegment seg1Right = slQ.getRightSegment(seg1,array);
				//System.out.println("seg "+seg1.name+" L:"+seg1Left+" R:"+seg1Right.name);
				
				LineSegment seg2Left = slQ.getLeftSegment(seg2,array);
				LineSegment seg2Right = slQ.getRightSegment(seg2,array);
				//System.out.println("seg "+seg2.name+" L:"+seg2Left.name+" R:"+seg2Right);
				
				CollisionPoint cp1L=null;
				CollisionPoint cp1R=null;
				CollisionPoint cp2L=null;
				CollisionPoint cp2R=null;
				
				CollisionPoint nbr1=null;
				CollisionPoint nbr2=null;
				
				if(seg1Right!=null & seg1Left!=null){
					nbr1= MathFactory.getInstance().getIntersection(seg1Left, seg1Right);
				}
				if(nbr1!=null){
					if( ep.y<nbr1.y && (!(ep.x==nbr1.x && ep.y==nbr1.y)) )
						epQ.addEndPoint(nbr1);
				}
				if(seg2Right!=null & seg2Left!=null){
					nbr2= MathFactory.getInstance().getIntersection(seg2Left, seg2Right);
				}
				if(nbr2!=null){
					if( ep.y<nbr2.y && (!(ep.x==nbr2.x && ep.y==nbr2.y)) )
						epQ.addEndPoint(nbr2);
				}
				//do segment 1 first
				if(seg1Left==seg2){//then check for collision between seg1 and seg1Right
					if(seg1Right!=null)
						cp1R = MathFactory.getInstance().getIntersection(seg1, seg1Right);
					
				}else{//then check for collision between seg1 and seg1Left
					if(seg1Left!=null)
						cp1L = MathFactory.getInstance().getIntersection(seg1, seg1Left);
				}
				if(cp1R!=null){
					print("After collision point flip There is a right collision between these segments at "+cp1R.x+","+cp1R.y);
					if( ep.y<cp1R.y && (!(ep.x==cp1R.x && ep.y==cp1R.y)) )
						epQ.addEndPoint(cp1R);
				}
				if(cp1L!=null){
					print("After collision point flip There is a right collision between these segments at "+cp1L.x+","+cp1L.y);
					if( ep.y<cp1L.y && (!(ep.x==cp1L.x && ep.y==cp1L.y)) )
						epQ.addEndPoint(cp1L);
				}
				//do segment 2 now
				if(seg2Left==seg1){//then check for collision between seg2 and seg2Right
					if(seg2Right!=null)
						cp2R = MathFactory.getInstance().getIntersection(seg2, seg2Right);
				}else{//then check for collision between seg2 and seg2Left
					if(seg2Left!=null)
						cp2L = MathFactory.getInstance().getIntersection(seg2, seg2Left);
				}
				if(cp2R!=null){
					print("After collison point flip There is a right collision between these segments"+cp2R.x+","+cp2R.y);
					if( ep.y<cp2R.y && (!(ep.x==cp2R.x && ep.y==cp2R.y)) )
						epQ.addEndPoint(cp2R);
				}
				if(cp2L!=null){
					print("After collison point flip There is a left collision between these segments"+cp2L.x+","+cp2L.y);
					if( ep.y<cp2L.y && (!(ep.x==cp2L.x && ep.y==cp2L.y)) )
						epQ.addEndPoint(cp2L);
				}
			}else{
				if(ep!=null){
					
					EndPoint ordered[] = MathFactory.getInstance().getSweepLineOrdered(ep.seg);
					if(ep == ordered[0]){
						//print("\nStart EP detected");
						this.insertLinesegment(ep);
						
					}else{
						//print("\nStop EP detected");
						this.removeLinesegment(ep);
						
					}
				}else{
					System.err.println("ep was null from queue");
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
		//check left
		//DataStructureNode leftNode = slQ.getLeftSegment(ep.seg);
		LineSegment leftSeg=slQ.getLeftSegment(ep.seg,null);
		if(leftSeg==null){
			//print("Segment "+ep.x+","+ep.y+" has no left");
		}else{
			//print("Segment "+ep.x+","+ep.y+" has a left node "+leftSeg.start.x+","+leftSeg.start.y);
			//check for collision and insert collision into epQueue
			CollisionPoint cp = MathFactory.getInstance().getIntersection(ep.seg, leftSeg);
			if(cp!=null){
				//print("After insert segment There is a Left collision between these segments "+cp.x+","+cp.y);
				epQ.addEndPoint(cp);
			}else{
				//print("After Insert segment There is no Left collsion between these segments");
			}
		}
		
		//check right
		LineSegment rightSeg=slQ.getRightSegment(ep.seg,null);
		if(rightSeg==null){
			//print("Segment "+ep.x+","+ep.y+" has no right");
		}else{
			//print("Segment "+ep.x+","+ep.y+" has a right node "+rightSeg.start.x+","+rightSeg.start.y);
			CollisionPoint cp = MathFactory.getInstance().getIntersection(ep.seg, rightSeg);
			if(cp!=null){
				//print("After insert segment There is a right collision between these segments "+cp.x+","+cp.y);
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
		EndPoint array[] = new EndPoint[1];array[0]=ep;
		LineSegment seg1Left = slQ.getLeftSegment(ep.seg,array);
		LineSegment seg1Right = slQ.getRightSegment(ep.seg,array);
		boolean success = slQ.removeLineSegment(ep.seg,array,true);
		if(!success){
			System.out.println("remov seg in coll det class issue removing "+ep.seg.name);
			boolean success2 = slQ.removeLineSegment(ep.seg,array,false);
			if(!success2)
				System.out.println("***serious error removing line in CD class*****"+ep.seg.name);
			else
				System.out.println("It worked");
		}
		if(seg1Left!=null && seg1Right!=null){
			CollisionPoint cp1 = MathFactory.getInstance().getIntersection(seg1Left, seg1Right);
			if(cp1!=null){
				//print("After remove line segment "+ep.seg.name+" , There is a collision between LR segments "+cp1.x+","+cp1.y);
				//print("Between "+seg1Left.name+" & "+seg1Right.name);
				epQ.addEndPoint(cp1);
			}else{
				//print("After remove line segment , There is no collsion between LR segments");
			}
		}else{
			//print("Removed "+ep.seg.name+" but no collisions");
		}
	    //slQ.printEntireStructure();
	}
	
	
	public void print(String msg){
		System.out.println(msg);
	}
}
