package com.cermati.test.di

import android.content.Context
import com.cermati.test.model.UserRepository
import com.cermati.test.model.UserDataSource

object Injection {

    //it could be a singleton
    fun providerRepository(context:Context): UserDataSource {
        return UserRepository(context)
    }
}