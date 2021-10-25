package com.example.dummyapp

import androidx.lifecycle.*
import com.example.dummyapp.db.User
import com.example.libdummyapi.models.UserDetailsResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        //do nothing
    }

    private val _userDetailsLD = MutableLiveData<UserDetailsResponse>()

    val userListLD : LiveData<List<User>>
    get() = repository.getUserList()
    val userDetailsLD : LiveData<UserDetailsResponse>
    get() = _userDetailsLD

    init {
        fetchUserList()
    }

    private fun fetchUserList() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            repository.fetchUserList()
        }
    }

    fun fetchUserDetails(userId : String) {
        viewModelScope.launch {
            _userDetailsLD.postValue(repository.fetchUserDetails(userId))
        }
    }

    fun onScrolled(totalItemCount: Int, visibleItemCount: Int, lastVisibleItemPosition: Int) {
        val shouldFetchMore = visibleItemCount + lastVisibleItemPosition + THRESHOLD >= totalItemCount
        if (shouldFetchMore) {
            fetchUserList()
        }
    }

    companion object {
        const val THRESHOLD : Int = 5
    }
}

class HomeViewModelFactory(private val repository: HomeRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}