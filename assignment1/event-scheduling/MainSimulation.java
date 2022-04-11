import java.util.*;
import java.io.*;


public class MainSimulation extends GlobalSimulation{
    public static void main(String[] ignore) throws IOException {
        var argsList = new ArrayList<State.Args>();
        argsList.add(new State.Args(State.Distribution.Constant, State.Priority.B));
        argsList.add(new State.Args(State.Distribution.Exponential, State.Priority.B));
        argsList.add(new State.Args(State.Distribution.Constant, State.Priority.A));

        boolean first = true;

        for(var args : argsList) {
            if(!first) {
                System.out.println("-----------------------------------");
            }
            first = false;


            System.out.println(
                "Delay distribution: " + args.delayDistribution.toString());
            System.out.println(
                "Priority: " + args.priority.toString());

            var measurements = runSimulation(args);

            double mean =
                measurements
                    .stream()
                    .mapToInt(x -> x)
                    .sum() / (double)measurements.size();

            System.out.printf("Mean: %.10f\n", mean);
        }
    }

    private static List<Integer> runSimulation(State.Args args) {
    	State state = new State(args);

        insertEvent(ARRIVE_A, 0);  
        insertEvent(MEASURE, 10);

    	while (state.measurements.size() < 1000) {
    		var event = eventList.fetchEvent();
    		time = event.eventTime;
    		state.treatEvent(event);
    	}

        return state.measurements;
    }
}
