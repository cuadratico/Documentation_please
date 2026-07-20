package com.documentationplease

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class user_data (val name: String, val day: String, var note: String)
class db (context: Context): SQLiteOpenHelper(context, "info_usr.db", null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE user (name TEXT, day TEXT, note TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}


    fun add (values: ContentValues) {
        val db = this.writableDatabase

        db.insert("user", null, values)
    }

    fun update (values: ContentValues) {
        val db = this.writableDatabase

        db.update("user", values, null, null)
    }

    fun catch (data_name: Array<String>): String? {
        val db = this.readableDatabase

        val query = db.query("user", data_name, null, null, null, null, null)

        if (query.moveToFirst()) {
            return query.getString(0)
        } else {
            return null
        }

    }

}