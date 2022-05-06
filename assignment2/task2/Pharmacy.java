
import java.util.*;
import java.io.*;

class Pharmacy extends Proc {
    public Proc sendTo;
    Random random;
    double totalTime = 0;
    double noMeasurements = 0;
    Queue<Customer> queue= new LinkedList<>();
    List<Double> finishedTimes = new ArrayList<>();
    List<Integer> measurements = new ArrayList<>();
    double open = 9.0;
    double close = 17.0;
    boolean closed;
    double extraminutes;
    private double min = 10;
    private double max = 20;


    class Customer {
        double arrivalTime;
		double serviceStart;
    }
    public Pharmacy(long seed){
        random = new Random(seed);
    }

    public boolean isOpen(double arrivalTime){
        return arrivalTime <= (close - open)*60.0;
    }
    public double GetRandomNumber(double minimum, double maximum){ 
        return random.nextDouble() * (maximum - minimum) + minimum;
    }

    @Override
    public void TreatSignal(Signal x) {
        switch (x.signalType){
			case ARRIVAL:{
                if(isOpen(x.arrivalTime)){
                    var customer = new Customer();
                    customer.arrivalTime = x.arrivalTime;
                    queue.add(customer);

                    if(queue.size() == 1){
                        SignalList.SendSignal(DONE, this, time + GetRandomNumber(min, max));
                    }
                    

                }else{
                    closed = true;
                }   
			}
				break;
            
            case DONE:{
                var customer = queue.poll();
                var result = time - customer.arrivalTime;

                finishedTimes.add(result);
                if(queue.size() > 0){ // might have to change this. Same structure as lab5
                    SignalList.SendSignal(DONE, this, time + GetRandomNumber(min, max));
                }else if (closed) {
                    extraminutes = time - (close - open)*60.0;
                }
            }
                break;
            
            case MEASURE: {
                measurements.add(queue.size());
                SignalList.SendSignal(MEASURE, this, time + GetRandomNumber(1,2));
            }


    }

    }
}