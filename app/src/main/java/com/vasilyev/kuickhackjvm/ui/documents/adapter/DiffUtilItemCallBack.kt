package com.vasilyev.kuickhackjvm.ui.documents.adapter

import androidx.recyclerview.widget.DiffUtil
import com.vasilyev.kuickhackjvm.model.CheckingResult

class DiffUtilItemCallBack: DiffUtil.ItemCallback<CheckingResult>() {
    override fun areItemsTheSame(oldItem: CheckingResult, newItem: CheckingResult)
        = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CheckingResult, newItem: CheckingResult)
        = oldItem == newItem
}