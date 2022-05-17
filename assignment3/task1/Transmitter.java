import java.util.*;
import java.io.*;

class Transmitter extends Proc {
	private Random random;

    private List<Proc> neighbors;

    private double transmissionTime = 1.0; // FIXME
    private double meanSleepTime = 4000;

    public Transmitter(long seed) {
        this.random = new Random(seed);
    }

    public void setNeighbors(List<Proc> neighbors) {
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
                    TRANSMISSION_DONE,
                    this,
                    time+transmissionTime);
        } break;
        case TRANSMISSION_DONE: {
            SignalList.SendSignal(
                    TRANSMIT,
                    this,
                    time+expRandom(1.0/meanSleepTime));
        }
        }
	}

    private double expRandom(double lambda) {
        double p = random.nextDouble();
        return -Math.log(1-p)/lambda;
    }
}
