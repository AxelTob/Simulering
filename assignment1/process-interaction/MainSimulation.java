import java.util.*;
import java.io.*;

public class MainSimulation extends Global {
    static class SimulationResult {
        double meanLength = 0.0;
    }

    public static void main(String[] args) throws IOException {
        var meanArrivals = Arrays.asList(0.11, 0.15, 2.0);
        var policies = Arrays.asList(
                new Dispatcher.Policy() { // random
                    private Random random = new Random(1337);

                    public QueueProc pickNext(List<QueueProc> queues) {
                        return queues.get(random.nextInt(queues.size()));
                    }
                },
                new Dispatcher.Policy() { // round-robbin
                    private int current = 0;

                    public QueueProc pickNext(List<QueueProc> queues) {
                        current %= queues.size();
                        var next = queues.get(current);
                        ++current;
                        return next;
                    }
                });

        for (Double mean : meanArrivals) {
            for (var policy : policies) {
                var result = runSimulation(0.2, mean, policy);
                System.out.printf("Mean queue length: %f\n", result.meanLength);
            }
        }
    }

    private static SimulationResult runSimulation(
            double probability,
            double meanArrival,
            Dispatcher.Policy policy) {
        time = 0.0;
        new SignalList(); // Reset global state lmao

        var queues = new ArrayList<QueueProc>();
        queues.add(new QueueProc(0, 0.5));
        queues.add(new QueueProc(1, 0.5));
        queues.add(new QueueProc(2, 0.5));
        queues.add(new QueueProc(3, 0.5));
        queues.add(new QueueProc(4, 0.5));

        var dispatcher = new Dispatcher(5, queues, policy);

        var generator = new Gen(6, meanArrival, probability);
        generator.sendTo = dispatcher;

        SignalList.SendSignal(GENERATE, generator, time);
        for (var queue : queues) {
            SignalList.SendSignal(MEASURE, queue, time);
        }

        while (time < 100000) {
            var signal = SignalList.FetchSignal();
            time = signal.arrivalTime;
            signal.destination.TreatSignal(signal);
        }

        var result = new SimulationResult();

        for (var queue : queues) {
            result.meanLength += (double) (queue.accumulatedLength) / queue.noMeasurements / queues.size();
        }

        return result;
    }
}
