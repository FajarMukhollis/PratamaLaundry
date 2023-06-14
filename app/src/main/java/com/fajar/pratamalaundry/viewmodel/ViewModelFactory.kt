package com.fajar.pratamalaundry.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fajar.pratamalaundry.di.Injection
import com.fajar.pratamalaundry.model.preference.UserPreference
import com.fajar.pratamalaundry.model.usecase.UserUseCase
import com.fajar.pratamalaundry.presentation.history.HistoryViewModel
import com.fajar.pratamalaundry.presentation.login.LoginViewModel
import com.fajar.pratamalaundry.presentation.main.MainViewModel
import com.fajar.pratamalaundry.presentation.profile.ProfileViewModel
import com.fajar.pratamalaundry.presentation.register.RegisterViewModel
import com.fajar.pratamalaundry.presentation.splashscreen.SplashViewModel
import com.fajar.pratamalaundry.presentation.transaction.TransactionViewModel

class ViewModelFactory(
    private val userUseCase: UserUseCase,
    private val pref: UserPreference,
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel(
                pref
            ) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(
                userUseCase, pref
            ) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(
                userUseCase
            ) as T
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(
                pref
            ) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(
                pref
            ) as T
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> HistoryViewModel(
                pref
            ) as T
            modelClass.isAssignableFrom(TransactionViewModel::class.java) -> TransactionViewModel(
                pref
            ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context, pref: DataStore<Preferences>): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideUserUseCase(),
                    UserPreference.getInstance(pref),
                )
            }.also { instance = it }
    }
}