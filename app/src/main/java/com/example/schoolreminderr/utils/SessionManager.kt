package com.example.schoolreminderr.utils

import android.content.Context

class SessionManager(context: Context) {

    private val prefs =
        context.getSharedPreferences(
            "school_reminder",
            Context.MODE_PRIVATE
        )

    fun saveToken(token: String) {

        prefs.edit()
            .putString("TOKEN", token)
            .apply()

    }

    fun getToken(): String? {

        return prefs.getString("TOKEN", null)

    }

    fun saveRole(role: String) {

        prefs.edit()
            .putString("ROLE", role)
            .apply()

    }

    fun getRole(): String? {

        return prefs.getString("ROLE", null)

    }

    fun logout() {

        prefs.edit().clear().apply()

    }
}