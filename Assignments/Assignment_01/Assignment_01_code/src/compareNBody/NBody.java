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
	
	public NBody() {}

	NBody offsetMomentum(double px, double py, double pz) {
		vx = -px / NBodySystem.SOLAR_MASS;
		vy = -py / NBodySystem.SOLAR_MASS;
		vz = -pz / NBodySystem.SOLAR_MASS;
		return this;
	}
	
	@Override
	public NBody clone() {
		
		NBody copy = new NBody();
		
		copy.x = this.x;
		copy.y = this.y;
		copy.z = this.z;
		copy.vx = this.vx;
		copy.vy = this.vy;
		copy.vz = this.vz;
		copy.mass = this.mass;

		return copy;
		
	}
	
	public boolean equals(NBody compare) {
		
		if(this.x != compare.x)
			return false;
		if(this.y != compare.y)
			return false;
		if(this.z != compare.z)
			return false;
		if(this.vx != compare.vx)
			return false;
		if(this.vy != compare.vy)
			return false;
		if(this.vz != compare.vz)
			return false;
		if(this.mass != compare.mass)
			return false;
		
		return true;
	}

}
