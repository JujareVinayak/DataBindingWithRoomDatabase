package com.jujare.databinding

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jujare.databinding.databinding.ActivityMainBinding
import com.jujare.databinding.db.User
import com.jujare.databinding.db.UserDatabase
import com.jujare.databinding.db.UserRepository

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: MyRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val dao = UserDatabase.getInstance(application).userDAO
        val repository = UserRepository(dao)
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this,factory).get(UserViewModel::class.java)
        binding.myViewModel = userViewModel
        binding.setLifecycleOwner(this)
        initRecyclerView()

        userViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun initRecyclerView(){
        Log.d(TAG,"initRecyclerView")
        binding.userRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyRecyclerViewAdapter { selectedItem: User ->listItemClicked(selectedItem)}
        binding.userRecyclerView.adapter = adapter
        displayUsersList()
    }

    private fun displayUsersList(){
        userViewModel.users.observe(this, Observer {
            Log.d(TAG, "displayUsersList$it")
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(user: User){
        Toast.makeText(this,"selected name is ${user.name}",Toast.LENGTH_LONG).show()
        Log.d(TAG,"selected name is ${user.name}")
        userViewModel.initUpdateAndDelete(user)
    }
}
