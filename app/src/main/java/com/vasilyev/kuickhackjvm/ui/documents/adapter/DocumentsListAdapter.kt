package com.vasilyev.kuickhackjvm.ui.documents.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.vasilyev.kuickhackjvm.databinding.ItemRecentResultBinding
import com.vasilyev.kuickhackjvm.model.CheckingResult
import com.vasilyev.kuickhackjvm.utils.base64ToBitmap

class DocumentsListAdapter(
    private val menuActionListener: MenuActionListener
): ListAdapter<CheckingResult, ResultViewHolder>(DiffUtilItemCallBack()) {
    var onClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecentResultBinding.inflate(inflater, parent, false)

        return ResultViewHolder(binding, menuActionListener)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val resultChecking = currentList[position]

        with(holder.binding){
            tvDocNameFirst.text = resultChecking.documentName
            tvDocDateFirst.text = resultChecking.uploadDate
            ivPreview.setImageBitmap(base64ToBitmap(resultChecking.documentPreview))
        }

        holder.itemView.setOnClickListener {
            onClickListener?.invoke(resultChecking.id)
        }
    }

    interface MenuActionListener {
        fun onMenuItemClick(position: Int, action: String)
    }

}