package com.sys_ky.kynotepad

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sys_ky.kynotepad.adapter.MemoListAdapter
import com.sys_ky.kynotepad.room.DbCtrl
import com.sys_ky.kynotepad.room.entity.Memo

class ListFragment() : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val memoListView = view.findViewById<RecyclerView>(R.id.MemoListView)
        val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager(requireContext()).getOrientation())
        memoListView.addItemDecoration(dividerItemDecoration)

        val memoListAdapter = MemoListAdapter()

        memoListAdapter.setOnItemClickListener(object: MemoListAdapter.OnItemClickListener {
            override fun onItemClickEvent(id: Int) {
                val bundle: Bundle = Bundle()
                bundle.putInt(MainActivity.cParamKey, id)
                Navigation.findNavController(view).navigate(R.id.action_listFragment_to_detailFragment, bundle)
            }
        })

        val dbCtrl = DbCtrl.getInstance(requireContext())

        val memoList = dbCtrl.Memo().selectAll()
        memoListAdapter.submitList(memoList)
        memoListView.adapter =memoListAdapter

        val itemTouchHelper = ItemTouchHelper(object: SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val id: Int = (viewHolder as MemoListAdapter.MemoListViewHolder).id!!
                val fromPos: Int = viewHolder.adapterPosition
                val toPos: Int = target.adapterPosition
                dbCtrl.Memo().updateSortId(id, fromPos, toPos)

                memoListAdapter.notifyItemMoved(fromPos, toPos)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }
        })
        itemTouchHelper.attachToRecyclerView(memoListView)

        val addButton = view.findViewById<FloatingActionButton>(R.id.AddButton)
        addButton.setOnClickListener {
            val bundle: Bundle = Bundle()
            bundle.putInt(MainActivity.cParamKey, -1)
            Navigation.findNavController(view).navigate(R.id.action_listFragment_to_detailFragment, bundle)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}