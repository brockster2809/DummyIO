package com.example.dummyapp.ui.list

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dummyapp.HomeViewModel
import com.example.dummyapp.R
import com.example.libdummyapi.models.Data
import java.lang.IllegalStateException

class ListFragment : Fragment() {

    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var recyclerView : RecyclerView
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
        val view = inflater.inflate(R.layout.list_fragment, container, false)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        adapter = UserAdapter(interaction = {
            interactor?.selectUser(it)
        })

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        viewModel.userListLD.observe(viewLifecycleOwner, Observer {
            adapter?.setUserList(it)
            adapter?.notifyDataSetChanged()
        })
    }

    override fun onDetach() {
        super.onDetach()
        interactor = null
    }

    interface ListFragmentInteractor {
        fun selectUser(userData : Data)
    }

}