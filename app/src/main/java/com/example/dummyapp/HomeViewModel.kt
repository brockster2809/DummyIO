package com.example.dummyapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libdummyapi.models.Data
import com.example.libdummyapi.models.UserDetailsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository : HomeRepository = HomeRepository()

    private val _userListLD = MutableLiveData<List<Data>>(emptyList())
    private val _userDetailsLD = MutableLiveData<UserDetailsResponse>()

    val userListLD : LiveData<List<Data>>
    get() = _userListLD
    val userDetailsLD : LiveData<UserDetailsResponse>
    get() = _userDetailsLD

    init {
        fetchUserList()
    }

    fun fetchUserList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.fetchUserList()
            response?.let {
                _userListLD.postValue(it)
            }
        }
    }

    fun fetchUserDetails(userId : String) {
        viewModelScope.launch {
            _userDetailsLD.postValue(repository.fetchUserDetails(userId))
        }
    }
}