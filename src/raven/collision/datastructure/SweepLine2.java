package raven.collision.datastructure;

import java.util.LinkedList;

import raven.collision.CollisionPoint;
import raven.collision.EndPoint;
import raven.collision.LineSegment;
import raven.testing.DepthVisitor;
import raven.testing.TreeVisitor;

public class SweepLine2 {
	public EndPoint sweepLinePosition=null;
	public LineSegment rootSegment = null;
	public LinkedList<LineSegment> list = new LinkedList<LineSegment>();
	
	public void clear(){
		rootSegment=null;
		list.clear();
	}
	
	public void resetVisited(){
		//System.out.println("Reset the list "+list.size());
		for(int i=0;i<list.size();i++){ //takes n
			list.get(i).visited=false;
		}
	}
	
	public void addSegment(LineSegment seg,EndPoint cmpEP[]){
		if(seg==null)
			return;
		//for testing
		
		EndPoint[] ordEP;
		if(cmpEP!=null){
			ordEP=cmpEP;
			//System.out.println("Added seg "+seg.name+" using Coll Pt "+ordEP[0].x+","+ordEP[0].y);
		}else
			ordEP=MathFactory.getInstance().getSweepLineOrdered(seg);
		
		if(rootSegment==null){
			sweepLinePosition=ordEP[0];
			rootSegment=seg;
			//print("Added First segment "+seg.name);
			list.add(seg);
			return;
		}
		if(ordEP[0].y<sweepLinePosition.y){
			print("Sweep Line Violation ... ignore "+seg.name);
	    	return;
		}else
			sweepLinePosition=ordEP[0];
		
		list.add(seg);
		
		
		LineSegment ptr = rootSegment;
		//print("add segment "+seg.name);
		while(ptr!=null){
			double compRes = ptr.compareTo(ordEP[0]);
			if(cmpEP!=null)
				System.out.println("CP compare is "+compRes+" between tree seg "+ptr.name+" and insert seg "+seg.name);
			if(compRes<=0){//go right in data structure
				if(ptr.rightSegment!=null){
					ptr=ptr.rightSegment;
				}else{
					ptr.rightSegment=seg;
					seg.parentSegment=ptr;
					ptr=null;
				}
			}else{//go left in data Structure
				if(ptr.leftSegment!=null){
					ptr=ptr.leftSegment;
				}else{
					ptr.leftSegment=seg;
					seg.parentSegment=ptr;
					ptr=null;
				}
			}
			
		}
	}
	
