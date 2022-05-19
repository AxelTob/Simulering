
// As the name indicates this class contains the definition of an event. next is needed to 
// build a linked list which is used by the EventListClass. It would have been just as easy
// to use a priority list which sorts events using eventTime.

class Event{
	public double eventTime;
	public Grid.Student student;
	public Event next;

	public Event(){
		
	}
}

class EnteringEvent extends Event{
	public Grid.Square entering_square;

	public EnteringEvent(Grid.Student student, Grid.Square entering_square, double eventTime){
		this.student = student;
		this.entering_square = entering_square;
		this.eventTime = eventTime;
	}
}

class StopTalkingEvent extends Event{
	public StopTalkingEvent(Grid.Student student, double eventTime){
		this.student = student;
		this.eventTime = eventTime;
	}

}