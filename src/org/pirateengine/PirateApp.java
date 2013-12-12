package org.pirateengine;

import java.io.IOException;
import java.util.Iterator;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.window.ContextSettings;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

/**
 * Piraten Basis Klasse :P
 * 
 * @author Morph <admin@mds-tv.de>
 * 
 */
public abstract class PirateApp extends RenderWindow {
	protected final String appName;
	protected final ObjectManager objectManager;

	private Clock deltaClock;
	public Time lastDelta = org.jsfml.system.Time.ZERO;

	// Debug Objects
	private Text deltaInfo = new Text();
	private Text objectIdCounter = new Text();
	private Text objectCount = new Text();

	public PirateApp(String appName) {
		// Als erstes brauchen wir unseren Applikations ... namen ... Klingt
		// komisch <.<
		this.appName = appName;

		// Hier initialisieren wir unseren Objekt Manager.
		// Er wird unsere Objekte verwalten und die Daten über sie bereit
		// stellen.
		this.objectManager = new ObjectManager(this);

		// JSFML muss zum Schluss initialisiert werden
		this.initJSFML();
	}

	/**
	 * Initialisierung vom JSFML
	 */
	private final void initJSFML() {
		// Bevor wir unser Fenster anzeigen können, muss die maximale FPS Rate
		// feststehen ... Warum komme ich erst jetzt darauf!? -.-" Oder doch
		// nicht?
		setFramerateLimit(30);

		Font font = new Font();
		try {
			// This is case-sensitive on linux systems!
			font.loadFromStream(getClass().getResourceAsStream(
					"/org/pirateengine/res/ARIAL.TTF"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.deltaInfo.setColor(Color.BLUE);
		this.deltaInfo.setFont(font);
		this.deltaInfo.setPosition(10.0f, 10.0f);

		this.objectIdCounter.setColor(Color.CYAN);
		this.objectIdCounter.setFont(font);
		this.objectIdCounter.setPosition(10.0f, 40.0f);

		this.objectCount.setColor(Color.WHITE);
		this.objectCount.setFont(font);
		this.objectCount.setPosition(10.0f, 70.0f);

		// Anonymer Thread
		new Thread(new Runnable() {
			@Override
			public void run() {
				appLoop();
			}
		}).start();
	}

	private final void appLoop() {
		/*
		 * Wenn wir mit Anti-Aliasing (schweres "Wort") arbeiten wollen,
		 * brauchen wir die ContextSettings. Wir sprechen hier direkt den OpenGL
		 * Kontext an.
		 */
		ContextSettings context = new ContextSettings(16);

		// Hier erstellen wir unser eigentliches Fenster (Für das Zeichnen der
		// Objekte usw. - halt ein OpenGL Window ^^)
		create(new VideoMode(800, 600), appName, RenderWindow.DEFAULT, context);

		this.deltaClock = new Clock();
		while (isOpen()) {
			// UPDATE STUFF

			clear(Color.BLACK);

			// RENDER STUFF
			// TODO Gibt er hier eine performantere Variante?
			Iterator<PirateObject> objects = this.objectManager.getObjects()
					.iterator();
			while (objects.hasNext()) {
				final PirateObject currentObject = objects.next();
				if (!currentObject.isDestroyed()) {
					draw(currentObject);
				}
			}

			// Zuletzt zeichnen wir den Delta Wert, damit dieser wie ein Overlay
			// immer oben liegt. (Overlay -> Oben liegt ... Mja^^)
			draw(this.deltaInfo);
			draw(this.objectIdCounter);
			draw(this.objectCount);

			display();

			// Events
			for (Event evt : pollEvents()) {
				switch (evt.type) {
				case CLOSED: {
					close();
					System.exit(0);
					break;
				}
				default: {
					break;
				}
				}
			}
			
			// Jetzt darf sich der ObjectManager erstmal neu organisieren
			this.objectManager.update();

			this.deltaInfo.setString("Delta: " + this.lastDelta.asSeconds());
			this.objectIdCounter.setString("Last OID: "
					+ this.objectManager.getIdCounter());
			this.objectCount.setString("Objects: "
					+ this.objectManager.getObjects().size());
			
			// Wenn alles erledigt ist, starten wir unseren Delta Timer neu und
			// speichern die Zeit, die er für alles gebraucht hat, ab.
			this.lastDelta = this.deltaClock.restart();
		}
	}
}
