package com.cermati.test.myapi.datasource

import com.cermati.test.myapi.field.UserField

interface UserDataSource {
    fun retrieveUsers(keyword: UserField, callback: OperationCallback)
    fun cancel()
}