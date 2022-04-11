import java.util.*;
import java.io.*;
import java.util.function.Function;

class State extends GlobalSimulation{
    public enum Distribution {
        Constant("Constant"),
        Exponential("Exponential");

        private String representation;

        Distribution(String rep) {
            representation = rep;
        }

        @Override
        public String toString() {
            return representation;
        }
    }
    public enum Priority {
        A("A"),
        B("B");

        private String representation;

        Priority(String rep) {
            representation = rep;
        }

        @Override
        public String toString() {
            return representation;
        }
    }

    public static class Args {
        public State.Distribution delayDistribution;
        public State.Priority priority;

        public Args(State.Distribution dist, State.Priority prio) {
            delayDistribution = dist;
            priority = prio;
        }
    }

	private int asInQueue = 0;
	private int bsInQueue = 0;
    private Distribution delayDistribution;
    private Priority priority;

    public List<Integer> measurements = new ArrayList<Integer>();

	private Random random = new Random();

	
    public State(Args args) {
        delayDistribution = args.delayDistribution;
        priority = args.priority;
    }

	
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
			case MEASURE:
				measure();
				break;
		}
	}
	
	
    private double constant(double lambda) {
        return lambda;
    }
	
    private double expRandom(double lambda) {
        double p = random.nextDouble();
        return -Math.log(1-p)/lambda;
    }
	
    private void servePrioritized(
            int numPrimaryQueue,
            int primaryEvent,
            double primaryDelay,
            int numSecondaryQueue,
            int secondaryEvent,
            double secondaryDelay) {
        if (numPrimaryQueue > 0) {
			insertEvent(primaryEvent, time + primaryDelay);
        }
        else if (numSecondaryQueue > 0) {
			insertEvent(secondaryEvent, time + secondaryDelay);
        }
    }
    private void serveNext() {
        if(priority == priority.A) {
            servePrioritized(asInQueue, DONE_A, 0.002, bsInQueue, DONE_B, 0.004);
        }
        else {
            servePrioritized(bsInQueue, DONE_B, 0.004, asInQueue, DONE_A, 0.002);
        }
    }

	private void arriveA(){
		asInQueue++;
		if (asInQueue == 1 && bsInQueue == 0) {
            serveNext();
        }
		insertEvent(ARRIVE_A, time + expRandom(150));
	}
	
	private void doneA(){
		asInQueue--;
        var delayConstant = true;

        Function<Double, Double> dist =
            delayDistribution == Distribution.Constant
                ? this::constant
                : this::expRandom;

        insertEvent(ARRIVE_B, time + dist.apply(1.0));
        serveNext();
	}
	
	private void arriveB(){
		bsInQueue++;
		if (asInQueue == 0 && bsInQueue == 1) {
            serveNext();
        }
	}

	private void doneB(){
		bsInQueue--;
        serveNext();
	}

	private void measure(){
        measurements.add(asInQueue + bsInQueue);
        insertEvent(MEASURE, time + 0.1);
	}
}
