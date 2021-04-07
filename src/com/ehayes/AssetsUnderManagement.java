package com.ehayes;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class AssetsUnderManagement {
    //initialize map to hold name of advisor and the net amount
    public Map<String, String> output2 = new HashMap<>();

    //put in format the NetAmount
    public DecimalFormat df = new DecimalFormat("#.00");

    public void run() {
        String file = "aixTestData.csv";

        try (FileInputStream fis = new FileInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            int iteration = 0;
            while ((line = reader.readLine()) != null) {
                if (iteration == 0) {
                    iteration++;
                    continue;
                }
                //split the string regular expression to ignore "" and , inside of "" the negative is to apply regex as many time as possible
                String[] columns = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                //CALCULATE PRICE OF THE STOCK
                String firstNetAmount = PriceOfTheStock(columns[2], columns[3]);

                //find the advisor
                if (output2.containsKey(columns[6])) {

                    //get value Net amount that advisor already has
                    String num = output2.get(columns[6]);
                    //run method to add net amount to the new entry
                    String newNetAmount = NetAmount(columns[1], firstNetAmount, num);
                    //put new net amount inside the map with that advisor
                    output2.put(columns[6], newNetAmount);

                } else {
                    //put values into map if it is a new advisor
                    output2.put(columns[6], firstNetAmount);
                }
            }

        } catch (IOException e) {
            System.out.printf("Problems loading %s %n", file);
            e.printStackTrace();
        }
    }

    //multiply the shares * price
    public String PriceOfTheStock(String TXN_SHARES, String TXN_PRICE) {
        // DecimalFormat df = new DecimalFormat("#.00");
        Double a = Double.parseDouble(TXN_SHARES);
        Double b = Double.parseDouble(TXN_PRICE.replace("$", ""));
        return df.format(a * b);
    }

    public String NetAmount(String TXN_TYPE, String price, String value) {
        //get the value: net Amount that is inside the map
        double total = Double.parseDouble(value);

        if (TXN_TYPE.toUpperCase().equals("BUY")) {
            //if buy add the price stock to the total net amount in map
            total += Double.parseDouble(price);
        } else {
            //if not buy then its sell so you subtract net amount by the stock
            total -= Double.parseDouble(price);
        }

        return df.format(total);
    }

    //print out map for output2
    //if wanted to create another output just put logic into run and values into a new map and print that one
    public void OutPut2() {
        run();
        output2.forEach((key, value) -> {
            System.out.println("Advisor: " + key);
            Double numParsed = Double.parseDouble(value);
            System.out.println("Net Amount Held: " + String.format("%,.2f", numParsed));
            System.out.printf("%n");
        });
    }

}
