package raven.testing.collision.mathfactory;

import raven.collision.CollisionPoint;
import raven.collision.EndPoint;
import raven.collision.LineSegment;
import raven.collision.datastructure.MathFactory;
import junit.framework.TestCase;

public class MathFactoryTest extends TestCase {

	public void testIntersectionTestCase(){
		double yptsx[] = {6,9};
		double yptsy[] = {1,1};
		double yptsx2[] = {8,7};
		double yptsy2[] = {3,3};
		LineSegment array[] = new LineSegment[yptsx.length];
		for(int i=0;i<yptsx.length;i++){
			EndPoint ep1 = new EndPoint(yptsx[i],yptsy[i]);
			EndPoint ep2 = new EndPoint(yptsx2[i],yptsy2[i]);
			LineSegment seg1 = new LineSegment();
			ep1.seg=seg1;ep2.seg=seg1;
			seg1.start=ep1;
			seg1.stopref=ep2;
			seg1.currstop=ep2;
			array[i]=seg1;
		}
		//System.out.println("check segs start "+array[0].start.x+","+array[0].start.y+" and "+array[0].currstop.x+","+array[0].currstop.y);
		//System.out.println("check segs start "+array[1].start.x+","+array[1].start.y+" and "+array[1].currstop.x+","+array[1].currstop.y);
		
		CollisionPoint collision = MathFactory.getInstance().getIntersection(array[0], array[1]);
		
		assertTrue(collision.x==7.5 && collision.y == 2.5);
	}
	
	public void testGetLength() {
		LineSegment seg1 = new LineSegment();
		seg1.start=new EndPoint(1,1);
		seg1.currstop=new EndPoint(4,4);
		seg1.stopref=new EndPoint(4,4);
		double length = MathFactory.getInstance().getLength(seg1);
		assertEquals("Test Length Result",4.242640687119285,length);
	}

	public void testCrossproduct() {
		LineSegment seg = new LineSegment();
		seg.start = new EndPoint(1,2);
		seg.currstop = new EndPoint(2,2);
		EndPoint cmpPoint = new EndPoint(0,2);
		double res = MathFactory.getInstance().crossproduct(seg, cmpPoint);
		assertEquals("Check if point is in line",0.0,res);
		cmpPoint = new EndPoint(0,1);
		res = MathFactory.getInstance().crossproduct(seg, cmpPoint);
		assertTrue(res<0);
		cmpPoint = new EndPoint(0,3);
		res = MathFactory.getInstance().crossproduct(seg, cmpPoint);
		assertTrue(res>0);
	}

}
