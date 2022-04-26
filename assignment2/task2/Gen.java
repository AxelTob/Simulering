
import java.io.*;
import java.util.Random;

//Denna klass �rver Proc, det g�r att man kan anv�nda time och signalnamn utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation 

class Gen extends Proc{
	public Proc sendTo;    //Anger till vilken process de genererade kunderna ska skickas //Where to send customers
	public int generatedPeople = 0;
    public int mean = 15; // 15 minutes / arrival. (4 per hour)

	Random random;
	public Gen(long seed){
		random = new Random(seed);
	}

	public void TreatSignal(Signal x){
		switch (x.signalType){
			case GENERATE:{
                
                generatedPeople++;
                SignalList.SendSignal(ARRIVAL, sendTo, time);

                // call back
                SignalList.SendSignal(GENERATE, this, time + getPoissonRandom(mean)); // 4 peps / h
                
			}
				break;
        }
	}

    private static int getPoissonRandom(double mean) {
        Random r = new Random();
        double L = Math.exp(-mean);
        int k = 0;
        double p = 1.0;
        do {
            p = p * r.nextDouble();
            k++;
        } while (p > L);
        return k - 1;
    }
}