package com.toksaitov.doodler

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.toksaitov.doodler.data.Doodle
import com.toksaitov.doodler.data.DoodleViewModel

class DoodleFragment : Fragment() {
    interface OnListFragmentInteractionListener {
        fun onListFragmentClick(doodle: Doodle)
        fun onListFragmentSwipe(doodle: Doodle)
    }

    companion object {
        @JvmStatic
        fun newInstance() = DoodleFragment()
    }

    private var listener: OnListFragmentInteractionListener? = null

    private lateinit var listView : RecyclerView
    private lateinit var adapter : DoodleRecyclerViewAdapter
    private lateinit var doodleViewModel: DoodleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listView = inflater.inflate(R.layout.fragment_doodle_list, container, false) as RecyclerView
        adapter = DoodleRecyclerViewAdapter(listView, listener)
        listView.adapter = adapter

        doodleViewModel = ViewModelProvider(this).get(DoodleViewModel::class.java)
        doodleViewModel.allDoodles.observe(viewLifecycleOwner, Observer { doodles ->
            doodles?.let { adapter.setDoodles(it) }
        })

        return listView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
