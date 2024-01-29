package com.weave.project.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.weave.project.R
import com.weave.project.databinding.ActivityMainBinding
import com.weave.project.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        binding.bottomNavi.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_UNLABELED
        replaceFragment(HouseFragment())
        binding.bottomNavi.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_house->{
                    Log.i("MAIN", "house")
                    replaceFragment(HouseFragment())
                }
                R.id.item_cards->{
                    Log.i("MAIN", "cards")
                    replaceFragment(CardFragment())
                }
            }

            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fl_main, fragment).commit()
    }
}