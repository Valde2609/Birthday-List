package com.example.birthdaylist.data

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

//class BooksRepositoryImpl(
//    private val booksAPI: BooksAPI,
//    private val dispatcher: CoroutineDispatcher
//) : BooksRepository {
//    override suspend fun getBooks(): NetworkResult<List<Book>> {
//        return withContext(dispatcher) {
//            try {
//                val response = booksAPI.getBooks()
//                if (response.isSuccessful) {
//                    val body = response.body()
//                    if (body != null) {
//                        NetworkResult.Success(body)
//                    } else {
//                        NetworkResult.Error("Response body is null")
//                    }
//                } else {
//                    NetworkResult.Error(response.message())
//                }
//            } catch (e: CancellationException) {
//                throw e
//            } catch (e: Exception) {
//                NetworkResult.Error(e.message ?: "Unknown error")
//            }
//        }
//    }
//
//    override suspend fun addBook(book: Book): NetworkResult<Book> {
//        return withContext(dispatcher) {
//            try {
//                val response = booksAPI.addBook(book)
//                if (response.isSuccessful)
//                    if (response.body() != null)
//                        NetworkResult.Success(response.body()!!)
//                    else
//                        NetworkResult.Error("Response body is null")
//                else
//                    NetworkResult.Error(response.message())
//            } catch (e: CancellationException) {
//                throw e
//            } catch (e: Exception) {
//                NetworkResult.Error(e.message ?: "Unknown error")
//            }
//        }
//    }
//
//    override suspend fun deleteBook(bookId: Int): NetworkResult<Book> {
//        return withContext(dispatcher) {
//            try {
//                val response = booksAPI.deleteBook(bookId)
//                if (response.isSuccessful) {
//                    val body = response.body()
//                    if (body != null)
//                        NetworkResult.Success(body)
//                    else
//                        NetworkResult.Error("Response body is null")
//                } else
//                    NetworkResult.Error(response.message())
//            } catch (e: CancellationException) {
//                throw e
//            } catch (e: Exception) {
//                NetworkResult.Error(e.message ?: "Unknown error")
//            }
//        }
//    }
//
//
//    override suspend fun updateBook(bookId: Int, data: Book): NetworkResult<Book> {
//        return withContext(dispatcher) {
//            try {
//                val response = booksAPI.updateBook(bookId, data)
//                if (response.isSuccessful) {
//                    val body = response.body()
//                    if (body != null) {
//                        NetworkResult.Success(body)
//                    } else {
//                        NetworkResult.Error("Response body is null")
//                    }
//                    NetworkResult.Success(response.body()!!)
//                } else
//                    NetworkResult.Error(response.message())
//            } catch (e: CancellationException) {
//                throw e
//            } catch (e: Exception) {
//                NetworkResult.Error(e.message ?: "Unknown error")
//            }
//        }
//    }
//}