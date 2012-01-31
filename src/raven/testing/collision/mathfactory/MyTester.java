package raven.testing.collision.mathfactory;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class MyTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Result result = JUnitCore.runClasses(MathFactoryTest.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}


	}

}
