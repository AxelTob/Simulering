import java.util.*;
import java.io.*;


public class MainSimulation extends GlobalSimulation {
    public static void main(String[] args) throws IOException {
    	State state = new State(0);
        insertEvent(ARRIVAL, 0);  
        insertEvent(MEASURE, 0);
        
    	while (state.measurements.size() < state.maxMeasurements){
    		Event event = eventList.fetchEvent();
    		time = event.eventTime;
    		state.treatEvent(event);
    	}
    	
    	System.out.println(state.measurements.size());
    }
}
