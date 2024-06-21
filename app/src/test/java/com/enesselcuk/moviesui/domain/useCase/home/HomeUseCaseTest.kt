package com.enesselcuk.moviesui.domain.useCase.home

import com.enesselcuk.moviesui.data.model.response.MoviesResponse
import com.enesselcuk.moviesui.data.repository.FakeRepositoryImpl
import com.enesselcuk.moviesui.data.repository.FakeRepositoryImpl.MoviesList.fakeMoviesResponse
import com.enesselcuk.moviesui.domain.repository.Repos
import com.enesselcuk.moviesui.util.NetworkResult
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeUseCaseTest{


    @Mock
    private lateinit var fakeRepos : FakeRepositoryImpl

    @Mock
    private lateinit var homeUseCase: HomeUseCase

    @Before
    fun setUp(){
        fakeRepos = FakeRepositoryImpl()
        homeUseCase = HomeUseCase(fakeRepos)
    }

    @Test
    fun getHome()= runTest{
        // given
        val movies = homeUseCase.invoke("title","tr",1).first()
        val moviesList = fakeMoviesResponse

        // when
        when(movies){
            is NetworkResult.Success -> {
                // then
               assertEquals(moviesList,movies.data)
            }

            is NetworkResult.Error -> TODO()
            is NetworkResult.Loading -> TODO()
        }

    }
}