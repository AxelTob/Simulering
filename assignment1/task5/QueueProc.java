import java.util.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class QueueProc extends Proc {
	public int noMeasurements = 0;
	public Proc sendTo;

	public double meanService;

	class Customer {
		double arrivalTime;
		double serviceStart;
	}

    QueueProc(int seed, double meanService) {
        random = new Random(seed);
        this.meanService = meanService;
    }

    Random random;

	public Queue<Customer> normalQueue = new LinkedList<>();
	public Queue<Customer> specialQueue = new LinkedList<>();

	public int specialInQueue = 0;
	public int normInQueue = 0;
	public int accumulatedLength = 0;
	public int specialFinished = 0;
	public int normalFinished = 0;
	public int specialTotaltime = 0;
	public int normalTotaltime = 0;
	public int specialAccumulated = 0;
	public int normAccumulated = 0;

	public void TreatSignal(Signal x) {
		switch (x.signalType) {
			case NORM_ARRIVAL: {
				var customer = new Customer();
				customer.arrivalTime = time;
				normalQueue.add(customer);
				normInQueue++;

				if (normInQueue == 1 && specialInQueue == 0) {
					serveNext();
				}
			}
				break;

			case SPEC_ARRIVAL: {
				var customer = new Customer();
				customer.arrivalTime = time;
				specialQueue.add(customer);
				specialInQueue++;

				if (specialInQueue == 1 && normInQueue == 0) {
					serveNext();
				}
			}
				break;

			case NORM_DONE: {
				normInQueue--;
				normalFinished++;

				var customer = normalQueue.poll();
				var resultTime = customer.serviceStart - customer.arrivalTime;
				normalTotaltime += resultTime;

				serveNext();
			}
				break;

			case SPEC_DONE: {
				specialInQueue--;
				specialFinished++;

				var customer = specialQueue.poll();
				var resultTime = customer.serviceStart - customer.arrivalTime;
				specialTotaltime += resultTime;

				serveNext();
			}
				break;

			case MEASURE: {
				Random rand = new Random();
				noMeasurements++;
				accumulatedLength += normInQueue + specialInQueue;

				SignalList.SendSignal(MEASURE, this, time + 2 * rand.nextDouble());
			}
				break;
		}
	}

	private void serveNext() {
		if (specialInQueue > 0) {
			specialQueue.peek().serviceStart = time;
			SignalList.SendSignal(SPEC_DONE, this, time + expo(1.0 / meanService, random));
		} else if (normInQueue > 0) {
			normalQueue.peek().serviceStart = time;
			SignalList.SendSignal(NORM_DONE, this, time + expo(1.0 / meanService, random));
		}
	}
}
