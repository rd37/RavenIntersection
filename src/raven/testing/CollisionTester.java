package raven.testing;

import java.util.LinkedList;
import java.util.Random;

import raven.collision.CollisionDetection;
import raven.collision.CollisionPoint;
import raven.collision.EndPoint;
import raven.collision.LineSegment;
import raven.collision.datastructure.EndPointQueue;
import raven.collision.datastructure.MathFactory;
import raven.collision.datastructure.SweepLine;

public class CollisionTester {
	
	
	
	private void collisionTestCase1(){
		CollisionDetection cd = new CollisionDetection();
		
		int segs[] = {1,1,4,4  ,6,1,3,4  ,4,2,6,5   ,2,5,1,6  };
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
			print("Collision at "+cd.collisions.get(i).toString());
		}
	}
	
	private void collisionTestCase2(){
		CollisionDetection cd = new CollisionDetection();
		
		int segs[] = {0,0,4,6  ,7,1,1,2  ,3,1,7,8   ,6,3,0,7  };
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
			print("Collision at "+cd.collisions.get(i).toString());
		}
	}
	
	private void collisionTestCase3(){
		CollisionDetection cd = new CollisionDetection();
		
		int segs[] = {4,0,11,7  ,14,0,5,9  ,3,5,8,10   ,9,1,0,10  };
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
			print("Collision at "+cd.collisions.get(i).toString());
		}
	}
	
	
	private void collisionTestCase4(){
		CollisionDetection cd = new CollisionDetection();
		cd.clear();
		
		int segs[] = {0,1,3,4  ,2,1,2,2  ,3,1,3,2   ,4,1,2,4  };
		LineSegment array[] = new LineSegment[segs.length/4];
		for(int i=0;i<segs.length/4;i++){
			LineSegment seg1 = new LineSegment();
			EndPoint ep1 = new EndPoint(segs[4*i],segs[4*i+1]);
			EndPoint ep2 = new EndPoint(segs[4*i+2],segs[4*i+3]);
			seg1.start=ep1;ep1.seg=seg1;
			seg1.currstop=ep2;ep2.seg=seg1;
			seg1.name="seg"+i;
			array[i]=seg1;
			cd.insertLinesegment(ep1);
		}
		cd.populateEventQ(array);
		cd.slQ.printEntireStructure();
		
		print("Remove Seg 1");
		EndPoint ordEP[] = MathFactory.getInstance().getSweepLineOrdered(array[1]);
		cd.removeLinesegment(ordEP[1]);
		cd.slQ.printEntireStructure();
		
		print("Remove Seg 2");
		ordEP = MathFactory.getInstance().getSweepLineOrdered(array[2]);
		cd.removeLinesegment(ordEP[1]);
		cd.slQ.printEntireStructure();
		
		print("\n***************Now Solve again using solver *************");
		//clear segment points in array
		for(int i=0;i<array.length;i++){
			array[i].parentSegment=null;
			array[i].leftSegment=null;
			array[i].rightSegment=null;
		}
		cd.clear();
		cd.populateEventQ(array);
		cd.solve();
		for(int i=0;i<cd.collisions.size();i++){
			print("Collision at "+cd.collisions.get(i).toString());
		}
	}
	
	private void sweepLineTestCase4(){
		CollisionDetection cd = new CollisionDetection();
		cd.clear();
		
		double segs[] = {1,1,6,5  ,6,1,1,4  ,1,2,6,7 ,6,2,1,7  };
		LineSegment array[] = new LineSegment[segs.length/4];
		for(int i=0;i<segs.length/4;i++){
			LineSegment seg1 = new LineSegment();
			EndPoint ep1 = new EndPoint(segs[4*i],segs[4*i+1]);
			EndPoint ep2 = new EndPoint(segs[4*i+2],segs[4*i+3]);
			seg1.start=ep1;ep1.seg=seg1;
			seg1.currstop=ep2;ep2.seg=seg1;
			seg1.name="seg"+i;
			array[i]=seg1;
			cd.insertLinesegment(ep1);
		}
		
		cd.slQ.printEntireStructure();
		CollisionPoint cp = MathFactory.getInstance().getIntersection(array[0], array[1]);
		cd.slQ.updateLineSegments(cp);
		cd.slQ.printEntireStructure();
		//cd.showEPLeftRight(array[1]);
		//for(int i=0;i<array.length;i++){
		//	cd.showEPLeftRight(array[i]);
		//}
		
	}
	
	private void collisionTestCase5(){
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
	
	public void runtestcases(){
		//endPointQueueTestCase();
		//mathFactoryTestCase();
		//sweepLineTestCase4();
		collisionTestCase5();
		//intersectionTestCase();
	}
	
	public static void main(String args[]){
		CollisionTester ct = new CollisionTester();
		ct.runtestcases();
	}
	
	public void print(String msg){
		System.out.println("CollisionTester::"+msg);
	}
}
