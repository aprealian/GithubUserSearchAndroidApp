package com.cermati.test.myapi.datasource

interface OperationCallback {
    fun onSuccess(obj:Any?)
    fun onError(obj:Any?)
}