import java.util.*;
import java.io.*;
import java.util.function.Function;


public class MainSimulation extends GlobalSimulation{
    public static void main(String[] ignore) throws IOException {
        var argsList = new ArrayList<State.Args>();
        argsList.add(new State.Args(1337, State.Distribution.Constant, State.Priority.B));
        argsList.add(new State.Args(1337, State.Distribution.Exponential, State.Priority.B));
        argsList.add(new State.Args(1337, State.Distribution.Constant, State.Priority.A));

        boolean verify = true;
        boolean outputCsv = true;

        if(verify) {
            State.runVerification();
            System.out.println("-----------------------------------");
        }

        boolean first = true;

        for(var args : argsList) {
            if(!first) {
                System.out.println("-----------------------------------");
            }
            first = false;


            System.out.printf(
                    "Delay distribution: %s\n",
                    args.delayDistribution.toString());
            System.out.printf(
                    "Priority: %s\n", args.priority.toString());

            var measurements = runSimulation(args);

            double mean = measurements.stream()
                    .mapToDouble(m -> m.asInQueue + m.bsInQueue)
                    .sum() / measurements.size();

            System.out.printf("Mean: %.2f\n", mean);

            if(outputCsv) {
                String filename =
                    args.delayDistribution.toString() +
                    args.priority.toString() +
                    ".csv";

                var out = new PrintWriter(new FileOutputStream(filename));

                for(var m : measurements) {
                    out.print(String.format("%d,%d\n", m.asInQueue, m.bsInQueue));
                }

                out.close();
            }
        }
    }

    private static List<State.Measurement> runSimulation(State.Args args) {
        time = 0;
        eventList = new EventListClass();
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
