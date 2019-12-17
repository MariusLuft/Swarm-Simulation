package particle_version10_steering_cohesion;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUseProgram;
import org.lwjgl.input.Mouse;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import math.Vektor2D;
import bur_kapitel01.POGL;
import bur_kapitel02.ShaderUtilities;

import org.lwjgl.opengl.Display;

import bur_kapitel01.LWJGLBasisFenster;

public class SchwarmShaderVersuch extends LWJGLBasisFenster {
	private ObjektManager flummies;
	float r = 0.15f;
	float g = 0.15f;
	float b = 0.15f;
	float Qr = 0.6f;
	float Qg = 0.4f;
	float Qb = 0.8f;
	int i = 0;
	int schrift_speed_x = 1;
	int schrift_speed_y = 1;
	
	Viereck vier[] = new Viereck[7];

	boolean directionR = false;
	boolean directionG = false;
	boolean directionB = false;
	boolean QdirectionR = false;
	boolean QdirectionG = false;
	boolean QdirectionB = false;
	static int width = 1600;
	static int heigth = 900;

	private int uniform_fragShader_r, uniform_fragShader_g, uniform_fragShader_b;
	
	
	private static String vertexShaderSource = ""
			+ "void main() {"
			+ "   gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;" 
			+ "}";

		private static String fragShaderSource = ""
			+ "void main() { " 
			+ "float r = 0.15f;"
			+ "float g = 0.15f;"
			+ "float b = 0.15f;"
			+ "   gl_FragColor = vec4(r, g, b, 1); "
			+ "}";
	
	public SchwarmShaderVersuch() {
		super("CG2 Beleg - Partikelsimulation", width, heigth);
		initDisplay();
		flummies = ObjektManager.getExemplar();
		erzeugeVierecke();
		erzeugeFlummies(650);

	}

	private void erzeugeFlummies(int anz) {

		Random rand = ThreadLocalRandom.current();
		for (int i = 0; i < anz; i++) {
			Flummi flummi = new Flummi(new Vektor2D(rand.nextInt(width), rand.nextInt(heigth)),
					new Vektor2D(0, rand.nextFloat() + 1), rand.nextInt(4) + 1, r, g, b);
			for (int h = 0; h < 7; h++) {
				if (flummi.position.x <= vier[h].point2.x && flummi.position.x >= vier[h].point1.x
						&& flummi.position.y <= vier[h].point2.y && flummi.position.y >= vier[h].point1.y) {
					flummi.position.x += 300;
				}
			}
			if (flummi.position.x >= width) {
				flummi.position.x -= 1600;
			}
			flummi.setVerhalten(new VerhaltenSchwarm(flummi));
			flummi.setObjektManager(flummies);
			flummies.registriereFlummi(flummi);
		}
	}

	private void erzeugeVierecke() {
		Random rand = ThreadLocalRandom.current();
		vier[0] = new Viereck(new Vektor2D(rand.nextInt(width - 550) + 50, rand.nextInt(heigth - 400)), 150, 50);
		vier[1] = new Viereck(new Vektor2D(vier[0].point1.x - 50, vier[0].point1.y), 50, 350);
		vier[2] = new Viereck(new Vektor2D(vier[0].point1.x, vier[0].point1.y + 300), 150, 50);

		vier[3] = new Viereck(new Vektor2D(vier[0].point1.x + 300, vier[0].point1.y), 150, 50);
		vier[4] = new Viereck(new Vektor2D(vier[3].point1.x - 50, vier[3].point1.y), 50, 350);
		vier[5] = new Viereck(new Vektor2D(vier[3].point1.x, vier[3].point1.y + 300), 150, 50);
		vier[6] = new Viereck(new Vektor2D(vier[3].point1.x + 100, vier[3].point1.y + 200), 50, 100);
	}

	private void prepareShader() {
		int myProgram = glCreateProgram();

		int vertShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertShader, vertexShaderSource);
		glCompileShader(vertShader);
		System.out.println(glGetShaderInfoLog(vertShader, 1024));
		glAttachShader(myProgram, vertShader);

