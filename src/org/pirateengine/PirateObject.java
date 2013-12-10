package org.pirateengine;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

/**
 * Repr�sentiert ein zeichenbares Objekt. Diese Klasse muss geerbt werden.
 * 
 * @author Morph <admin@mds-tv.de>
 * 
 */
public abstract class PirateObject implements Drawable {
	/**
	 * Der Schl�ssel des Objektes
	 */
	protected String key;

	/**
	 * Referenz zum Objekt Manager. Sie dient dazu die tats�chlichen Daten �ber
	 * das Objekt abrufen zu k�nnen.
	 */
	protected ObjectManager objectManager;

	/**
	 * Referenz zur zugrunde liegenden {@link PirateApp}
	 */
	protected PirateApp app;

	/**
	 * Gibt den Schl�ssel dieses Objektes zur�ck
	 * 
	 * @return Der Schl�ssel dieses Objektes.
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * Setzt den Schl�ssel f�r dieses Objekt. Dieser sollte nur einmal gesetzt
	 * werden!
	 * 
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Gibt die Position dieses Objektes zur�ck. Wenn dieses Objekt nicht beim
	 * {@link ObjectManager} registriert ist, wird eine {@link PirateException}
	 * geworfen.
	 * 
	 * @return Die Position des Objektes als {@link Vector2f}
	 */
	public Vector2f getPosition() {
		if (this.objectManager == null) {
			throw new PirateException("The object '" + key
					+ "' was not registered to the ObjectManager.");
		}

		return this.objectManager.getPosition(this.key);
	}

	/**
	 * Zerst�rt dieses Objekt. Es wird im {@link ObjectManager} der Schl�ssel
	 * dieses Objektes hinterlegt, damit er wei�, dass er dieses zerst�ren soll.
	 */
	public void destroy() {
		this.objectManager.destroyObject(this.key);
	}

	@Override
	public final void draw(RenderTarget target, RenderStates states) {
		this.render(target, states, this.app.lastDelta.asSeconds());
	}

	/**
	 * Dient als Aufsatz auf die draw() Methode von JSFML und liefert noch den
	 * Delta Wert des letzten Frames mit.
	 * 
	 * @param target 
	 * @param states 
	 * @param delta Der Delta Wert des letzten Frames
	 */
	public abstract void render(RenderTarget target, RenderStates states,
			float delta);
}
