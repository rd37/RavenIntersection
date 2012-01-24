package raven.collision;

import raven.collision.datastructure.MathFactory;

public class LineSegment implements Comparable<Object>{
	public EndPoint start;
	public EndPoint stopref;
	public EndPoint currstop;
	
	@Override
	public int compareTo(Object arg0) {
		return MathFactory.getInstance().crossproduct(this, (EndPoint)arg0);
	}
}
