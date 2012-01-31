package raven.collision;

import raven.collision.datastructure.MathFactory;
import raven.collision.datastructure.Visitor;

public class LineSegment {
	public EndPoint start;
	public EndPoint stopref;
	public EndPoint currstop;
	
	public LineSegment parentSegment=null;
	public LineSegment leftSegment=null;
	public LineSegment rightSegment=null;
	public String name="seg";
	public RavenArm arm=null;
	public boolean visited=false;
	private double error=0.0000001;
	
	public double compareTo(Object arg0) {
		double res = MathFactory.getInstance().crossproduct(this, (EndPoint)arg0);
		double absres = Math.abs(res);
		if(absres<error)
			return 0;
		else
			return res;
	}
	
	public void accept(Visitor dv) {
		dv.visit(this);
	}
	
}
