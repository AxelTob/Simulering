import java.util.*;
import java.io.*;

//Denna klass �rver Global s� att man kan anv�nda time och signalnamnen utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation


public class MainSimulation extends Global{
	static class SimulationResult {
		double extraMinutesOpen;
		List<Double> queueTimes;
	}

    public static void main(String[] args) throws IOException {
		File dir = new File("results");
		if(!dir.exists()){
			dir.mkdirs();
		}
		FileWriter averages = new FileWriter(new File(dir,"queue_averages.txt"));
		FileWriter extraminutes = new FileWriter(new File(dir,"extraminutes.txt"));
		
		for(int i = 0; i < 10000; i++){

			var result = runSimulation(i);

			OptionalDouble average = result.queueTimes
				.stream()
				.mapToDouble(a -> a)
				.average();
			
			averages.write(
				String.valueOf(average.isPresent() ? average.getAsDouble() : 0)
				+ System.lineSeparator());

			 extraminutes.write(
				 String.valueOf(result.extraMinutesOpen)
				 + System.lineSeparator()
			 );
	
		}
		averages.close();
		extraminutes.close();

    }

	private static SimulationResult runSimulation(int i) {
    	new SignalList();
		time = 0.0;

		Pharmacy Ph = new Pharmacy(i);

    	Gen Generator = new Gen(i+1);
    	Generator.sendTo = Ph;

    	SignalList.SendSignal(GENERATE, Generator, time);
    	//SignalList.SendSignal(MEASURE, Ph, time);

    	while (!Ph.closed || Ph.queue.size() > 0) {
    		var actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}

		var result = new SimulationResult();
		
		result.extraMinutesOpen = Ph.extraminutes;
        result.queueTimes = Ph.finishedTimes;

		return result;
	}

	// from 0-100 produced specials in queue.
	// private void testSystem(){
	// 	System.out.println("numNormal\tnumSpecial\ttimeNormal\ttimeSpecial");
	// 	for (int i = 0; i <= 100; ++i) {
	// 		var result = runSimulation((double)(i)/100);
    	
	// 		System.out.printf("%fst\t%fst\t%fmin\t%fmin\t%f\n",
	// 				result.meanNumNormal,
	// 				result.meanNumSpecial,
	// 				result.meanTimeNormal,
	// 				result.meanTimeSpecial,
	// 				result.meanNumNormal + result.meanNumSpecial);		
	// 	}
	// }
}
