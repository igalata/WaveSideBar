package com.igalata.wavesidebardemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content.view.*

class MainActivity : AppCompatActivity() {

    private var adapter = FriendsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sideBar.view = LayoutInflater.from(this).inflate(R.layout.content, null, false).apply {
            recyclerView.adapter = adapter
            adapter.friends = getFriends("")
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    adapter.friends = getFriends(s?.toString() ?: "")
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            })
        }

        /*  friendsList.adapter = FriendsAdapter().apply {
              friends = getFriends("")
          }
          friendsList.layoutManager = LinearLayoutManager(this)*/
    }

    private fun getFriends(query: String): List<User> {
        val friends = arrayListOf(
                User("Alyssa Vincent", "https://randomuser.me/api/portraits/women/82.jpg", "Pully", true),
                User("Elya Bonnet", "https://randomuser.me/api/portraits/women/84.jpg", "Echichens", false),
                User("Anita Allen", "https://randomuser.me/api/portraits/women/19.jpg", "Warragul", true),
                User("Jovito Da Mata", "https://randomuser.me/api/portraits/men/14.jpg", "Palhoça", true),
                User("Soren Marie", "https://randomuser.me/api/portraits/men/12.jpg", "Montpreveyres", false),
                User("Karen Berry", "https://randomuser.me/api/portraits/women/6.jpg", "Edinburgh", true),
                User("Alyssa Vincent", "https://randomuser.me/api/portraits/women/82.jpg", "Pully", false),
                User("Elya Bonnet", "https://randomuser.me/api/portraits/women/84.jpg", "Echichens", true),
                User("Anita Allen", "https://randomuser.me/api/portraits/women/19.jpg", "Warragul", false),
                User("Jovito Da Mata", "https://randomuser.me/api/portraits/men/14.jpg", "Palhoça", true),
                User("Soren Marie", "https://randomuser.me/api/portraits/men/12.jpg", "Montpreveyres", true),
                User("Karen Berry", "https://randomuser.me/api/portraits/women/6.jpg", "Edinburgh", false)
        )

        return friends.filter { it.name.startsWith(query, true) }
    }

    class User(val name: String, val avatar: String, val city: String, val online: Boolean)
}
