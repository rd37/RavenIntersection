package raven.collision.datastructure;

import raven.collision.CollisionPoint;
import raven.collision.EndPoint;
import raven.collision.LineSegment;

public class MathFactory {
	private static MathFactory factory = new MathFactory();
	
	private MathFactory(){}
	
	public static MathFactory getInstance(){return factory;}
	
	public CollisionPoint getIntersection(LineSegment seg1, LineSegment seg2){
		if(seg1==null || seg2 == null)
			return null;
		EndPoint eps1[] = this.getSweepLineOrdered(seg1);
		double x1=eps1[0].x;
		double y1=eps1[0].y;
		double x2=eps1[1].x;
		double y2=eps1[1].y;
		
		EndPoint eps2[] = this.getSweepLineOrdered(seg2);
		double x4=eps2[0].x;
		double y4=eps2[0].y;
		double x3=eps2[1].x;
		double y3=eps2[1].y;
		//double epx = ( (x1*y2-y1*x2)*(x3-x4)-(x1-x2)*(x3*x4-y3*x4) ) / ( (x1-x2)*(y3-y4)-(y1-y2)*(x3-x4) );
		//double epy = ( (x1*y2-y1*x2)*(y3-y4)-(y1-y2)*(x3*y4-y3*x4) ) / ( (x1-x2)*(y3-y4)-(y1-y2)*(x3-x4) );
		double ua = ( (x4-x3)*(y1-y3)-(y4-y3)*(x1-x3) ) / ( (y4-y3)*(x2-x1) - (x4-x3)*(y2-y1) );
		if(ua<Double.POSITIVE_INFINITY && ua>Double.NEGATIVE_INFINITY){
			double x = x1+ua*(x2-x1);
			double y = y1+ua*(y2-y1);
			if(y>=y1 && y>=y4 && y<=y2 && y<=y3){
				CollisionPoint cp = new CollisionPoint(seg1,seg2);
				cp.x=x;
				cp.y=y;
				return cp;
			}else{
				//System.out.println("There is a potential collision at "+x+","+y);
				return null;
			}
		}else{
			//System.out.println("Compared Lines are Parallel "+seg1.name+" "+seg2.name);
			return null;
		}
		
	}
	public double crossproduct(LineSegment seg, EndPoint ep){
		/*
		 * determine top point of line seg
		 * shift end point by start point
		 * shift ep by start point
		 * get dot product sum
		 * return result
		 */
		EndPoint ordered[] = this.getSweepLineOrdered(seg);
		EndPoint segVectorEP=this.subtractEndPoints(ordered[1], ordered[0]);
		EndPoint epVectorEP=this.subtractEndPoints(ep, ordered[0]);
		
		double crossprod=segVectorEP.x*epVectorEP.y-segVectorEP.y*epVectorEP.x;
		return crossprod;
	}
	
	public EndPoint subtractEndPoints(EndPoint ep1,EndPoint ep2){
		EndPoint sub = new EndPoint();
		sub.x=ep1.x-ep2.x;
		sub.y=ep1.y-ep2.y;
		return sub;
	}
	
	public EndPoint[] getSweepLineOrdered(LineSegment seg){
		EndPoint segStartEP;
		EndPoint segEndEP;
		if(seg==null){
			System.err.println("Seg found is null ..  this should probably not happen");
		}
		if(seg.start.y<seg.currstop.y){
			segStartEP=seg.start;
			segEndEP=seg.currstop;
		}else if(seg.start.y>seg.currstop.y){
			segStartEP=seg.currstop;
			segEndEP=seg.start;
		}else{
			if(seg.start.x<seg.currstop.x){
				segStartEP=seg.start;
				segEndEP=seg.currstop;
			}else if(seg.start.x<seg.currstop.x){
				segStartEP=seg.currstop;
				segEndEP=seg.start;
			}else{
				System.err.println("This segment has same end point and start points? should not happen");
				segStartEP=seg.start;
				segEndEP=seg.currstop;
			}
		}
		EndPoint array[] = new EndPoint[2];
		array[0]=segStartEP;
		array[1]=segEndEP;
		return array;
	}
}
