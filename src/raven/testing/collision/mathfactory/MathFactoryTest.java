package raven.testing.collision.mathfactory;

import raven.collision.EndPoint;
import raven.collision.LineSegment;
import raven.collision.datastructure.MathFactory;
import junit.framework.TestCase;

public class MathFactoryTest extends TestCase {

	public void testGetLength() {
		LineSegment seg1 = new LineSegment();
		seg1.start=new EndPoint(1,1);
		seg1.currstop=new EndPoint(4,4);
		seg1.stopref=new EndPoint(4,4);
		double length = MathFactory.getInstance().getLength(seg1);
		assertEquals("Test Length Result",4.242640687119285,length);
	}

	public void testCrossproduct() {
		//fail("Not yet implemented");
	}

}
