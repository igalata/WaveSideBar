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

    private var adapter = FriendsAdapter(false)

    private val friends = arrayListOf(
            User("Alyssa Vincent", "https://randomuser.me/api/portraits/women/82.jpg", "Pully"),
            User("Elya Bonnet", "http://www.fakepersongenerator.com/Face/female/female20151024016299896.jpg", "Echichens"),
            User("Anita Allen", "https://randomuser.me/api/portraits/women/19.jpg", "Warragul"),
            User("Jovito Da Mata", "http://www.fakepersongenerator.com/Face/male/male1084956637451.jpg", "Palhoça"),
            User("Soren Marie", "http://www.fakepersongenerator.com/Face/male/male1085571348236.jpg", "Montpreveyres"),
            User("Karen Berry", "http://www.fakepersongenerator.com/Face/female/female1021469382050.jpg", "Edinburgh"),
            User("Alyssa Vincent", "https://randomuser.me/api/portraits/women/82.jpg", "Pully"),
            User("Elya Bonnet", "http://www.fakepersongenerator.com/Face/female/female20151024016299896.jpg", "Echichens"),
            User("Anita Allen", "https://randomuser.me/api/portraits/women/19.jpg", "Warragul"),
            User("Jovito Da Mata", "http://www.fakepersongenerator.com/Face/male/male1084956637451.jpg", "Palhoça"),
            User("Soren Marie", "http://www.fakepersongenerator.com/Face/male/male1085571348236.jpg", "Montpreveyres"),
            User("Karen Berry", "http://www.fakepersongenerator.com/Face/female/female1021469382050.jpg", "Edinburgh")
    )

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
        sideBar.backgroundColorRes = R.color.backgroundColor

        friendsList.layoutManager = LinearLayoutManager(this)
        friendsList.adapter = FriendsAdapter(true).apply {
            friends = getFriends("")
        }
    }

    private fun getFriends(query: String) = friends.filter { it.name.contains(query, true) }
}
