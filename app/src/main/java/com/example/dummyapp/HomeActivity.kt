package com.example.dummyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.dummyapp.ui.details.DetailsFragment
import com.example.dummyapp.ui.list.ListFragment
import com.example.libdummyapi.models.Data

class HomeActivity : AppCompatActivity(), ListFragment.ListFragmentInteractor {

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ListFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun selectUser(userData: Data) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, DetailsFragment.newInstance(userData.id))
            .addToBackStack(null)
            .commit()
    }
}