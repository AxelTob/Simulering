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
		var result = runSimulation();

		OptionalDouble average = result.queueTimes
			.stream()
			.mapToDouble(a -> a)
			.average();

		System.out.println(average.isPresent() ? average.getAsDouble() : 0);

		System.out.println("yoo  " + result.extraMinutesOpen);
    }

	private static SimulationResult runSimulation() {
    	new SignalList();
		time = 0.0;

		Pharmacy Ph = new Pharmacy(0);

    	Gen Generator = new Gen(1);
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
