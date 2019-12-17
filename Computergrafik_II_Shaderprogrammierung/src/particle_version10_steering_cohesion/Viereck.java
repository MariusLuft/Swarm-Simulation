package particle_version10_steering_cohesion;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glColor3d;

import static org.lwjgl.opengl.GL11.glRectd;

import math.Vektor2D;

public class Viereck extends BasisObjekt {

	
	Vektor2D point1;
	Vektor2D point2 = new Vektor2D(0,0);
	int length;
	int height;
	private float r, g, b;
	public ObjektManager objektManager;

	

	public Viereck(Vektor2D position1, int length, int height) {
		this.r = 0.7f;
		this.g = 0.7f;
		this.b = 0.7f;
		point1 = position1;
		point2.x = point1.x+length;
		point2.y = point1.y+height;
		this.length = length;
		this.height = height;
	}

	public void setObjektManager(ObjektManager objektManager) {
		this.objektManager = objektManager;
	}

	public void render(float r, float g, float b) {
		glColor3d(r, g, b);
		glRectd(point1.x, point1.y, point2.x, point2.y);
	}
	
	public void move(double move_x, double move_y) {
		point1.x += move_x;
		point1.y += move_y;
		point2.x = point1.x+length;
		point2.y = point1.y+height;
	}
}
