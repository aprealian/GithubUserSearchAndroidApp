package com.cermati.test.myapi.repository

import android.content.Context
import com.cermati.test.myapi.datasource.SearchUsersApiInterface
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class SearchUsersApiClient(val context: Context) : ApiClient(context) {

    private var servicesApiInterface: SearchUsersApiInterface?=null

    fun build(): SearchUsersApiInterface?{

        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor())
        httpClient.addInterceptor(ChuckInterceptor(context))

        val retrofit: Retrofit = builder.client(httpClient.build()).build()
        servicesApiInterface = retrofit.create(
            SearchUsersApiInterface::class.java)

        return servicesApiInterface as SearchUsersApiInterface
    }

}