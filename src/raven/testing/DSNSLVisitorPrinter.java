package raven.testing;

import raven.collision.EndPoint;
import raven.collision.LineSegment;
import raven.collision.datastructure.DataStructureNode;
import raven.collision.datastructure.MathFactory;
import raven.collision.datastructure.Visitor;

public class DSNSLVisitorPrinter implements Visitor{
	
	public void visit(DataStructureNode dsn){
		if(dsn.leftnode!=null && dsn.leftnode.visited==false)
			dsn.leftnode.accept(this);
		else{//left node is null so print this node and go right
			if(dsn.visited==false){
				EndPoint ordered[] = MathFactory.getInstance().getSweepLineOrdered((LineSegment)dsn.rootnode);
				System.out.println( "Seg x:"+ordered[0].x +" y:"+ordered[0].y+" , x:"+ordered[1].x +" y:"+ordered[1].y);
			}
			dsn.visited=true;
			if(dsn.rightnode!=null && dsn.rightnode.visited==false){
				dsn.rightnode.accept(this);
			}else{
				if(dsn.parentnode!=null)
					dsn.parentnode.accept(this);
				else
					System.out.println("Done");
			}
		}
	}
}