package raven.testing.collision.collisiondetection;

import raven.collision.CollisionDetection;
import raven.collision.EndPoint;
import raven.collision.LineSegment;
import junit.framework.TestCase;


public class CollisionDetectionTest extends TestCase{

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
		
		for(int i=0;i<cd.collisions.size();i++){
			System.out.println("Collision at "+cd.collisions.get(i).x+","+cd.collisions.get(i).y);
		}
	}
}
