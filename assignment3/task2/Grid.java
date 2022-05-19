import java.util.*;


import java.io.*;
import java.math.*;

public class Grid extends GlobalSimulation{
	public Square grid[][];
	public int new_meetings = 0;
	public List<Student> students = new ArrayList<>();
	public int meetings = 0;
	public double talking_time;
	public int number_of_students;
	public Random random;

	public Grid(long seed, int number_of_students){
		random = new Random(seed);
		this.number_of_students = number_of_students;
		grid = new Square[20][20];

		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				
				Square square = new Square();
				square.position = new Position(col, row); // might mess it up
				grid[col][row] = square;
			//    System.out.println("--------------------------------");
			//    System.out.println("x: " + col + " y = " + row);
			}
		 }
		
	}
	public class Student {
		public int student_id;
		public Square square;
		public double speed;
		public boolean talking;
		public Direction direction;
		public int squares_counter;
		public List<Student> students_met = new ArrayList<>();
		public EnteringEvent nextEvent;
		public List<Square> walkingRoute = new ArrayList<>();
		public int unique_meetings = 0;
		public double final_time;
	}

	public static class Position {
		public int x;
		public int y;

		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Position add(Position other) {
			return new Position(x+other.x, y+other.y);
		}
	}
	
	public class Square {
		public Position position;
		public List<Student> students = new ArrayList<>();
		public int Meeting_Counter = 0;
	}

	public enum Direction {
		UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT;

		public Position offset() {
			switch(this){
				case UP:
					return new Position(0, 1);
				case UP_RIGHT:
					return new Position(1, 1);
				case RIGHT:
					return new Position(1, 0);
				case DOWN_RIGHT:
					return new Position(1, -1);
				case DOWN:
					return new Position(0, -1);
				case DOWN_LEFT:
					return new Position(-1, -1);
				case LEFT:
					return new Position(-1, 0);
				case UP_LEFT:
					return new Position(-1, 1);
				default:
					System.out.println("should never happend");
					return null;
			}
		}
	}

	

	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	

	
	
	
	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){

		if(x instanceof EnteringEvent){
			treatEnteringEvent((EnteringEvent) x);
		}
		if(x instanceof StopTalkingEvent){
			x.student.talking = false;
		}
	}
	
	// The following methods defines what should be done when an event takes place. This could
	// have been placed in the case in treatEvent, but often it is simpler to write a method if 
	// things are getting more complicated than this.
	
	public Square enteringSquare(Student student){
		// System.out.println("Direction: " + student.direction.toString());
		// System.out.println("before -- x =" + student.square.position.x 
		// + ", y =" + student.square.position.y);
		var newPos = student.square.position.add(student.direction.offset());
		// System.out.println("after -- x =" + newPos.x 
		// + ", y =" + newPos.y);

		if(!outSideGrid(newPos)){
			return grid[newPos.x][newPos.y];
		}else{
			student.direction = randomDirection();
			return enteringSquare(student);
		}

	}
	public Direction randomDirection(){
		return Direction.values()[random.nextInt(Direction.values().length)];
	}
	public boolean outSideGrid(Position newPos){
		return newPos.x > 19 || newPos.y > 19 || newPos.x < 0 || newPos.y < 0;
	}

	public void treatEnteringEvent(EnteringEvent x){
		if(x != x.student.nextEvent){
			return;
		}
		x.student.square.students.remove(
			x.student
		);
		
		x.entering_square.students.add(
			x.student
		);

		x.student.square = x.entering_square;
		// x.student.walkingRoute.add(x.entering_square);
		if(x.student.squares_counter == 0){
			x.student.direction = randomDirection();
			x.student.squares_counter = random.nextInt(10) + 1;
		}
		Square entering_square = enteringSquare(x.student);

		double distance = 
				Math.sqrt(Math.pow((double)x.student.direction.offset().x,2.0)
						+ Math.pow((double)x.student.direction.offset().y,2.0));

		double eventTime = x.eventTime + (distance/ x.student.speed);

		for (Student s : x.student.square.students){
			
			if(!s.talking && s != x.student){
				x.student.square.Meeting_Counter++;
				meetings++;
				s.talking = true;
				// insert postponed Event for partner s 
				EnteringEvent postponed_event =
						new EnteringEvent(s, s.nextEvent.entering_square, s.nextEvent.eventTime + talking_time);
				insertEvent(postponed_event);
				s.nextEvent = postponed_event;
				StopTalkingEvent stop_talking_event_s = new StopTalkingEvent(s , time + talking_time); 
				insertEvent(stop_talking_event_s);
				// ugly but should be fine
				if(!s.students_met.contains(x.student)){
					// new friends
					x.student.unique_meetings++;
					s.unique_meetings++;

					if(x.student.unique_meetings == (number_of_students - 1)){
			
						x.student.final_time = time;
					}

					if(s.unique_meetings == (number_of_students - 1)){
			
						s.final_time = time;
					}

					new_meetings++;
				}
				s.students_met.add(x.student);
				//System.out.println(s.students_met.size());
				// Set x to talking and postpone eventTime
				x.student.talking = true;
				eventTime += 60.0;
				x.student.students_met.add(s);
				StopTalkingEvent stop_talking_event_x = new StopTalkingEvent(x.student , time + talking_time); 
				insertEvent(stop_talking_event_x);

				break; // makes sense , right?
				
			}
		}
		x.student.squares_counter--;
		EnteringEvent newEvent = new EnteringEvent(x.student, entering_square, eventTime);

		x.student.nextEvent	= newEvent;


		insertEvent(newEvent);

		// check if he has met everyone
		

	}
}