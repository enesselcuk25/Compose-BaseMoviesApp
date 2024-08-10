package com.enesselcuk.moviesui.domain.base


interface BaseUseCase<in input, output> {

    suspend fun execute(input: input): output



}