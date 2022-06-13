package com.brainoptimax.peakmeup.utils

import android.content.Context
import android.content.SharedPreferences

class Preferences(val context: Context) {
    companion object{
        const val USER_PREF = "USER_PREF"
    }

    private var sharedUserPreferences = context.getSharedPreferences(USER_PREF,0)

    fun setValues(key:String,value:String)
    {
        val editor: SharedPreferences.Editor = sharedUserPreferences.edit()
        editor.putString(key,value)
        editor.apply()
    }
    fun getValues(key: String):String?
    {
        return sharedUserPreferences.getString(key,"")
    }


    fun toLogout() {
        val editor: SharedPreferences.Editor = sharedUserPreferences.edit()
        editor.clear()
        editor.apply()
    }

}