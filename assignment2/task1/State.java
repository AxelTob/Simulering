import java.util.*;
import java.io.*;

class State extends GlobalSimulation{

    int customers = 0;

    double arrivalRate;
    int servers;
    double serviceTime;

    double measurementInterval;
    int maxMeasurements;

    List<Integer> measurements = new ArrayList<Integer>();

	Random random;
    
    public State(
            long seed,
            double _arrivalRate,
            int _servers,
            double _serviceTime,
            double _measurementInterval) {
        random = new Random(seed);
        arrivalRate = _arrivalRate;
        servers = _servers;
        serviceTime = _serviceTime;
        measurementInterval = _measurementInterval;
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
