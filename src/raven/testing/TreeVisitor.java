package raven.testing;

import raven.collision.LineSegment;
import raven.collision.datastructure.DataStructureNode;
import raven.collision.datastructure.Visitor;

public class TreeVisitor implements Visitor{
	private int index=1;
	public String treearray[];
	
	public TreeVisitor(int depth){
		treearray = new String[(int)Math.pow(2, depth)];
		for(int i=0;i<Math.pow(2, depth);i++){
			treearray[i]="[  ,  :seg--]";
		}
	}
	@Override
	public void visit(DataStructureNode dsn) {
		int x = ((LineSegment)dsn.rootnode).start.x;
		int y = ((LineSegment)dsn.rootnode).start.y;
		treearray[index]="["+x+","+y+":"+((LineSegment)dsn.rootnode).name+"]";
		
		if(dsn.leftnode!=null && dsn.leftnode.visited==false){
			index=index*2;
			print("Go Down Left new index "+index);
			dsn.leftnode.accept(this);
		}else if(dsn.rightnode!=null && dsn.rightnode.visited==false){
			index=index*2;index++;
			print("Go Down Right "+index);
			dsn.rightnode.accept(this);
		}else{
			
			dsn.visited=true;
			if(dsn.parentnode!=null){
				
				if(dsn.parentnode.leftnode==dsn){
					index=index/2;
				}else{
					index--;index=index/2;
				}
				print("Go Up "+index);
				dsn.parentnode.accept(this);
			}
		}
	}
	
	public void print(String msg){
		//System.out.println(msg);
	}

}
