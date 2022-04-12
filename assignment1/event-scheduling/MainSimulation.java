import java.util.*;
import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State actState = new State(); // The state that shoud be used
    	// Some events must be put in the event list at the beginning
        insertEvent(ARRIVAL1, 1.1);  
        insertEvent(MEASURE, 5);
        // The main simulation loop
    	while (time < 1000000){
    		actEvent = eventList.fetchEvent();
    		time = actEvent.eventTime;
    		actState.treatEvent(actEvent);
    	}
    	
    	// Printing the result of the simulation, in this case a mean value
    	System.out.println("Arrivals: " + actState.numberOfArrivals);
    	System.out.println("Accumulated Queue 1: " + actState.accumulatedQueue1);
    	System.out.println("Accumulated Queue 2: " + actState.accumulatedQueue2);
    	System.out.println("Rejected: " + actState.numberOfRejected);
    	System.out.println("Measurements: " + actState.numberMeasurements);
    	double average = actState.timeInSystemList
                .stream()
                .mapToDouble(a -> a)
                .average()
                .getAsDouble();
    	FileWriter writer = new FileWriter("Time_in_System.csv"); 
    	writer.write("Times in System" + System.lineSeparator());
    	for(Double dd: actState.timeInSystemList) {
    	  writer.write(dd + System.lineSeparator());
    	}
		FileWriter writer2 = new FileWriter("Customers_in_Queue.csv"); 
    	writer2.write("Time, Queue1, Queue2" + System.lineSeparator());
    	for(Double[] o: actState.currentNumberOfCustomersList) {
    		writer2.write(o[0] +","+ o[1] + "," + "," + o[2] +  System.lineSeparator());
    	}
    	System.out.println("Time in the system: " + average);
    }
}