package com.jujare.databinding

import android.util.Log
import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jujare.databinding.db.User
import com.jujare.databinding.db.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel(), Observable {
    private val TAG = "UserViewModel"

    val users = repository.users
    private var isUpdateOrDelete = false
    private lateinit var userToUpdateOrDelete: User


    @Bindable
    val inputName = MutableLiveData<String>()

    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()

    @Bindable
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {
        Log.d(TAG,"SaveOrUpdate")

        if (inputName.value == null) {
            statusMessage.value = Event("Please enter user's name")
        } else if (inputEmail.value == null) {
            statusMessage.value = Event("Please enter user's email")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            statusMessage.value = Event("Please enter a correct email address")
        } else {
            if (isUpdateOrDelete) {
                userToUpdateOrDelete.name = inputName.value!!
                userToUpdateOrDelete.email = inputEmail.value!!
                update(userToUpdateOrDelete)
            } else {
                val name = inputName.value!!
                val email = inputEmail.value!!
                insert(User(0, name, email))
                inputName.value = null
                inputEmail.value = null
            }
        }


    }

    fun clearAllOrDelete() {
        Log.d(TAG,"clearAllOrDelete")
        if (isUpdateOrDelete) {
            delete(userToUpdateOrDelete)
        } else {
            clearAll()
        }

    }

    fun insert(user: User) = viewModelScope.launch {
        val newRowId = repository.insert(user)
        if (newRowId > -1) {
            statusMessage.value = Event("User Inserted Successfully $newRowId")
            Log.d(TAG,"User Inserted Successfully $newRowId")
        } else {
            statusMessage.value = Event("Error Occurred")
            Log.d(TAG,"Error Occurred")
        }
    }

    fun update(user: User) = viewModelScope.launch {
        val noOfRows = repository.update(user)
        if (noOfRows > 0) {
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRows Row Updated Successfully")
            Log.d(TAG,"$noOfRows Row Updated Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
            Log.d(TAG,"Error Occurred")
        }

    }

    fun delete(user: User) = viewModelScope.launch {
        val noOfRowsDeleted = repository.delete(user)

        if (noOfRowsDeleted > 0) {
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRowsDeleted Row Deleted Successfully")
            Log.d(TAG,"$noOfRowsDeleted Row Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
            Log.d(TAG,"Error Occurred")
        }

    }

    fun clearAll() = viewModelScope.launch {
        val noOfRowsDeleted = repository.deleteAll()
        if (noOfRowsDeleted > 0) {
            statusMessage.value = Event("$noOfRowsDeleted Users Deleted Successfully")
            Log.d(TAG,"$noOfRowsDeleted Row Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
            Log.d(TAG,"Error Occurred")
        }
    }

    fun initUpdateAndDelete(user: User) {
        Log.d(TAG,"initUpdateAndDelete")
        inputName.value = user.name
        inputEmail.value = user.email
        isUpdateOrDelete = true
        userToUpdateOrDelete = user
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"

    }


    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }


}