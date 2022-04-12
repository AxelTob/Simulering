import java.util.*;
import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State actState = new State(); // The state that shoud be used
    	// Some events must be put in the event list at the beginning
        insertEvent(ARRIVAL1, 2);  
        insertEvent(MEASURE, 5);
        // The main simulation loop
    	while (time < 500000){
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
    }
}