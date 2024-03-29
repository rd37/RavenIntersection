package raven.collision;

import java.util.LinkedList;

public class EndPoint implements Comparable<Object>{
	public double x=0;
	public double y=0;
	public LineSegment seg;
	public LinkedList<EndPoint> collisions = new LinkedList<EndPoint>();
	
	public EndPoint(){}
	
	public EndPoint(double x,double y){
		this.x=x;
		this.y=y;
	}
	@Override
	public int compareTo(Object ep1) {
		EndPoint ep = (EndPoint)ep1;
		if(y<ep.y){
			return 1;
		}else if(y>ep.y){
			return -1;
		}else if(x<ep.x){
			return 1;
		}else if(x>ep.x){
			return -1;
		}else{
			return 0;
		}
	}
}
