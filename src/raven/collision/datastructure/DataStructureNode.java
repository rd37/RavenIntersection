package raven.collision.datastructure;

public class DataStructureNode {
	public Comparable<Object> rootnode;
	
	public DataStructureNode parentnode;
	public DataStructureNode leftnode;
	public DataStructureNode rightnode;
	
	public boolean visited=false;
	
	public void accept(Visitor dsnvp){
		dsnvp.visit(this);
	}
}
