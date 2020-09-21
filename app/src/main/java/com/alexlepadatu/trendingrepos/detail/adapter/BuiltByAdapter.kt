package com.alexlepadatu.trendingrepos.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexlepadatu.trendingrepos.R
import com.alexlepadatu.trendingrepos.domain.models.TrendingRepoAuthor

class BuiltByAdapter: RecyclerView.Adapter<BuiltByViewHolder>() {
    var data : List<TrendingRepoAuthor> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuiltByViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_built_by, parent, false)

        return BuiltByViewHolder(view)
    }

    override fun onBindViewHolder(holder: BuiltByViewHolder, position: Int) {
        holder.bindData(data[position])
    }
}