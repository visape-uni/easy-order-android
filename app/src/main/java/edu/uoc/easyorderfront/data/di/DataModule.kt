package edu.uoc.easyorderfront.data.di

import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.data.authentication.AuthenticationBackendDataSource
import edu.uoc.easyorderfront.data.authentication.AuthenticationRepository
import edu.uoc.easyorderfront.data.authentication.AuthenticationRepositoryImpl
import edu.uoc.easyorderfront.data.authentication.FirebaseDataSource
import edu.uoc.easyorderfront.data.menu.MenuBackendDataSource
import edu.uoc.easyorderfront.data.menu.MenuRepository
import edu.uoc.easyorderfront.data.menu.MenuRepositoryImpl
import edu.uoc.easyorderfront.data.network.Network
import edu.uoc.easyorderfront.data.order.OrderBackendDataSource
import edu.uoc.easyorderfront.data.order.OrderRepository
import edu.uoc.easyorderfront.data.order.OrderRepositoryImpl
import edu.uoc.easyorderfront.data.payment.PaymentBackendDataSource
import edu.uoc.easyorderfront.data.payment.PaymentRepository
import edu.uoc.easyorderfront.data.payment.PaymentRepositoryImpl
import edu.uoc.easyorderfront.data.profile.ProfileBackendDataSource
import edu.uoc.easyorderfront.data.profile.ProfileRepository
import edu.uoc.easyorderfront.data.profile.ProfileRepositoryImpl
import edu.uoc.easyorderfront.data.restaurant.RestaurantBackendDataSource
import edu.uoc.easyorderfront.data.restaurant.RestaurantRepository
import edu.uoc.easyorderfront.data.restaurant.RestaurantRepositoryImpl
import edu.uoc.easyorderfront.data.table.TableBackendDataSource
import edu.uoc.easyorderfront.data.table.TableRepository
import edu.uoc.easyorderfront.data.table.TableRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    factory { Network.createHttpClient(androidContext()) }

    single { SessionManager(get()) }

    // DATASOURCE
    single { AuthenticationBackendDataSource(get()) }
    single { RestaurantBackendDataSource(get()) }
    single { ProfileBackendDataSource(get())}
    single { TableBackendDataSource(get()) }
    single { MenuBackendDataSource(get()) }
    single { OrderBackendDataSource(get()) }
    single { PaymentBackendDataSource(get()) }

    single { FirebaseDataSource() }

    // REPOSITORIES
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get())}
    single<RestaurantRepository> { RestaurantRepositoryImpl(get())}
    single<ProfileRepository> { ProfileRepositoryImpl(get())}
    single<TableRepository> { TableRepositoryImpl(get())}
    single<MenuRepository> { MenuRepositoryImpl(get())}
    single<OrderRepository> { OrderRepositoryImpl(get()) }
    single<PaymentRepository> { PaymentRepositoryImpl(get())}

}