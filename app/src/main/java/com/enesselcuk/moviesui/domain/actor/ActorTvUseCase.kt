package com.enesselcuk.moviesui.domain.actor

import com.enesselcuk.moviesui.repos.reposRemote.Repos
import com.enesselcuk.moviesui.source.model.response.ActorDetailResponse
import com.enesselcuk.moviesui.source.model.response.ActorMoviesResponse
import com.enesselcuk.moviesui.source.model.response.ActorTvResponse
import com.enesselcuk.moviesui.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ActorTvUseCase @Inject constructor(
    private val repos: Repos
) {
    suspend operator fun invoke(id: Int, language: String):Flow<NetworkResult<ActorTvResponse>> {
       return repos.getActorTv(id, language)
    }
}