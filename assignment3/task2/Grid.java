import java.util.*;
import java.io.*;
import java.math.*;

public class Grid extends GlobalSimulation{
	public Square grid[][];
	public int new_meetings = 0;
	public Grid(){
		grid = new Square[20][20];

		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				
				Square square = new Square();
				square.position = new Position(col, row); // might mess it up
				grid[col][row] = square;
			   
			}
		 }
		
	}
	public class Student {
		public Square square;
		public double speed;
		public boolean talking;
		public Direction direction;
		public int squares_counter;
		public List<Student> students_met = new ArrayList<>();;
		public Event nextEvent;
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
					return null;
			}
		}
	}

	

	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	

	Random random = new Random(1); // This is just a random number generator
	
	
	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){
		
		if(x != x.student.nextEvent){
			return;
		}
		x.student.square.students.remove(
			x.student
		);
		x.entering_square.students.add(
			x.student
		);

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
				s.talking = true;
				// insert postponed Event for partner s 
				Event postponed_event = new Event(s, s.nextEvent.entering_square, s.nextEvent.eventTime + 60.0);
				insertEvent(postponed_event);
				s.nextEvent = postponed_event;
				// ugly but should be fine
				if(!s.students_met.contains(x.student)){
					// new friends
					new_meetings++;
				}
				s.students_met.add(x.student);
				// Set x to talking and postpone eventTime
				x.student.talking = true;
				eventTime += 60.0;
				x.student.students_met.add(s);
				break; // makes sense , right?
			}
		}

		Event newEvent = new Event(x.student, entering_square, eventTime);

		x.student.nextEvent	= newEvent;

		insertEvent(newEvent);
		

	}

	
	// The following methods defines what should be done when an event takes place. This could
	// have been placed in the case in treatEvent, but often it is simpler to write a method if 
	// things are getting more complicated than this.
	
	public Square enteringSquare(Student student){
		
		var newPos = student.square.position.add(student.direction.offset());
		if(outSideGrid(newPos)){
			student.direction = randomDirection();
			enteringSquare(student);
		}
		return grid[newPos.x][newPos.y];
	}
	public Direction randomDirection(){
		return Direction.values()[new Random().nextInt(Direction.values().length)];
	}
	public boolean outSideGrid(Position newPos){
		return newPos.x > 19 || newPos.y > 19 || newPos.x < 0 || newPos.y < 0;
	}
}