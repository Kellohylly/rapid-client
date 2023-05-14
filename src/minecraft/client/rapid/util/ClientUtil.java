package client.rapid.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.rapid.Client;

public class ClientUtil {

    public static boolean isLatest() {
        try(Scanner scan = new Scanner((new URL("https://raw.githubusercontent.com/geuxy/rapid-client-code/main/version.txt")).openStream(), "UTF-8").useDelimiter("\\A")) {
            String s = scan.next();
            return compareVersionStrings(Client.getInstance().getVersion(), s);
        } catch (Exception e) {
            return false;
        }
    }
    
    public static int[] findNumbersInString(String input) {
        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(input);
        List<Integer> numbersList = new ArrayList<>();
        while (matcher.find()) {
            String numberString = matcher.group();
            double number = Double.parseDouble(numberString);
            numbersList.add((int) number);
        }
        int[] numbersArray = new int[numbersList.size()];
        for (int i = 0; i < numbersList.size(); i++) {
            numbersArray[i] = numbersList.get(i);
        }
        return numbersArray;
    }
    
    public static boolean compareVersionStrings(String version1, String version2) {
    	int[] v1 = findNumbersInString(version1);
        int[] v2 = findNumbersInString(version2);
        
        for (int i = 0; i < v1.length && i < v2.length; i++) {
            int n1 = (v1[i]);
            int n2 = (v2[i]);

            if (n1 > n2) {
                return true;
            } else if (n1 < n2) {
                return false;
            }
        }
        return v1.length >= v2.length;
    }

}
