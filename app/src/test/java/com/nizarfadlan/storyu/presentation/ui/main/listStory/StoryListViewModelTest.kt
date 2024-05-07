package com.nizarfadlan.storyu.presentation.ui.main.listStory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.nizarfadlan.storyu.data.repository.StoryRepositoryImpl
import com.nizarfadlan.storyu.domain.model.Story
import com.nizarfadlan.storyu.presentation.ui.main.listStory.adapter.StoryListAdapter
import com.nizarfadlan.storyu.utils.DummyData
import com.nizarfadlan.storyu.utils.MainDispatcherRule
import com.nizarfadlan.storyu.utils.PagingDataSourceTest
import com.nizarfadlan.storyu.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryListViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepositoryImpl: StoryRepositoryImpl

    private lateinit var storyListViewModel: StoryListViewModel

    @Before
    fun setup() {
        storyRepositoryImpl = mock(StoryRepositoryImpl::class.java)
        storyListViewModel = StoryListViewModel(storyRepositoryImpl)
    }

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dataDummyStories = DummyData.listStoryDummy()
        val data: PagingData<Story> = PagingDataSourceTest.snapshot(dataDummyStories)

        val dataStories = MutableLiveData<PagingData<Story>>()
        dataStories.value = data

        `when`(storyRepositoryImpl.getPagingStories()).thenReturn(dataStories)
        val actualData: PagingData<Story> = storyListViewModel.getStories().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainDispatcherRules.testDispatcher,
            workerDispatcher = mainDispatcherRules.testDispatcher
        )

        differ.submitData(actualData)

        assertNotNull(differ.snapshot())
        assertEquals(dataDummyStories.size, differ.snapshot().size)
        assertEquals(dataDummyStories[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val dataDummyStories = DummyData.emptyListStoryDummy()
        val data: PagingData<Story> = PagingDataSourceTest.snapshot(dataDummyStories)

        val dataStories = MutableLiveData<PagingData<Story>>()
        dataStories.value = data

        `when`(storyRepositoryImpl.getPagingStories()).thenReturn(dataStories)
        val actualData: PagingData<Story> = storyListViewModel.getStories().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainDispatcherRules.testDispatcher,
            workerDispatcher = mainDispatcherRules.testDispatcher
        )

        differ.submitData(actualData)

        assertEquals(0, differ.snapshot().size)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}