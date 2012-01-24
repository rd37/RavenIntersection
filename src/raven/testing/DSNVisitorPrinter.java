package raven.testing;

import raven.collision.EndPoint;
import raven.collision.datastructure.DataStructureNode;
import raven.collision.datastructure.Visitor;

public class DSNVisitorPrinter implements Visitor{
	public void visit(DataStructureNode dsn){
		if(dsn.leftnode!=null && dsn.leftnode.visited==false)
			dsn.leftnode.accept(this);
		else{//left node is null so print this node and go right
			if(dsn.visited==false)
				System.out.println( "EP x:"+((EndPoint)dsn.rootnode).x +" y:"+((EndPoint)dsn.rootnode).y );
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
