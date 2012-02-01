package raven.testing.collision.endpointqueue;

import java.util.Random;

import junit.framework.TestCase;

import org.junit.Test;

import raven.collision.EndPoint;
import raven.collision.datastructure.EndPointQueue;

public class EndPointQueueTest extends TestCase{
	
	@Test
	public void testInsertSequence(){
		Random rand = new Random(555);
		EndPointQueue epq = new EndPointQueue();
		//System.out.println("***************Create Fake End points************");
		for(int i=0;i<20;i++){
			double x = 200*rand.nextDouble();
			double y = 200*rand.nextDouble();
			EndPoint ep = new EndPoint();
			ep.x=(int)x;
			ep.y=(int)y;
			//System.out.println("Create Node "+i+":"+ep.x+","+ep.y+" ");
			epq.addEndPoint(ep);
		}
		for(int i=0;i<20;i++){
			EndPoint ep=epq.pop();
			//System.out.println("Popped "+ep.x+","+ep.y+" ");
			if(i==0)
				assertEquals("First Pop should be ",67.0,ep.x);
			if(i==7){
				assertEquals("Eight Pop should be ",78.0,ep.x);
				assertEquals("Eight Pop should be ",58.0,ep.y);
			}
		}
	}

}
