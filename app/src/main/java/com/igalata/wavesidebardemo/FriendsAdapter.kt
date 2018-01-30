package com.igalata.wavesidebardemo

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

/**
 * Created by irinagalata on 1/30/18.
 */
class FriendsAdapter() : RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    var friends: List<MainActivity.User> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount() = friends.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val user = friends[position]
        holder?.view?.apply {
            Picasso.with(context).load(user.avatar).into(avatar)
            textName.text = user.name
            textCity.text = user.city
            marker.setBackgroundColor(ContextCompat.getColor(context,
                    if (user.online) android.R.color.holo_green_light else android.R.color.holo_red_light))
            setOnClickListener {
                Toast.makeText(context, user.name, Toast.LENGTH_SHORT).show()
            }
        }
    }


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}