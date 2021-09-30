package com.example.dummyapp.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libdummyapi.models.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {
    private val repository : UserListRepository = UserListRepository()
    private val _userListLD = MutableLiveData<List<Data>>(emptyList())

    val userListLD : LiveData<List<Data>>
    get() = _userListLD

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
}