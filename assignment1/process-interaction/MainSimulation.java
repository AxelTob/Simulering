import java.util.*;
import java.io.*;

//Denna klass �rver Global s� att man kan anv�nda time och signalnamnen utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation


public class MainSimulation extends Global{
	static class SimulationResult {
		double meanNumNormal;
		double meanNumSpecial;
		double meanTimeNormal;
		double meanTimeSpecial;
	}

    public static void main(String[] args) throws IOException {
		System.out.println("numNormal\tnumSpecial\ttimeNormal\ttimeSpecial");
		for (int i = 0; i <= 100; ++i) {
			var result = runSimulation((double)(i)/100);
    	
			System.out.printf("%fst\t%fst\t%fmin\t%fmin\t%f\n",
					result.meanNumNormal,
					result.meanNumSpecial,
					result.meanTimeNormal,
					result.meanTimeSpecial,
					result.meanNumNormal + result.meanNumSpecial);		
		}
	
    }

	private static SimulationResult runSimulation(double probability) {
    	new SignalList();
		time = 0.0;

		QueueProc Q1 = new QueueProc();
    	Q1.sendTo = null;

    	Gen Generator = new Gen(probability);
    	Generator.mean = 5;
    	Generator.sendTo = Q1;

    	SignalList.SendSignal(GENERATE, Generator, time);
    	SignalList.SendSignal(MEASURE, Q1, time);

    	while (time < 100000) {
    		var actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}

		var result = new SimulationResult();
		
		result.meanNumSpecial = (double)(Q1.specialAccumulated)/Q1.noMeasurements;
		result.meanNumNormal = (double)(Q1.normAccumulated)/Q1.noMeasurements;

		result.meanTimeSpecial = (double)(Q1.specialTotaltime)/Q1.specialFinished;
		result.meanTimeNormal = (double)(Q1.normalTotaltime)/Q1.normalFinished;

		return result;
	}
}