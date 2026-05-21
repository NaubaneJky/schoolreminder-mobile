package com.example.schoolreminderr.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.schoolreminderr.utils.NotificationHelper

class DeadlineReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Deadline Tugas"
        val message = intent.getStringExtra("message") ?: "Tugas akan segera berakhir!"
        val id = intent.getIntExtra("id", 0)
        NotificationHelper.showDeadlineNotification(context, title, message, id)
    }
}