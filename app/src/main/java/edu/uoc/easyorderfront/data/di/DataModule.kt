package edu.uoc.easyorderfront.data.di

import edu.uoc.easyorderfront.data.authentication.AuthenticationBackendDataSource
import edu.uoc.easyorderfront.data.authentication.AuthenticationRepository
import edu.uoc.easyorderfront.data.authentication.AuthenticationRepositoryImpl
import edu.uoc.easyorderfront.data.authentication.FirebaseDataSource
import edu.uoc.easyorderfront.data.network.Network
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    factory { Network.createHttpClient(androidContext()) }

    single { AuthenticationBackendDataSource(get()) }
    single { FirebaseDataSource() }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get())}
}