	public void removeSegment(LineSegment seg,EndPoint cmpEP[]){
		if(rootSegment==null || seg == null)
			return;
		
		list.remove(seg);
		/*
		 * Update Sweep Current Sweep Line Position
		 */
		EndPoint[] ordEP;
		if(cmpEP!=null){
			ordEP=cmpEP;
		}else{
			ordEP=MathFactory.getInstance().getSweepLineOrdered(seg);
		}
		
		
		if(ordEP[1].y<sweepLinePosition.y){
			System.out.println("****This segment should have already been removed "+seg.name+" epY:"+ordEP[1].y+" sweepline y "+this.sweepLinePosition.y);
	    	return;
		}else
			sweepLinePosition=ordEP[1];
		
		/*
		 * case 1 is both children are null
		 */
		if(seg.leftSegment==null && seg.rightSegment==null){
			if(seg!=rootSegment){ //this node is not the root node
				if(seg.parentSegment.leftSegment==seg){
					seg.parentSegment.leftSegment=null;
					seg.parentSegment=null;
				}else{
					seg.parentSegment.rightSegment=null;
					seg.parentSegment=null;
				}
			}else{//remove the root node since no children, this is root node
				this.rootSegment=null;
			}
			seg.rightSegment=null;seg.leftSegment=null;seg.parentSegment=null;
			return;
		}
		
		/*
		 * case 2 left child is null and right is not null
		 */
		if(seg.leftSegment==null && seg.rightSegment!=null){//left is null, but right node is not null
			if(seg==rootSegment){ //this is a root node
				rootSegment=seg.rightSegment;
				seg.rightSegment.parentSegment=null;
			}else{
				if(seg.parentSegment.leftSegment==seg){
					seg.parentSegment.leftSegment=seg.rightSegment;
					seg.rightSegment.parentSegment=seg.parentSegment;
				}else{
					seg.parentSegment.rightSegment=seg.rightSegment;
					seg.rightSegment.parentSegment=seg.parentSegment;
				}
			}
			seg.rightSegment=null;seg.leftSegment=null;seg.parentSegment=null;
			return;
		}
		
		/*
		 * case 3 left child is not null and right is null
		 */
		if(seg.rightSegment==null && seg.leftSegment!=null){//left node is not null but right node is null
			if(seg.parentSegment==null){ //this is root node
				rootSegment=seg.leftSegment;
				seg.leftSegment.parentSegment=null;
			}else{
				if(seg.parentSegment.leftSegment==seg){
					seg.parentSegment.leftSegment=seg.leftSegment;
					seg.leftSegment.parentSegment=seg.parentSegment;
				}else{
					seg.parentSegment.rightSegment=seg.leftSegment;
					seg.leftSegment.parentSegment=seg.parentSegment;
				}
			}
			seg.rightSegment=null;seg.leftSegment=null;seg.parentSegment=null;
			return;
		}
		
		/*
		 * case 4 segment has both children
		 */
		if(seg.rightSegment!=null && seg.leftSegment!=null){
			if(seg==rootSegment){ //seg is root and has both children
				rootSegment=seg.rightSegment;
				seg.rightSegment.parentSegment=null;
				
				LineSegment rightMost=seg.leftSegment;
				LineSegment rightMostPtr=seg.leftSegment;
				while(rightMostPtr!=null){
					rightMost=rightMostPtr;
					rightMostPtr=rightMostPtr.rightSegment;
				}
				rightMost.rightSegment=seg.rightSegment.leftSegment;
				if(seg.rightSegment.leftSegment!=null)
					seg.rightSegment.leftSegment.parentSegment=rightMost;
				
				seg.rightSegment.leftSegment=seg.leftSegment;
				//if(seg.leftSegment!=null)
					seg.leftSegment.parentSegment=seg.rightSegment;
			}else{ //seg is not root and has both children
				if(seg.parentSegment.leftSegment==seg){ //I'm on my parents left side
					seg.parentSegment.leftSegment=seg.rightSegment;
					seg.rightSegment.parentSegment=seg.parentSegment;
					
					LineSegment rightMost=seg.leftSegment;
					LineSegment rightMostPtr=seg.leftSegment;
					while(rightMostPtr!=null){
						rightMost=rightMostPtr;
						rightMostPtr=rightMostPtr.rightSegment;
					}
					rightMost.rightSegment=seg.rightSegment.leftSegment;
					if(seg.rightSegment.leftSegment!=null)
						seg.rightSegment.leftSegment.parentSegment=rightMost;
					
					seg.rightSegment.leftSegment=seg.leftSegment;
					//if(seg.leftSegment!=null)
						seg.leftSegment.parentSegment=seg.rightSegment;
				}else{  //I'm on my parents right side
					seg.parentSegment.rightSegment=seg.rightSegment;
					seg.rightSegment.parentSegment=seg.parentSegment;
					
					LineSegment rightMost=seg.leftSegment;
					LineSegment rightMostPtr=seg.leftSegment;
					while(rightMostPtr!=null){
						rightMost=rightMostPtr;
						rightMostPtr=rightMostPtr.rightSegment;
					}
					rightMost.rightSegment=seg.rightSegment.leftSegment;
					if(seg.rightSegment.leftSegment!=null)
						seg.rightSegment.leftSegment.parentSegment=rightMost;
					
					seg.rightSegment.leftSegment=seg.leftSegment;
					//if(seg.leftSegment!=null)
						seg.leftSegment.parentSegment=seg.rightSegment;
				}
				
			}
			seg.rightSegment=null;seg.leftSegment=null;seg.parentSegment=null;
			return;
		}
		seg.rightSegment=null;seg.leftSegment=null;seg.parentSegment=null;
	}
	
