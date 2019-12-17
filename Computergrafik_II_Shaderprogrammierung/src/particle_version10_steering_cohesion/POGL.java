package particle_version10_steering_cohesion;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glVertex3d;
import static org.lwjgl.opengl.GL11.glVertex3f;

import math.LineareAlgebra;
import math.Vektor2D;
import math.Weg2DDynamisch;

// POGL = "Primitives of OpenGL" 
public class POGL {
	private POGL() {}

	private static Torus torus;

	public static void clearBackgroundWithColor(float r, float g, float b, float a) {
		glClearColor(r, g, b, a);
		glClear(GL_COLOR_BUFFER_BIT);
	}

	public static void setBackGroundColorClearDepth(float a, float b, float c) {
		glClearColor(a, b, c, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public static void renderKreis(float x, float y, float step, float radius) {
		glBegin(GL_TRIANGLE_FAN);
		glVertex2f(x, y);
		for (int angle = 0; angle < 360; angle += step)
			glVertex2f(x + (float) Math.sin(angle) * radius, y + (float) Math.cos(angle) * radius);
		glEnd();
	}

	public static void renderViereck() {
		glBegin(GL_QUADS);
		glVertex3f(-1, -1, 0);
		glVertex3f(1, -1, 0);
		glVertex3f(1, 1, 0);
		glVertex3f(-1, 1, 0);
		glEnd();
	}

	public static void renderViereckMitTexturbindung() {
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex3f(-1.0f, -1.0f, 0.0f);
		glTexCoord2f(1, 0);
		glVertex3f(1.0f, -1.0f, 0.0f);
		glTexCoord2f(1, 1);
		glVertex3f(1.0f, 1.0f, 0.0f);
		glTexCoord2f(0, 1);
		glVertex3f(-1.0f, 1.0f, 0.0f);
		glEnd();
	}

	public static void renderWuerfel() {
		glBegin(GL_QUADS);
		glVertex3f(-1, -1, -1);
		glVertex3f(1, -1, -1);
		glVertex3f(1, 1, -1);
		glVertex3f(-1, 1, -1);

		glVertex3f(-1, -1, 1);
		glVertex3f(1, -1, 1);
		glVertex3f(1, 1, 1);
		glVertex3f(-1, 1, 1);

		glVertex3f(-1, -1, -1);
		glVertex3f(-1, 1, -1);
		glVertex3f(-1, 1, 1);
		glVertex3f(-1, -1, 1);

		glVertex3f(1, -1, -1);
		glVertex3f(1, 1, -1);
		glVertex3f(1, 1, 1);
		glVertex3f(1, -1, 1);

		glVertex3f(-1, -1, -1);
		glVertex3f(1, -1, -1);
		glVertex3f(1, -1, 1);
		glVertex3f(-1, -1, 1);

		glVertex3f(-1, 1, -1);
		glVertex3f(1, 1, -1);
		glVertex3f(1, 1, 1);
		glVertex3f(-1, 1, 1);
		glEnd();
	}

	public static void renderWehendeFlaecheKonstruktion(double t) {
		glBegin(GL_QUADS);
		double periode = 20, amplitude = 0.03, step = 0.1;
		float r = 0.5f, g = 0.0f, b = 0.0f;
		double bl, br, tr, tl;

		double flag = 1;
		for (double y = -0.5; y < 0.5; y += step) {
			for (double x = -0.5; x < 0.5; x += step) {
				bl = Math.sin((t * 10 + x * y * periode)) * amplitude;
				br = Math.sin((t * 10 + (x + step) * y * periode)) * amplitude;
				tr = Math.sin((t * 10 + (x + step) * (y + step) * periode)) * amplitude;
				tl = Math.sin((t * 10 + x * (y + step) * periode)) * amplitude;

				glColor3d(r + bl + flag * step, g + bl + flag * step, b + bl + flag * step);
				glVertex2d(x + bl, y + bl);
				glColor3d(r + br, g + br, b + br);
				glVertex2d(x + step + br, y + br);
				glColor3d(r + tr, g + tr, b + tr);
				glVertex2d(x + step + tr, y + step + tr);
				glColor3d(r + tl, g + tl, b + tl);
				glVertex2d(x + tl, y + step + tl);

				flag *= -1;
			}
		}
		glEnd();
	}

	public static void renderWehendeFlaeche(double t) {
		glBegin(GL_QUADS);
		double periode = 15, amplitude = 0.03, step = 0.01;
		float r = 0.0f, g = 0.5f, b = 0.0f;
		double bl, br, tl, tr;

		for (double y = -0.5; y < 0.5; y += step) {
			for (double x = -0.5; x < 0.5; x += step) {
				bl = Math.sin((t * 10 + x * y * periode)) * amplitude;
				br = Math.sin((t * 10 + (x + step) * y * periode)) * amplitude;
				tr = Math.sin((t * 10 + (x + step) * (y + step) * periode)) * amplitude;
				tl = Math.sin((t * 10 + x * (y + step) * periode)) * amplitude;

				glColor3d(r + bl, g + bl, b + bl);
				glVertex2d(x + bl, y + bl);
				glColor3d(r + br, g + br, b + br);
				glVertex2d(x + step + br, y + br);
				glColor3d(r + tr, g + tr, b + tr);
				glVertex2d(x + step + tr, y + step + tr);
				glColor3d(r + tl, g + tl, b + tl);
				glVertex2d(x + tl, y + step + tl);
			}
		}
		glEnd();
	}

	public static void createTorus(double x, double y, double z, double r, double thickness, double steps) {
		torus = new Torus(x, y, z, r, thickness, steps);
	}

	public static void renderTorus() {
		if (torus == null)
			createTorus(0, 0, 0, 1, 0.3, 20.0f);

		torus.render();
	}
	
	public static void renderObjectWithPath(float x, float y, float r, float g, float b, float a, int radius, Weg2DDynamisch path) {
		for (int j = path.getSize()-1; j >= 0 ; j--) {
			float anteil = 1-((float)j/path.getSize());
			glColor4f(r*anteil, g*anteil, b*anteil, 1f);
			renderKreis((float)path.getElement(j).x, (float)path.getElement(j).y, 5, radius);
		}

		glColor4f(r, g, b, a);
		renderKreis(x, y, 5, radius);
	}
	
	public static void renderPfeil(float x, float y, int off, float winkel, int size) {
		glLoadIdentity();
		glTranslated(x, y, 0);
		
		glRotatef(winkel, 0, 0, 1);
		glTranslated(off, 0, 0);
		glScaled(size, size, size);
		
		glBegin(GL_LINES);
		glVertex3d(  0f,  0f, 0);
		glVertex3d(-off/15., 0, 0);
		glEnd();

        glBegin(GL_TRIANGLES);
        glVertex3d(  0f,  .2f, 0);
        glVertex3d(  0f, -.2f, 0);
        glVertex3d( .5f,   0f, 0);
        glEnd();    		
	}
	
	public static void renderObjectWithForces(float x, float y, int radius, Vektor2D velocity, Vektor2D acceleration) {
		glLoadIdentity();
		glTranslated(x, y, 0);
		
		glColor4f(1, 1, 1, 1);
		renderKreis(0, 0, 5, radius);
		glColor4f(0, 0, 0, 1);
		renderKreis(0, 0, 5, radius-2);
		
		// *****************************************************************
		// Visualisierung der Geschwindigkeit
		// der Wert off soll die Geschwindigkeit durch einen größeren Abstand visualisieren
		int off = radius + 1 + (int)(velocity.length()/5);
		double winkel = LineareAlgebra.angleDegree(velocity, new Vektor2D(1,0));
		
		// da immer der kleinere Winkel zwischen den Vektoren geliefert wird, müssen
		// wir etwas korrigieren
		if (velocity.y<0)
			winkel = 180 + (180-winkel);

		glColor4f(1, 1, 0, 1);
		renderPfeil(x, y, off, (float)winkel, 15);
		// *****************************************************************
		
		// *****************************************************************
		// Visualisierung der Beschleunigung
		off = radius + 1 + (int)(acceleration.length()/10);
		winkel = LineareAlgebra.angleDegree(acceleration, new Vektor2D(1,0));
		if (acceleration.y<0)
			winkel = 180 + (180-winkel);

		glColor4f(1, 0, 0, 1);
		renderPfeil(x, y, off, (float)winkel, 15);
		// *****************************************************************
	}
	
	public static void renderSwarmObjectWithForces(float x, float y, int radius, Vektor2D velocity, Vektor2D acceleration) {
		glLoadIdentity();
		glTranslated(x, y, 0);
		
		glColor4f(1, 1, 1, 1);
		renderKreis(0, 0, 5, radius);
		glColor4f(0, 0, 0, 1);
		renderKreis(0, 0, 5, radius-2);
		
		// *****************************************************************
		// Visualisierung der Geschwindigkeit
		// der Wert off soll die Geschwindigkeit durch einen größeren Abstand visualisieren
		int off = radius + 1 + (int)(velocity.length()/5);
		double winkel = LineareAlgebra.angleDegree(velocity, new Vektor2D(1,0));
		
		// da immer der kleinere Winkel zwischen den Vektoren geliefert wird, müssen
		// wir etwas korrigieren
		if (velocity.y<0)
			winkel = 180 + (180-winkel);

		glColor4f(1, 1, 0, 1);
		renderPfeil(x, y, off, (float)winkel, 15);
		// *****************************************************************
		
		// *****************************************************************
		// Visualisierung der Beschleunigung
		off = radius + 1 + (int)(acceleration.length()/10);
		winkel = LineareAlgebra.angleDegree(acceleration, new Vektor2D(1,0));
		if (acceleration.y<0)
			winkel = 180 + (180-winkel);

		glColor4f(1, 0, 0, 1);
		renderPfeil(x, y, off, (float)winkel, 15);
		// *****************************************************************
	}
}
