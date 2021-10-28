package com.example.dummyapp.ui.list

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dummyapp.HomeViewModel
import com.example.dummyapp.ResponseWrapper
import com.example.dummyapp.databinding.ListFragmentBinding
import com.example.dummyapp.db.User
import java.lang.IllegalStateException

class ListFragment : Fragment() {

    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var binding : ListFragmentBinding
    private lateinit var viewModel: HomeViewModel
    private var adapter : UserAdapter? = null
    private var interactor: ListFragmentInteractor? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is ListFragmentInteractor){
            interactor = activity as ListFragmentInteractor
        } else {
            throw IllegalStateException("ListFragment should implement ListFragmentInteractor")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListFragmentBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        adapter = UserAdapter(interaction = {
            interactor?.selectUser(it)
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
                val visibleItemCount = recyclerView.layoutManager?.childCount ?: 0
                val lastVisibleItemPosition = (recyclerView.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition() ?: 0
                viewModel.onScrolled(totalItemCount, visibleItemCount, lastVisibleItemPosition)
            }
        })

        viewModel.userListLD.observe(viewLifecycleOwner, Observer { response ->

            when(response) {
                is ResponseWrapper.Loading<*> -> {
                    if (adapter?.itemCount == 0) {
                        showOrHideProgressBar(true)
                        showOrHideRecyclerView(false)
                        showOrHideMessageBox(false)
                    }
                }

                is ResponseWrapper.Success<*> -> {

                    (response.data as? List<User>)?.takeIf { it.isNotEmpty() }?.let {
                        showOrHideProgressBar(false)
                        showOrHideRecyclerView(true)
                        showOrHideMessageBox(false)
                        adapter?.setUserList(it)
                        adapter?.notifyDataSetChanged()
                    }
                }

                is ResponseWrapper.Error -> {
                    if (adapter?.itemCount == 0) {
                        showOrHideProgressBar(false)
                        showOrHideRecyclerView(false)
                        showOrHideMessageBox(true,getString(response.stringId))
                    } else {
                        Toast.makeText(context,getString(response.stringId),Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })
    }

    private fun showOrHideProgressBar(toBeShown : Boolean) {
        binding.userListProgressBar.visibility = if (toBeShown) View.VISIBLE else View.GONE
    }

    private fun showOrHideRecyclerView(toBeShown : Boolean) {
        binding.recyclerView.visibility = if (toBeShown) View.VISIBLE else View.GONE
    }

    private fun showOrHideMessageBox(toBeShown: Boolean, message : String? = null){
        binding.userListMessageBox.visibility = if (toBeShown) View.VISIBLE else View.GONE
        message?.let { binding.userListMessageBox.text = it }
    }

    override fun onDetach() {
        super.onDetach()
        interactor = null
    }

    interface ListFragmentInteractor {
        fun selectUser(userData : User)
    }

}