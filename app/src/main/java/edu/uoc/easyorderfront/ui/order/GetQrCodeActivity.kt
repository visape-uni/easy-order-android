package edu.uoc.easyorderfront.ui.order

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        view.btn_guardar.setOnClickListener({
            guardarFichero()
        })

    }

    fun guardarFichero() {

        val fileName = "QR_code_${System.currentTimeMillis()}.jpg"

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val resolver = context?.contentResolver

        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val itemUri = resolver?.insert(collection, values)
        if (itemUri != null) {
            resolver.openFileDescriptor(itemUri, "w").use { parcelFileDescriptor ->
                val acos = ParcelFileDescriptor.AutoCloseOutputStream(parcelFileDescriptor)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, acos)
            }
            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(itemUri, values, null, null)
            Toast.makeText(context, "El QR se ha guardado correctamente", Toast.LENGTH_LONG).show()
        }
    }
}