import java.util.*;
import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
		double[] arrivalArray = {1.1, 1.5, 2.0};
		FileWriter writerResult = new FileWriter("Results.csv"); 
		writerResult.write("Time between arrivals,Arrivals,Rejected,Accumulated Queue 1, Accumulated Queue 2,Measurements,Average time" + System.lineSeparator());
		for (double dd:arrivalArray){
			time = 0;
			System.out.println("Time between arrivals: " + dd);
			Event actEvent;
			State actState = new State(); // The state that shoud be used
			// Some events must be put in the event list at the beginning
			insertEvent(ARRIVAL1, dd);  
			insertEvent(MEASURE, 5);
			// The main simulation loop
			actState.setArrival_time(dd);
			while (time < 1000000){
				actEvent = eventList.fetchEvent();
				time = actEvent.eventTime;
				actState.treatEvent(actEvent);
			}
			// Calculate mean time in the system
			double averageTime = actState.timeInSystemList
					.stream()
					.mapToDouble(a -> a)
					.average()
					.getAsDouble();

			// Saving the results of the simulation
			writerResult.write(dd + "," + actState.numberOfArrivals + "," + actState.numberOfRejected + "," +
				 actState.accumulatedQueue1 + "," + actState.accumulatedQueue2 + "," + 
				 actState.numberMeasurements + "," + averageTime + System.lineSeparator());
			FileWriter writer = new FileWriter("Time_in_System_"+dd+".csv");
			writer.write("Times in System" + System.lineSeparator());
			for(Double o: actState.timeInSystemList) {
				 writer.write(o + System.lineSeparator());
				 }
			writer.close();
			FileWriter writer2 = new FileWriter("Customers_in_Queue_"+dd+".csv"); 
			writer2.write("Time, Queue1, Queue2" + System.lineSeparator());
			for(Double[] o: actState.currentNumberOfCustomersList) {
					 writer2.write(o[0] +","+ o[1] + "," + o[2] +  System.lineSeparator());
				 }
			writer2.close();
			// Printing the result of the simulation
			System.out.println("Arrivals: " + actState.numberOfArrivals);
			System.out.println("Accumulated Queue 1: " + actState.accumulatedQueue1);
			System.out.println("Accumulated Queue 2: " + actState.accumulatedQueue2);
			System.out.println("Measurements: " + actState.numberMeasurements);
			System.out.println("Avrg Queue 1: " + actState.accumulatedQueue1*1.0/actState.numberMeasurements);
			System.out.println("Avrg Queue 2: " + actState.accumulatedQueue2*1.0/actState.numberMeasurements);
			
			System.out.println("Average time in the system: " + averageTime);

			System.out.println("\n \n");
		
		}
		writerResult.close();
    }
}