package edu.uoc.easyorderfront.ui.di

import edu.uoc.easyorderfront.ui.login.TabLoginViewModel
import edu.uoc.easyorderfront.ui.main.MainClientMenuViewModel
import edu.uoc.easyorderfront.ui.main.MainClientViewModel
import edu.uoc.easyorderfront.ui.main.MainWorkerMenuViewModel
import edu.uoc.easyorderfront.ui.menu.*
import edu.uoc.easyorderfront.ui.order.OrderClientViewModel
import edu.uoc.easyorderfront.ui.order.OrderWorkerViewModel
import edu.uoc.easyorderfront.ui.payment.PaymentMethodViewModel
import edu.uoc.easyorderfront.ui.payment.WaitingBillViewModel
import edu.uoc.easyorderfront.ui.profile.ClientProfileViewModel
import edu.uoc.easyorderfront.ui.profile.WorkerProfileViewModel
import edu.uoc.easyorderfront.ui.register.TabRegisterViewModel
import edu.uoc.easyorderfront.ui.restaurant.AddWorkerDialogViewModel
import edu.uoc.easyorderfront.ui.restaurant.CreateRestaurantViewModel
import edu.uoc.easyorderfront.ui.restaurant.RestaurantProfileViewModel
import edu.uoc.easyorderfront.ui.restaurant.WorkerListViewModel
import edu.uoc.easyorderfront.ui.table.CreateTableViewModel
import edu.uoc.easyorderfront.ui.table.OcupyTableViewModel
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
    viewModel { EditarMenuViewModel(get(), get()) }
    viewModel { CreateCategoryViewModel(get()) }
    viewModel { CreateDishViewModel(get()) }
    viewModel { OrderWorkerViewModel(get(), get())}
    viewModel { OcupyTableViewModel(get())}
    viewModel { MenuRestaurantViewModel(get(), get(), get(), get()) }
    viewModel { AddDishToOrderViewModel(get()) }
    viewModel { OrderClientViewModel(get()) }
    viewModel { MainClientMenuViewModel(get(), get()) }
    viewModel { MainWorkerMenuViewModel(get()) }
    viewModel { MainClientViewModel(get()) }
    viewModel { WorkerListViewModel(get()) }
    viewModel { AddWorkerDialogViewModel(get()) }
    viewModel { PaymentMethodViewModel(get()) }
    viewModel { WaitingBillViewModel(get()) }
}