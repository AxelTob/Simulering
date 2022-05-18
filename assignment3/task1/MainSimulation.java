import java.util.*;
import java.io.*;

public class MainSimulation extends Global {
    static class Position {
        public double x;
        public double y;

        public Position(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double distanceTo(Position other) {
            return Math.sqrt(
                    Math.pow(x-other.x, 2.0)+
                    Math.pow(y-other.y, 2.0));
        }
    }
    static class SimulationArgs {
        public int numTransmitterArgss;
        public double meanSleepTime;
        public double transmissionTime;
        public double radius;
    }

    static class Configuration {
        List<TransmitterArgs> transmitters;
        List<SimulationArgs> argss;
    }

    static class TransmitterArgs {
        public Position position;
        public double startTime;
    }

    public static void main(String[] dummy) throws IOException {
        var configuration = loadConfiguration();
        for(var args : configuration.argss) {
            var transmitters =
                configuration
                    .transmitters
                    .subList(0, args.numTransmitterArgss);

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

            args.numTransmitterArgss = Integer.parseInt(fields[0]);
            args.meanSleepTime = Double.parseDouble(fields[1]);
            args.transmissionTime = Double.parseDouble(fields[2]);
            args.radius = Double.parseDouble(fields[3]);

            argss.add(args);
        }

        var transmitters = new ArrayList<TransmitterArgs>();

        line = reader.readLine();
        while(line != null) {
            var fields = line.split(",");
            var transmitter = new TransmitterArgs();

            transmitter.position =
                new Position(
                    Double.parseDouble(fields[0]),
                    Double.parseDouble(fields[1]));
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
            List<TransmitterArgs> transmitterArgs) {
    	new SignalList();
        time = 0;

        var gateway = new Gateway();
        var transmissionCounter = new TransmissionCounter();

        var transmitters = new ArrayList<Transmitter>();
        for(int i = 0; i < transmitterArgs.size(); ++i) {
            transmitters.add(new Transmitter(i));
        }

        for(int i = 0; i < transmitterArgs.size(); ++i) {
            var currentPosition = transmitterArgs.get(i).position;
            var neighbors = new ArrayList<Proc>();

            for(int j = 0; j < transmitterArgs.size(); ++j) {
                if(i == j) {
                    continue;
                }

                var otherPosition = transmitterArgs.get(j).position;
                var distance = currentPosition.distanceTo(otherPosition);
                if(distance < args.radius) {
                    neighbors.add(transmitters.get(j));
                }
            }

            var distance =
                currentPosition.distanceTo(new Position(5.0, 5.0));
            if(distance < args.radius) {
                neighbors.add(gateway);
            }

            neighbors.add(transmissionCounter);

            transmitters.get(i).setNeighbors(neighbors);
        }

        for(int i = 0; i < transmitters.size(); ++i) {
            SignalList.SendSignal(
                    TRANSMIT,
                    transmitters.get(i),
                    transmitterArgs.get(i).startTime);
        }

    	while(true) {
    		Signal actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
            if(time > 10000) {
                break;
            }
    		actSignal.destination.TreatSignal(actSignal);
    	}

        System.out.println(""
                + gateway.successfulTransmissions + ", "
                + transmissionCounter.totalTransmissions);
    }
}
