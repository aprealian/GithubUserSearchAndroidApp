package com.cermati.test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cermati.test.data.OperationCallback
import com.cermati.test.model.User
import com.cermati.test.model.UserDataSource
import com.cermati.test.model.UserField

class UserViewModel(private val repository: UserDataSource): ViewModel() {

    private val _users = MutableLiveData<List<User>>().apply { value = emptyList() }
    val users: LiveData<List<User>> = _users

    private val _isViewLoading=MutableLiveData<Boolean>()
    val isViewLoading:LiveData<Boolean> = _isViewLoading

    private val _onMessageError=MutableLiveData<Any>()
    val onMessageError:LiveData<Any> = _onMessageError

    private val _isEmptyList=MutableLiveData<Boolean>()
    val isEmptyList:LiveData<Boolean> = _isEmptyList

    val field = UserField("", 1, 10)


    fun loadUsers(){
        _isViewLoading.postValue(true)
        repository.retrieveUsers(field, object: OperationCallback {
            override fun onError(obj: Any?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue( obj)
            }

            override fun onSuccess(obj: Any?) {
                _isViewLoading.postValue(false)

                if(obj!=null && obj is List<*>){
                    if(obj.isEmpty()){
                        _isEmptyList.postValue(true)
                    }else{
                        _users.value= obj as List<User>
                    }
                }
            }
        })
    }

}