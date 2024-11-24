package com.storeonline.infrastructure.view

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.storeonline.application.client.ProductContract
import com.storeonline.databinding.ActivityAddProductBinding
import com.storeonline.infrastructure.repository.ProductDbRepository
import java.io.File


class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var database: SQLiteDatabase
    private lateinit var imageCapture: ImageCapture
    private var currentPhotoPath: String? = null

    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = ProductDbRepository(this)
        database = dbHelper.writableDatabase

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            setupCamera()
            } else {
                requestCameraPermission()
            }

        binding.btnCaptureImage.setOnClickListener {
            captureImage()
        }

        binding.btnAddProduct.setOnClickListener {
            val productName = binding.etProductName.text.toString()
            val productPrice = binding.etProductPrice.text.toString().toDoubleOrNull()

            if (productName.isNotEmpty() && productPrice != null && currentPhotoPath != null) {
                addProductToDb(productName, productPrice, currentPhotoPath!!)
                Toast.makeText(this, "Producto agregado con éxito", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val preview = Preview.Builder().build()

            imageCapture = ImageCapture.Builder().build()

            preview.setSurfaceProvider(binding.previewView.surfaceProvider)

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
            this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(this, "Error al configurar la cámara: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun captureImage() {
        val photoFile = File(externalMediaDirs.firstOrNull(), "${System.currentTimeMillis()}.jpg")
        currentPhotoPath = photoFile.absolutePath

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(this@AddProductActivity, "Imagen guardada: $currentPhotoPath", Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@AddProductActivity, "Error al capturar imagen: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupCamera()
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addProductToDb(name: String, price: Double, imagePath: String) {
        val values = ContentValues().apply {
            put(ProductContract.ProductEntry.COLUMN_NAME_NAME, name)
            put(ProductContract.ProductEntry.COLUMN_NAME_PRICE, price)
            put(ProductContract.ProductEntry.COLUMN_NAME_IMAGES, imagePath)
        }
        database.insert(ProductContract.ProductEntry.TABLE_NAME, null, values)
    }
}
