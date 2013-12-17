package org.pirateengine;

import java.io.IOException;
import java.util.Iterator;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.window.ContextSettings;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;

/**
 * Piraten Basis Klasse :P
 * 
 * @author Morph <admin@mds-tv.de>
 * 
 */
public abstract class PirateApp extends RenderWindow {
	protected final String appName;
	protected final ObjectManager objectManager;

	// Delta Counter
	private Clock deltaClock;
	private Time lastDelta = Time.ZERO;

	// Debug Objects
	private Text deltaInfo = new Text();
	private Text objectIdCounter = new Text();
	private Text objectCount = new Text();
	private Text fpsCountView = new Text();

	// FPS Counter
	private Clock fpsClock;
	private int fpsCount = 0;
	private int lastFps = 0;

	public PirateApp(String appName) {
		// Als erstes brauchen wir unseren Applikations ... namen ... Klingt
		// komisch <.<
		this.appName = appName;

		// Hier initialisieren wir unseren Objekt Manager.
		// Er wird unsere Objekte verwalten und die Daten über sie bereit
		// stellen.
		this.objectManager = new ObjectManager(this);

		this.preInit();

		// JSFML muss zum Schluss initialisiert werden
		this.initJSFML();

		this.postInit();
	}

	/**
	 * Wird VOR der Initialisierung von JSFML aufgerufen.
	 */
	public void preInit() {
	}

	/**
	 * Wird NACH der Initialisierung von JSFML aufgerufen.
	 */
	public void postInit() {
	}

	/**
	 * Definiert, was beim Schließen der Anwendung passieren soll. Diese Methode
	 * kann überschrieben werden.
	 */
	public void onExit() {
		this.close();
	}

	/**
	 * Initialisierung vom JSFML
	 */
	private final void initJSFML() {
		// =============================================================================
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
		this.deltaInfo.setCharacterSize(12);

		this.objectIdCounter.setColor(Color.CYAN);
		this.objectIdCounter.setFont(font);
		this.objectIdCounter.setPosition(10.0f, 25.0f);
		this.objectIdCounter.setCharacterSize(12);

		this.objectCount.setColor(Color.WHITE);
		this.objectCount.setFont(font);
		this.objectCount.setPosition(10.0f, 40.0f);
		this.objectCount.setCharacterSize(12);

		this.fpsCountView.setColor(Color.GREEN);
		this.fpsCountView.setFont(font);
		this.fpsCountView.setPosition(10.0f, 55.0f);
		this.fpsCountView.setCharacterSize(12);
		this.fpsCountView.setString("FPS: 0");
		// =============================================================================

		// Anonymer Thread
		new Thread(new Runnable() {
			@Override
			public void run() {
				appLoop();
			}
		}).start();
	}

	/**
	 * Gibt den letzten Delta Wert als {@link Time} zurück.
	 * 
	 * @return Der letzte Delta Wert
	 */
	public Time getDelta() {
		return this.lastDelta;
	}

	/**
	 * Gibt die letzte gemessene FPS Rate zurück
	 * 
	 * @return Letzte gemessene FPS Rate
	 */
	public int getFPS() {
		return this.lastFps;
	}

	private final void appLoop() {
		/*
		 * Wenn wir mit Anti-Aliasing (schweres "Wort") arbeiten wollen,
		 * brauchen wir die ContextSettings. Wir sprechen hier direkt den OpenGL
		 * Kontext an.
		 */
		ContextSettings context = new ContextSettings();

		// Hier erstellen wir unser eigentliches Fenster (Für das Zeichnen der
		// Objekte usw. - halt ein OpenGL Window ^^)
		create(new VideoMode(800, 600), appName, RenderWindow.DEFAULT, context);

		this.deltaClock = new Clock();
		this.fpsClock = new Clock();
		while (isOpen()) {
			clear(Color.BLACK);

			// RENDER MAIN CLASS
			draw(new Drawable() {
				@Override
				public void draw(RenderTarget target, RenderStates states) {
					render(target, states, getDelta().asSeconds());
				}
			});

			// RENDER STUFF
			long renderTime = System.currentTimeMillis();
			Iterator<PirateObject> objects = this.objectManager.getObjects()
					.iterator();
			while (objects.hasNext()) {
				final PirateObject currentObject = objects.next();
				if (!currentObject.isDestroyed()) {
					draw(currentObject);
				}
			}
			
			System.out.println("Time to render: " + (System.currentTimeMillis() - renderTime) + "ms");

			// TODO DEBUG
			// Zuletzt zeichnen wir den Delta Wert, damit dieser wie ein Overlay
			// immer oben liegt. (Overlay -> Oben liegt ... Mja^^)
			draw(this.deltaInfo);
			draw(this.objectIdCounter);
			draw(this.objectCount);
			draw(this.fpsCountView);

			display();

			// Jetzt darf sich der ObjectManager erstmal neu organisieren
			this.objectManager.update();

			this.deltaInfo.setString("Delta: " + this.lastDelta.asSeconds());
			this.objectIdCounter.setString("Last OID: "
					+ this.objectManager.getIdCounter());
			this.objectCount.setString("Objects: "
					+ this.objectManager.getObjects().size());

			// Hier holen wir uns noch die aktuelle FPS Rate
			if (this.fpsClock.getElapsedTime().asSeconds() >= 1.0f) {
				this.lastFps = this.fpsCount;
				this.fpsCount = 0;

				this.fpsClock.restart();

				this.fpsCountView.setString("FPS: " + this.lastFps);
			} else {
				this.fpsCount++;
			}

			// Wenn alles erledigt ist, starten wir unseren Delta Timer neu und
			// speichern die Zeit, die er für alles gebraucht hat, ab.
			this.lastDelta = this.deltaClock.restart();

			// Hier fangen wir noch das EXIT Event ab, damit das Fenster auch
			// geschlossen werden kann.
			Iterator<Event> events = this.pollEvents().iterator();
			while (events.hasNext()) {
				Event currentEvent = events.next();
				if (currentEvent.type == Type.CLOSED) {
					this.onExit();
				}
			}
		}
	}

	public abstract void render(RenderTarget target, RenderStates states,
			float delta);
}
