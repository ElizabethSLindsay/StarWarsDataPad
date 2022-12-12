package com.example.starwarsdatapad.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.starwarsdatapad.data.QuestName
import com.example.starwarsdatapad.databinding.ListItemQuestBinding

class QuestAdapter(
    private val clickListener: (QuestName) -> Unit
): ListAdapter<QuestName, QuestAdapter.QuestViewHolder>(DiffCallback) {

    class QuestViewHolder(val binding: ListItemQuestBinding
        ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestViewHolder {
        val binding = ListItemQuestBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return QuestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestViewHolder, position: Int) {
        val questName = getItem(position)
        holder.binding.questName.text = questName.name

        holder.itemView.setOnClickListener {
            clickListener(questName)
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<QuestName>() {
        override fun areItemsTheSame(oldItem: QuestName, newItem: QuestName): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: QuestName, newItem: QuestName): Boolean {
            return oldItem == newItem
        }
    }
}