package particle_version10_steering_cohesion;

import org.lwjgl.input.Mouse;

import math.LineareAlgebra;
import math.Vektor2D;

public class VerhaltenSchwarm implements Verhalten {
	private Flummi flummi;
	private Steuerungsverhalten steering;
	private float MAX_VELOCITY = 5;
	private final float REAL_MAX_VELOCITY = 14;
	float slow = 1.0f;
	float factor1 = 1.0f;
	float factor2 = 1.0f;
	float factor3 = 1.0f;
	

	public VerhaltenSchwarm(Flummi flummi) {
		this.flummi = flummi;
		this.steering = new Steuerungsverhalten();
	}
//sdf
	@Override
	public void update() {
		if(Mouse.isButtonDown(0)) {
			Vektor2D mausForce = steering.followMousePosition(flummi.position);
			mausForce.mult(0.8);
			steering.applyForce(mausForce);
			
			if(factor1 > 0.1f)
				factor1 -= 0.0001f;
			if(factor2 > 0.2f)
				factor2 -= 0.0001f;
			if(factor3 > 0.3f)
				factor3 -= 0.0001f;
			
			Vektor2D separationForce = steering.separation(flummi, 10);
			separationForce.mult(1.0f * factor1);
			steering.applyForce(separationForce);
			Vektor2D alignmentForce = steering.alignment(flummi, 15);
			alignmentForce.mult(0.12f * factor2);
			steering.applyForce(alignmentForce);
			
			Vektor2D cohesionForce = steering.cohesion(flummi, 15);
			cohesionForce.mult(0.01 * factor3);
			steering.applyForce(cohesionForce);
			
			if(MAX_VELOCITY <= REAL_MAX_VELOCITY)
				MAX_VELOCITY += 0.05f;
			
			flummi.velocity.add(steering.acceleration);
			flummi.velocity.truncate(MAX_VELOCITY);
			flummi.position.add(flummi.velocity);
	
			steering.resetAcceleration();
		}		
	
		else {			
			
			factor1 = 0.0f;
			factor2 = 0.0f;
			factor3 = 0.0f;
			
			Vektor2D separationForce = steering.separation(flummi, 5);
			separationForce.mult(0.7);
			steering.applyForce(separationForce);
			Vektor2D alignmentForce = steering.alignment(flummi, 5);
			alignmentForce.mult(0.03);
			steering.applyForce(alignmentForce);
			
			Vektor2D cohesionForce = steering.cohesion(flummi, 9);
			cohesionForce.mult(0.01);
			steering.applyForce(cohesionForce);
			
			if(MAX_VELOCITY >= 3)
				MAX_VELOCITY /= 1.01f ;
			
			flummi.velocity.add(steering.acceleration);
			flummi.velocity.truncate(MAX_VELOCITY);
			flummi.position.add(flummi.velocity);
	
			steering.resetAcceleration();
		}
		
		
	}
}
