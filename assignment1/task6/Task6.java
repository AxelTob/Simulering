import java.util.*;

import javax.swing.event.SwingPropertyChangeSupport;

import java.math.*;


public class Task6 {

    public static void main(String[] args) {
        
        Random slump = new Random(123);

        ArrayList<Double> maxList = new ArrayList<Double>();

        for (int i=0; i<1000; i++){
            double rnd1 = 1 + 4*slump.nextDouble();
            double rnd2 = 1 + 4*slump.nextDouble();
            double max = Math.max(rnd1, rnd2);
            maxList.add(max);
        }

        double averageTime = maxList
					.stream()
					.mapToDouble(a -> a)
					.average()
					.getAsDouble();

        System.out.println(averageTime);
    }
    
}
