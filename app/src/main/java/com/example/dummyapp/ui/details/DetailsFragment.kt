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
import com.example.dummyapp.databinding.DetailsFragmentBinding

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

    private lateinit var binding : DetailsFragmentBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        viewModel.userDetailsLD.observe(viewLifecycleOwner, Observer {
            binding.userDetailsName.text = "${it.firstName} ${it.lastName}" //todo check this
        })

        val userid = arguments?.getString(ARG_USER_ID,"")
        userid?.let { viewModel.fetchUserDetails(it) }
    }
}