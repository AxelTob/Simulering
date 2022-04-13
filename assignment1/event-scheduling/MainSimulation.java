import java.util.*;
import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
		double[] arrivalArray = {1.1, 1.5, 2.0};
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
			// Printing the result of the simulation, in this case a mean value
			System.out.println("Arrivals: " + actState.numberOfArrivals);
			System.out.println("Accumulated Queue 1: " + actState.accumulatedQueue1);
			System.out.println("Accumulated Queue 2: " + actState.accumulatedQueue2);
			System.out.println("Rejected: " + actState.numberOfRejected);
			System.out.println("Measurements: " + actState.numberMeasurements);
			System.out.println("\n \n");
		}
    }
}