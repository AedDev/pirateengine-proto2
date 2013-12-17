package org.pirateengine.testing;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.pirateengine.PirateApp;

public class MainTest extends PirateApp {
	public static final String APP_NAME = "Pirate Test";
	
	public MainTest(String appName) {
		super(appName);
	}
	
	@Override
	public void preInit() { }
	
	@Override
	public void postInit() { }
	
	@Override
	public void render(RenderTarget target, RenderStates states, float delta) { }

	public static void main(String[] args) {
		new MainTest(APP_NAME);
	}
}
