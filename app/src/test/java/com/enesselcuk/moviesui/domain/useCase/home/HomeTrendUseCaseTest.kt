package com.enesselcuk.moviesui.domain.useCase.home

import com.enesselcuk.moviesui.data.repository.FakeRepositoryImpl
import com.enesselcuk.moviesui.data.repository.FakeRepositoryImpl.MoviesList.fakeTrendMovies
import com.enesselcuk.moviesui.util.NetworkResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeTrendUseCaseTest{


    @Mock
    private lateinit var homeTrendUseCase: HomeTrendUseCase

    @Mock lateinit var fakeRepositoryImpl: FakeRepositoryImpl


    @Before
    fun setUp(){
        fakeRepositoryImpl = FakeRepositoryImpl()
        homeTrendUseCase = HomeTrendUseCase(fakeRepositoryImpl)
    }

    @Test
    fun trendMovies()= runTest{
        // given
        val trendMovies = homeTrendUseCase.invoke().first()
        val fakeTrendingResponse = fakeTrendMovies

        // when
        when(trendMovies){
            is NetworkResult.Success -> {

                // then
                assertEquals(fakeTrendingResponse, trendMovies.data)
            }

            is NetworkResult.Error -> TODO()
            is NetworkResult.Loading -> TODO()
        }

    }
}