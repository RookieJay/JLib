package pers.jay.demo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import kotlin.Pair;
import pers.jay.library.base.BaseActivity;
import pers.jay.library.base.ext.CommonExtKt;
import pers.jay.library.base.ext.ComponentExtKt;

public class Test extends BaseActivity {

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Intent intent = new Intent();
        ComponentExtKt.putExtras(intent, new Pair<>("1", "1"));
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
    }
}
