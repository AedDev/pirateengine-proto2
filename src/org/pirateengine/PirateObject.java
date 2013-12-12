package org.pirateengine;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

/**
 * Repräsentiert ein zeichenbares Objekt. Diese Klasse muss geerbt werden.
 * 
 * @author Morph <admin@mds-tv.de>
 * 
 */
public abstract class PirateObject implements Drawable {
	protected int ID;

	/**
	 * Referenz zum Objekt Manager. Sie dient dazu die tatsächlichen Daten über
	 * das Objekt abrufen zu können.
	 */
	protected ObjectManager objectManager;

	/**
	 * Referenz zur zugrunde liegenden {@link PirateApp}
	 */
	protected PirateApp app;

	/**
	 * Zerstört dieses Objekt. Es wird im {@link ObjectManager} der Schlüssel
	 * dieses Objektes hinterlegt, damit er weiß, dass er dieses zerstören soll.
	 * 
	 * Diese Operation sollte nur durchgeführt werden, wenn es notwendig ist, da
	 * sie bei unsachgemäßer Verwendung die Rechenleistung stark beeinträchtigen
	 * kann.
	 */
	public void destroy() {
		this.objectManager.destroyObject(this);
	}

	// TODO Diese Methode kann viel Rechenleistung beanspruchen. Hier sollte
	// eine Lösung gefunden werden.
	/**
	 * Prüft, ob dieses Objekt als zerstört definiert wurde.
	 * 
	 * @return <code>true</code>, wenn es als zerstört definiert wurde,
	 *         <code>false</code> wenn nicht.
	 */
	public boolean isDestroyed() {
		return this.objectManager.isDestroyed(this);
	}

	@Override
	public final void draw(RenderTarget target, RenderStates states) {
		this.render(target, states, this.app.getDelta().asSeconds());
	}

	/**
	 * Dient als Aufsatz auf die draw() Methode von JSFML und liefert noch den
	 * Delta Wert des letzten Frames mit.
	 * 
	 * @param target
	 * @param states
	 * @param delta
	 *            Der Delta Wert des letzten Frames
	 */
	public abstract void render(RenderTarget target, RenderStates states,
			float delta);
}
