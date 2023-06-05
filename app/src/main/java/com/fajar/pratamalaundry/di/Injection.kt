package com.fajar.pratamalaundry.di

import com.fajar.pratamalaundry.model.remote.ApiConfig
import com.fajar.pratamalaundry.model.repository.IUserRepository
import com.fajar.pratamalaundry.model.repository.UserRepository
import com.fajar.pratamalaundry.model.usecase.UserInteract
import com.fajar.pratamalaundry.model.usecase.UserUseCase

object Injection {

    private fun provideUserRepository(): IUserRepository {
        val apiConfig = ApiConfig.getApiService()
        return UserRepository(apiConfig)
    }

    fun provideUserUseCase(): UserUseCase {
        val repository = provideUserRepository()
        return UserInteract(repository)
    }

}