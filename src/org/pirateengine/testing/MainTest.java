package org.pirateengine.testing;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;
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
	public void render(RenderTarget target, RenderStates states, float delta) {
		if (Keyboard.isKeyPressed(Key.ESCAPE)) {
			this.close();
		}
		
		if (Keyboard.isKeyPressed(Key.S)) {
			this.objectManager.registerObject(new TestObject());
		}
	}

	public static void main(String[] args) {
		new MainTest(APP_NAME);
	}
}
