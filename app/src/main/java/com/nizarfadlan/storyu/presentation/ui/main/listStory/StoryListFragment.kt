package com.nizarfadlan.storyu.presentation.ui.main.listStory

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.nizarfadlan.storyu.R
import com.nizarfadlan.storyu.databinding.FragmentStoryListBinding
import com.nizarfadlan.storyu.presentation.ui.auth.AuthViewModel
import com.nizarfadlan.storyu.presentation.ui.base.BaseFragment
import com.nizarfadlan.storyu.presentation.ui.main.addStory.StoryAddFragment.Companion.IS_UPLOAD_SUCCESS_BUNDLE
import com.nizarfadlan.storyu.presentation.ui.main.addStory.StoryAddFragment.Companion.IS_UPLOAD_SUCCESS_KEY
import com.nizarfadlan.storyu.presentation.ui.main.listStory.adapter.StoryListAdapter
import com.nizarfadlan.storyu.presentation.ui.main.listStory.adapter.StoryLoadStateAdapter
import com.nizarfadlan.storyu.presentation.ui.map.OnStoryMapActivityReadyCallback
import com.nizarfadlan.storyu.presentation.ui.map.StoryMapActivity
import com.nizarfadlan.storyu.utils.helpers.gone
import com.nizarfadlan.storyu.utils.helpers.observe
import com.nizarfadlan.storyu.utils.helpers.show
import com.nizarfadlan.storyu.utils.helpers.showSnackBar
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class StoryListFragment : BaseFragment<FragmentStoryListBinding>() {
    private val authViewModel: AuthViewModel by viewModel()
    private val storyViewModel: StoryListViewModel by viewModel()

    private val listAdapter by lazy { StoryListAdapter { storyId -> moveToDetail(storyId) } }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentStoryListBinding =
        FragmentStoryListBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.getSession().observe(viewLifecycleOwner) {
            binding.tvName.text = it.name
        }

        initRecyclerView()
        initObserver()
        initActions()
        resultState()
    }

    private fun resultState() {
        setFragmentResultListener(IS_UPLOAD_SUCCESS_KEY) { _, bundle ->
            val isUploadSuccess = bundle.getBoolean(IS_UPLOAD_SUCCESS_BUNDLE)
            Timber.d("isUploadSuccess: $isUploadSuccess")

            if (isUploadSuccess) {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.rvStory.scrollToPosition(0)
                }, 300)
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            rvStory.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = listAdapter.withLoadStateFooter(
                    footer = StoryLoadStateAdapter {
                        listAdapter.retry()
                    }
                )
                isNestedScrollingEnabled = false
            }

            listAdapter.addLoadStateListener { loadState ->
                if (loadState.append.endOfPaginationReached) {
                    if (listAdapter.itemCount < 1) {
                        showEmpty(true)
                    }
                }

                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        showLoading(true)
                    }

                    is LoadState.NotLoading -> {
                        showLoading(false)
                        binding.rvStory.scheduleLayoutAnimation()
                    }

                    is LoadState.Error -> {
                        showLoading(false)
                        binding.root.showSnackBar(getString(R.string.label_error))
                        showErrorLayout(true)
                    }

                    else -> showLoading(false)
                }
            }
        }
    }

    private fun initActions() {
        binding.apply {
            mapButton.setOnClickListener {
                moveToMapStories()
            }

            layoutRefresh.setOnRefreshListener {
                listAdapter.refresh()
                layoutRefresh.isRefreshing = false
            }

            errorLayout.retryButton.setOnClickListener {
                listAdapter.refresh()
                showErrorLayout(false)
            }
        }
    }

    private fun initObserver() {
        observe(storyViewModel.getStories()) { data ->
            listAdapter.submitData(lifecycle, data)
        }
    }

    private fun moveToDetail(storyId: String) {
        findNavController().navigate(
            StoryListFragmentDirections.actionStoryListFragmentToStoryDetailFragment(
                storyId
            )
        )
    }

    private fun moveToMapStories() {
        showLoading(true)

        StoryMapActivity.setOnReadyCallback(object : OnStoryMapActivityReadyCallback {
            override fun onReady() {
                showLoading(false)
            }
        })

        findNavController().navigate(
            StoryListFragmentDirections.actionStoryListFragmentToStoryMapActivity()
        )
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingLayout.root.apply {
            if (isLoading) show() else gone()
        }
    }

    private fun showEmpty(isEmpty: Boolean) {
        binding.apply {
            if (isEmpty) {
                emptyLayout.root.show()
                errorLayout.root.gone()
                rvStory.gone()
            } else {
                emptyLayout.root.gone()
                rvStory.show()
            }
        }
    }

    private fun showErrorLayout(isError: Boolean) {
        binding.apply {
            if (isError) {
                errorLayout.root.show()
                emptyLayout.root.gone()
                rvStory.gone()
            } else {
                errorLayout.root.gone()
                rvStory.show()
            }
        }
    }
}