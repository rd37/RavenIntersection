package raven.collision.datastructure;

import raven.collision.EndPoint;
import raven.collision.LineSegment;

public class MathFactory {
	private static MathFactory factory = new MathFactory();
	
	private MathFactory(){}
	
	public static MathFactory getInstance(){return factory;}
	
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
				System.err.println("This segment has same end point and start points?");
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
