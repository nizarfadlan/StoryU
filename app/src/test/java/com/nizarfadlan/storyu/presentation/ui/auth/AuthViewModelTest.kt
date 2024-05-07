package com.nizarfadlan.storyu.presentation.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nizarfadlan.storyu.data.repository.AuthRepositoryImpl
import com.nizarfadlan.storyu.domain.common.ResultState
import com.nizarfadlan.storyu.utils.DummyData
import com.nizarfadlan.storyu.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var authRepositoryImpl: AuthRepositoryImpl


    private lateinit var authViewModel: AuthViewModel

    private val dummyEmail = DummyData.emailDummy()
    private val dummyPassword = DummyData.passwordDummy()
    private val dummyToken = DummyData.tokenDummy()

    @Before
    fun setUp() {
        authRepositoryImpl = mock(authRepositoryImpl::class.java)
        authViewModel = AuthViewModel(authRepositoryImpl)
    }

    @Test
    fun `Sign in success and get result success`(): Unit = runTest {
        val expectedResult = ResultState.Success(dummyToken)
        val flowResult = flowOf(expectedResult)

        `when`(authRepositoryImpl.signIn(dummyEmail, dummyPassword)).thenReturn(flowResult)

        val observer = Observer<ResultState<String>> { resultState ->
            assertNotNull(resultState)
            assertTrue(resultState is ResultState.Success)
            assertEquals(expectedResult, resultState)
            assertEquals(dummyToken, (resultState as ResultState.Success).data)
        }

        val liveData = authViewModel.signIn(dummyEmail, dummyPassword)
        try {
            liveData.observeForever(observer)
            verify(authRepositoryImpl).signIn(dummyEmail, dummyPassword)
        } finally {
            liveData.removeObserver(observer)
        }
    }

    @Test
    fun `Sign in success and get error result with Exception`(): Unit = runTest {
        val expectedResult = ResultState.Error("Sign in failed")
        val flowResult = flowOf(expectedResult)

        `when`(authRepositoryImpl.signIn(dummyEmail, dummyPassword)).thenReturn(flowResult)

        val observer = Observer<ResultState<String>> { resultState ->
            assertNotNull(resultState)
            assertTrue(resultState is ResultState.Error)
            assertEquals(expectedResult, resultState)
        }

        val liveData = authViewModel.signIn(dummyEmail, dummyPassword)
        try {
            liveData.observeForever(observer)
            verify(authRepositoryImpl).signIn(dummyEmail, dummyPassword)
        } finally {
            liveData.removeObserver(observer)
        }
    }
}