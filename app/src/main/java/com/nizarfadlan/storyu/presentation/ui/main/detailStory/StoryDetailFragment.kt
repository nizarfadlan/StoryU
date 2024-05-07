package com.nizarfadlan.storyu.presentation.ui.main.detailStory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.nizarfadlan.storyu.R
import com.nizarfadlan.storyu.databinding.FragmentStoryDetailBinding
import com.nizarfadlan.storyu.domain.common.ResultState
import com.nizarfadlan.storyu.domain.model.Story
import com.nizarfadlan.storyu.presentation.ui.base.BaseFragment
import com.nizarfadlan.storyu.utils.helpers.formatDate
import com.nizarfadlan.storyu.utils.helpers.gone
import com.nizarfadlan.storyu.utils.helpers.observe
import com.nizarfadlan.storyu.utils.helpers.setImageUrl
import com.nizarfadlan.storyu.utils.helpers.show
import com.nizarfadlan.storyu.utils.helpers.showSnackBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoryDetailFragment : BaseFragment<FragmentStoryDetailBinding>() {

    private lateinit var storyId: String

    private val storyViewModel: StoryDetailViewModel by viewModel()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentStoryDetailBinding =
        FragmentStoryDetailBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storyId = StoryDetailFragmentArgs.fromBundle(
            requireArguments()
        ).storyId

        if (storyId.isNotEmpty()) {
            initToolbar()
            initObserver()
        } else {
            backToList()
        }
    }

    private fun initToolbar() {
        binding.apply {
            toolbar.apply {
                tvPage.text = getString(R.string.title_fragment_story_detail)
                backButton.setOnClickListener { backToList() }
            }
        }
    }

    private fun initObserver() {
        observe(storyViewModel.getStory(storyId), ::onStoryDetailResult)
    }

    private fun onStoryDetailResult(result: ResultState<Story>) {
        when (result) {
            is ResultState.Loading -> showLoading(true)

            is ResultState.Success -> {
                showLoading(false)
                setStoryDetail(result.data)
            }

            is ResultState.Error -> {
                showLoading(false)
                binding.root.showSnackBar(result.message)
            }
        }
    }

    private fun setStoryDetail(story: Story) {
        binding.apply {
            ivDetailPhoto.setImageUrl(story.photoUrl)
            tvDetailName.text = story.name
            tvDetailDate.text = story.createdAt.formatDate()
            tvDetailDescription.text = story.description
        }
    }

    private fun backToList() {
        findNavController().navigateUp()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingLayout.root.apply {
            if (isLoading) show() else gone()
        }
    }

    companion object {
        const val EXTRA_STORY_ID = "storyId"
    }
}