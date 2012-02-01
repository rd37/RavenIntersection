package raven.testing.collision.sweepline;

import java.util.LinkedList;

import raven.collision.EndPoint;
import raven.collision.LineSegment;
import raven.collision.datastructure.SweepLine;
import junit.framework.TestCase;


public class SweepLineTest extends TestCase{

	
	public void testRemoveOneAtATimeAndCheckLeftRight(){
		/*
		 * create several segments, add to sweepline then print out with visitor
		 * make sure the right and left children of each are correct
		 */
		LinkedList<LineSegment> list = new LinkedList<LineSegment>();
		SweepLine sl = new SweepLine();
		double segs[] = {9,0,3,4  ,10,0,7,4  ,8,0,6,4  ,2,0,3,4  ,8.5,0,3,4   ,11,0,5,4   ,9.5,0,2,4  ,1,0,9,4  ,9.25,0,6,4   ,8.75,0,7,4   ,8.25,0,3,4   ,4,0,9,4   ,9.75,0,2,4   ,10.5,0,4,4  ,15,0,8,4};
		for(int i=0;i<segs.length/4;i++){
			LineSegment seg1 = new LineSegment();
			EndPoint ep1 = new EndPoint(segs[4*i],segs[4*i+1]);
			EndPoint ep2 = new EndPoint(segs[4*i+2],segs[4*i+3]);
			seg1.start=ep1;ep1.seg=seg1;
			seg1.currstop=ep2;ep2.seg=seg1;
			list.add(seg1);
			seg1.name="seg"+i;
			sl.addSegment(seg1,null);
		}
		
		String segmentNames[] = {"seg7","seg3","seg11","seg2","seg10","seg4","seg9","seg0","seg8","seg6","seg12","seg1","seg13","seg5","seg14"};
		LinkedList<String> segNames = new LinkedList<String>();
		for(int i=0;i<segmentNames.length;i++){
			segNames.add(segmentNames[i]);
		}
		
		//sl.printEntireStructure();
		while(list.size()>0){
			sl.removeSegment(list.get(0), null);
			LineSegment rem = list.remove(0);
			segNames.remove(rem.name);
			this.assertSweepLine(sl, list, segNames);
		}
		//sl.printEntireStructure();
	}
	
	public void testSweepLineDataStructureLeftRight(){
		/*
		 * create several segments, add to sweepline then print out with visitor
		 * make sure the right and left children of each are correct
		 */
		LinkedList<LineSegment> list = new LinkedList<LineSegment>();
		
		SweepLine sl = new SweepLine();
		double segs[] = {9,0,3,4  ,10,0,7,4  ,8,0,6,4  ,2,0,3,4  ,8.5,0,3,4   ,11,0,5,5   ,9.5,0,2,3  ,1,0,9,9  ,9.25,0,6,6   ,8.75,0,7,6   ,8.25,0,3,1   ,4,0,9,9   ,9.75,0,2,6   ,10.5,0,4,4  ,15,0,8,8};
		for(int i=0;i<segs.length/4;i++){
			LineSegment seg1 = new LineSegment();
			EndPoint ep1 = new EndPoint(segs[4*i],segs[4*i+1]);
			EndPoint ep2 = new EndPoint(segs[4*i+2],segs[4*i+3]);
			seg1.start=ep1;ep1.seg=seg1;
			seg1.currstop=ep2;ep2.seg=seg1;
			list.add(seg1);
			seg1.name="seg"+i;
			sl.addSegment(seg1,null);
		}
		
		String segmentNames[] = {"seg7","seg3","seg11","seg2","seg10","seg4","seg9","seg0","seg8","seg6","seg12","seg1","seg13","seg5","seg14"};
		LinkedList<String> segNames = new LinkedList<String>();
		for(int i=0;i<segmentNames.length;i++){
			segNames.add(segmentNames[i]);
		}
		this.assertSweepLine(sl, list, segNames);
	}

	private void assertSweepLine(SweepLine sl, LinkedList<LineSegment> list,LinkedList<String> segNames){
		//System.out.println("Assert Expected segment neighbors "+segNames.toString());
		for(int i=0;i<list.size();i++){
			LineSegment seg = list.get(i);
			int index = segNames.indexOf(seg.name);
			
			String leftCheck=null;
			if((index-1)>=0 )
				leftCheck=segNames.get(index-1);
			
			String rightCheck=null;
			if( (index+1)< list.size() )
				rightCheck=segNames.get(index+1);
			
			String leftName=null;
			LineSegment left = sl.getLeftSegment(seg);
			if(left!=null){
				leftName = left.name;
				assertTrue(leftName.equals(leftCheck));
			}else{
				assertTrue(leftCheck==null);
			}
			
			String rightName=null;
			LineSegment right = sl.getRightSegment(seg);
			if(right!=null){
				rightName = right.name;
				assertTrue(rightName.equals(rightCheck));
			}else{
				assertTrue(rightCheck==null);
			}
		}
	}
}
