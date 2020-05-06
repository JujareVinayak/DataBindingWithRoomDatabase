package com.jujare.databinding


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jujare.databinding.databinding.ListItemBinding
import com.jujare.databinding.db.User


class MyRecyclerViewAdapter(private val clickListener:(User)->Unit)
    : RecyclerView.Adapter<MyViewHolder>()
{
    private val TAG = "MyRecyclerViewAdapter"
    private val usersList = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
      val layoutInflater = LayoutInflater.from(parent.context)
      val binding : ListItemBinding =
          DataBindingUtil.inflate(layoutInflater,R.layout.list_item,parent,false)
      return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return usersList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      holder.bind(usersList[position],clickListener)
    }

    fun setList(users: List<User>) {
        Log.d(TAG,"setList")
        usersList.clear()
        usersList.addAll(users)

    }

}

class MyViewHolder(val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root){

    fun bind(user: User,clickListener:(User)->Unit){
          binding.nameTextView.text = user.name
          binding.emailTextView.text = user.email
          binding.listItemLayout.setOnClickListener{
             clickListener(user)
          }
    }
}
