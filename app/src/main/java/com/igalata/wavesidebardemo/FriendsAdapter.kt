package com.igalata.wavesidebardemo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chat_list_item.view.*

/**
 * Created by irinagalata on 1/30/18.
 */
class FriendsAdapter(private val expanded: Boolean) : RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    var friends: List<User> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(
                if (!expanded) R.layout.list_item else R.layout.chat_list_item,
                parent, false))
    }

    override fun getItemCount() = friends.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val user = friends[position]
        holder?.view?.apply {
            Picasso.with(context).load(user.avatar).into(avatar)
            textName.text = user.name
            textCity.text = user.city
            setOnClickListener {
                Toast.makeText(context, user.name, Toast.LENGTH_SHORT).show()
            }
        }
    }


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}