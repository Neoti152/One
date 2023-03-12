package com.example.calculator;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculate {
    public static final double E = 2.7182818285;
    public static final double PI = 3.1415926536;
    public static boolean isRad;

    public static String getResult(String s) throws NumberFormatException {


        while (s.contains("(")) {
            int end = s.indexOf(")");
            int start = s.lastIndexOf("(", end);
            String s1 = getResult(s.substring(++start, end));

            String s2 = s.substring(--start, ++end);
            System.out.println(s2);
            s = s.replace(s2, s1);
        }


        return calculation(s);
    }


    public static String calculation(String s) throws NumberFormatException {
        boolean boo = false;
        if (s.isEmpty()) {
            throw new NumberFormatException();
        }

        if (s.toCharArray()[0] == '-') {
            s = s.substring(1, s.length());
            boo = true;
        }

        Pattern pattern = Pattern.compile("[\\^*+/-]");
        String[] strings = pattern.split(s);
        if (boo) {
            strings[0] = "-" + strings[0];
        }

        List<Number> numbers = getNumbers(strings);


        Pattern pattern2 = Pattern.compile("\\d\\.\\d|\\d|sin|cos|tg|log|lg|ln|\u221A|!|E|:");
        String[] znaki = pattern2.split(s);
        List<String> listznaki = deleteEmpty(znaki);


        List<PosishionHelper> operations = getPosishonList(listznaki);


        for (int i = 0; i < operations.size(); i++) {
            if (numbers.size() > 1) {
                int pos = operations.get(i).posishion;
                int pos2 = pos + 1;
                Number a = numbers.get(pos);
                Number b = numbers.get(pos2);
                Number c = doOperation(operations.get(i).operation, a, b);
                numbers.set(pos, c);
                numbers.remove(pos2);
                for (int j = i; j < operations.size(); j++) {
                    if (operations.get(j).posishion >= pos) {
                        operations.get(j).posishion--;
                    }
                }
            }

        }

        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setDecimalSeparator('.');
        double d = (double) numbers.get(0);
        DecimalFormat format = new DecimalFormat("#.##########", symbols);
        return format.format(d);
    }


    public static List<Number> getNumbers(String[] strings) throws NumberFormatException {
        List<Number> list = new ArrayList<>();
        for (String s : strings) {
            list.add(doOperationMore(s));
        }
        return list;
    }

    public static List<PosishionHelper> getPosishonList(List<String> strings) {
        List<PosishionHelper> list = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {

            list.add(new PosishionHelper(strings.get(i), i));
        }
        Collections.sort(list);
        return list;
    }

    public static List<String> deleteEmpty(String[] strings) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            if (!strings[i].isEmpty()) {
                list.add(strings[i]);
            }
        }

        return list;
    }


    public static Number doOperation(String s, Number a, Number b) {
        double c = 0;

        switch (s) {
            case "+":
                c = (double) a + (double) b;
                break;
            case "-":
                c = (double) a - (double) b;
                break;
            case "*":
                c = (double) a * (double) b;
                break;
            case "/":
                c = (double) a / (double) b;
                break;
            case "^":
                c = Math.pow((double) a, (double) b);
                break;
        }
        return c;
    }

    public static Double doOperationMore(String s) throws NumberFormatException {
        Pattern pattern = Pattern.compile("sin|cos|tg|log|lg|ln|\u221A|!");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            int end = matcher.end();
            String str = s.substring(end);

            if (str.isEmpty()) {
                String a = s.substring(0, end - 1);
                return factorial(Double.parseDouble(a));
            } else {

                s = s.substring(0, end);
                double d = doFunctions(s, str);
                return d;
            }

        } else {
            return Double.parseDouble(s);
        }

    }

    public static Double doFunctions(String s, String d) {
        double c = 0;

        switch (s) {
            case "sin":
                if (isRad) {
                    c = Math.sin(Double.parseDouble(d));
                }else{
                    c = Math.sin(Math.toRadians(Double.parseDouble(d)));
                }
                break;
            case "cos":
                if (isRad) {
                    c = Math.cos(Double.parseDouble(d));
                }else{
                    c = Math.cos(Math.toRadians(Double.parseDouble(d)));
                }
                break;
            case "tg":
                if (isRad) {
                    c = Math.tan(Double.parseDouble(d));
                }else{
                    c = Math.tan(Math.toRadians(Double.parseDouble(d)));
                }
                break;
            case "lg":
                c = Math.log10(Double.parseDouble(d));
                break;
            case "ln":
                c = Math.log(Double.parseDouble(d));
                break;
            case "log":
                int points = d.indexOf(':');
                if (points == -1) {
                    throw new NumberFormatException();
                }
                double number = Double.parseDouble(d.substring(0, points));
                double base = Double.parseDouble(d.substring(points + 1));
                c = Math.log10(number) / Math.log10(base);
                break;
            case "\u221A":
                c = Math.sqrt(Double.parseDouble(d));
                break;
        }
        return c;
    }

    public static double factorial(double d) {
        BigInteger n = new BigInteger("1");
        for (int i = 1; i <= d; i++) {
            n = n.multiply(new BigInteger(String.valueOf(i)));
        }
        return n.doubleValue();
    }
}
