package pers.jay.demo.room

import android.content.Context
import pers.jay.demo.common.JApp
import pers.jay.library.utils.SingletonHolder

class TestSingleton private constructor(context: Context){


    companion object: SingletonHolder<TestSingleton, Context>(::TestSingleton)

    init {
        TestSingleton.getInstance(JApp.instance)
    }

}