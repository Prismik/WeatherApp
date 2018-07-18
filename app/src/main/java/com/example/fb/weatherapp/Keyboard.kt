package com.example.fb.weatherapp

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


class Keyboard {
    companion object {
        fun hide(context: Context, view: View) {
            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}