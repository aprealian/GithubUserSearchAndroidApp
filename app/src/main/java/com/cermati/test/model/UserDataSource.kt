package com.cermati.test.model

import com.cermati.test.data.OperationCallback

interface UserDataSource {

    fun retrieveUsers(keyword: UserField, callback: OperationCallback)
    fun cancel()
}