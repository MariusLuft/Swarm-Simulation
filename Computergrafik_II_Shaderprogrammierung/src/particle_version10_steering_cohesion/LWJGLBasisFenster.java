package particle_version10_steering_cohesion;

import java.awt.Canvas;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/*
 * Beschreibung:
 * Als Erweiterung kommt hinzu, dass wir ein Canvas-Objekt zum zeichnen
 * übergeben können. Dazu müssen wir den init-Teil selbst aktiv im Konstruktor
 * ausführen und start() auf den renderLoop() und das Abmelden reduzieren.
 */
public abstract class LWJGLBasisFenster {
   private int Width, Height;
   private String TITLE;
   
   public LWJGLBasisFenster() {
      this("BasisFenster", 1280, 720);
   }

   public LWJGLBasisFenster(int width, int height) {
      this("BasisFenster", width, height);
   }
   
   public LWJGLBasisFenster(String title, int width, int height) {
      Width  = width;
      Height = height;
      TITLE  = title;
   }
   
   public int getWidth() {
	   return Width;
   }
   
   public int getHeight() {
	   return Height;
   }
   
   public void initDisplay(Canvas c) {
      try {
         Display.setParent(c);
      } catch (LWJGLException e) {
         e.printStackTrace();
      }
      
      initDisplay();
   }
   
   public void initDisplay() {
      try {
         Display.setDisplayMode(new DisplayMode(Width, Height));
         Display.setTitle(TITLE);
         Display.create();
      } catch (LWJGLException e) {
         e.printStackTrace();
      }      
   }
   
   public abstract void renderLoop();
   
   public void start() {     
      renderLoop();
      
      Display.destroy();
      System.exit(0);
   }
}


