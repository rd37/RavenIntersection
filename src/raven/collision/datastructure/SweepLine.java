package raven.collision.datastructure;

import java.util.LinkedList;

import raven.collision.EndPoint;
import raven.collision.LineSegment;
import raven.testing.DSNSLVisitorPrinter;
import raven.testing.DepthVisitor;
import raven.testing.TreeVisitor;

public class SweepLine {
	public DataStructureNode rootNode;
	public LinkedList<DataStructureNode> list = new LinkedList<DataStructureNode>();
	
	public void clear(){
		rootNode=null;
		list.clear();
	}
	/*
	 * Will insert new segment into sweep line.
	 * 
	 * then a check for new collision with neighbors is required
	 */
	public void addLineSegment(LineSegment seg){ //
		DataStructureNode n = new DataStructureNode();
		list.add(n);
		n.rootnode=seg;//assign this endpoints segment as the nodes comparable
		if(rootNode==null){
			//System.out.println("added line segment to root node");
			rootNode = n;
		}else{
			//rootNode.addNewLineSegment(n);//for add node function use segments start endpoint for comparision
			//identify new nodes start endpoint. is in position 0
			EndPoint newEP[] = MathFactory.getInstance().getSweepLineOrdered((LineSegment)n.rootnode);
			DataStructureNode ptr = rootNode;
			while(ptr!=null){
				//compare endpoint to this nodes line segment
				int compRes = ptr.rootnode.compareTo(newEP[0]);
				print("Compare performed "+compRes);
				if(compRes<0){//go right in data structure since newEP[0] is to right of this line segment
					if(ptr.rightnode==null){
						print("added new line segment to right sub tree");
						n.parentnode=ptr;
						ptr.rightnode=n;
						ptr=null;//to get out of while loop
					}else{
						print("right sub tree node exists, add to sub node");
						ptr=ptr.rightnode;
					}
				}else if(compRes>0){
					if(ptr.leftnode==null){
						print("added new line segment to left sub tree");
						n.parentnode=ptr;
						ptr.leftnode=n;
						ptr=null;//to get out of while loop
					}else{
						print("left sub tree node exists, add to sub node");
						ptr=ptr.leftnode;
					}
				}else{
					System.err.println("start point is on line being compared, send to right in data structure");
					if(ptr.rightnode==null){
						n.parentnode=ptr;
						ptr.rightnode=n;
						ptr=null;
					}else{
						ptr=ptr.rightnode;
					}
				}	
			}
			
		}
	}
	