	public LineSegment getLeftSegment(LineSegment seg){
		if(seg==null)
			return null;
		
		/*
		 * First case where segment is root
		 */
		if(seg==rootSegment){
			print("Get Left Segment First case "+seg.name);
			return this.getMostLeftSegmentLower(seg);
		}
		
		if(seg.parentSegment==null){
			System.err.println("getLeftSegment: On Left Error seg has no parent, but is not root "+seg.name);
			return null;
		}
		
		/*
		 * Second case where this segment is to right of parent so parent is smaller or to left 
		 */
		if(seg.parentSegment.rightSegment==seg){
			print("Get Left Segment Second Case for seg "+seg.name+" which is smaller than parent");
			LineSegment mostLeft = this.getMostLeftSegmentLower(seg);
			if(mostLeft==null){
				//return this.getMostLeftSegmentUpper(seg);
				return seg.parentSegment;
			}else
				return mostLeft;
		}
		
		/*
		 * Third case where this segment is to left of parent so parent is larger or to right
		 */
		if(seg.parentSegment.leftSegment==seg){
			print("Get Left Segment Third Case for seg "+seg.name+" which is larger than parent");
			LineSegment mostLeft = this.getMostLeftSegmentLower(seg);
			if(mostLeft==null){
				//return seg.parentSegment;
				return this.getMostLeftSegmentUpper(seg);
			}else{
				return mostLeft;
			}
			//return this.getMostLeftSegmentLower(seg);
			
			//return seg.leftSegment;
		}
		return null;
	}
	
	/*
	 * work up segements parents lefts until there is a right set ptrRet to the right if 
	 * found
	 */
	private LineSegment getMostLeftSegmentUpper(LineSegment seg){
		LineSegment ptr=seg.parentSegment;
		LineSegment ptrRet=null;
		while(ptr!=null){
			if(ptr.parentSegment==null){
				//print(seg.name+" Segment has no left child " );
				ptr=null;
			}else{
				if(ptr.parentSegment.rightSegment==ptr){
					ptrRet=ptr.parentSegment;
					ptr=null;
				}else{//then must be a left segemnt so go upward
					ptr=ptr.parentSegment;
				}
			}
		}
		return ptrRet;
	}
	
	private LineSegment getMostLeftSegmentLower(LineSegment seg){
		LineSegment ptr=seg.leftSegment;
		LineSegment ptrRet = seg.leftSegment;
		while(ptr!=null){
			ptrRet=ptr;
			ptr=ptr.rightSegment;
		}
		return ptrRet;
	}
	
	
	public LineSegment getRightSegment(LineSegment seg){
		if(seg==null)
			return null;
		
		/*
		 * First case where segment is root
		 */
		if(seg==rootSegment){
			print("Get Right Segment First case "+seg.name);
			return this.getMostRightSegmentLower(seg);
		}
		
		if(seg.parentSegment==null){
			System.err.println("getRightSegment: On Get Right Error seg has no parent, but is not root "+seg.name);
			return null;
		}
		
		/*
		 * Second case where this segment is to right of parent so parent is smaller or to left 
		 */
		if(seg.parentSegment.rightSegment==seg){
			print("Get Right Segment Second Case for seg "+seg.name+" which is larger than parent");
			LineSegment mostRight = this.getMostRightSegmentLower(seg);
			if(mostRight==null){
				return this.getMostRightSegmentUpper(seg);
				//return seg.parentSegment;
			}else
				return mostRight;
				
			//return this.getMostRightSegmentLower(seg);
		}
		
		/*
		 * Third case where this segment is to left of parent so parent is larger or to right
		 */
		if(seg.parentSegment.leftSegment==seg){
			print("Get Right Segment Third Case for seg "+seg.name+" which is smaller than parent");
			LineSegment mostRight = this.getMostRightSegmentLower(seg);
			if(mostRight==null){
				return seg.parentSegment;
				//return this.getMostRightSegmentUpper(seg);
			}else{
				return mostRight;
			}
			//return seg.rightSegment;
		}
		
		print("This should not happen, "+seg.name+" parent is "+seg.parentSegment.name);
		return null;
	}
	
