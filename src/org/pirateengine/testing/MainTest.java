package org.pirateengine.testing;

import org.pirateengine.PirateApp;

public class MainTest extends PirateApp {
	public static final String APP_NAME = "Pirate Test";
	private TestObject testObject = new TestObject();
	
	public MainTest(String appName) {
		super(appName);
		
		// Hinzufügen des Test Objektes
		this.objectManager.registerObject(testObject);
	}

	public static void main(String[] args) {
		new MainTest(APP_NAME);
	}
}
