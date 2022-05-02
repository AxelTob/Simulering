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
        double drop;
        if(p<0.1){
            drop = 0.75;
        }
        else if(p < 0.35){
            drop = 0.5;
        }
        else if(0 < 0.6){
            drop = 0.4;
        }
        else{
            drop = 0.9;
        }
        return wealth * drop;
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
        FileWriter writer = new FileWriter("MonthsTillBoat.csv");
        writer.write("Months Till Boat" + System.lineSeparator());
        for(Integer i: monthsTillBoatList) {
                writer.write(i + System.lineSeparator());
                }
        writer.close();
    }
}