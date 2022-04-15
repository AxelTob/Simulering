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
        public long seed;

        public Args(long seed, State.Distribution dist, State.Priority prio) {
            delayDistribution = dist;
            priority = prio;
            this.seed = seed;
        }
    }

    public static class Measurement {
        public int asInQueue;
        public int bsInQueue;
    }

	private int asInQueue = 0;
	private int bsInQueue = 0;
    private Distribution delayDistribution;
    private Priority priority;
    private double arrivalFreq = 150;

    public List<Measurement> measurements = new ArrayList<Measurement>();

	private Random random;

	
    public State() {
        delayDistribution = Distribution.Constant;
        priority = Priority.B;
        random = new Random(0);
    }

    public State(Args args) {
        delayDistribution = args.delayDistribution;
        priority = args.priority;
        random = new Random(args.seed);
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
		insertEvent(ARRIVE_A, time + expRandom(arrivalFreq));
	}
	
	private void doneA(){
		asInQueue--;
        var delayConstant = true;

        Function<Double, Double> dist =
            delayDistribution == Distribution.Constant
                ? mean -> mean
                : mean -> expRandom(1.0/mean);

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
        var measurement = new Measurement();
        measurement.asInQueue = asInQueue;
        measurement.bsInQueue = bsInQueue;
        measurements.add(measurement);
        insertEvent(MEASURE, time + 0.1);
	}


    public static void runVerification() {
        System.out.println("VERIFICATION OF EXPONENTIAL DISTRIBUTION");
        var state = new State();

        final int numBuckets = 20;
        final int bucketSize = 2;
        var buckets =
            new ArrayList<Integer>(Collections.nCopies(numBuckets, 0));

        final double lambda = 0.1;
        final int numSamples = 10000;

        double total = 0.0;

        for(int i = 0; i < numSamples; ++i) {
            final double sample = state.expRandom(lambda);
            if(sample < 0.0) {
                System.err.println("ERROR: expRandom generated value less than zero");
                return;
            }

            total += sample;

            final int bucketIndex = (int)(sample/bucketSize);
            if(bucketIndex < numBuckets) {
                buckets.set(
                        bucketIndex,
                        buckets.get(bucketIndex)+1);
            }
        }

        final double mean = total / numSamples;

        System.out.printf("Lambda: %f\n", lambda);
        System.out.printf("Mean: %f\n", mean);

        System.out.println("Distribution:");
        final int max = buckets.stream().mapToInt(x->x).max().getAsInt();
        for(int bucket : buckets) {
            int columns = (int)(50.0 * bucket / max);
            var bar = "=".repeat(columns);
            System.out.println(bar);
        }
    }
}
