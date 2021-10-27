package com.example.dummyapp

import androidx.lifecycle.*
import com.example.dummyapp.db.User
import com.example.libdummyapi.models.UserDetailsResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private val userListExceptionHandler = CoroutineExceptionHandler { _, t ->
        when (t) {
            is UnknownHostException -> _userListLD.postValue(ResponseWrapper.Error(R.string.internet_not_available_for_listing_page))
            else -> _userListLD.postValue(ResponseWrapper.Error(R.string.error_occurred))
        }
    }

    private val userDetailsExceptionHandler = CoroutineExceptionHandler { _, t ->
        when (t) {
            is UnknownHostException -> _userDetailsLD.postValue(ResponseWrapper.Error(R.string.internet_not_available))
            else -> _userDetailsLD.postValue(ResponseWrapper.Error(R.string.error_occurred))
        }
    }

    private val _userDetailsLD = MutableLiveData<ResponseWrapper<UserDetailsResponse>>()
    private val _userListLD = MediatorLiveData<ResponseWrapper<List<User>>>()

    val userListLD : LiveData<ResponseWrapper<List<User>>>
    get() = _userListLD
    val userDetailsLD : LiveData<ResponseWrapper<UserDetailsResponse>>
    get() = _userDetailsLD

    init {
        _userListLD.addSource(repository.getUserList()) {
            _userListLD.value = ResponseWrapper.Success(it)
        }
        _userListLD.value = ResponseWrapper.Loading()
        fetchUserList()
    }

    private fun fetchUserList() {
        viewModelScope.launch(Dispatchers.IO + userListExceptionHandler) {
            repository.fetchUserList()
        }
    }

    fun fetchUserDetails(userId : String) {
        _userDetailsLD.value = ResponseWrapper.Loading()
        viewModelScope.launch(Dispatchers.IO + userDetailsExceptionHandler) {
            val userDetails = repository.fetchUserDetails(userId)
            userDetails?.let {
                _userDetailsLD.postValue(ResponseWrapper.Success(it))
            } ?: run {
                _userDetailsLD.postValue(ResponseWrapper.Error(R.string.user_details_not_found))
            }

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