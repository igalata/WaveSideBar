package com.igalata.wavesidebardemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
        setupSideBar()
        setupList()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    private fun setupSideBar() {
        sideBar.startColorRes = R.color.colorPrimary
        sideBar.endColorRes = R.color.colorAccent
        val content = LayoutInflater.from(this).inflate(R.layout.content,
                null, false)
        sideBar.view = content
        content.menu.setNavigationItemSelectedListener {
            Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setupList() {
        flowersList.adapter = FlowersAdapter(arrayOf(
                R.drawable.image_placeholder_1,
                R.drawable.image_placeholder_2,
                R.drawable.image_placeholder_3,
                R.drawable.image_placeholder_4,
                R.drawable.image_placeholder_5,
                R.drawable.image_placeholder_6
        ), resources.getStringArray(R.array.titles)) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        flowersList.layoutManager = GridLayoutManager(this, 2)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                sideBar.expand()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