	private LineSegment getMostRightSegmentLower(LineSegment seg){
		LineSegment ptr=seg.rightSegment;
		LineSegment ptrRet = seg.rightSegment;
		while(ptr!=null){
			ptrRet=ptr;
			ptr=ptr.leftSegment;
		}
		return ptrRet;
	}
	
	private LineSegment getMostRightSegmentUpper(LineSegment seg){
		LineSegment ptr=seg.parentSegment;
		LineSegment ptrRet=null;
		while(ptr!=null){
			if(ptr.parentSegment==null){
				//print(seg.name+" Segment has no right child" );
				ptr=null;
			}else{
				if(ptr.parentSegment.leftSegment==ptr){
					ptrRet=ptr.parentSegment;
					ptr=null;
				}else{//then must be a left segment so go upward
					ptr=ptr.parentSegment;
				}
			}
		}
		return ptrRet;
	}
	
	public void updateLineSegments(CollisionPoint collisionEP){
		if(collisionEP==null){
			return;
		}
		
		EndPoint array[] = new EndPoint[2];
		array[0]=collisionEP;
		array[1]=collisionEP;
		
		LineSegment seg1 = collisionEP.seg1;
		LineSegment seg2 = collisionEP.seg2;
		
		LineSegment seg1Left = this.getLeftSegment(seg1);
		
		//System.out.println("Had Collision, remove first segment from DS");
		this.removeSegment(seg1,array);
		//this.printEntireStructure();
		//System.out.println("Had Collision, remove second segment from DS");
		this.removeSegment(seg2,array);
		
		//this.printEntireStructure();
		//System.out.println("Now re add them "+seg2.name+" and "+seg1.name);
		if(seg1Left==seg2){//seg2 is to left of seg1
			this.addSegment(seg1, array);
			System.out.println("Added "+seg1.name+" first then "+seg2.name	);
			this.addSegment(seg2, array);
		}else{//seg2 is to right of seg1
			this.addSegment(seg2, array);
			System.out.println("Added "+seg2.name+" first then "+seg1.name	);
			this.addSegment(seg1, array);
		}
		//System.out.println("done");
		
	}
	
	public void printEntireStructure(){
		//print("Show structure");
		if(this.rootSegment==null){
			print("depth is zero");
			return;
		}
		this.resetVisited();
		DepthVisitor dv = new DepthVisitor();
		this.rootSegment.accept(dv);
		print("depth is "+dv.maxdepth);
		
		this.resetVisited();
		TreeVisitor tv = new TreeVisitor(dv.maxdepth);
		this.rootSegment.accept(tv);
		
		String array[] = tv.treearray;
		//print("********");print("********");
		//for(int i=0;i<array.length;i++){
		//	print(i+":"+array[i]);
		//}
		//print("********");print("********");
		int olddivisor=-1;
		for(int i=0;i<array.length;i++){
			if(i!=0){
				//determine divisor length
				int divisor =((int) (Math.log(i)/Math.log(2)) );
				//System.out.print(i+": is at level div is "+divisor+" is divisor is "+(Math.pow(2, divisor)+1));
				double length =  ( (Math.pow(2, dv.maxdepth))- Math.pow(2,divisor) ) / (Math.pow(2, divisor)+1) ;
				//System.out.println("at "+i+" length div is "+length);
				StringBuffer sb = new StringBuffer();
				for(double j=0.0;j<length;j=j+0.085){
					sb.append(" ");
				}
				if(olddivisor!=divisor){
					System.out.println("");
					olddivisor=divisor;
				}
				System.out.print(sb.toString());
				System.out.print(array[i]);
				
			}
		}
		System.out.println("");
	}
	
	private void print(String msg){
		//System.out.println("SweepLine2::"+msg);
	}
}
