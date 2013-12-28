package org.pirateengine;

import java.util.ArrayList;
import java.util.List;

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
	/**
	 * Die eindeutige ID dieses Objektes
	 */
	private int id;

	/**
	 * Die ID des Elternobjektes. 0 bedeutet, dass dieses Objekt keinem Eltern
	 * Objekt zugeordnet ist. (Default)
	 */
	private int parentId = 0;

	/**
	 * Referenz zum Objekt Manager. Sie dient dazu die tatsächlichen Daten über
	 * das Objekt abrufen zu können.
	 */
	private ObjectManager objectManager;

	/**
	 * Referenz zur zugrunde liegenden {@link PirateApp}
	 */
	private PirateApp app;
	
	/**
	 * Liste der {@link PirateModule}s, die an dieses Objekt angehangen sind
	 */
	private List<PirateModule> modules = new ArrayList<>();

	/**
	 * Initialisiert dieses {@link PirateObject} Diese Methode darf nur einmal
	 * vom {@link ObjectManager} aufgerufen werden. Sollte das
	 * {@link PirateObject} schon initialisiert sein, wird eine
	 * {@link PirateException} geworfen.
	 * 
	 * @param id
	 *            Die ID für dieses Objekt
	 * @param parentId
	 *            Die ID des Elternobjektes für dieses Objekt
	 * @param objMgr
	 *            Die Referenz zum globalen {@link ObjectManager}
	 * @param app
	 *            Die Referenz zur Hauptklasse dieser Anwendung
	 */
	public void preInit(int id, int parentId, ObjectManager objMgr,
			PirateApp app) {
		// Wenn der ObjectManager null ist, dann ist dieses Objekt noch nicht
		// initialisiert
		if (this.objectManager == null) {
			this.id = id;
			this.parentId = parentId;
			this.objectManager = objMgr;
			this.app = app;
		} else {
			throw new PirateException("This object was already initialized.");
		}
	}

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

	/**
	 * Prüft, ob dieses Objekt als zerstört definiert wurde.
	 * 
	 * @return <code>true</code>, wenn es als zerstört definiert wurde,
	 *         <code>false</code> wenn nicht.
	 */
	public boolean isDestroyed() {
		return this.objectManager.hasDestroyedFlag(this);
	}

	/**
	 * Gibt die eindeutige ID dieses Objektes zurück.
	 * 
	 * @return Die eindeutige ID dieses Objektes.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Gibt die ID des Elternobjektes zurück. 0 bedeutet, dass dieses Objekt
	 * keinem Eltern Objekt zugeordnet ist. (Default)
	 * 
	 * @return Die ID des Elternobjektes.
	 */
	public int getParentId() {
		return this.parentId;
	}

	/**
	 * Gibt das Elternobjekt zurück, welches diesem Objekt zugeordnet ist.
	 * Sofern dieses Objekt kein Elternobjekt hat, wird <code>null</code>
	 * zurückgegeben.
	 * 
	 * @return Das Elternobjekt, sofern vorhanden.
	 */
	public PirateObject getParentObject() {
		return this.objectManager.getById(this.id);
	}

	/**
	 * Setzt die ID des Elternobjektes für dieses Objekt neu.
	 * 
	 * @param id
	 *            Die ID des Elternobjektes.
	 */
	public void setParentId(int id) {
		this.parentId = id;
	}

	/**
	 * Gibt die Instanz des globalen {@link ObjectManager} zurück.
	 * 
	 * @return Der {@link ObjectManager}.
	 */
	public ObjectManager getObjectManager() {
		return this.objectManager;
	}

	/**
	 * Gibt die Referenz zur Hauptklasse dieser Anwendung zurück.
	 * 
	 * @return Referenz zur Hauptklasse dieser Anwendung.
	 */
	public PirateApp getApp() {
		return this.app;
	}
	
	/**
	 * 
	 * @param type
	 */
	public void getModule(Class<PirateModule> type) {
		
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

	/**
	 * Wenn dieses {@link PirateObject} mit einem anderen kollidierbaren
	 * {@link PirateObject} kollidiert, wird diese Methode aufgrunfen.
	 * 
	 * @param other
	 *            Das {@link PirateObject}, mit welchem die Kollision
	 *            stattgefunden hat.
	 */
	public void onCollision(PirateObject other) {
	}
}
