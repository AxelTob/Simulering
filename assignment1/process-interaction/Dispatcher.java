import java.util.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class Dispatcher extends Proc {
	public interface Policy {
		public abstract QueueProc pickNext(List<QueueProc> queues);
	}

    Random random;
    List<QueueProc> queues;
	Policy policy;

    public Dispatcher(int seed, List<QueueProc> queues, Policy policy) {
        random = new Random(seed);
        this.queues = queues;
		this.policy = policy;
    }

	public void TreatSignal(Signal x) {
		switch (x.signalType) {
			case NORM_ARRIVAL: {
                sendNext(NORM_ARRIVAL);
			}
				break;

			case SPEC_ARRIVAL: {
                sendNext(SPEC_ARRIVAL);
			}
				break;

			case MEASURE: {
			}
				break;
		}
	}

	private void sendNext(int event) {
        var destination = policy.pickNext(queues);
		queues.get(random.nextInt(queues.size()));
        SignalList.SendSignal(
                event,
                destination,
                time);
	}
}
