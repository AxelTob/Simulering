import java.util.*;
import java.io.*;

//Denna klass �rver Global s� att man kan anv�nda time och signalnamnen utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation


public class MainSimulation extends Global{
	static class SimulationResult {
		double extra_minutes_open;
		List<Double> queueTimes;
		List<Integer> queue_size_measure;
	}

    public static void main(String[] args) throws IOException {
		File dir = new File("results");
		if(!dir.exists()){
			dir.mkdirs();
		}

		FileWriter average_queuing_time = new FileWriter(new File(dir,"queue_average_queuing_time.txt"));
		FileWriter extra_minutes_open = new FileWriter(new File(dir,"extra_minutes_open.txt"));
		FileWriter queue_size_measurements = new FileWriter(
			new File(dir,"queue_size_measurements.txt"));
		
		for(int i = 0; i < 100000; i++){
			
			
			var result = runSimulation(i);

			OptionalDouble average_waiting_time = result.queueTimes
				.stream()
				.mapToDouble(a -> a)
				.average();

			OptionalDouble average_queue_size = result.queue_size_measure
				.stream()
				.mapToInt(a -> a)
				.average();

			average_queuing_time.write(
				String.valueOf(average_waiting_time.isPresent() ? average_waiting_time.getAsDouble() : 0)
				+ System.lineSeparator());

			 extra_minutes_open.write(
				 String.valueOf(result.extra_minutes_open)
				 + System.lineSeparator()
			 );

			 queue_size_measurements.write(
				String.valueOf(average_queue_size.isPresent() ? average_queue_size.getAsDouble() : 0)
				+ System.lineSeparator()
			 );

			// one run with queue-size for each measurement
			 if(i==1){
				FileWriter queue_one_run = new FileWriter(new File(dir,"queue_one_run.txt"));
				for(var x: result.queue_size_measure){
					queue_one_run.write(
						String.valueOf(x)
				 		+ System.lineSeparator()
					);
				}
				queue_one_run.close();
			 }


	
		}
		average_queuing_time.close();
		extra_minutes_open.close();
		queue_size_measurements.close();

    }

	private static SimulationResult runSimulation(int i) {
    	new SignalList();
		time = 0.0;

		Pharmacy Ph = new Pharmacy(i);

    	Gen Generator = new Gen(i+1);
    	Generator.sendTo = Ph;

    	SignalList.SendSignal(GENERATE, Generator, time);
		SignalList.SendSignal(MEASURE, Ph, time);
    	//SignalList.SendSignal(MEASURE, Ph, time);

    	while (!Ph.closed || Ph.queue.size() > 0) {
    		var actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}

		var result = new SimulationResult();
		
		result.extra_minutes_open = Ph.extraminutes;
        result.queueTimes = Ph.finishedTimes;
		result.queue_size_measure = Ph.measurements;

		return result;
	}
}
