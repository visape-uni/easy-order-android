package edu.uoc.easyorderfront.ui.di

import edu.uoc.easyorderfront.ui.login.TabLoginViewModel
import edu.uoc.easyorderfront.ui.register.TabRegisterViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { TabRegisterViewModel(get()) }
    viewModel { TabLoginViewModel(get()) }
}