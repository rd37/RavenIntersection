package raven.testing;

import java.util.LinkedList;
import java.util.Random;

import raven.collision.CollisionDetection;
import raven.collision.EndPoint;
import raven.collision.LineSegment;
import raven.collision.datastructure.EndPointQueue;
import raven.collision.datastructure.MathFactory;
import raven.collision.datastructure.SweepLine;

public class CollisionTester {
	
	private void endPointQueueTestCase(){
		Random rand = new Random(555);
		EndPointQueue epq = new EndPointQueue();
		System.out.println("***************Create Fake End points************");
		for(int i=0;i<20;i++){
			double x = 200*rand.nextDouble();
			double y = 200*rand.nextDouble();
			EndPoint ep = new EndPoint();
			ep.x=(int)x;
			ep.y=(int)y;
			System.out.println("Create Node "+i+":"+ep.x+","+ep.y+" ");
			epq.addEndPoint(ep);
		}
		System.out.println("");
		System.out.println("***************done************");
		System.out.println("***************Now print them in order************");
		epq.showDataStructure();
		System.out.println("***************done************");
		System.out.println("***************Now pop 10 of them the reprint************");
		for(int i=0;i<10;i++){
			EndPoint ep=epq.pop();
			System.out.println("Popped "+ep.x+","+ep.y+" ");
		}
		System.out.println("***************show ds***********");
		epq.showDataStructure();
		System.out.println("***************done now add 5 more************");
		for(int i=0;i<5;i++){
			double x = 200*rand.nextDouble();
			double y = 200*rand.nextDouble();
			EndPoint ep = new EndPoint();
			ep.x=(int)x;
			ep.y=(int)y;
			System.out.println("Create Node "+i+":"+ep.x+","+ep.y+" ");
			epq.addEndPoint(ep);
		}
		System.out.println("***************Now pop 5 of them the reprint************");
		for(int i=0;i<15;i++){
			EndPoint ep=epq.pop();
			System.out.println("Popped "+ep.x+","+ep.y+" ");
		}
		System.out.println("***************show ds***********");
		epq.showDataStructure();
	}
	
	private void mathFactoryTestCase(){
		LineSegment seg = new LineSegment();
		seg.start = new EndPoint(1,2);
		seg.currstop = new EndPoint(2,2);
		EndPoint cmpPoint = new EndPoint(0,2);
		double res = MathFactory.getInstance().crossproduct(seg, cmpPoint);
		System.out.println("Res "+res);
	}
	
	private void sweepLineTestCase(){
		/*
		 * create several segments, add to sweepline then print out with visitor
		 */
		LinkedList<LineSegment> list =new LinkedList<LineSegment>();
		SweepLine sl = new SweepLine();
		//int segs[] = {9,0,3,4  ,7,3,7,4  ,8,1,6,4  ,6,1,8,4  ,5,1,5,4  ,7,1,7,2  ,1,1,4,4  ,0,0,8,8  ,2,3,7,8  ,1,0,4,4};
		int segs[] = {9,0,3,4  ,7,3,7,4  ,8,1,6,5   ,6,1,8,4  };
		for(int i=0;i<segs.length/4;i++){
			LineSegment seg1 = new LineSegment();
			EndPoint ep1 = new EndPoint(segs[4*i],segs[4*i+1]);
			EndPoint ep2 = new EndPoint(segs[4*i+2],segs[4*i+3]);
			seg1.start=ep1;ep1.seg=seg1;
			seg1.currstop=ep2;ep2.seg=seg1;
			sl.addLineSegment(seg1);
			list.add(seg1);
			seg1.name="seg"+i;
		}
		
		sl.printEntireStructure();
		
		
	}
	
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
		cd.solve(array);
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
		cd.solve(array);
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
		cd.solve(array);
		for(int i=0;i<cd.collisions.size();i++){
			print("Collision at "+cd.collisions.get(i).toString());
		}
	}
	
	private void intersectionTestCase(){
		double yptsx[] = {6,9};
		double yptsy[] = {1,1};
		double yptsx2[] = {7,8};
		double yptsy2[] = {2,2};
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
		EndPoint collision = MathFactory.getInstance().getIntersection(array[0], array[1]);
		if(collision!=null)
			print("Collsion at "+collision.x+","+collision.y);
	}
	
	public void runtestcases(){
		//endPointQueueTestCase();
		//mathFactoryTestCase();
		//sweepLineTestCase();
		collisionTestCase3();
		//intersectionTestCase();
	}
	
	public static void main(String args[]){
		CollisionTester ct = new CollisionTester();
		ct.runtestcases();
	}
	
	public void print(String msg){
		System.out.println(msg);
	}
}
