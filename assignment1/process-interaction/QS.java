import java.util.*;
import java.io.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class QS extends Proc{
	public int accumulated, noMeasurements;
	public Proc sendTo;

	private double lambda = 4; // 4 minutes
	private Queue<Double> queue = new LinkedList<>();
	private int special_in_Q = 0; private int norm_in_Q = 0;
	private int special_finished = 0; private int norm_finished = 0;
	private int special_totaltime = 0; private int norm_totaltime = 0;

	// .size() has o(1) timecomplexity
	public void TreatSignal(Signal x){
		switch (x.signalType){
			
			case ARRIVAL:
				queue.add(time);
				if (queue.size() == 1 || (x.special && special_in_Q == 0)){
					SignalList.SendSignal(READY,this, time + expo(4), x.special);
				}
			 	break;

			case READY:
				if(x.special && special_in_Q > 0)

				numberInQueue--;

				// why? For future if expand processes?
				if (sendTo != null){
					SignalList.SendSignal(ARRIVAL, sendTo, time);
				}
				if (queue.size() > 0){
					SignalList.SendSignal(READY, this, time + expo(lambda), x.next.special);
				}
			 	break;

			case MEASURE:{
				noMeasurements++;
				accumulated = accumulated + numberInQueue;
				SignalList.SendSignal(MEASURE, this, time + 2*slump.nextDouble());
			} break;
		}
	}
}