import java.util.*;
import java.io.*;

public class MainSimulation extends Global {
    static class SimulationResult {
        double meanLength = 0.0;
    }

    static class RandomPolicy implements Dispatcher.Policy {
        private Random random = new Random(1337);

        public QueueProc pickNext(List<QueueProc> queues) {
            return queues.get(random.nextInt(queues.size()));
        }

        @Override
        public String toString() {
            return "Random";
        }
    }

    static class RoundRobbinPolicy implements Dispatcher.Policy {
        private int current = 0;

        public QueueProc pickNext(List<QueueProc> queues) {
            current %= queues.size();
            var next = queues.get(current);
            ++current;
            return next;
        }

        @Override
        public String toString() {
            return "Round-robbin";
        }
    }

    static class ShortestPolicy implements Dispatcher.Policy {
        public QueueProc pickNext(List<QueueProc> queues) {
            return queues.stream()
                    .min(Comparator.comparing(q -> q.normInQueue + q.specialInQueue))
                    .get();
        }

        @Override
        public String toString() {
            return "Shortest";
        }
    }

    public static void main(String[] args) throws IOException {
        var meanArrivals = Arrays.asList(0.11, 0.15, 2.0);
        var policies = Arrays.asList(
                new RandomPolicy(),
                new RoundRobbinPolicy(),
                new ShortestPolicy());

        System.out.println("Policy & Mean arrival time & Mean jobs in queue \\\\ \\hline");

        for (Double mean : meanArrivals) {
            for (var policy : policies) {
                var result = runSimulation(0.2, mean, policy);
                System.out.printf(
                        "%s & %f & %f  \\\\ \\hline \n",
                        policy.toString(),
                        mean,
                        result.meanLength);
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
            result.meanLength += (double) (queue.accumulatedLength) / queue.noMeasurements;
        }

        return result;
    }
}
