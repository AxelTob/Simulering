import java.util.*;
import java.io.*;

//Denna klass �rver Proc, det g�r att man kan anv�nda time och signalnamn utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation 

class Gen extends Proc{
	public Proc sendTo;    //Anger till vilken process de genererade kunderna ska skickas //Where to send customers
	public double probability;
	public double lambda; 

	Random random;
	public Gen(double probability){
		this.probability = probability;
		lambda = 5;
		sendTo = new QS();
		random = new Random();
	}

	public void TreatSignal(Signal x){
		switch (x.signalType){
			case READY:{
				SignalList.SendSignal(ARRIVAL, sendTo, time, generate_special());

				//Borde ändra här. Special boolean blir ej snyggt
				SignalList.SendSignal(READY, this, time + expo(lambda), true); //anropar sig själv

			}
				break;
		}
	}
	private boolean generate_special(){
        double r = random.nextDouble();
        if (r > probability){
            return false;
        }
        return false;
    }
}