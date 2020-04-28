package com.cermati.test.di

import android.content.Context
import com.cermati.test.myapi.repository.UserRepository
import com.cermati.test.myapi.datasource.UserDataSource

object Injection {

    //it could be a singleton
    fun providerRepository(context:Context): UserDataSource {
        return UserRepository(context)
    }
}