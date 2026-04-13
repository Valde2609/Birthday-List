package com.example.birthdaylist.dependencyinjection

import com.example.birthdaylist.data.FriendsAPI
import com.example.birthdaylist.data.FriendsRepository
import com.example.birthdaylist.data.FriendsRepositoryImplementation
import com.example.birthdaylist.viewModel.AuthenticationViewModel
import com.example.birthdaylist.viewModel.FriendViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModules = module {
    single<FriendsRepository> { FriendsRepositoryImplementation(get(), get()) }
    single { Dispatchers.IO }
    viewModel { FriendViewModel(get()) }
    viewModel { AuthenticationViewModel() }
    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://birthdaysrest.azurewebsites.net/api/")
            .build()
    }
    single { get<Retrofit>().create(FriendsAPI::class.java) }
}
