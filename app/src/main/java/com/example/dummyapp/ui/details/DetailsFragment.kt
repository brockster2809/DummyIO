package com.example.dummyapp.ui.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.dummyapp.R
import com.example.dummyapp.HomeViewModel

class DetailsFragment : Fragment() {

    companion object {
        const val ARG_USER_ID = "user_id"

        fun newInstance(userId : String) : DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_USER_ID,userId)
            }
            return fragment
        }

    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var userDetailsTextView : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.details_fragment, container, false)
        userDetailsTextView = view.findViewById(R.id.user_details_name)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        viewModel.userDetailsLD.observe(viewLifecycleOwner, Observer {
            userDetailsTextView.text = it.firstName + " " + it.lastName //todo check this
        })

        val userid = arguments?.getString(ARG_USER_ID,"")
        userid?.let { viewModel.fetchUserDetails(it) }

    }

}