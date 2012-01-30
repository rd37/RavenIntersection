package raven.testing;

import raven.collision.LineSegment;
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

	@Override
	public void visit(LineSegment seg) {
		if(maxdepth<=currdepth)
			maxdepth=currdepth;
		
		if(seg.leftSegment!=null && seg.leftSegment.visited==false){
			print("Go Down Left");
			currdepth++;
			seg.leftSegment.accept(this);
		}else if(seg.rightSegment!=null && seg.rightSegment.visited==false){
			print("Go Down Right");
			currdepth++;
			seg.rightSegment.accept(this);
		}else{
			
			seg.visited=true;
			if(seg.parentSegment!=null){
				currdepth=currdepth-1;
				print("Go Up");
				seg.parentSegment.accept(this);
			}
		}
	}
}
