import java.util.*;
import java.io.*;

class State extends GlobalSimulation{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int AsInQueue = 0;
	public int BsInQueue = 0;

	Random slump = new Random(); // This is just a random number generator
	
	
	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){
		switch (x.eventType){
			case ARRIVE_A:
				arriveA();
				break;
			case DONE_A:
				doneA();
				break;
			case ARRIVE_B:
				arriveB();
				break;
			case DONE_B:
				doneB();
				break;
		}
	}
	
	
	// The following methods defines what should be done when an event takes place. This could
	// have been placed in the case in treatEvent, but often it is simpler to write a method if 
	// things are getting more complicated than this.
	
    private void serveNext() {
		if (BsInQueue > 0) {
			insertEvent(DONE_B, time + 0.004);
        }
        else if (AsInQueue > 0) {
			insertEvent(DONE_A, time + 0.002);
        }
    }

	private void arriveA(){
		AsInQueue++;
		if (AsInQueue == 1 && BsInQueue == 0) {
            serveNext();
        }
		insertEvent(ARRIVE_A, time + 1.0/150);
	}
	
	private void doneA(){
		AsInQueue--;
        serveNext();
	}
	
	private void arriveB(){
		BsInQueue++;
		if (AsInQueue == 0 && BsInQueue == 1) {
            serveNext();
        }
	}

	private void doneB(){
		BsInQueue--;
        serveNext();
	}
}
