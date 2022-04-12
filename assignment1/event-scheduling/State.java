import java.util.*;
import java.io.*;
import java.math.*;

class State extends GlobalSimulation{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int numberOfArrivals=0, numberInQueue1 = 0, numberInQueue2 = 0, numberOfRejected = 0, numberMeasurements = 0, accumulatedQueue1 = 0, accumulatedQueue2;

	Random slump = new Random(); // This is just a random number generator
	
	Deque <Double> customerQueue = new LinkedList<>();

	public ArrayList<Double[]> currentNumberOfCustomersList = new ArrayList<Double[]>();
	
	public ArrayList<Double> timeInSystemList = new ArrayList<Double>();
	
	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){
		switch (x.eventType){
			case ARRIVAL1:
				arrival();
				break;
			case DEPARTURE1:
				departure1();
				break;
			case DEPARTURE2:
				departure2();
				break;
			case MEASURE:
				measure();
				break;
		}
	}
	
	
	// The following methods defines what should be done when an event takes place. This could
	// have been placed in the case in treatEvent, but often it is simpler to write a method if 
	// things are getting more complicated than this.
	
	private void arrival(){
		customerQueue.addLast(time);
		numberOfArrivals++;
		numberInQueue1++;
		if (numberInQueue1==1)
			insertEvent(DEPARTURE1, time + expRnd(1)); 
		insertEvent(ARRIVAL1, time + expRnd(1.1)); //HERE Input for Arrival 
	}
	
	private void departure1(){
		numberInQueue1--;
		numberInQueue2++;
		if (numberInQueue2==1)
			insertEvent(DEPARTURE2, time + expRnd(1));
		if (numberInQueue1>0)
			insertEvent(DEPARTURE1, time + expRnd(1)); 
	}
	
	private void departure2(){
		double entryTime = customerQueue.removeFirst();
		timeInSystemList.add(time - entryTime);
		numberInQueue2--;
		if (numberInQueue2>0)
			insertEvent(DEPARTURE2, time + expRnd(1));
	}
	
	private void measure(){
		Double[] current_measure = {time, (double) numberInQueue1, (double) numberInQueue2};
		currentNumberOfCustomersList.add(current_measure);
		accumulatedQueue1 = accumulatedQueue1 + numberInQueue1;
		accumulatedQueue2 = accumulatedQueue2 + numberInQueue2;
		numberMeasurements++;
		insertEvent(MEASURE, time + expRnd(1)); 
	}
	public double expRnd(double expectedValue) {
		return (Math.log(slump.nextDouble())/(-1.0/expectedValue));
	}
}