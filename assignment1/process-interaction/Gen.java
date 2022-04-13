import java.util.*;
import java.io.*;

//Denna klass �rver Proc, det g�r att man kan anv�nda time och signalnamn utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation 

class Gen extends Proc{
	public Proc sendTo;    //Anger till vilken process de genererade kunderna ska skickas //Where to send customers
	public double probability;
	public double mean; 
	public int generatedPeople = 0;

	Random random;
	public Gen(int seed, double probability) {
		this.probability = probability;
		random = new Random(seed);
	}

	public void TreatSignal(Signal x){
		switch (x.signalType){
			case GENERATE:{
				generatedPeople++;
				if(generate_special()){
					SignalList.SendSignal(SPEC_ARRIVAL, sendTo, time);
				}
				else{
					SignalList.SendSignal(NORM_ARRIVAL, sendTo, time);
				}

				SignalList.SendSignal(GENERATE, this, time + expo(1/mean, random));
			}
				break;
		}
	}

	private boolean generate_special(){
        double r = random.nextDouble();
        return r < probability;
    }
}
