package com.alexlepadatu.ing.gitrepos.ui.fragments.repos.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexlepadatu.trendingrepos.R
import com.alexlepadatu.trendingrepos.domain.models.TrendingRepo

class RepositoryAdapter (private val itemSelectListener: (item: TrendingRepo) -> Unit): RecyclerView.Adapter<RepositoryViewHolder>() {
    var data : List<TrendingRepo> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_repo, parent, false)

        return RepositoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bindData(data[position], itemSelectListener)
    }
}