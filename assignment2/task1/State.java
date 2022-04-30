import java.util.*;
import java.io.*;

class State extends GlobalSimulation{

    int customers = 0;

    double arrivalRate = 8.0;
    int servers = 1000;
    double serviceTime = 100.0;

    double measurementInterval = 1.0;
    int maxMeasurements = 1000;

    List<Integer> measurements = new ArrayList<Integer>();

	Random random;
    
    public State(long seed) {
        random = new Random(seed);
    }
	
	public void treatEvent(Event x){
		switch (x.eventType){
			case ARRIVAL:
				arrival();
				break;
			case DONE:
				done();
				break;
			case MEASURE:
				measure();
				break;
		}
	}
	
    private double expRandom(double lambda) {
        double p = random.nextDouble();
        return -Math.log(1-p)/lambda;
    }
	
	private void arrival(){
        if (customers < servers) {
            customers++;
            insertEvent(DONE, time + serviceTime);
        }
		insertEvent(ARRIVAL, time + expRandom(arrivalRate));
	}
	
	private void done(){
		customers--;
	}
	
	private void measure(){
        measurements.add(customers);
		insertEvent(MEASURE, time + measurementInterval);
	}
}
