import java.util.*;
import java.io.*;

public class MainSimulation extends Global {
    static class SimulationArgs {
        public int numTransmitters;
        public double meanSleepTime;
        public double transmissionTime;
        public double radius;
    }

    static class Configuration {
        List<Transmitter> transmitters;
        List<SimulationArgs> argss;
    }

    static class Transmitter {
        public double x;
        public double y;
        public double startTime;
    }

    public static void main(String[] args) throws IOException {
        var configuration = loadConfiguration();
    }
    
    private static Configuration loadConfiguration() throws IOException {
        var reader =
            new BufferedReader(
                new InputStreamReader( System.in ) );

        var argss = new ArrayList<SimulationArgs>();

        String line = reader.readLine();
        int numSimulations = Integer.parseInt(line);
        for(int i = 0; i < numSimulations; ++i) {
            line = reader.readLine();
            var fields = line.split(",");
            var args = new SimulationArgs();

            args.numTransmitters = Integer.parseInt(fields[0]);
            args.meanSleepTime = Double.parseDouble(fields[1]);
            args.transmissionTime = Double.parseDouble(fields[2]);
            args.radius = Double.parseDouble(fields[3]);

            argss.add(args);
        }

        var transmitters = new ArrayList<Transmitter>();

        line = reader.readLine();
        while(line != null) {
            var fields = line.split(",");
            var transmitter = new Transmitter();

            transmitter.x = Double.parseDouble(fields[0]);
            transmitter.y = Double.parseDouble(fields[1]);
            transmitter.startTime = Double.parseDouble(fields[2]);
            transmitters.add(transmitter);

            line = reader.readLine();
        }

        var configuration = new Configuration();
        configuration.argss = argss;
        configuration.transmitters = transmitters;
        return configuration;
    }

    private static void runSimulation(String[] args) {
        /*
    	new SignalList();

    	SignalList.SendSignal(READY, Generator, time);
    	SignalList.SendSignal(MEASURE, Q1, time);

    	while (time < 100000){
    		Signal actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}

    	System.out.println("Mean number of customers in queuing system: " + 1.0*Q1.accumulated/Q1.noMeasurements);
        */
    }
}
