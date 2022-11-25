package com.sys_ky.kynotepad.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sys_ky.kynotepad.R
import com.sys_ky.kynotepad.room.entity.Memo
import java.util.*

class MemoListAdapter: ListAdapter<Memo, MemoListAdapter.MemoListViewHolder>(DiffCallback()) {

    interface OnItemClickListener: EventListener {
        fun onItemClickEvent(id: Int)
    }
    private var mOnItemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    class MemoListViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val listText = view.findViewById<TextView>(R.id.listText)
        var id: Int? = null

        fun bind(memo: Memo) {
            listText.text = memo.text
            id = memo.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MemoListViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemoListViewHolder, position: Int) {
        holder.bind(getItem(position))

        holder.listText.setOnClickListener(View.OnClickListener { view ->
            if (mOnItemClickListener != null && holder.id != null) {
                mOnItemClickListener!!.onItemClickEvent(holder.id!!)
            }
        })
    }

    private class DiffCallback: DiffUtil.ItemCallback<Memo>() {

        override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean {
            return oldItem == newItem
        }
    }
}

