package com.example.rickandmorty

import android.app.Application
import com.example.rickandmorty.di.AppModule
import com.example.rickandmorty.di.AppModuleImpl

class Application : Application() {
    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}