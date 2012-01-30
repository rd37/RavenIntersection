package raven.collision;

import raven.collision.datastructure.MathFactory;
import raven.collision.datastructure.Visitor;
import raven.testing.DepthVisitor;

public class LineSegment {
	public EndPoint start;
	public EndPoint stopref;
	public EndPoint currstop;
	
	public LineSegment parentSegment=null;
	public LineSegment leftSegment=null;
	public LineSegment rightSegment=null;
	public String name="seg";
	public boolean visited=false;
	private double error=0.0000001;
	//public boolean removed=false;
	
	public double compareTo(Object arg0) {
		double res = MathFactory.getInstance().crossproduct(this, (EndPoint)arg0);
		double absres = Math.abs(res);
		if(absres<error)
			return 0;
		else
			return res;
	}
	//public double compaeToDouble(Object arg0) {
	//	return MathFactory.getInstance().crossproduct(this, (EndPoint)arg0);
	//}

	public void accept(Visitor dv) {
		dv.visit(this);
	}
	
}
