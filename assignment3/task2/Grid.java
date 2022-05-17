import java.util.*;
import java.io.*;
import java.math.*;

class Grid extends GlobalSimulation{
	class Student {
		public Square square;
		public double speed;
		public boolean talking;
		public Direction direction;
		public int squares_counter;
	}

	class Position {
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
	
	class Square {
		public Position position;
		public List<Student> students;
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
			}
		}
	}

	public Square grid[][] = new Square[20][20];

	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	

	Random slump = new Random(); // This is just a random number generator
	
	
	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){
		x.student.square.students.remove(
			x.student
		);
		x.entering_square.students.add(
			x.student
		);
		Square entering_square = enteringSquare(x.student);

		double distance = 
				Math.sqrt(Math.pow((double)direction.offset().x,2.0)
						+ Math.pow((double)direction.offset().y,2.0));

		double eventTime = x.eventTime + (distance/ x.student.speed);

		insertEvent(x.student, entering_square, eventTime);

	}

	
	// The following methods defines what should be done when an event takes place. This could
	// have been placed in the case in treatEvent, but often it is simpler to write a method if 
	// things are getting more complicated than this.
	
	private Square enteringSquare(Student student){
		
		var newPos = student.square.position.add(student.direction.offset());
		return Grid[newPos.x][newPos.y];
	}
	private Direction randomDirection(){
		return Direction.values()[new Random().nextInt(Direction.values().length)];
	}

}