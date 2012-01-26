package raven.collision;

import raven.collision.datastructure.DataStructureNode;

public class CollisionPoint extends EndPoint {
	public LineSegment seg1;
	public LineSegment seg2;
	public DataStructureNode node1;
	public DataStructureNode node2;
	
	public CollisionPoint(){};
	
	public CollisionPoint(LineSegment seg1,LineSegment seg2){
		this.seg1=seg1;this.seg2=seg2;
	}
	
	public String toString(){
		return this.x+","+this.y+" s1:"+seg1.name+" s2:"+seg2.name;
	}
}
