package edu.uoc.easyorderfront.ui.di

import edu.uoc.easyorderfront.ui.login.TabLoginViewModel
import edu.uoc.easyorderfront.ui.menu.CreateCategoryViewModel
import edu.uoc.easyorderfront.ui.menu.CreateDishViewModel
import edu.uoc.easyorderfront.ui.menu.EditarMenuViewModel
import edu.uoc.easyorderfront.ui.order.OrderWorkerViewModel
import edu.uoc.easyorderfront.ui.profile.ClientProfileViewModel
import edu.uoc.easyorderfront.ui.profile.WorkerProfileViewModel
import edu.uoc.easyorderfront.ui.register.TabRegisterViewModel
import edu.uoc.easyorderfront.ui.restaurant.CreateRestaurantViewModel
import edu.uoc.easyorderfront.ui.restaurant.RestaurantProfileViewModel
import edu.uoc.easyorderfront.ui.table.CreateTableViewModel
import edu.uoc.easyorderfront.ui.table.TableListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { TabRegisterViewModel(get()) }
    viewModel { TabLoginViewModel(get(), get()) }
    viewModel { CreateRestaurantViewModel(get()) }
    viewModel { WorkerProfileViewModel(get()) }
    viewModel { ClientProfileViewModel(get()) }
    viewModel { RestaurantProfileViewModel(get()) }
    viewModel { CreateTableViewModel(get()) }
    viewModel { TableListViewModel(get(), get()) }
    viewModel { OrderWorkerViewModel()}
    viewModel { EditarMenuViewModel(get(), get()) }
    viewModel { CreateCategoryViewModel(get()) }
    viewModel { CreateDishViewModel(get()) }
}