	/*
	 * Will remove the segment. use lowest endpoint
	 * 
	 * new neighbors need to be checked for collision
	 */
	public void removeLineSegment(LineSegment seg){
		if(rootNode!=null){
			//rootNode.removeLineSegment(seg);
			DataStructureNode ptr = rootNode;
			while(ptr!=null){
				if(((LineSegment)ptr.rootnode)==seg){
					
					print("found segment to remove");
					
					if(ptr.leftnode==null){
						if(ptr.rightnode==null){//easiest to solve both are null
							
							if(ptr.parentnode!=null){ //this node is not the root node
								print("node to remove has no children but is not root");
								if(ptr.parentnode.leftnode==ptr){
									ptr.parentnode.leftnode=null;
									ptr.parentnode=null;
								}else{
									ptr.parentnode.rightnode=null;
									ptr.parentnode=null;
								}
							}else{//remove the root node since no children, this is root node
								print("node to remove has no children and is root");
								this.rootNode=null;
							}
						}else{//left is null, but right node is not null
							print("node to remove has a right  child");
							if(ptr.parentnode==null){ //this is a root node
								this.rootNode=ptr.rightnode;
								ptr.rightnode.parentnode=null;
							}else{
								if(ptr.parentnode.leftnode==ptr){
									ptr.parentnode.leftnode=ptr.rightnode;
									ptr.rightnode.parentnode=ptr.parentnode;
								}else{
									ptr.parentnode.rightnode=ptr.rightnode;
									ptr.rightnode.parentnode=ptr.parentnode;
								}
							}
						}
					}else{
						if(ptr.rightnode==null){//left node is not null but right node is null
							
							if(ptr.parentnode==null){ //this is root node
								print("node to remove has a left child only, node to remove is root");
								this.rootNode=ptr.leftnode;
								ptr.leftnode.parentnode=null;
							}else{
								print("node to remove has a left child only, node to remove is not root");
								if(ptr.parentnode.leftnode==ptr){
									ptr.parentnode.leftnode=ptr.leftnode;
									ptr.leftnode.parentnode=ptr.parentnode;
								}else{
									ptr.parentnode.rightnode=ptr.leftnode;
									ptr.leftnode.parentnode=ptr.parentnode;
								}
							}
						}else{ //hardest case to handle node has two subtrees
							print("node to remove has both children");
							if(ptr.parentnode==null){
								print("node to remove has both children and is root");
								this.rootNode=ptr.rightnode;
								ptr.rightnode.parentnode=null;
								
								DataStructureNode rightMost=ptr.leftnode;
								DataStructureNode rightMostPtr=ptr.leftnode;
								while(rightMostPtr!=null){
									rightMost=rightMostPtr;
									rightMostPtr=rightMostPtr.rightnode;
								}
								rightMost.rightnode=ptr.rightnode.leftnode;
								if(ptr.rightnode.leftnode!=null)
									ptr.rightnode.leftnode.parentnode=rightMost;
								
								ptr.rightnode.leftnode=ptr.leftnode;
								if(ptr.leftnode!=null)
									ptr.leftnode.parentnode=ptr.rightnode;
							}else{
								print("node to remove has both children and is not root");
								if(ptr.parentnode.leftnode==ptr){
									ptr.parentnode.leftnode=ptr.rightnode;
									ptr.rightnode.parentnode=ptr.parentnode;
									
									DataStructureNode rightMost=ptr.leftnode;
									DataStructureNode rightMostPtr=ptr.leftnode;
									while(rightMostPtr!=null){
										rightMost=rightMostPtr;
										rightMostPtr=rightMostPtr.rightnode;
									}
									rightMost.rightnode=ptr.rightnode.leftnode;
									if(ptr.rightnode.leftnode!=null)
										ptr.rightnode.leftnode.parentnode=rightMost;
									
									ptr.rightnode.leftnode=ptr.leftnode;
									if(ptr.leftnode!=null)
										ptr.leftnode.parentnode=ptr.rightnode;
								}else{
									ptr.parentnode.rightnode=ptr.rightnode;
									ptr.rightnode.parentnode=ptr.parentnode;
									
									DataStructureNode rightMost=ptr.leftnode;
									DataStructureNode rightMostPtr=ptr.leftnode;
									while(rightMostPtr!=null){
										rightMost=rightMostPtr;
										rightMostPtr=rightMostPtr.rightnode;
									}
									rightMost.rightnode=ptr.rightnode.leftnode;
									if(ptr.rightnode.leftnode!=null)
										ptr.rightnode.leftnode.parentnode=rightMost;
									
									ptr.rightnode.leftnode=ptr.leftnode;
									if(ptr.leftnode!=null)
										ptr.leftnode.parentnode=ptr.rightnode;
								}
							}
						}
					}
					ptr=null;
				}else{
					EndPoint segOrderedEP[] = MathFactory.getInstance().getSweepLineOrdered(seg);
					//need to look left or right
					int compRes = ptr.rootnode.compareTo(segOrderedEP[0]);
					//print("Compare performed "+compRes);
					if(compRes<0){//go right in data structure since segOrderedEP[0] is to right of this line segment
						ptr=ptr.rightnode;
					}else if(compRes>0){//go left
						ptr=ptr.leftnode;
					}else{//just look to right
						ptr=ptr.rightnode;
					}
					if(ptr==null){
						print("unable to find segment");
					}
				}//end else
			}
		}else{
			System.err.println("No segments in data structure");
		}
	}
	
	public void updateLineSegments(EndPoint collisionEP){
		
	}
	
	
	public void showDataStructure(){
		for(int i=0;i<list.size();i++){ //takes n
			list.get(i).visited=false;
		}
		DSNSLVisitorPrinter dsnvp = new DSNSLVisitorPrinter();
		if(rootNode!=null)
			rootNode.accept(dsnvp);
		
	}
	
	public void resetVisited(){
		for(int i=0;i<list.size();i++){ //takes n
			list.get(i).visited=false;
		}
	}
	
