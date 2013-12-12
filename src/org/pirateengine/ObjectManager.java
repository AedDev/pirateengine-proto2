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

	private List<PirateObject> pirateObjects = new ArrayList<>();
	private List<Integer> destroyedObjects = new ArrayList<>();
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
	 * Sofern der Schlüssel des Objektes bereits registriert ist wird eine
	 * {@link PirateException} geworfen.
	 * 
	 * @param object
	 *            Das zu registrierende {@link PirateObject}
	 * @throws PirateException
	 */
	public void registerObject(PirateObject object) {
		// Das Zielobjekt darf nicht null sein
		if (object == null) {
			throw new NullPointerException("Cannot register 'null'");
		}

		// Wenn keine der vorherigen Fälle eingetreten ist, können wir das
		// Objekt
		// registrieren.
		object.objectManager = this;
		object.app = this.app;

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
		Integer id = this.getID(object);
		this.destroyObject(id);
	}

	/**
	 * Markiert das Objekt mit der gegebenen ID als zerstört. Es wird beim
	 * nächsten Durchlauf des Garbage Collectors des {@link ObjectManager}s
	 * zerstört.
	 * 
	 * @param id
	 *            Die ID des zu zerstörenden {@link PirateObject}s.
	 */
	public void destroyObject(Integer id) {
		this.destroyedObjects.add(id);
	}

	/**
	 * Liefert die ID eines {@link PirateObject}s. Achtung, diese Methode sollte
	 * nach Möglichkeit umgangen werden, da sie bei größeren Applikationen viel
	 * Rechenleistung beanspruchen kann.
	 * 
	 * @param object
	 *            Das zu suchende {@link PirateObject}
	 * @return Die ID des Objektes, oder <code>null</code>, wenn es nicht
	 *         gefunden wurde
	 */
	public Integer getID(PirateObject object) {
		for (int index = 0; index < this.pirateObjects.size(); index++) {
			if (this.pirateObjects.get(index).equals(object)) {
				return index;
			}
		}

		return null;
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

		return (this.pirateObjects.get(id) == null);
	}

	/**
	 * Räumt den {@link ObjectManager} auf, indem alle als zerstört markierten
	 * Objekte entfernt werden.
	 */
	public void garbage() {
		Iterator<Integer> toKill = this.destroyedObjects.iterator();

		// Entfernen der nicht mehr existenten IDs
		while (toKill.hasNext()) {
			Integer index = toKill.next();

			this.pirateObjects.set(index, null);
			this.destroyedObjects.remove(index);
		}

		// Entfernen der nicht mehr existenten IDs aus den Tag Listen
		Iterator<String> tags = this.objectTags.keySet().iterator();
		while (tags.hasNext()) {
			String currentTag = tags.next();

			Iterator<Integer> ids = this.objectTags.get(currentTag).iterator();
			while (ids.hasNext()) {
				Integer currentID = ids.next();
				if (isDestroyed(currentID)) {
					ids.remove();
				}
			}
		}
	}
}
