package com.cermati.test.model

import android.content.Context
import android.util.Log
import com.cermati.test.R
import com.cermati.test.data.ApiClient
import com.cermati.test.data.UserResponse
import com.cermati.test.data.OperationCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TAG="CONSOLE"

class UserRepository(val context:Context):UserDataSource {

    private var call:Call<UserResponse>?=null

    override fun retrieveUsers(field: UserField, callback: OperationCallback) {

        call = ApiClient(context).build()?.users(field.search, field.page)

        call?.enqueue(object :Callback<UserResponse>{
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                callback.onError(t.message)
            }

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {

                when(response.code()){
                    200 -> {
                        response.body()?.let {
                            //if(response.isSuccessful && (it.isSuccess())){
                            val incompleteResult = it.incompleteResults

                            if(response.isSuccessful && incompleteResult != null && !incompleteResult){
                                Log.v(TAG, "data ${it.items}")
                                callback.onSuccess(it.items)
                            } else{

                                it.message?.let { message ->
                                    callback.onError(message)
                                } ?: run{
                                    callback.onError(context.getString(R.string.api_error_something_wrong))
                                }

                            }
                        }
                    }

                    403 -> {
                        //limit time excedeed
                        callback.onError(context.getString(R.string.api_error_excedeed_limit))
                    }

                    422 -> {
                        //validation failed, init request blank "q" param
                        callback.onError(null)
                    }

                    else -> {
                        callback.onError(context.getString(R.string.api_error_something_wrong))
                    }
                }
            }
        })
    }

    override fun cancel() {
        call?.let {
            it.cancel()
        }
    }
}