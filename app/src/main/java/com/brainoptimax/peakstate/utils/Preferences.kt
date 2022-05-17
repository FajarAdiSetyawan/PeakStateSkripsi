package com.brainoptimax.peakstate.utils

import android.content.Context
import android.content.SharedPreferences

class Preferences(val context: Context) {
    companion object{
        const val USER_PREF = "USER_PREF"
        const val EMO_PREF = "USER_PREF"
    }

    private var sharedUserPreferences = context.getSharedPreferences(USER_PREF,0)
    private var sharedEmotionPreferences = context.getSharedPreferences(EMO_PREF,0)

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

    fun setValuesEmotion(key:String,value:String)
    {
        val editor: SharedPreferences.Editor = sharedEmotionPreferences.edit()
        editor.putString(key,value)
        editor.apply()
    }

    fun getValuesEmotion(key: String):String?
    {
        return sharedEmotionPreferences.getString(key,"")
    }

    fun deleteEmotion() {
        val editor: SharedPreferences.Editor = sharedEmotionPreferences.edit()
        editor.clear()
        editor.apply()
    }

}