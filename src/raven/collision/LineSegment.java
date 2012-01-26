package raven.collision;

import raven.collision.datastructure.MathFactory;

public class LineSegment implements Comparable<Object>{
	public EndPoint start;
	//public EndPoint collisionpoint=null;
	public EndPoint stopref;
	public EndPoint currstop;
	public String name="seg";
	
	@Override
	public int compareTo(Object arg0) {
		return (int)MathFactory.getInstance().crossproduct(this, (EndPoint)arg0);
	}
}
