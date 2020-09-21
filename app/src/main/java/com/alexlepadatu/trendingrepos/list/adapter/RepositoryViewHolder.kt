package com.alexlepadatu.ing.gitrepos.ui.fragments.repos.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alexlepadatu.trendingrepos.domain.models.TrendingRepo
import com.google.android.flexbox.*
import kotlinx.android.synthetic.main.cell_repo.view.*

class RepositoryViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val lblRepoName: TextView = view.lblRepoName
    private val lblRepoDescription: TextView = view.lblRepoDescription
    private val lblLanguage: TextView = view.lblLanguage
    private val lblStars: TextView = view.lblStars
    private val lblForks: TextView = view.lblForks

    fun bindData(repo: TrendingRepo, itemSelectListener: ((item: TrendingRepo) -> Unit)? = null) {
        lblRepoName.text = repo.name
        lblRepoDescription.text = repo.description
        lblLanguage.text = repo.language
        lblStars.text = repo.stars.toString()
        lblForks.text = repo.forks.toString()

        itemView.setOnClickListener {
            itemSelectListener?.invoke(repo)
        }
    }
}