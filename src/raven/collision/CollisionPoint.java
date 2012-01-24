package raven.collision;

public class CollisionPoint extends EndPoint {
	public LineSegment seg1;
	public LineSegment seg2;
	
	public CollisionPoint(){};
	
	public CollisionPoint(LineSegment seg1,LineSegment seg2){
		this.seg1=seg1;this.seg2=seg2;
	}
}
