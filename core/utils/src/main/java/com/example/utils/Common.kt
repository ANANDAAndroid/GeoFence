package com.example.utils

import android.content.Context
import android.widget.Toast


object Common {
     fun getFormatedLatLng(value: Double)=
        "%.4f".format(value)

    fun String.showToast(context: Context){
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }

}