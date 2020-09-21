package com.alexlepadatu.trendingrepos.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.alexlepadatu.ing.gitrepos.ui.fragments.repos.adapters.RepositoryAdapter
import com.alexlepadatu.trendingrepos.R
import com.alexlepadatu.trendingrepos.base.BaseFragment
import com.alexlepadatu.trendingrepos.data.api.GitApi
import com.alexlepadatu.trendingrepos.data.api.GitService
import com.alexlepadatu.trendingrepos.data.database.AppDatabase
import com.alexlepadatu.trendingrepos.data.repository.trending.TrendingReposRepositoryImpl
import com.alexlepadatu.trendingrepos.detail.DetailRepoFragment
import com.alexlepadatu.trendingrepos.domain.common.ResultState
import com.alexlepadatu.trendingrepos.domain.common.RuntimeSchedulerProvider
import com.alexlepadatu.trendingrepos.domain.useCase.TrendingReposUseCase
import com.alexlepadatu.trendingrepos.domain.useCase.TrendingReposUseCaseImpl
import kotlinx.android.synthetic.main.fragment_repos.*


class ListReposFragment : BaseFragment() {
    private val viewModel: ListReposViewModel by viewModels {
        val repo = TrendingReposRepositoryImpl(
            GitService.getRetrofit().create(GitApi::class.java),
            AppDatabase.getInstance(requireContext()).repositoryItemDao()
        )
        val trendingReposUseCase : TrendingReposUseCase = TrendingReposUseCaseImpl(
            RuntimeSchedulerProvider(), repo)

        return@viewModels ListReposViewModelFactory(trendingReposUseCase)
    }

    private val loadingDialog: AlertDialog by lazy {
        AlertDialog.Builder(requireContext())
            .setView(LayoutInflater.from(requireActivity()).inflate(R.layout.component_loading, null))
            .setCancelable(false)
            .create()
    }

    private val adapter = RepositoryAdapter {
        val detailRepoFragment = DetailRepoFragment.getInstance(it)
        addFragment(detailRepoFragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View =
        inflater.inflate(R.layout.fragment_repos, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvData.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
                ContextCompat.getDrawable(requireContext(), R.drawable.divider)?.let {
                    setDrawable(it)
                }
            })

            adapter = this@ListReposFragment.adapter
        }

        initObservables()
    }

    private fun initObservables() {
        viewModel.trendingRepos.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResultState.Loading -> loadingDialog.show()

                is ResultState.Error -> {
                    loadingDialog.dismiss()

                    Toast.makeText(requireContext(), result.throwable.message, Toast.LENGTH_LONG)
                        .show()
                }

                is ResultState.Success ->  {
                    loadingDialog.dismiss()

                    result.data.let { items ->
                        adapter.data = items
                        rvData.adapter?.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}