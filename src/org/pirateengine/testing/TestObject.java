package org.pirateengine.testing;

import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.pirateengine.PirateObject;

public class TestObject extends PirateObject {
	private CircleShape circle = new CircleShape(5f);
	private float speed = 200f;
	
	public TestObject() {
		this.circle.setOutlineThickness(0f);
		this.circle.setFillColor(Color.GREEN);
	}
	
	@Override
	public void render(RenderTarget target, RenderStates states, float delta) {
		// Bewegen des Objektes mithilfe des Delta Wertes, um Ruckler zu vermeiden
		this.circle.move(speed * delta, speed * delta);
		
		// Zeichnen des Objektes
		target.draw(circle);
		
		if (this.circle.getPosition().x > this.app.getSize().x
				|| this.circle.getPosition().y > this.app.getSize().y) {
			this.destroy();
		}
	}
}
