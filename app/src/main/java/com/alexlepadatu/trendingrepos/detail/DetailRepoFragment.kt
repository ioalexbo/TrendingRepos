package com.alexlepadatu.trendingrepos.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.alexlepadatu.ing.gitrepos.ui.fragments.repos.adapters.RepositoryViewHolder
import com.alexlepadatu.trendingrepos.R
import com.alexlepadatu.trendingrepos.base.BaseFragment
import com.alexlepadatu.trendingrepos.detail.adapter.BuiltByAdapter
import com.alexlepadatu.trendingrepos.domain.models.TrendingRepo
import com.google.android.flexbox.*
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailRepoFragment : BaseFragment() {
    companion object {
        private const val EXTRA_TRENDING_REPO = "EXTRA_TRENDING_REPO"

        fun getInstance(trendingRepo: TrendingRepo): DetailRepoFragment {
            val fragment = DetailRepoFragment()
            fragment.arguments = bundleOf(EXTRA_TRENDING_REPO to trendingRepo)
            return fragment
        }
    }

    private val adapter = BuiltByAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View =
        inflater.inflate(R.layout.fragment_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvBuiltBy.apply {
            layoutManager = FlexboxLayoutManager(view.context).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.FLEX_START
            }

            adapter = this@DetailRepoFragment.adapter
        }

        arguments?.let {
            setTrendingRepo(it.getParcelable(EXTRA_TRENDING_REPO))
        }
    }

    private fun setTrendingRepo(repo: TrendingRepo?) {
        repo?.let {
            val holder = RepositoryViewHolder(viewCellRepo)
            holder.bindData(it)

            adapter.data = repo.builtBy
        }
    }
}