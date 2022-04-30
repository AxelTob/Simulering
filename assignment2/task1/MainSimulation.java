import java.util.*;
import java.io.*;


public class MainSimulation extends GlobalSimulation {
    static class SimulationArgs {
        public double arrivalRate;
        public int servers;
        public double serviceTime;

        public double measurementInterval;
        public int maxMeasurements;

        public SimulationArgs(
                double _arrivalRate,
                int _servers,
                double _serviceTime,
                double _measurementInterval,
                int _maxMeasurements) {
            arrivalRate = _arrivalRate;
            servers = _servers;
            serviceTime = _serviceTime;
            measurementInterval = _measurementInterval;
            maxMeasurements = _maxMeasurements;
        }
    }

    public static void main(String[] dummy) throws IOException {
        List<SimulationArgs> argss = Arrays.asList(
                new SimulationArgs(8, 1000, 100, 1, 1000),
                new SimulationArgs(80, 1000, 10, 1, 1000),
                new SimulationArgs(4, 1000, 200, 1, 1000),
                new SimulationArgs(4, 100, 10, 4, 1000),
                new SimulationArgs(4, 100, 10, 1, 4000),
                new SimulationArgs(4, 100, 10, 4, 4000));
    	
        for(var args : argss) {
            var measurements = runSimulation(args);
            System.out.println(measurements.size());
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
        
    	while (state.measurements.size() < args.maxMeasurements){
    		Event event = eventList.fetchEvent();
    		time = event.eventTime;
    		state.treatEvent(event);
    	}

        return state.measurements;
    }
}
