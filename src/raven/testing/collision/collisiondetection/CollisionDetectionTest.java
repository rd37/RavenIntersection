package raven.testing.collision.collisiondetection;

import raven.collision.CollisionDetection;
import raven.collision.EndPoint;
import raven.collision.LineSegment;
import junit.framework.TestCase;


public class CollisionDetectionTest extends TestCase{

	public void testEndPointBoxTestCase(){
		CollisionDetection cd = new CollisionDetection();
		cd.clear();
		
		int segs[] = {2,1,2,3  ,2,1,5,1  ,5,1,5,3   ,2,3,5,3  };
		LineSegment array[] = new LineSegment[segs.length/4];
		for(int i=0;i<segs.length/4;i++){
			LineSegment seg1 = new LineSegment();
			EndPoint ep1 = new EndPoint(segs[4*i],segs[4*i+1]);
			EndPoint ep2 = new EndPoint(segs[4*i+2],segs[4*i+3]);
			seg1.start=ep1;ep1.seg=seg1;
			seg1.currstop=ep2;ep2.seg=seg1;
			seg1.name="seg"+i;
			array[i]=seg1;
		}
		cd.populateEventQ(array);
		cd.solve();
		
		assertTrue(cd.collisions.get(0).x==2.0 && cd.collisions.get(0).y==1.0);
		assertTrue(cd.collisions.get(1).x==2.0 && cd.collisions.get(1).y==1.0);
		assertTrue(cd.collisions.get(2).x==5.0 && cd.collisions.get(2).y==1.0);
		assertTrue(cd.collisions.get(3).x==2.0 && cd.collisions.get(3).y==3.0);
		assertTrue(cd.collisions.get(4).x==5.0 && cd.collisions.get(4).y==3.0);
		assertTrue(cd.collisions.get(5).x==5.0 && cd.collisions.get(5).y==3.0);
		assertTrue(cd.collisions.size()==6);
	}
	
	public void testCollisionTestCase(){
		CollisionDetection cd = new CollisionDetection();
		cd.clear();
		
		int segs[] = {1,1,6,8  ,12,1,2,3  ,5,2,12,11   ,8,5,1,11  };
		LineSegment array[] = new LineSegment[segs.length/4];
		for(int i=0;i<segs.length/4;i++){
			LineSegment seg1 = new LineSegment();
			EndPoint ep1 = new EndPoint(segs[4*i],segs[4*i+1]);
			EndPoint ep2 = new EndPoint(segs[4*i+2],segs[4*i+3]);
			seg1.start=ep1;ep1.seg=seg1;
			seg1.currstop=ep2;ep2.seg=seg1;
			seg1.name="seg"+i;
			array[i]=seg1;
		}
		cd.populateEventQ(array);
		cd.solve();
		
		assertTrue(cd.collisions.get(0).x==5.269230769230769);
		assertTrue(cd.collisions.get(0).y==2.3461538461538463);
		
		assertTrue(cd.collisions.get(1).x==2.375);
		assertTrue(cd.collisions.get(1).y==2.925);
		
		assertTrue(cd.collisions.get(2).x==7.6);
		assertTrue(cd.collisions.get(2).y==5.3428571428571425);
		
		assertTrue(cd.collisions.get(3).x==5.4303797468354436);
		assertTrue(cd.collisions.get(3).y==7.2025316455696204);
		
	}
}
