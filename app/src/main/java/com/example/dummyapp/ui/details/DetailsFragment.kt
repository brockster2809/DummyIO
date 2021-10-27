package com.example.dummyapp.ui.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.dummyapp.HomeViewModel
import com.example.dummyapp.R
import com.example.dummyapp.ResponseWrapper
import com.example.dummyapp.databinding.DetailsFragmentBinding
import com.example.libdummyapi.models.UserDetailsResponse

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

        viewModel.userDetailsLD.observe(viewLifecycleOwner, Observer { responseWrapper ->

            when (responseWrapper) {
                is ResponseWrapper.Loading -> {
                    showOrHideProgressBar(true)
                    showOrHideUserDetailsContainer(false)
                    showOrHideMessageContainer(false)
                }
                is ResponseWrapper.Success<*> -> {
                    showOrHideProgressBar(false)
                    showOrHideUserDetailsContainer(true)
                    showOrHideMessageContainer(false)
                    (responseWrapper.data as? UserDetailsResponse)?.let { renderUserDetails(it) }
                }
                is ResponseWrapper.Error -> {
                    showOrHideProgressBar(false)
                    showOrHideUserDetailsContainer(false)
                    showOrHideMessageContainer(true, getString(responseWrapper.stringId))
                }
            }
        })

        val userid = arguments?.getString(ARG_USER_ID,"")
        userid?.let { viewModel.fetchUserDetails(it) }
    }

    private fun renderUserDetails(userDetailsResponse: UserDetailsResponse) {
        context?.let { ctx ->
            Glide.with(ctx).load(userDetailsResponse.picture)
                .into(binding.userDetailsImage)
        }
        binding.userDetailsName.text = getString(
            R.string.user_details_name,
            userDetailsResponse.firstName,
            userDetailsResponse.lastName
        )
        binding.userDetailsGender.text =
            getString(R.string.user_details_gender, userDetailsResponse.gender)
        binding.userDetailsDateOfBirth.text =
            getString(R.string.user_details_dob, userDetailsResponse.dateOfBirth)
        binding.userDetailsRegisterDate.text =
            getString(R.string.user_details_register_date, userDetailsResponse.registerDate)
        binding.userDetailsEmail.text =
            getString(R.string.user_details_email, userDetailsResponse.email)
        binding.userDetailsPhone.text =
            getString(R.string.user_details_phone, userDetailsResponse.phone)
        binding.userDetailsState.text =
            getString(R.string.user_details_state, userDetailsResponse.location.state)
        binding.userDetailsStreet.text =
            getString(R.string.user_details_street, userDetailsResponse.location.street)
        binding.userDetailsCity.text =
            getString(R.string.user_details_city, userDetailsResponse.location.city)
        binding.userDetailsCountry.text =
            getString(R.string.user_details_country, userDetailsResponse.location.country)
        binding.userDetailsTimezone.text =
            getString(R.string.user_details_timezone, userDetailsResponse.location.timezone)
    }

    private fun showOrHideProgressBar(toBeShown : Boolean) {
        binding.userDetailsProgressBar.visibility = if (toBeShown) View.VISIBLE else View.GONE
    }

    private fun showOrHideUserDetailsContainer(toBeShown : Boolean) {
        binding.userDetailsContainer.visibility = if (toBeShown) View.VISIBLE else View.GONE
    }

    private fun showOrHideMessageContainer(toBeShown: Boolean, message : String? = null){
        binding.userDetailsMessageBox.visibility = if (toBeShown) View.VISIBLE else View.GONE
        message?.let { binding.userDetailsMessageBox.text = it }
    }

}