package edu.uoc.easyorderfront.data.di

import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.data.authentication.AuthenticationBackendDataSource
import edu.uoc.easyorderfront.data.authentication.AuthenticationRepository
import edu.uoc.easyorderfront.data.authentication.AuthenticationRepositoryImpl
import edu.uoc.easyorderfront.data.authentication.FirebaseDataSource
import edu.uoc.easyorderfront.data.network.Network
import edu.uoc.easyorderfront.data.profile.ProfileBackendDataSource
import edu.uoc.easyorderfront.data.profile.ProfileRepository
import edu.uoc.easyorderfront.data.profile.ProfileRepositoryImpl
import edu.uoc.easyorderfront.data.restaurant.RestaurantBackendDataSource
import edu.uoc.easyorderfront.data.restaurant.RestaurantRepository
import edu.uoc.easyorderfront.data.restaurant.RestaurantRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    factory { Network.createHttpClient(androidContext()) }

    single { SessionManager(get()) }

    // DATASOURCE
    single { AuthenticationBackendDataSource(get()) }
    single { RestaurantBackendDataSource(get()) }
    single { ProfileBackendDataSource(get())}

    single { FirebaseDataSource() }

    // REPOSITORIES
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get())}
    single<RestaurantRepository> { RestaurantRepositoryImpl(get())}
    single<ProfileRepository> { ProfileRepositoryImpl(get())}

}