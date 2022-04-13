import java.util.*;
import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
		double[] arrivalArray = {1, 2, 5};
		FileWriter writer = new FileWriter("Results.csv"); 
		writer.write("Time between arrivals,Arrivals,Rejected,Accumulated Queue 1, Accumulated Queue 2,Measurements" + System.lineSeparator());
		for (double dd:arrivalArray){
			time = 0;
			System.out.println("Time between arrivals: " + dd);
			Event actEvent = new Event();
	
			State actState = new State(); // The state that shoud be used
			// Some events must be put in the event list at the beginning
			actState.setArrival_time(dd);
			insertEvent(ARRIVAL1, actState.getArrival_time());  
			insertEvent(MEASURE, 5);
			// The main simulation loop
			while (time < 1000000){
				actEvent = eventList.fetchEvent();
				time = actEvent.eventTime;
				actState.treatEvent(actEvent);
			}
			FileWriter writer2 = new FileWriter("Customers_in_Queue_" + dd + ".csv"); 
			writer2.write("Time,Queue1,Queue2" + System.lineSeparator());
			for(Double[] o: actState.currentNumberOfCustomersList) {
				writer2.write(o[0] +","+ o[1] + "," + o[2] +  System.lineSeparator());
			}
			writer2.close();
			// Saving the results of the simulation
			writer.write(dd + "," + actState.numberOfArrivals + "," + actState.numberOfRejected + "," + actState.accumulatedQueue1 + "," + actState.accumulatedQueue2 + "," + actState.numberMeasurements + System.lineSeparator());
			// Printing the results of the simulation
			System.out.println("Arrivals: " + actState.numberOfArrivals);
			System.out.println("Rejected: " + actState.numberOfRejected);
			System.out.println("Accumulated Queue 1: " + actState.accumulatedQueue1);
			System.out.println("Accumulated Queue 2: " + actState.accumulatedQueue2);
			System.out.println("Measurements: " + actState.numberMeasurements);
			System.out.println("Avrg Queue 1: " + actState.accumulatedQueue1*1.0/actState.numberMeasurements);
			System.out.println("Avrg Queue 2: " + actState.accumulatedQueue2*1.0/actState.numberMeasurements);
			System.out.println("Probability rejected: " + actState.numberOfRejected*1.0/actState.numberOfArrivals);
			
			System.out.println("\n \n");
		}
		writer.close();
    }
}