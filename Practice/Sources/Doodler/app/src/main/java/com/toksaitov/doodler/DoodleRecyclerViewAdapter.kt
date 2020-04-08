package com.toksaitov.doodler

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper

import com.toksaitov.doodler.DoodleFragment.OnListFragmentInteractionListener
import com.toksaitov.doodler.data.Doodle

import kotlinx.android.synthetic.main.fragment_doodle.view.*

class DoodleRecyclerViewAdapter(
    private var recyclerView: RecyclerView,
    private val listener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<DoodleRecyclerViewAdapter.ViewHolder>() {
    private val onClickListener: View.OnClickListener
    private var doodles = emptyList<Doodle>()

    inner class SwipeToDeleteCallback :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false;
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            listener?.onListFragmentSwipe(viewHolder.itemView.tag as Doodle)
        }
    }

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Doodle
            listener?.onListFragmentClick(item)
        }

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback())
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun setDoodles(doodles: List<Doodle>) {
        this.doodles = doodles
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_doodle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = doodles[position]
        holder.nameView.text = item.name

        with(holder.view) {
            tag = item; setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount(): Int = doodles.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.name
    }
}
