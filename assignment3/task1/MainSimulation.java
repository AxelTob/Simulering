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

    public static void main(String[] dummy) throws IOException {
        var configuration = loadConfiguration();
        for(var args : configuration.argss) {
            var transmitters =
                configuration
                    .transmitters
                    .subList(0, args.numTransmitters);

            runSimulation(args, transmitters);
        }
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

    private static void runSimulation(
            SimulationArgs args, 
            List<Transmitter> transmitters) {
    	new SignalList();

    	while (time < 100000) {
    		Signal actSignal = SignalList.FetchSignal();
            if(actSignal.destination == null) { return; }
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}
    }
}
