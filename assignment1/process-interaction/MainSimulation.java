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
		List<Double> normalTimes;
		List<Double> specialTimes;
	}

    public static void main(String[] args) throws IOException {
		File dir = new File("results");
		if(!dir.exists()){
			dir.mkdirs();
		}
		FileWriter writerResult = new FileWriter(new File(dir,"result.csv"));

		writerResult.write("Special Probability,meanNumNormal,meanNumSpecial,meanTimeNormal,meanTimeSpecial" + System.lineSeparator());
		for (Double i : Arrays.asList(0.1,0.2,0.5)) {
			var result = runSimulation(i);
						
			writerResult.write(String.valueOf(i) + ","
			+ result.meanNumNormal + ","
			+ result.meanNumSpecial + ","
			+ result.meanTimeNormal + ","
			+ result.meanTimeSpecial + System.lineSeparator());
			
			// all normal times for given probability
			FileWriter normalTimes = new FileWriter(new File(dir,"normaltimes" + 
			String.valueOf(i) + ".txt"));
			for(Double time: result.normalTimes){
				normalTimes.write(time + System.lineSeparator());
			}
			normalTimes.close();

			// same for special
			FileWriter specialTimes = new FileWriter(new File(dir,"specialtimes" + 
			String.valueOf(i) + ".txt"));
			for(Double time: result.specialTimes){
				specialTimes.write(time + System.lineSeparator());
			}
			specialTimes.close();

			System.out.printf("%fst\t%fst\t%fmin\t%fmin\t%f\n",
					result.meanNumNormal,
					result.meanNumSpecial,
					result.meanTimeNormal,
					result.meanTimeSpecial,
					result.meanNumNormal + result.meanNumSpecial);		
		}
		writerResult.close();
    }

	private static SimulationResult runSimulation(double probability) {
    	new SignalList();
		time = 0.0;

		QueueProc Q1 = new QueueProc(0);
    	Q1.sendTo = null;

    	Gen Generator = new Gen(1, probability);
    	Generator.mean = 5;
    	Generator.sendTo = Q1;

    	SignalList.SendSignal(GENERATE, Generator, time);
    	SignalList.SendSignal(MEASURE, Q1, time);

    	while (Generator.generatedPeople < 1000) {
    		var actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}

		var result = new SimulationResult();
		
		result.meanNumSpecial = (double)(Q1.specialAccumulated)/Q1.noMeasurements;
		result.meanNumNormal = (double)(Q1.normAccumulated)/Q1.noMeasurements;

		result.meanTimeSpecial = (double)(Q1.specialTotaltime)/Q1.specialFinished;
		result.meanTimeNormal = (double)(Q1.normalTotaltime)/Q1.normalFinished;
		result.normalTimes = Q1.invNormalResultTimes;
		result.specialTimes = Q1.invSpecialResultTimes;

		return result;
	}

	// from 0-100 produced specials in queue.
	private void testSystem(){
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
}
