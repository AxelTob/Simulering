import java.util.*;

import java.io.*;


public class MainSimulation extends GlobalSimulation{

	public static class SimulationRun{
		int number_of_simulations;
		int number_of_students;
		double speed; // m/s
		double talking_time ; // s

		public SimulationRun(int number_of_simulations, int number_of_students, double speed, double talking_time){
			this.number_of_simulations = number_of_simulations;
			this.number_of_students = number_of_students;
			this.speed = speed;
			this.talking_time = talking_time;
		}
	
	}
	
    public static void main(String[] args) throws IOException {
		List<SimulationRun> simulation_list = new ArrayList();
		SimulationRun simulation_run_1 = new SimulationRun(1, 20, 2.0, 60.0);
		simulation_list.add(simulation_run_1);

		for(SimulationRun run: simulation_list){
			for(int i=0; i < run.number_of_simulations;i++){

				runSimulation(run.number_of_students, run.speed, run.talking_time);

			}
		}
		
		
	}



	public static void runSimulation(int number_of_students, double speed, double talking_time) throws IOException {
		Event actEvent;
		Grid actGrid = new Grid(); // The state that shoud be used
		actGrid.talking_time = talking_time;
		// Some events must be put in the event list at the beginning
		readFile(actGrid, number_of_students, speed);
		
		// The main simulation loop
		while (actGrid.new_meetings < 190){
			actEvent = eventList.fetchEvent();
			time = actEvent.eventTime;
			actGrid.treatEvent(actEvent);
			System.out.println(actGrid.new_meetings);
		}

		writeMeetingsPerSquare(actGrid);
		writeStudentMeetings(actGrid);

		writeStudentData(actGrid);
		System.out.println("Meetings " + actGrid.meetings);
		System.out.println("time[s]: " + time);
		
		// Printing the result of the simulation, in this case a mean value
	
	}
	/*
	public static List<SimulationRun> readInputParameterFile() throws IOException{
		List<SimulationRun> simulation_list = new ArrayList();

		BufferedReader csvReader = new BufferedReader(new FileReader("SimulationInputParameter.csv"));
	
		while ((row = csvReader.readLine()) != null) {
			SimulationRun simulation = new SimulationRun();
			String[] data = row.split(",");
			System.out.println(data[0]);

			simulation.number_of_simulations = (int) Integer.parseInt(tempArr[0]);
			simulation.number_of_students = (int) Integer.parseInt(tempArr[1]);
			simulation.speed = (double) Double.parseDouble(tempArr[2]);
			simulation.talking_time = (double) Double.parseDouble(tempArr[3]);
			
			simulation_list.add(simulation);
		}
		csvReader.close();

		return simulation_list;
	}
		*/
	public static void readFile(Grid actGrid, int number_of_students, double speed) throws IOException {
		var reader =
            new BufferedReader(
                new InputStreamReader( System.in ) );
		String line = reader.readLine();
		int student_id = 0;
		while(line != null && student_id < number_of_students) {
			var fields = line.split(",");
			var student = actGrid.new Student();
			System.out.println("x :" + fields[0] + " y : " + fields[1]);
			student.student_id = student_id;
			student.square = actGrid.grid[Integer.parseInt(fields[0])][Integer.parseInt(fields[1])];
			student.speed = speed;
			student.talking = false;
			student.direction = Grid.Direction.values()[Integer.parseInt(fields[2])];
			student.squares_counter = Integer.parseInt(fields[3]);
			EnteringEvent firstEvent = new EnteringEvent(student, student.square, 0.0);
			student.nextEvent = firstEvent;
			insertEvent(firstEvent);
			line = reader.readLine();
			actGrid.students.add(student);
			student_id++;
		}
	}
	public static void writeMeetingsPerSquare(Grid actGrid) throws IOException {
		FileWriter meetingWriter = new FileWriter("MeetingsPerGrid.csv");
		meetingWriter.write("x,y,#Meetings" + System.lineSeparator());
		for (Grid.Square[] row : actGrid.grid){
			for (Grid.Square s: row){
				meetingWriter.write("" + s.position.x + "," + s.position.y + "," + s.Meeting_Counter + System.lineSeparator());
			}
		}
		meetingWriter.close();
	}

	public static void writeStudentData(Grid actGrid) throws IOException {
		//writes student routes
		FileWriter routeWriter = new FileWriter("StudentsRoute.csv");
		routeWriter.write("Student,Step,x,y" + System.lineSeparator());

		for (Grid.Student student : actGrid.students){
			int step = 0;
			for (Grid.Square square: student.walkingRoute){
				routeWriter.write("" + student.student_id + "," + step + "," + square.position.x + "," + square.position.y + System.lineSeparator());
				step++;
			}
		}
		routeWriter.close();
	}

	public static void writeStudentMeetings(Grid actGrid) throws IOException{

		FileWriter studentMeetingWriter = new FileWriter("MeetingsPerStudent.csv");
		// write header
		studentMeetingWriter.write(",");
		for(int i=0; i<actGrid.students.size();i++){
			studentMeetingWriter.write(i+",");
		}
		studentMeetingWriter.write(System.lineSeparator());

		//calculate number of meetings
		int[][] student_meetings = new int[actGrid.students.size()][];
		for(Grid.Student student : actGrid.students){
			int[] number_of_meetings = new int[actGrid.students.size()];
			for (int i = 0; i < number_of_meetings.length; i++) {
				number_of_meetings[i] = 0;
			}
			for(Grid.Student student_met: student.students_met){
				number_of_meetings[student_met.student_id]++;
			}
			student_meetings[student.student_id] = number_of_meetings;
		}

		//write lines
		int line = 0;
		for(int[] student_array : student_meetings){
			studentMeetingWriter.write(""+line);
			for(int number : student_array){
				studentMeetingWriter.write(","+ number);
			}
			studentMeetingWriter.write(System.lineSeparator());
			line++;
		}
		studentMeetingWriter.close();
	}
}