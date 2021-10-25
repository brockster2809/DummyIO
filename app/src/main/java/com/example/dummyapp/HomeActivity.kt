package com.example.dummyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import androidx.lifecycle.ViewModelProvider
import com.example.dummyapp.db.User
import com.example.dummyapp.db.UserDatabase
import com.example.dummyapp.ui.details.DetailsFragment
import com.example.dummyapp.ui.list.ListFragment
import com.example.libdummyapi.models.Data

class HomeActivity : AppCompatActivity(), ListFragment.ListFragmentInteractor {

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        viewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(
                HomeRepository(
                    UserDatabase.getInstance(context = baseContext).getUserDao()
                )
            )
        ).get(HomeViewModel::class.java)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ListFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun selectUser(userData: User) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, DetailsFragment.newInstance(userData.id))
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) finish()
        else super.onBackPressed()

    }
}