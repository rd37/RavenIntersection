package raven.testing.collision.mathfactory;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllMathFactoryTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for raven.testing.collision.mathfactory");
		//$JUnit-BEGIN$
		suite.addTestSuite(MathFactoryTest2.class);
		suite.addTestSuite(MathFactoryTest.class);
		//$JUnit-END$
		return suite;
	}

}
