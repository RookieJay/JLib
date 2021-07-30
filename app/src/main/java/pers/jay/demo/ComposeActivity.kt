package pers.jay.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

/**
 * Copyright (c) 2011, 北京视达科技有限责任公司 All rights reserved.
 * author：juncai.zhou
 * date：2021/7/30 15:58
 * description：
 */
class ComposeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            test()
        }
    }

    @Preview
    @Composable
    fun test() {
//        Column {
//            Text(text = "Hello Compose")
//        }
    }

}