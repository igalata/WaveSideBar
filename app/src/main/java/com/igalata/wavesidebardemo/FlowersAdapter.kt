package com.igalata.wavesidebardemo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item.view.*

/**
 * Created by irinagalata on 1/30/18.
 */
class FlowersAdapter(private val images: Array<Int>, private val titles: Array<String>,
                     private val onClick: (String) -> Unit) :
        RecyclerView.Adapter<FlowersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(
                R.layout.list_item, parent, false))
    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.view?.apply {
            image.setImageResource(images[position])
            title.text = titles[position]
            setOnClickListener { onClick(titles[position]) }
        }
    }


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}