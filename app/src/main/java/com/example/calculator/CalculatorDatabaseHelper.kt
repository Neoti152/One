package com.example.calculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CalculatorDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "calculator"; // Имя базы данных
    private static final int DB_VERSION = 1;


    CalculatorDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE CALCULATOR (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "EXAMPLE TEXT, "
                + "RESULT TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    private static void insertResult(SQLiteDatabase db, String text,String result) {
        ContentValues calculatorValues = new ContentValues();
        calculatorValues.put("EXAMPLE", text);
        calculatorValues.put("RESULT", result);
        db.insert("DRINK", null, calculatorValues);
    }
}
