package com.vasilyev.kuickhackjvm.ui.documents.adapter

import android.view.ContextMenu
import android.view.MenuInflater
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.vasilyev.kuickhackjvm.R
import com.vasilyev.kuickhackjvm.databinding.ItemRecentResultBinding
import com.vasilyev.kuickhackjvm.model.CheckingResult
class ResultViewHolder(
    val binding: ItemRecentResultBinding,
    private val menuActionListener: DocumentsListAdapter.MenuActionListener
): RecyclerView.ViewHolder(binding.root) {

    init {
        binding.btnOptionFirst.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }

    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(view.context, view)
        popup.menuInflater.inflate(R.menu.item_context_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            val action = when (item.itemId) {
                R.id.delete -> "delete"
                else -> ""
            }
            if (action.isNotEmpty()) {
                menuActionListener.onMenuItemClick(adapterPosition, action)
                true
            } else {
                false
            }
        }
        popup.show()
    }
}
