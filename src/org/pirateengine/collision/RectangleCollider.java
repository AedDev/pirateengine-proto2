package org.pirateengine.collision;

import org.jsfml.graphics.FloatRect;

/**
 * Beschreibt eine Rechteckige Kollisionszone
 * 
 * @author Morph <admin@mds-tv.de>
 * 
 */
public class RectangleCollider implements Collidable {
	private FloatRect collisionZone = FloatRect.EMPTY;
	
	public void setCollisionZone(FloatRect zone) {
		if (zone != null) {
			this.collisionZone = zone;
		} else {
			throw new NullPointerException("The collision zone cannot be null.");
		}
	}
	
	public FloatRect getCollisionZone() {
		return this.collisionZone;
	}
}
