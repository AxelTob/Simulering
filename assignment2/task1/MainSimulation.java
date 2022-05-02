import java.util.*;
import java.io.*;


public class MainSimulation extends GlobalSimulation {
    static class SimulationArgs {
        public double arrivalRate;
        public int servers;
        public double serviceTime;

        public double measurementInterval;
        public int maxMeasurements;

        public String outputFilename;

        public SimulationArgs(
                double _arrivalRate,
                int _servers,
                double _serviceTime,
                double _measurementInterval,
                int _maxMeasurements,
                String _outputFilename) {
            arrivalRate = _arrivalRate;
            servers = _servers;
            serviceTime = _serviceTime;
            measurementInterval = _measurementInterval;
            maxMeasurements = _maxMeasurements;
            outputFilename = _outputFilename;
        }
    }

    public static void main(String[] dummy) throws IOException {
        List<SimulationArgs> argss = Arrays.asList(
                new SimulationArgs(8, 1000, 100, 1, 1000, "1.csv"),
                new SimulationArgs(80, 1000, 10, 1, 1000, "2.csv"),
                new SimulationArgs(4, 1000, 200, 1, 1000, "3.csv"),
                new SimulationArgs(4, 100, 10, 4, 1000, "4.csv"),
                new SimulationArgs(4, 100, 10, 1, 4000, "5.csv"),
                new SimulationArgs(4, 100, 10, 4, 4000, "6.csv"));
    	
        for(var args : argss) {
            var measurements = runSimulation(args);

            var out =
                new PrintWriter(new FileOutputStream(args.outputFilename));

            out.println("" + args.serviceTime + "," + args.measurementInterval);
            for(var measurement : measurements) {
                out.println(measurement);
            }
            out.close();
        }
    }

    public static List<Integer> runSimulation(SimulationArgs args) {
        time = 0;
        eventList = new EventListClass();

        var state =
            new State(
                    0,
                    args.arrivalRate,
                    args.servers,
                    args.serviceTime,
                    args.measurementInterval);

        insertEvent(ARRIVAL, 0);  
        insertEvent(MEASURE, 0);
        
    	while (state.measurements.size() < args.maxMeasurements) {
    		Event event = eventList.fetchEvent();
    		time = event.eventTime;
    		state.treatEvent(event);
    	}

        return state.measurements;
    }
}
