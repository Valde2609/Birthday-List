package com.example.birthdaylist.dependencyinjection

import com.example.birthdaylist.data.FriendsAPI
import com.example.birthdaylist.data.FriendsRepository
import com.example.birthdaylist.data.FriendsRepositoryImplementation
import com.example.birthdaylist.viewModel.FriendsViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModules = module {
    single<FriendsRepository> { FriendsRepositoryImplementation(get(), get()) }
    single { Dispatchers.IO }
    single { FriendsViewModel() }
    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://birthdaysrest.azurewebsites.net/api/")
            .build()
    }
    single { get<Retrofit>().create(FriendsAPI::class.java) }
}