	public void printEntireStructure(){
		print("Show structure");
		if(this.rootNode==null){
			print("depth is zero");
			return;
		}
		this.resetVisited();
		DepthVisitor dv = new DepthVisitor();
		this.rootNode.accept(dv);
		print("depth is "+dv.maxdepth);
		
		this.resetVisited();
		TreeVisitor tv = new TreeVisitor(dv.maxdepth);
		this.rootNode.accept(tv);
		
		String array[] = tv.treearray;
		int olddivisor=-1;
		for(int i=0;i<array.length;i++){
			if(i!=0){
				//determine divisor length
				int divisor =((int) (Math.log(i)/Math.log(2)) );
				//System.out.print(i+": is at level div is "+divisor+" is divisor is "+(Math.pow(2, divisor)+1));
				double length =  ( (Math.pow(2, dv.maxdepth))- Math.pow(2,divisor) ) / (Math.pow(2, divisor)+1) ;
				//System.out.println("at "+i+" length div is "+length);
				StringBuffer sb = new StringBuffer();
				for(double j=0.0;j<length;j=j+0.07){
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
	}
	
	private void print(String msg){
		//System.out.println(msg);
	}
	
	public LineSegment getRightSegment(LineSegment seg) {
		DataStructureNode ptr=this.rootNode;
		DataStructureNode mostRightptr=null;
		EndPoint segEP[] = MathFactory.getInstance().getSweepLineOrdered(seg);
		boolean foundnode=false;
		
		while(ptr!=null){
			if(ptr.rootnode==seg){
				//System.out.println("Found the segment");
				//found node now do left check use mostRightptr may still be null
				foundnode=true;
				//check right, if null then done, if not null, set ptr to right and then lefts for ever
				if(ptr.rightnode!=null){
					ptr=ptr.rightnode;
					while(ptr!=null){
						if(mostRightptr!=null){
							//compare smallest values
							double curr=MathFactory.getInstance().crossproduct((LineSegment)mostRightptr.rootnode, segEP[0]);
							double comp=MathFactory.getInstance().crossproduct((LineSegment)ptr.rootnode, segEP[0]);
							System.out.println("********Comparision is curr "+curr+" compared to "+comp);
							if(comp<curr){
								mostRightptr=ptr;
							}
							ptr=ptr.leftnode;
						}else{
							mostRightptr=ptr;
							ptr=ptr.leftnode;
						}
					}
				}else{
					//System.out.println("Found the segment, but no left tree so done");
				}
				ptr=null;
			}else{//search for seg
				int compRes = ptr.rootnode.compareTo(segEP[0]);
				//print("Compare performed "+compRes);
				if(compRes<0){//go right in data structure since newEP[0] is to right of this line segment
					ptr=ptr.rightnode;
				}else if(compRes>0){
					mostRightptr=ptr;
					ptr=ptr.leftnode;
				}else{
					ptr=ptr.rightnode;
				}
				
			}
		}
		if(mostRightptr==null || foundnode==false)
			return null;
		return (LineSegment)mostRightptr.rootnode;
	}
	
	public LineSegment getLeftSegment(LineSegment seg) {
		DataStructureNode ptr=this.rootNode;
		DataStructureNode mostLeftptr=null;
		EndPoint segEP[] = MathFactory.getInstance().getSweepLineOrdered(seg);
		boolean foundnode=false;
		
		while(ptr!=null){
			if(ptr.rootnode==seg){
				//System.out.println("Found the segment");
				//found node now do left check use mostLeftptr may still be null
				foundnode=true;
				//check left, if null then done, if not null, set ptr to left and then rights for ever
				if(ptr.leftnode!=null){
					ptr=ptr.leftnode;
					while(ptr!=null){
						if(mostLeftptr!=null){
							//compare smallest values
							double curr=MathFactory.getInstance().crossproduct((LineSegment)mostLeftptr.rootnode, segEP[0]);
							double comp=MathFactory.getInstance().crossproduct((LineSegment)ptr.rootnode, segEP[0]);
							//System.out.println("Comparision is curr "+curr+" compared to "+comp);
							if(comp<curr){
								mostLeftptr=ptr;
							}
							ptr=ptr.rightnode;
						}else{
							mostLeftptr=ptr;
							ptr=ptr.rightnode;
						}
					}
				}else{
					//System.out.println("Found the segment, but no left tree so done");
				}
				ptr=null;
			}else{//search for seg
				int compRes = ptr.rootnode.compareTo(segEP[0]);
				//print("Compare performed "+compRes);
				if(compRes<0){//go right in data structure since newEP[0] is to right of this line segment
					mostLeftptr=ptr;
					ptr=ptr.rightnode;
				}else if(compRes>0){
					ptr=ptr.leftnode;
				}else{
					ptr=ptr.rightnode;
				}
				
			}
		}
		if(mostLeftptr==null || foundnode==false)
			return null;
		return (LineSegment)mostLeftptr.rootnode;
	}
}
