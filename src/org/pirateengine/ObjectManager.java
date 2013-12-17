package org.pirateengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Verwaltet die Objekte (was sonst ... ^^)
 * 
 * @author Morph <admin@mds-tv.de>
 * 
 */
public final class ObjectManager {
	private final PirateApp app;
	private int idCounter = 0;

	/**
	 * Die {@link PirateObject}s
	 */
	private List<PirateObject> pirateObjects = new ArrayList<>();

	/**
	 * Temporäre Liste der zu zerstörenden Objekte
	 */
	private List<Integer> destroyedObjects = new ArrayList<>();

	/**
	 * Objekt Tags
	 */
	private HashMap<String, List<Integer>> objectTags = new HashMap<String, List<Integer>>();

	/**
	 * Initialisiert den {@link ObjectManager} mit der Referenz zur
	 * {@link PirateApp}
	 * 
	 * @param app
	 *            Die zugrunde liegende {@link PirateApp}
	 */
	public ObjectManager(PirateApp app) {
		this.app = app;
	}

	/**
	 * Registriert ein neues {@link PirateObject} beim {@link ObjectManager}.
	 * 
	 * @param object
	 *            Das zu registrierende {@link PirateObject}
	 */
	public void registerObject(PirateObject object) {
		// Das Zielobjekt darf nicht null sein
		if (object == null) {
			throw new NullPointerException("Cannot register 'null'");
		}

		// Das Objekt wird initialisiert und in die Liste der Objekte
		// aufgenommen
		object.preInit(++idCounter, 0, this, this.app);
		this.pirateObjects.add(object);
	}

	/**
	 * Gibt eine {@link List} mit allen {@link PirateObject}s zurück, die den
	 * gegebenen Tag besitzen.
	 * 
	 * @param tag
	 *            Der Tag, nach dem gesucht werden soll
	 * @return Alle gefundenen {@link PirateObject}s
	 */
	public List<PirateObject> findByTag(String tag) {
		List<PirateObject> objects = new ArrayList<>();
		List<Integer> keys = this.objectTags.get(tag);

		for (int index = 0; index < keys.size(); index++) {
			objects.add(this.pirateObjects.get(index));
		}

		return objects;
	}

	/**
	 * Gibt alle aktuell registrierten {@link PirateObject}s als {@link List}
	 * zurück.
	 * 
	 * @return
	 */
	public List<PirateObject> getObjects() {
		return this.pirateObjects;
	}

	/**
	 * Gibt ein {@link PirateObject} anhand seiner ID zurück. Wenn das Objekt
	 * nicht existiert wird <code>null</code> zurück gegeben.
	 * 
	 * @param objectId
	 *            Die ID des zu suchenden Objektes.
	 * @return Das {@link PirateObject}, welches der gegebenen ID zugeordenet
	 *         ist, oder <code>null</code> wenn das gesuchte Objekt nicht
	 *         existiert.
	 */
	public PirateObject getById(int objectId) {
		Iterator<PirateObject> objects = this.pirateObjects.iterator();

		while (objects.hasNext()) {
			final PirateObject object = objects.next();
			if (object.getId() == objectId) {
				return object;
			}
		}

		return null;
	}

	/**
	 * Gibt die zugrunde liegende {@link PirateApp} zurück.
	 * 
	 * @return Die {@link PirateApp} halt ... -.-"
	 */
	public PirateApp getApp() {
		return this.app;
	}

	/**
	 * Sucht den Index des zu zerstörenden Objektes und markiert das Objekt mit
	 * der gegebenen ID als zerstört. Es wird beim nächsten Durchlauf des
	 * Garbage Collectors des {@link ObjectManager}s zerstört.
	 * 
	 * @param object
	 *            Das zu zerstörende Objekt
	 */
	public void destroyObject(PirateObject object) {
		this.destroyObject(object.getId());
	}

	/**
	 * Markiert das Objekt mit der gegebenen ID als zerstört. Es wird beim
	 * nächsten Durchlauf des Garbage Collectors des {@link ObjectManager}s
	 * zerstört.
	 * 
	 * @param id
	 *            Die ID des zu zerstörenden {@link PirateObject}s.
	 */
	public void destroyObject(int id) {
		this.destroyedObjects.add(id);
	}

	/**
	 * Prüft, ob das angegebene {@link PirateObject} als zerstört definiert
	 * wurde.
	 * 
	 * @param object
	 *            Das zu prüfende {@link PirateObject}.
	 * @return <code>true</code>, wenn es als zerstört definiert ist,
	 *         <code>false</code> wenn nicht.
	 */
	public boolean hasDestroyedFlag(PirateObject object) {
		return this.hasDestroyedFlag(object.getId());
	}

	/**
	 * Prüft, ob das {@link PirateObject} mit der angegebenen ID als zerstört
	 * definiert wurde.
	 * 
	 * @param id
	 *            Die zu prüfende Objekt ID
	 * @return <code>true</code>, wenn es als zerstört definiert ist,
	 *         <code>false</code> wenn nicht.
	 */
	public boolean hasDestroyedFlag(int id) {
		return this.destroyedObjects.contains(id);
	}

	/**
	 * Prüft, ob das {@link PirateObject} mit der angegebenen ID bereits
	 * zerstört wurde.
	 * 
	 * @param id
	 *            Die ID des zu prüfenden {@link PirateObject}
	 * @return <code>true</code>, wenn es zerstört ist, <code>false</code> wenn
	 *         nicht
	 */
	public boolean isDestroyed(Integer id) {
		if (id < 0 || id > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(
					"Cannot destroy an Object with invalid key.");
		}

		return (this.getById(id) == null);
	}

	/**
	 * Führt alle relevanten Updates für den {@link ObjectManager} durch
	 */
	public void update() {
		// Der Garbage Collector des ObjectManagers
		this.garbage();
	}

	/**
	 * Räumt den {@link ObjectManager} auf, indem alle als zerstört markierten
	 * Objekte entfernt werden.
	 */
	private void garbage() {
		Iterator<Integer> toKill = this.destroyedObjects.iterator();

		// Entfernen der nicht mehr existenten IDs
		while (toKill.hasNext()) {
			int id = toKill.next();
			PirateObject objectToKill = this.getById(id);

			this.pirateObjects.remove(objectToKill);
			toKill.remove();
		}

		// Entfernen der nicht mehr existenten IDs aus den Tag Listen
		Iterator<String> tags = this.objectTags.keySet().iterator();
		while (tags.hasNext()) {
			String currentTag = tags.next();

			Iterator<Integer> ids = this.objectTags.get(currentTag).iterator();
			while (ids.hasNext()) {
				Integer currentID = ids.next();
				if (hasDestroyedFlag(currentID)) {
					ids.remove();
				}
			}
		}
	}

	/**
	 * Gibt den aktuellen Stand des ID Counters zurück. Dies dient nur zu Debug
	 * Zwecken.
	 * 
	 * @return Den aktuellen Stand des ID Counters.
	 */
	public int getIdCounter() {
		return this.idCounter;
	}
}
