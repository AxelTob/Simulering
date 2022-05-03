import java.util.*;
import java.io.*;
import java.math.*;

class Main{

    private static int nextDrop(int current, Random random) {
        int spread = 4*12-1;
        return current + random.nextInt(4*12-spread, 4*12+spread+1);
    }
    
    private static double drop(double wealth, Random random) {
        double p = random.nextDouble();
        double remaining;
        if(p<0.1){
            remaining = 0.75;
        }
        else if(p < 0.35){
            remaining = 0.5;
        }
        else if(0 < 0.6){
            remaining = 0.4;
        }
        else{
            remaining = 0.9;
        }
        double newWealth = wealth * remaining;
        return newWealth;
        }

        public static double std(ArrayList<Integer> arrList)
        {
            double sum = 0.0, deviation = 0.0;
            int length = arrList.size();
    
            for(double num : arrList) {
                sum += num;
            }
    
            double mean = sum/length;
    
            for(double num: arrList) {
                deviation += Math.pow(num - mean, 2);
            }
            double std = Math.sqrt(deviation/length);
            return std;
        }
    

    public static void main(String[] args) throws IOException{
        FileWriter wealthWriter = new FileWriter("wealth_per_month.csv");
        wealthWriter.write("Month,Wealth"+System.lineSeparator());

        var random = new Random(1);
        ArrayList<Integer> monthsTillBoatList = new ArrayList<Integer>();

        for(int i = 0; i < 1000; i++){
            int month = 0;
            double yearlyGrowth = 1.3;
            double monthlyGrowth = Math.pow(yearlyGrowth, 1.0/12);
            double monthlySavings = 5000;
            double wealth = monthlySavings;
            int monthsUntilDrop = nextDrop(month, random);
            double goal = 2e6;

            while(wealth < goal){
                if(monthsUntilDrop==0){
                    wealth = drop(wealth, random);
                    monthsUntilDrop = nextDrop(month, random);
                }
                wealth *= monthlyGrowth;
                wealth += monthlySavings;
                month++;
                monthsUntilDrop--;
                wealthWriter.write("" + month + "," + wealth + System.lineSeparator());
            }
            System.out.println(month);
            monthsTillBoatList.add(month);
        }
        wealthWriter.close();
        FileWriter writer = new FileWriter("months_till_boat.csv");
        writer.write("Months Till Boat" + System.lineSeparator());
        for(Integer i: monthsTillBoatList) {
                writer.write(i + System.lineSeparator());
                }
        writer.close();

        OptionalDouble mean = monthsTillBoatList
            .stream()
            .mapToDouble(a -> a)
            .average();
        
        double stDev = std(monthsTillBoatList);
        double meanStDev = stDev/Math.sqrt(monthsTillBoatList.size());

        System.out.println("mean: " + mean.getAsDouble());
        System.out.println("std: " + stDev);
        System.out.println("std_mean: " + meanStDev);
        
    }
}