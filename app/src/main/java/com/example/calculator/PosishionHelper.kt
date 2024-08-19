package com.example.calculator;

public class PosishionHelper implements Comparable {
    public String operation;
    public int priorety;
    public int posishion;





    PosishionHelper(String operation, int posishion) {
        this.operation = operation;
        switch (operation) {
            case "+":
                priorety = 3;
                break;
            case "-":
                priorety = 3;
                break;
            case "\u00d7":
                priorety = 2;
                break;
            case "/":
                priorety = 2;
                break;
            case "^":
                priorety = 1;
                break;
        }
        this.posishion = posishion;
    }


    @Override
    public int compareTo(Object o) {
        return this.priorety - ((PosishionHelper) o).priorety;
    }

    @Override
    public String toString() {
        return operation + " позиция первого элемента: " + posishion;
    }

}
