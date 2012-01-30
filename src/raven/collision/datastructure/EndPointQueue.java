package raven.collision.datastructure;

import java.util.LinkedList;

import raven.collision.CollisionPoint;
import raven.collision.EndPoint;
import raven.testing.DSNVisitorPrinter;

public class EndPointQueue {
	public DataStructureNode rootNode;
	public LinkedList<DataStructureNode> list = new LinkedList<DataStructureNode>();
	public int queuecount=0;
	
	public void clear(){
		rootNode=null;
		queuecount=0;
		list.clear();
	}
	
	public void addEndPoint(EndPoint ep){
		queuecount++;
		DataStructureNode n = new DataStructureNode();
		list.add(n);
		n.rootnode=ep;
		if(rootNode==null){
			//System.out.println("Set Root Node "+((EndPoint)n.rootnode).x+","+((EndPoint)n.rootnode).y);
			rootNode = n;
		}else{
			//rootNode.addNode(n);
			DataStructureNode ptr=rootNode;
			while(ptr!=null){
				if(ptr.rootnode.compareTo(n.rootnode)==1){//then this node is smaller along x axis
					if(ptr.rightnode==null){
						n.parentnode=ptr;
						ptr.rightnode=n;
						ptr=null;
					}else{
						ptr=ptr.rightnode;
					}
				}else if(ptr.rootnode.compareTo(n.rootnode)==-1){//then this node is smaller along x axis
					if(ptr.leftnode==null){
						n.parentnode=ptr;
						ptr.leftnode=n;
						ptr=null;
					}else{
						ptr=ptr.leftnode;
					}
				}else{//these two endpoints are the same
					print("Two end points are the same check if previously added collision point");
					if(n.rootnode instanceof raven.collision.CollisionPoint){
						if(ptr.rootnode instanceof raven.collision.CollisionPoint){
							if( ((CollisionPoint)n.rootnode).x==((CollisionPoint)ptr.rootnode).x && ((CollisionPoint)n.rootnode).y==((CollisionPoint)ptr.rootnode).y ){
								print("Collision Point Previously added using "+((CollisionPoint)n.rootnode).x+","+((CollisionPoint)n.rootnode).y);
								ptr=null;
								this.queuecount--;
								return;
							}
						}
					}
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
	
	private void print(String msg) {
		//System.out.println("EndPointQueue::"+msg);
	}

	public EndPoint pop(){
		if(this.rootNode==null){
			queuecount=0;
			return null;
		}else{
			queuecount--;
			DataStructureNode tmp=rootNode;
			DataStructureNode parent=rootNode;
			while(tmp!=null){
				parent=tmp;
				tmp=tmp.leftnode;
			}
			if(parent.rightnode!=null){//fix tree structure for removal of endpoint
				if(parent.parentnode!=null){
					parent.parentnode.leftnode=parent.rightnode;
					parent.rightnode.parentnode=parent.parentnode;
				}else{
					this.rootNode=parent.rightnode;
					this.rootNode.parentnode=null;
				}
			}else{
				if(parent.parentnode!=null)
					parent.parentnode.leftnode=null;
				else
					this.rootNode=null;
			}
			return (EndPoint)parent.rootnode;
		}
	}
	
	public void showDataStructure(){
		for(int i=0;i<list.size();i++){ //takes n
			list.get(i).visited=false;
		}
		DSNVisitorPrinter dsnvp = new DSNVisitorPrinter();
		if(rootNode!=null)
			rootNode.accept(dsnvp);
		
	}
	
}
