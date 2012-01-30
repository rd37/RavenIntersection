package raven.collision.datastructure;

import raven.collision.LineSegment;

public class DataStructureNode {
	public Comparable<Object> rootnode;
	public LineSegment segnode;
	public DataStructureNode parentnode;
	public DataStructureNode leftnode;
	public DataStructureNode rightnode;
	
	public boolean visited=false;
	
	public void accept(Visitor dsnvp){
		dsnvp.visit(this);
	}
}
