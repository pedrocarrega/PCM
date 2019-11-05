package compareNBody;

import java.util.Random;

public class NBody {

	public double x;
	public double y;
	public double z;
	public double vx;
	public double vy;
	public double vz;
	public double mass;

	public NBody(Random r) {
		x = r.nextDouble();
		y = r.nextDouble();
		z = r.nextDouble();
		vx = r.nextDouble();
		vy = r.nextDouble();
		vz = r.nextDouble();
		mass = r.nextDouble();

	}

	NBody offsetMomentum(double px, double py, double pz) {
		vx = -px / NBodySystem.SOLAR_MASS;
		vy = -py / NBodySystem.SOLAR_MASS;
		vz = -pz / NBodySystem.SOLAR_MASS;
		return this;
	}
	
	public boolean equals(NBody body) {
		
		if(this.x != body.x)
			return false;
		if(this.y != body.y)
			return false;
		if(this.z != body.z)
			return false;
		if(this.vx != body.vx)
			return false;
		if(this.vy != body.vy)
			return false;
		if(this.vz != body.vz)
			return false;
		if(this.mass != body.mass)
			return false;
		
		return true;
	}

}
