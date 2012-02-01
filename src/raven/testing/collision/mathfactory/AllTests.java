package raven.testing.collision.mathfactory;

import raven.testing.collision.endpointqueue.EndPointQueueTest;
import raven.testing.collision.sweepline.SweepLineTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for raven.testing.collision.mathfactory");
		//$JUnit-BEGIN$
		suite.addTestSuite(MathFactoryTest.class);
		suite.addTestSuite(EndPointQueueTest.class);
		suite.addTestSuite(SweepLineTest.class);
		//$JUnit-END$
		return suite;
	}

}
