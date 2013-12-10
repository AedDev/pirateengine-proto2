package org.pirateengine.testing;

import org.pirateengine.PirateApp;

public class MainTest extends PirateApp {
	public static final String APP_NAME = "Pirate Test";
	
	public MainTest(String appName) {
		super(appName);
	}

	public static void main(String[] args) {
		new MainTest(APP_NAME);
	}
}
