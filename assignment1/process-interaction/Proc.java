import java.util.Random;

// This is an abstract class which all classes that are used for defining real 
// process types inherit. The puropse is to make sure that they all define the 
// method treatSignal, which is needed in the main program.

public abstract class Proc extends Global{
	
	public abstract void TreatSignal(Signal x);

	public double expo(double lambda) {
		Random random = new Random();
		return  Math.log(1-random.nextDouble())/(-lambda);
	}
}

