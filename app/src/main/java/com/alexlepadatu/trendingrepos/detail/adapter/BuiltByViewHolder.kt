package com.alexlepadatu.trendingrepos.detail.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alexlepadatu.trendingrepos.domain.models.TrendingRepoAuthor
import kotlinx.android.synthetic.main.cell_built_by.view.*

class BuiltByViewHolder (view: View): RecyclerView.ViewHolder(view) {
    private val imgBuilder: ImageView = view.imgBuilder
    private val lblAuthor: TextView = view.lblAuthor

    fun bindData(author: TrendingRepoAuthor) {
        lblAuthor.text = author.username
    }
}