import java.util.*;
import java.io.*;


public class MainSimulation extends GlobalSimulation{
	
    public static void main(String[] args) throws IOException {
		runSimulation();
	}

	public static void runSimulation() throws IOException {
		Event actEvent;
		Grid actGrid = new Grid(); // The state that shoud be used
		// Some events must be put in the event list at the beginning
		readFile(actGrid);
		
		// The main simulation loop
		while (actGrid.new_meetings < 5){
			actEvent = eventList.fetchEvent();
			time = actEvent.eventTime;
			actGrid.treatEvent(actEvent);
			System.out.println(actGrid.new_meetings);
		}
		System.out.println(time);
		
		// Printing the result of the simulation, in this case a mean value
	
	}

	public static void readFile(Grid actGrid) throws IOException {
		var reader =
            new BufferedReader(
                new InputStreamReader( System.in ) );
		String line = reader.readLine();
		while(line != null) {
			var fields = line.split(",");
			var student = actGrid.new Student();
			System.out.println("x :" + fields[0] + " y : " + fields[1]);
			student.square = actGrid.grid[Integer.parseInt(fields[0])][Integer.parseInt(fields[1])];
			student.speed = 2.0;
			student.talking = false;
			student.direction = Grid.Direction.values()[Integer.parseInt(fields[2])];
			student.squares_counter = Integer.parseInt(fields[3]);
			Event firstEvent = new Event(student, student.square, 0.0);
			student.nextEvent = firstEvent;
			insertEvent(firstEvent);
			line = reader.readLine();
		}
	}
}