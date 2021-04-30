package edu.uoc.easyorderfront.ui.order

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import edu.uoc.easyorderfront.R
import kotlinx.android.synthetic.main.bottom_fragment_get_qr_code.view.*

class GetQrCodeActivity(
    private val bitmap: Bitmap
) : BottomSheetDialogFragment() {
    private val TAG = "GetQrCodeActivity"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_fragment_get_qr_code, container, false)

        prepareUI(view)

        return view;
    }

    fun prepareUI(view: View) {

        view.img_code_qr.setImageBitmap(bitmap)

    }
}