		int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragShader, fragShaderSource);
		glCompileShader(fragShader);
		System.out.println(glGetShaderInfoLog(fragShader, 1024));
		glAttachShader(myProgram, fragShader);

		glLinkProgram(myProgram);
		uniform_fragShader_r = glGetUniformLocation(myProgram, "r");
		uniform_fragShader_g = glGetUniformLocation(myProgram, "g");
		uniform_fragShader_b = glGetUniformLocation(myProgram, "b");
		glUseProgram(myProgram);
		
		glUniform1f(uniform_fragShader_r, r);
		glUniform1f(uniform_fragShader_g, g);
		glUniform1f(uniform_fragShader_b, b);
		
		ShaderUtilities.testShaderProgram(myProgram);
	}
	
	@Override
	public void renderLoop() {
		prepareShader();
		
		glEnable(GL_DEPTH_TEST);
		while (!Display.isCloseRequested()) {
			glClearColor(0.0f, 0.0f, 0.0f, 1);
			glClear(GL_COLOR_BUFFER_BIT);
			
			//Shader
			POGL.setBackGroundColorClearDepth(0.0f, 0.0f, 0.0f);

			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			double re = 600 * 1. / 800;
			glFrustum(-1, 1, -re, re, 1, 20);
			glMatrixMode(GL_MODELVIEW);

			glLoadIdentity();

			
			// ist ja 2d
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, width, heigth, 0, 0, 1);
			glMatrixMode(GL_MODELVIEW);
			glDisable(GL_DEPTH_TEST);

			// change flummi color
			if (!directionR) {
				r += 0.0001f;
				if (r >= 0.95f)
					directionR = true;
			} else {
				r -= 0.0001f;
				if (r <= 0.15f)
					directionR = false;
			}

			if (!directionB) {
				b += 0.0007f;
				if (b >= 0.95f)
					directionB = true;
			} else {
				b -= 0.0007f;
				if (b <= 0.15f)
					directionB = false;
			}

			if (!directionG) {
				g += 0.0003f;
				if (g >= 0.95f)
					directionG = true;
			} else {
				g -= 0.0003f;
				if (g <= 0.15f)
					directionG = false;
			}

			// change rectangle color
			if (!QdirectionR) {
				Qr += 0.001f;
				if (Qr >= 0.95f)
					QdirectionR = true;
			} else {
				Qr -= 0.001f;
				if (Qr <= 0.15f)
					QdirectionR = false;
			}

			if (!QdirectionB) {
				Qb += 0.007f;
				if (Qb >= 0.95f)
					directionB = true;
			} else {
				Qb -= 0.007f;
				if (Qb <= 0.15f)
					QdirectionB = false;
			}

			if (!QdirectionG) {
				Qg += 0.003f;
				if (Qg >= 0.95f)
					QdirectionG = true;
			} else {
				Qg -= 0.003f;
				if (Qg <= 0.15f)
					QdirectionG = false;
			}

			for (int j = 0; j < 7; j++) {
				vier[j].render(Qr, Qg, Qb);

			}
			// size change
			int m = (int) (Mouse.getDWheel() * 0.009);
			
		    
			vier[0].move(schrift_speed_x,schrift_speed_y);
			vier[1].move(schrift_speed_x,schrift_speed_y);
			vier[2].move(schrift_speed_x,schrift_speed_y);
			vier[3].move(schrift_speed_x,schrift_speed_y);
			vier[4].move(schrift_speed_x,schrift_speed_y);
			vier[5].move(schrift_speed_x,schrift_speed_y);
			vier[6].move(schrift_speed_x,schrift_speed_y);
		
			if(vier[0].point1.x >= width-450 || vier[0].point1.x <= 50) {
				schrift_speed_x *= -1;
			}
			if(vier[0].point1.y >= heigth-350 || vier[0].point1.y <= 0) {
				schrift_speed_y *= -1;
			}
			
			// bouncing
			for (int k = 1; k <= flummies.getFlummiSize(); k++) {
				Flummi aktFlummi = flummies.getFlummi(k);
				if (aktFlummi.position.x <= 0 || aktFlummi.position.x >= width)
					aktFlummi.velocity.x *= -1;
				if (aktFlummi.position.y <= 0 || aktFlummi.position.y >= heigth)
					aktFlummi.velocity.y *= -1;
								
				for (int h = 0; h < 7; h++) {
					// Flummies prallen an Vierecken ab
					if (aktFlummi.position.x <= vier[h].point2.x && aktFlummi.position.x >= vier[h].point1.x
							&& aktFlummi.position.y <= vier[h].point2.y && aktFlummi.position.y >= vier[h].point1.y) {
						aktFlummi.position.x -= (aktFlummi.velocity.x);
						aktFlummi.position.y -= (aktFlummi.velocity.y);
						aktFlummi.velocity.x *= -1;
						aktFlummi.velocity.y *= -1;
						
						if (aktFlummi.position.x <= vier[h].point2.x && aktFlummi.position.x >= vier[h].point1.x
								&& aktFlummi.position.y <= vier[h].point2.y && aktFlummi.position.y >= vier[h].point1.y) {
							aktFlummi.position.x += 50;
						}
						if (aktFlummi.position.x > width) {
							aktFlummi.position.x -= width-1;
						}
						if (aktFlummi.position.y > heigth) {
							aktFlummi.position.y -= heigth-1;
						}
						if (aktFlummi.position.x < 0) {
							aktFlummi.position.x += width-1;
						}
						if (aktFlummi.position.y < 0) {
							aktFlummi.position.y += heigth-1;
						}
					}
				}
				
				

				/* Mausrad bewegt hoch */
				// partikelgröße änder
				if (aktFlummi.radius + m <= aktFlummi.startradius + 15
						&& aktFlummi.radius + m >= aktFlummi.startradius - 15)
					aktFlummi.radius += m;

				aktFlummi.render(r, g, b);
				
				aktFlummi.update();
			}
			POGL.renderViereck();
			Display.update();
		}
	}

	//public static void main(String[] args) {
	//	new Schwarm().start();
	//}
}
