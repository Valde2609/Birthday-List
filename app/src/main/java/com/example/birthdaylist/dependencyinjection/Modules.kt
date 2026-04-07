package com.example.birthdaylist.dependencyinjection

//import com.example.bookstoremvvmcoroutines.data.BooksAPI
//import com.example.bookstoremvvmcoroutines.data.BooksRepository
//import com.example.bookstoremvvmcoroutines.data.BooksRepositoryImpl
//import com.example.bookstoremvvmcoroutines.viewmodel.BooksViewModel
//import kotlinx.coroutines.Dispatchers
////import kotlinx.serialization.json.Json
//import org.koin.dsl.module
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//// jakewarton is now official retrofit
//
///*private val json = Json {
//    ignoreUnknownKeys = true
//    isLenient = true
//}*/
//
//val appModules = module {
//    single<BooksRepository> { BooksRepositoryImpl(get(), get()) }
//    single { Dispatchers.IO }
//    single { BooksViewModel(get()) }
//    single {
//        Retrofit.Builder()
//            .addConverterFactory(
//                GsonConverterFactory.create()
//                //json.asConverterFactory("application/json; charset=UTF8".toMediaType())
//                // retrofit converter not working, using Gson
//            )
//            .baseUrl("https://birthdaysrest.azurewebsites.net/api/")
//            .build()
//    }
//    single { get<Retrofit>().create(BooksAPI::class.java) }
//}