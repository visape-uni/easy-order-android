package edu.uoc.easyorderfront.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.domain.model.Dish
import edu.uoc.easyorderfront.domain.model.Order
import edu.uoc.easyorderfront.domain.model.OrderedDish
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.android.synthetic.main.bottom_fragment_add_dish_to_order.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class AddDishToOrderFragment(
        private val order: Order,
        private val categoryId: String,
        private val dish: Dish,
        private val restaurantViewModel: MenuRestaurantViewModel
) : BottomSheetDialogFragment() {

    private val viewModel: AddDishToOrderViewModel by viewModel()
    private val TAG = "AddDishToOrderFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_fragment_add_dish_to_order, container, false)

        prepareUI(view)

        return view
    }

    fun prepareUI(view: View) {

        view.lbl_nombre.text = dish.name

        val df = DecimalFormat()
        df.maximumFractionDigits = 2
        df.minimumFractionDigits = 2
        df.roundingMode = RoundingMode.HALF_UP
        val priceText = df.format(dish.price)
        view.lbl_price.text = priceText + "€"

        viewModel.quantity.observe(this, {value ->
            view.lbl_cantidad.text = value.toString()
            if (value.equals(1)) {
                disableSubstractButton(view)
            } else if (value > 1){
                enableSubstractButton(view)
            }
        })

        disableSubstractButton(view)
        view.btn_substract.setOnClickListener({
            viewModel.quantity.postValue(viewModel.quantity.value!! - 1)
        })

        view.btn_add.setOnClickListener({
            viewModel.quantity.postValue(viewModel.quantity.value!! + 1)
        })

        view.btn_añadir.setOnClickListener({
            val totalPrice = dish.price.times(viewModel.quantity.value!!)

            val orderedDish = OrderedDish(
                Calendar.getInstance().timeInMillis.toString(),
                viewModel.quantity.value,
                totalPrice,
                categoryId,
                dish,
                true
            )

            order.price = order.price?.plus(totalPrice)
            order.orderedDishes?.add(orderedDish)

            restaurantViewModel.order.postValue(DataWrapper.success(order))
            dismiss()
        })

        view.btn_cancelar.setOnClickListener({
            dismiss()
        })
    }

    fun disableSubstractButton(view: View) {
        view.btn_substract.isClickable = false
        view.btn_substract.setTextColor(resources.getColor(R.color.light_orange))
    }

    fun enableSubstractButton(view: View) {
        view.btn_substract.isClickable = true
        view.btn_substract.setTextColor(resources.getColor(R.color.orange1))
    }
}