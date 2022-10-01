package com.example.map.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.map.core.Resource
import com.example.map.data.repository.UserRepo
import kotlinx.coroutines.Dispatchers

class UserViewModel(private val repo: UserRepo) : ViewModel() {
    fun singIn(email:String, password:String) = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repo.signIn(email, password)))

        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

}


class UserViewModelFactory(private val repo: UserRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(UserRepo::class.java).newInstance(repo)
    }

}