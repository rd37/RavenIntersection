package raven.testing;

import raven.collision.datastructure.DataStructureNode;
import raven.collision.datastructure.Visitor;

public class DepthVisitor implements Visitor {
	public int maxdepth=0;
	public int currdepth=1;
	
	public void visit(DataStructureNode dsn) {
		//currdepth++;
		if(maxdepth<=currdepth)
			maxdepth=currdepth;
		
		if(dsn.leftnode!=null && dsn.leftnode.visited==false){
			print("Go Down Left");
			currdepth++;
			dsn.leftnode.accept(this);
		}else if(dsn.rightnode!=null && dsn.rightnode.visited==false){
			print("Go Down Right");
			currdepth++;
			dsn.rightnode.accept(this);
		}else{
			
			dsn.visited=true;
			if(dsn.parentnode!=null){
				currdepth=currdepth-1;
				print("Go Up");
				dsn.parentnode.accept(this);
			}
		}
	}

	public void print(String msg){
		//System.out.println(msg);
	}
}
