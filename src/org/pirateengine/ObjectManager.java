package org.pirateengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsfml.system.Vector2f;

/**
 * Verwaltet die Objekte (was sonst ... ^^)
 * 
 * @author Morph <admin@mds-tv.de>
 * 
 */
public final class ObjectManager {
	private PirateApp app;

	private HashMap<String, PirateObject> pirateObjects = new HashMap<>();
	private HashMap<String, Vector2f> objectPositions = new HashMap<>();
	private List<String> destroyedObjects = new ArrayList<>();

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
	 * Sofern der Schl�ssel des Objektes bereits registriert ist wird eine
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

		// Das Zielobjekt muss �ber einen Schl�ssel verf�gen, der nicht leer
		// sein
		// darf.
		if (object.getKey() == null || object.getKey().isEmpty()) {
			throw new IllegalArgumentException(
					"Cannot register object without key");
		}

		// Jeder Schl�ssel darf nur einmal registriert werden.
		// (Ob das so gut ist? ...)
		if (this.pirateObjects.containsKey(object.getKey())) {
			throw new PirateException("Cannot add object '" + object.getKey()
					+ "'. This key is already registered.");
		}

		// Wenn keine der vorherigen F�lle eingetreten ist, k�nnen wir das
		// Objekt
		// registrieren.
		object.objectManager = this;
		object.app = this.app;
		this.pirateObjects.put(object.getKey(), object);
	}

	/**
	 * Gibt ein {@link PirateObject} anhand seines Schl�ssels zur�ck
	 * 
	 * @param key
	 * @return
	 */
	public PirateObject getObject(String key) {
		return this.pirateObjects.get(key);
	}

	/**
	 * Erzeugt ein Array aus den aktuell registrierten {@link PirateObject}s und
	 * gibt diesen zur�ck.
	 * 
	 * @return
	 */
	public PirateObject[] getObjects() {
		PirateObject[] objects = new PirateObject[this.pirateObjects.values()
				.size()];
		this.pirateObjects.values().toArray(objects);

		return objects;
	}

	/**
	 * Gibt die Position des Objektes mit dem gegebenen Schl�ssel zur�ck.
	 * 
	 * @param key
	 *            Der Schl�ssel des Objektes
	 * @return Die Position des Objektes
	 */
	public Vector2f getPosition(String key) {
		return this.objectPositions.get(key);
	}

	/**
	 * Gibt die zugrunde liegende {@link PirateApp} zur�ck.
	 * 
	 * @return Die {@link PirateApp} halt ... -.-"
	 */
	public PirateApp getApp() {
		return this.app;
	}

	/**
	 * Legt den Schl�ssel des Objektes in die Liste der zu zerst�renden Objekte.
	 * 
	 * @param key
	 *            Der Schl�ssel des zu zerst�renden Objektes.
	 */
	public void destroyObject(String key) {
		if (key.isEmpty()) {
			throw new IllegalArgumentException(
					"Cannot destroy an Object without key.");
		}

		this.destroyedObjects.add(key);
	}

	/**
	 * R�umt den {@link ObjectManager} auf, indem alle als zerst�rt markierten
	 * Objekte entfernt werden.
	 */
	public void garbage() {
		for (int index = 0; index < this.destroyedObjects.size(); index++) {
			String key = this.destroyedObjects.get(index);

			this.pirateObjects.remove(key);
			this.objectPositions.remove(key);

			this.destroyedObjects.remove(index);
		}
	}
}
