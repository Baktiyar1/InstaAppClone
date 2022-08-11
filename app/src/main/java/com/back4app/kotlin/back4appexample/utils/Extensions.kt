package com.back4app.kotlin.back4appexample.utils

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.back4app.kotlin.back4appexample.entities.Image
import com.parse.ParseFile

fun Fragment.toast(message: String){
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun ParseFile.toImage(): Image =
    Image(type = "File", name = name, url = url)