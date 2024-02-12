package com.enesselcuk.moviesui.domain.tvDetail

import com.enesselcuk.moviesui.repos.reposRemote.Repos
import com.enesselcuk.moviesui.source.model.response.TvDetailResponse
import com.enesselcuk.moviesui.source.model.response.TvRecommendationsResponse
import com.enesselcuk.moviesui.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvRecommendationsUseCase @Inject constructor(private val repos: Repos) {

    suspend operator fun invoke(id: Int, language: String, page: Int) :  Flow<NetworkResult<TvRecommendationsResponse>> {
        return repos.getTvRecommendations(id,language,page)
    }
}