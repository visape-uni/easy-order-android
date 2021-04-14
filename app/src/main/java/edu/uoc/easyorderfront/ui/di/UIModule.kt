package edu.uoc.easyorderfront.ui.di

import edu.uoc.easyorderfront.ui.login.TabLoginViewModel
import edu.uoc.easyorderfront.ui.profile.WorkerProfileViewModel
import edu.uoc.easyorderfront.ui.register.TabRegisterViewModel
import edu.uoc.easyorderfront.ui.restaurant.CreateRestaurantViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { TabRegisterViewModel(get()) }
    viewModel { TabLoginViewModel(get()) }
    viewModel { CreateRestaurantViewModel(get()) }
    viewModel { WorkerProfileViewModel(get(), get()) }
}