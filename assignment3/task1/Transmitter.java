import java.util.*;
import java.io.*;

class Transmitter extends Proc {
	private Random random;

    private List<Proc> neighbors;

    private double transmissionTime;
    private double meanSleepTime = 4000;
    private double lastInterruption = -1e50;

    private double lb = 0.0;
    private double ub = 0.0;

    private int strategy;

    public Transmitter(long seed, double transmissionTime, int strategy) {
        this.random = new Random(seed);
        this.transmissionTime = transmissionTime;
        this.strategy = strategy;
    }

    public void setNeighbors(List<Proc> neighbors) {
        this.neighbors = neighbors;
    }

	public void TreatSignal(Signal x)  {
        switch(x.signalType) {
        case TRANSMIT: {
            if(strategy == STRATEGY2
                    && time < lastInterruption + transmissionTime) {
                SignalList.SendSignal(
                        TRANSMIT,
                        this,
                        time+uniformRandom(lb, ub));
            }
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

    private double uniformRandom(double l, double u) {
        double p = random.nextDouble();
        return l + (u - l)*p;
    }
}
