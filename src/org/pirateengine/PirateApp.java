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
import org.pirateengine.testing.TestObject;

/**
 * Piraten Basis Klasse :P
 * @author Morph <admin@mds-tv.de>
 *
 */
public abstract class PirateApp extends RenderWindow {
	private final String appName;
	private final ObjectManager objectManager;
	
	private Clock deltaClock;
	public Time lastDelta = org.jsfml.system.Time.ZERO;
	
	// Debug Objects
	private Text deltaInfo = new Text();
	private TestObject testObject = new TestObject();
	
	public PirateApp(String appName) {
		// Als erstes brauchen wir unseren Applikations ... namen ... Klingt komisch <.<
		this.appName = appName;
		
		// Hier initialisieren wir unseren Objekt Manager.
		// Er wird unsere Objekte verwalten und die Daten �ber sie bereit stellen.
		this.objectManager = new ObjectManager(this);
		
		// Hinzuf�gen des Test Objektes
		this.objectManager.registerObject(testObject);
		
		// JSFML muss zum Schluss initialisiert werden
		this.initJSFML();
	}
	
	/**
	 * Initialisierung vom JSFML
	 */
	private final void initJSFML() {
		// Bevor wir unser Fenster anzeigen k�nnen, muss die maximale FPS Rate
		// feststehen ... Warum komme ich erst jetzt darauf!? -.-" Oder doch nicht?
		setFramerateLimit(30);
		
		Font font = new Font();
		try {
			// This is case-sensitive on linux systems!
			font.loadFromStream(getClass().getResourceAsStream("/org/pirateengine/res/ARIAL.TTF"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.deltaInfo.setColor(Color.BLUE);
		this.deltaInfo.setFont(font);
		this.deltaInfo.setPosition(10.0f, 10.0f);

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
		 * Wenn wir mit Anti-Aliasing (schweres "Wort") arbeiten wollen, brauchen
		 * wir die ContextSettings. Wir sprechen hier direkt den OpenGL Kontext an.
		 */
		ContextSettings context = new ContextSettings(16);
		
		// Hier erstellen wir unser eigentliches Fenster (F�r das Zeichnen der
		// Objekte usw. - halt ein OpenGL Window ^^)
		create(new VideoMode(800, 600), appName, RenderWindow.DEFAULT, context);
		
		this.deltaClock = new Clock();
		while (isOpen()) {
			// UPDATE STUFF
			
			clear(Color.BLACK);
			
			// RENDER STUFF
			// TODO Gibt er hier eine performantere Variante?
			Iterator<PirateObject> objects = this.objectManager.getObjects().iterator();
			while (objects.hasNext()) {
				final PirateObject currentObject = objects.next();
				
			}
			
			// Zuletzt zeichnen wir den Delta Wert, damit dieser wie ein Overlay
			// immer oben liegt. (Overlay -> Oben liegt ... Mja^^)
			draw(deltaInfo);
			
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
			
			// Nach den Events wird der Garbage Collector des Object Managers
			// aufgerufen, um tote Objekte zu ... eliminieren! :O
			this.objectManager.garbage();
			
			// Wenn alles erledigt ist, starten wir unseren Delta Timer neu und
			// speichern die Zeit, die er f�r alles gebraucht hat, ab.
			this.lastDelta = this.deltaClock.restart();
			
			this.deltaInfo.setString("Delta: " + String.valueOf(this.lastDelta.asSeconds()));
		}
	}
}
