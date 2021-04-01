package me.ertugrul.superbottombar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import me.ertugrul.lib.OnItemReselectedListener
import me.ertugrul.lib.OnItemSelectedListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomBar.onItemSelected = { pos ->
            Log.e("onItemSelected", "$pos")
        }

        bottomBar.onItemReselected = { pos ->
            Log.e("onItemReselected", "$pos")
        }

        bottomBar.setOnItemSelectListener(object : OnItemSelectedListener {
            override fun onItemSelect(pos: Int) {
                Log.e("selectedListener", "$pos")
            }
        })

        bottomBar.setOnItemReselectListener(object : OnItemReselectedListener {
            override fun onItemReselect(pos: Int) {
                Log.e("reSelectedListener", "$pos")
            }
        })
    }
}