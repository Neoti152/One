package com.example.calculator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class Calculate {
    public static String getResult(String s) throws NumberFormatException {


        if (s.contains("(")) {
            while (s.contains("(")) {
                int end = s.indexOf(")");
                int start = s.lastIndexOf("(", end);
                String s1 = getResult(s.substring(++start, end));

                String s2 = s.substring(--start, ++end);
                System.out.println(s2);
                s = s.replace(s2, s1);
            }
        }

        return calculation(s);
    }


    public static String calculation(String s) throws NumberFormatException {
        boolean boo = false;
        if (s.toCharArray()[0] == '-') {
            s = s.substring(1, s.length());
            boo = true;
        }

        Pattern pattern = Pattern.compile("[*+/-]");
        String[] strings = pattern.split(s);
        if (boo) {
            strings[0] = "-" + strings[0];
        }

        List<Number> numbers = getNumbers(strings);


        Pattern pattern2 = Pattern.compile("\\d\\.\\d|\\d");
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

        double d = (double) numbers.get(0);
        DecimalFormat format = new DecimalFormat("#.##########");
        return format.format(d);
    }


    public static List<Number> getNumbers(String[] strings) throws NumberFormatException {
        List<Number> list = new ArrayList<>();
        for (String s : strings) {
            list.add(Double.parseDouble(s));
        }
        return list;
    }

    public static List<PosishionHelper> getPosishonList(List<String> strings) {
        List<PosishionHelper> list = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            int a = i;
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
        }
        return c;
    }

}
