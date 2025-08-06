package com.example.plantfinder

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.torchvision.TensorImageUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var module: Module
    private lateinit var labels: List<String>

    private val REQUEST_CODE_PERMISSIONS = 10
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    private val MODEL_ASSET_NAME = "plant_classifier_efficientnetb0.pts"
    private val LABELS_ASSET_NAME = "labels.txt"
    private val ASSET_CHECK_TAG = "AssetCheck"

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        previewView = findViewById(R.id.previewView)
//        val captureButton: Button = findViewById(R.id.captureButton)
//
//        // Request camera permissions
//        if (allPermissionsGranted()) {
//            startCamera()
//        } else {
//            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
//        }
//
//        // Load model and labels
//        module = Module.load(assetFilePath(this, "plant_classifier_efficientnetb0.pts"))
//        labels = assets.open("labels.txt").bufferedReader().readLines()
//
//        captureButton.setOnClickListener { takePhoto() }
//
//        cameraExecutor = Executors.newSingleThreadExecutor()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- START TEMPORARY ASSET DEBUGGING ---
        Log.d(ASSET_CHECK_TAG, "--- Checking Assets ---")
        try {
            val assetList = assets.list("") // List files in the root of the assets folder
            if (assetList != null && assetList.isNotEmpty()) {
                Log.d(ASSET_CHECK_TAG, "Files found in assets root:")
                for (asset in assetList) {
                    Log.d(ASSET_CHECK_TAG, "- $asset")
                }

                // Specifically check for your model and labels file
                if (assetList.contains(MODEL_ASSET_NAME)) {
                    Log.i(ASSET_CHECK_TAG, "SUCCESS: Model file '$MODEL_ASSET_NAME' found in assets.")
                } else {
                    Log.e(ASSET_CHECK_TAG, "ERROR: Model file '$MODEL_ASSET_NAME' NOT FOUND in assets root!")
                }
                if (assetList.contains(LABELS_ASSET_NAME)) {
                    Log.i(ASSET_CHECK_TAG, "SUCCESS: Labels file '$LABELS_ASSET_NAME' found in assets.")
                } else {
                    Log.e(ASSET_CHECK_TAG, "ERROR: Labels file '$LABELS_ASSET_NAME' NOT FOUND in assets root!")
                }

            } else {
                Log.e(ASSET_CHECK_TAG, "ERROR: Asset folder is empty or inaccessible, or assets.list(\"\") returned null/empty.")
            }
        } catch (e: IOException) {
            Log.e(ASSET_CHECK_TAG, "ERROR: IOException while listing assets", e)
        }
        Log.d(ASSET_CHECK_TAG, "--- Finished Checking Assets ---")
        // --- END TEMPORARY ASSET DEBUGGING ---

        previewView = findViewById(R.id.previewView)
        val captureButton: Button = findViewById(R.id.captureButton)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Load model and labels
        // Use a try-catch block here as well, as assetFilePath can throw an IOException
        // if the asset is not found (which is the problem you're debugging)
        try {
            val modelPath = assetFilePath(this, MODEL_ASSET_NAME)
            if (modelPath != null) { // assetFilePath now returns nullable if there's an issue
                module = Module.load(modelPath)
                Log.i("MainActivity", "Model loaded successfully from: $modelPath")
            } else {
                Log.e("MainActivity", "Failed to get model path for '$MODEL_ASSET_NAME'. Model will not be loaded.")
                // Handle the error appropriately - maybe show a Toast or disable capture button
                Toast.makeText(this, "Error: Model file not found. Classification disabled.", Toast.LENGTH_LONG).show()
                // Potentially disable the capture button if the model is essential
                // captureButton.isEnabled = false
                // Or you could finish the activity if the app cannot function without the model
                // finish()
                // For now, let's proceed and see if labels can be loaded.
            }

            labels = assets.open(LABELS_ASSET_NAME).bufferedReader().readLines()
            Log.i("MainActivity", "Labels loaded successfully.")

        } catch (e: IOException) {
            Log.e("MainActivity", "FATAL ERROR: Could not load model or labels from assets.", e)
            Toast.makeText(this, "Error: Critical files not found. App may not function.", Toast.LENGTH_LONG).show()
            // Depending on how critical these files are, you might want to finish the activity
            // finish()
        }


        captureButton.setOnClickListener { takePhoto() }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val photoFile = File.createTempFile("plant_", ".jpg", cacheDir)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(outputOptions, cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    classify(bitmap)
                }

                override fun onError(exc: ImageCaptureException) {
                    runOnUiThread {
                        Toast.makeText(baseContext, "Photo capture failed", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    private fun classify(bitmap: Bitmap) {
        val resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
        val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
            resized,
            TensorImageUtils.TORCHVISION_NORM_MEAN_RGB,
            TensorImageUtils.TORCHVISION_NORM_STD_RGB
        )
        val outputTensor = module.forward(IValue.from(inputTensor)).toTensor()
        val scores = outputTensor.dataAsFloatArray
        val maxIdx = scores.indices.maxByOrNull { scores[it] } ?: -1
        val plantName = labels[maxIdx]

        runOnUiThread {
            Toast.makeText(this, "Plant: $plantName", Toast.LENGTH_LONG).show()
        }
    }

//    private fun assetFilePath(context: Context, assetName: String): String {
//        val file = File(context.filesDir, assetName)
//        if (file.exists() && file.length() > 0) return file.absolutePath
//
//        context.assets.open(assetName).use { inputStream ->
//            FileOutputStream(file).use { outputStream ->
//                val buffer = ByteArray(4 * 1024)
//                var read: Int
//                while (inputStream.read(buffer).also { read = it } != -1) {
//                    outputStream.write(buffer, 0, read)
//                }
//                outputStream.flush()
//            }
//        }
//        return file.absolutePath
//    }

    private fun assetFilePath(context: Context, assetName: String): String? { // Return String?
        val file = File(context.filesDir, assetName)
        // Optimization: if file already exists in internal storage, return its path
        if (file.exists() && file.length() > 0) {
            Log.d(ASSET_CHECK_TAG, "Asset '$assetName' already exists in internal storage: ${file.absolutePath}")
            return file.absolutePath
        }

        // Try to copy the asset from the assets folder to internal storage
        try {
            context.assets.open(assetName).use { inputStream -> // This is where FileNotFoundException can occur
                FileOutputStream(file).use { outputStream ->
                    val buffer = ByteArray(4 * 1024)
                    var read: Int
                    while (inputStream.read(buffer).also { read = it } != -1) {
                        outputStream.write(buffer, 0, read)
                    }
                    outputStream.flush()
                }
            }
            Log.i(ASSET_CHECK_TAG, "Successfully copied asset '$assetName' to: ${file.absolutePath}")
            return file.absolutePath
        } catch (e: IOException) { // Catch IOException which includes FileNotFoundException
            Log.e(ASSET_CHECK_TAG, "Error copying asset '$assetName' from assets to internal storage.", e)
            // Optionally, try to delete the partially created file if an error occurred
            if (file.exists()) {
                file.delete()
            }
            return null // Return null if copying fails
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
