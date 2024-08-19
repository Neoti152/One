package com.example.calculator

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CalculatorDatabaseHelper internal constructor(context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(
            "CREATE TABLE CALCULATOR (_id INTEGER PRIMARY KEY, "
                    + "EXAMPLE TEXT, "
                    + "RESULT TEXT);"
        )
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {}
    fun addRowInTable(db: SQLiteDatabase, example: String?, result: String?) {
        val calculatorValues = ContentValues()
        calculatorValues.put("_id", id)
        calculatorValues.put("EXAMPLE", example)
        calculatorValues.put("RESULT", result)
        if (tableIsClear) {
            db.insert("CALCULATOR", null, calculatorValues)
        } else {
            db.update("CALCULATOR", calculatorValues, "_id = ?", arrayOf(Integer.toString(id)))
        }
        id++
        if (id > 20) {
            id = 1
            if (tableIsClear) {
                tableIsClear = false
            }
        }
    }

    fun eraseTable(db: SQLiteDatabase) {
        db.delete("CALCULATOR", null, null)
        tableIsClear = true
    }

    companion object {
        private const val DB_NAME = "calculator" // Имя базы данных
        private const val DB_VERSION = 1
        private var id = 1
        private var tableIsClear = true
    }
}