package pers.jay.demo.room

import pers.jay.demo.common.JApp

object DBManager {

    fun getDB(): AppDatabase {
        return AppDatabase.getInstance(JApp.instance)
    }
}