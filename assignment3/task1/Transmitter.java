import java.util.*;
import java.io.*;

class Transmitter extends Proc {
	private Random random;

    private List<Proc> neighbors;

    private double transmissionTime = 1.0; // FIXME

    public Transmitter(long seed, List<Proc> neighbors) {
        this.random = new Random(seed);
        this.neighbors = neighbors;
    }

	public void TreatSignal(Signal x)  {
        switch(x.signalType) {
        case TRANSMIT: {
            for(var neighbor : neighbors) {
                var signal = new Signal();
                signal.destination = neighbor;
                signal.arrivalTime = time;
                signal.signalType = ARRIVAL;
                neighbor.TreatSignal(signal);
            }

            SignalList.SendSignal(
                    TRANSMIT,
                    this,
                    time+expRandom(1.0/transmissionTime));
        } break;
        }
	}

    private double expRandom(double lambda) {
        double p = random.nextDouble();
        return -Math.log(1-p)/lambda;
    }
}
