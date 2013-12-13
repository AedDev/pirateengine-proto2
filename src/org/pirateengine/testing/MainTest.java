package org.pirateengine.testing;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;
import org.jsfml.window.event.KeyEvent;
import org.pirateengine.PirateApp;

public class MainTest extends PirateApp {
	public static final String APP_NAME = "Pirate Test";
	
	public MainTest(String appName) {
		super(appName);
	}
	
	@Override
	public void preInit() {
		this.setKeyRepeatEnabled(false);
	}
	
	@Override
	public void postInit() {
		
	}
	
	@Override
	public void render(RenderTarget target, RenderStates states, float delta) {
		// Spawned ein Test Objekt
		for (Event evt : this.pollEvents()) {
			if (evt.type == Type.KEY_PRESSED) {
				KeyEvent kevt = evt.asKeyEvent();
				if (kevt.key == Key.SPACE) {
					this.objectManager.registerObject(new TestObject());
				}
			}
		}
	}

	public static void main(String[] args) {
		new MainTest(APP_NAME);
	}
}
