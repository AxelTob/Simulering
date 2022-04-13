import java.util.*;
import java.io.*;


public class MainSimulation extends Global{
    public static void main(String[] args) throws IOException {
        runSimulation(0.2);
    }

	private static void runSimulation(double probability) {
    	new SignalList(); // Reset global state lmao

        var queues = new ArrayList<QueueProc>();
        queues.add(new QueueProc(0, 0.5));
        queues.add(new QueueProc(1, 0.5));
        queues.add(new QueueProc(2, 0.5));
        queues.add(new QueueProc(3, 0.5));
        queues.add(new QueueProc(4, 0.5));

        var dispatcher = new Dispatcher(5, queues);

        final double meanArrival = 0.15;
    	var generator = new Gen(6, meanArrival, probability);
    	generator.sendTo = dispatcher;

    	SignalList.SendSignal(GENERATE, generator, time);

    	while (generator.generatedPeople < 10000) {
    		var signal = SignalList.FetchSignal();
    		signal.destination.TreatSignal(signal);
    	}
	}
}
