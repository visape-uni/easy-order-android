package edu.uoc.easyorderfront.ui.table

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import edu.uoc.easyorderfront.R
import kotlinx.android.synthetic.main.activity_ocupar_mesa.*
import java.io.File
import java.io.FileInputStream

class OcupyTableActivity : AppCompatActivity() {
    private val TAG = "OcupyTableActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocupar_mesa)

        prepareUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ocupar_mesa, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.qr_btn -> {
                // TODO: ESCANEAR CODIGO QR
                // initScanner
                IntentIntegrator(this).initiateScan()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun prepareUI() {
        btn_ocupar_mesa.setOnClickListener({
            val codigoMesa = txt_codigo_mesa.text
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "El valor escaneado es: " + result.contents, Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}