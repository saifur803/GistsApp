package com.saifurrahman.gist

import android.app.Application
import com.saifurrahman.gist.db.AppDatabase

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppDatabase.initialize(this)
    }
}