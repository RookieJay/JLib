package pers.jay.library.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

abstract class CommonTextWatcher(view : EditText) : TextWatcher {

    private val mView = view

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        val text = s?.toString()!!.trim()
        textChanged(mView, text)
    }

    abstract fun textChanged(view : EditText, text : String)
}