package com.example.plantfinder

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.torchvision.TensorImageUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService
    private var module: Module? = null
    private var labels: List<String>? = null
    private lateinit var captureButtonContainer: FrameLayout
    private lateinit var scanningLineView: View
    private var scanningAnimator: Animator? = null
    private var hasCompletedCycle = false
    private lateinit var galleryButton: ImageButton
    private lateinit var imageView: ImageView
    private val REQUEST_CODE_PERMISSIONS = 10
    private val REQUIRED_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private val REQUEST_CODE_GALLERY = 101
    private val GALLERY_PERMISSION =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

    companion object {
        private const val TAG = "PlantFinderMain"
        private const val MODEL_ASSET_NAME = "plant_classifier_efficientnetb0.pt"
        private const val LABELS_ASSET_NAME = "labels.txt"
    }

    private val plantDescriptions: Map<String, String> = mapOf(
        "aloevera" to "Aloe Vera is known for its healing and soothing properties, often used for skin treatment.",
        "banana" to "Banana is a tropical fruit rich in potassium and a staple in many tropical regions.",
        "bilimbi" to "Bilimbi is a tangy tropical fruit often used in traditional cooking and pickling.",
        "cantaloupe" to "Cantaloupe is a sweet orange-fleshed melon, high in water content and refreshing.",
        "cassava" to "Cassava is a starchy root vegetable, a major source of carbohydrates in many tropical countries.",
        "corn" to "Corn, or maize, is a cereal grain with edible kernels and used in a variety of foods.",
        "cucumber" to "Cucumber is a hydrating vegetable commonly used in salads and skin care.",
        "curcuma" to "Curcuma, also known as turmeric, is a root used as a spice with anti-inflammatory properties.",
        "eggplant" to "Eggplant is a purple vegetable often used in cooking and known for its spongy texture.",
        "galangal" to "Galangal is a root similar to ginger, commonly used in Southeast Asian cooking.",
        "ginger" to "Ginger is a spicy root used in food and medicine for its anti-inflammatory effects.",
        "guava" to "Guava is a tropical fruit rich in vitamin C and fiber, with a sweet and tangy flavor.",
        "kale" to "Kale is a leafy green vegetable packed with nutrients and antioxidants.",
        "longbeans" to "Long beans are slender green beans used in stir-fries and traditional dishes.",
        "mango" to "Mango is a sweet tropical fruit loved for its juicy texture and rich flavor.",
        "melon" to "Melon refers to various sweet, water-rich fruits such as honeydew and cantaloupe.",
        "orange" to "Orange is a citrus fruit known for its bright color, vitamin C, and refreshing juice.",
        "paddy" to "Paddy refers to rice before it is husked — a vital food crop around the world.",
        "papaya" to "Papaya is a tropical fruit with orange flesh, rich in enzymes that aid digestion.",
        "peperchili" to "Pepper chili is a spicy fruit used to add heat and flavor to dishes.",
        "pineapple" to "Pineapple is a tropical fruit with sweet-tart flavor and high vitamin C content.",
        "pomelo" to "Pomelo is the largest citrus fruit, with a thick rind and mild sweet flesh.",
        "shallot" to "Shallot is a mild onion-like vegetable used to enhance flavor in cooking.",
        "soybeans" to "Soybeans are protein-rich legumes used to make tofu, soy milk, and many foods.",
        "spinach" to "Spinach is a leafy green vegetable rich in iron and vitamins, often eaten cooked or raw.",
        "sweetpotatoes" to "Sweet potatoes are nutritious tubers with a sweet taste and high in fiber and beta-carotene.",
        "tobacco" to "Tobacco is a plant whose leaves are used for making products like cigarettes and cigars.",
        "waterapple" to "Water apple is a crisp, juicy fruit also known as rose apple, common in tropical areas.",
        "watermelon" to "Watermelon is a refreshing fruit made up mostly of water and loved during hot seasons."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        previewView = findViewById(R.id.previewView)
        captureButtonContainer = findViewById(R.id.captureButtonContainer)
        scanningLineView = findViewById(R.id.scanning_line_view)
        scanningLineView.visibility = View.GONE
        galleryButton = findViewById(R.id.galleryButton)
        imageView = findViewById(R.id.imageView)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        loadModelAndLabels()

        galleryButton.setOnClickListener {
            val permissionStatus = ContextCompat.checkSelfPermission(this, GALLERY_PERMISSION)
            Log.d(TAG, "Gallery permission status: $permissionStatus")
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(GALLERY_PERMISSION), REQUEST_CODE_GALLERY)
            }
        }

        captureButtonContainer.setOnClickListener { takePhoto() }
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun loadModelAndLabels() {
        try {
            val modelPath = assetFilePath(this, MODEL_ASSET_NAME)
            if (modelPath != null) {
                module = Module.load(modelPath)
                Log.i(TAG, "Model loaded successfully from: $modelPath")
            } else {
                Log.e(TAG, "Failed to get path for model '$MODEL_ASSET_NAME'. Classification will be unavailable.")
                Toast.makeText(this, "Error: Model file not found. Classification disabled.", Toast.LENGTH_LONG).show()
            }

            assets.open(LABELS_ASSET_NAME).bufferedReader().use {
                labels = it.readLines().map { label -> label.lowercase(Locale.getDefault()) }
            }
            Log.i(TAG, "Labels ('$LABELS_ASSET_NAME') loaded successfully and converted to lowercase.")

        } catch (e: IOException) {
            Log.e(TAG, "Error loading model or labels.", e)
            Toast.makeText(this, "Error: Critical files not found. App may not function as expected.", Toast.LENGTH_LONG).show()
        }
    }

    private fun assetFilePath(context: Context, assetName: String): String? {
        val targetFile = File(context.filesDir, assetName)
        if (targetFile.exists() && targetFile.length() > 0) {
            Log.d(TAG, "Asset '$assetName' already exists in internal storage: ${targetFile.absolutePath}")
            return targetFile.absolutePath
        }
        Log.d(TAG, "Asset '$assetName' not found or empty in internal storage. Attempting to copy from APK assets.")
        try {
            context.assets.open(assetName).use { inputStream ->
                FileOutputStream(targetFile).use { outputStream ->
                    val buffer = ByteArray(4 * 1024)
                    var read: Int
                    while (inputStream.read(buffer).also { read = it } != -1) {
                        outputStream.write(buffer, 0, read)
                    }
                    outputStream.flush()
                }
            }
            Log.i(TAG, "Successfully copied asset '$assetName' from APK to: ${targetFile.absolutePath}")
            return targetFile.absolutePath
        } catch (e: IOException) {
            Log.e(TAG, "Error copying asset '$assetName' from APK assets to internal storage.", e)
            if (targetFile.exists()) {
                targetFile.delete()
            }
            return null
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Gallery permission granted")
                openGallery()
            } else {
                Log.d(TAG, "Gallery permission denied")
                Toast.makeText(this, "Gallery permission denied.", Toast.LENGTH_SHORT).show()
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
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                Log.i(TAG, "Camera started successfully.")
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed for camera.", exc)
                Toast.makeText(this, "Failed to start camera.", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        if (!::imageCapture.isInitialized) {
            Log.e(TAG, "ImageCapture is not initialized.")
            Toast.makeText(baseContext, "Camera not ready.", Toast.LENGTH_SHORT).show()
            return
        }
        val photoFile = File(externalCacheDir ?: cacheDir, "plant_${System.currentTimeMillis()}.jpg")
        Log.d(TAG, "Photo will be saved to: ${photoFile.absolutePath}")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(outputOptions, cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    Log.i(TAG, "Photo capture succeeded: $savedUri")
                    try {
                        val bitmap = contentResolver.openInputStream(savedUri)?.use {
                            BitmapFactory.decodeStream(it)
                        }
                        if (bitmap != null) {
                            runOnUiThread {
                                startScanningAnimation()
                            }
                            classify(bitmap)
                        } else {
                            Log.e(TAG, "BitmapFactory.decodeStream returned null for $savedUri")
                            runOnUiThread { Toast.makeText(baseContext, "Failed to decode image.", Toast.LENGTH_SHORT).show() }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error decoding bitmap or during classification preparation.", e)
                        runOnUiThread { Toast.makeText(baseContext, "Error processing image.", Toast.LENGTH_SHORT).show() }
                    }
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                    runOnUiThread {
                        Toast.makeText(baseContext, "Photo capture failed: ${exc.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    private fun classify(bitmap: Bitmap) {
        val currentModule = module
        val currentLabels = labels

        if (currentModule == null) {
            Log.e(TAG, "Classification cannot proceed: PyTorch module is not initialized.")
            runOnUiThread { Toast.makeText(this, "Error: Model not loaded. Cannot classify.", Toast.LENGTH_LONG).show() }
            return
        }
        if (currentLabels == null || currentLabels.isEmpty()) {
            Log.e(TAG, "Classification cannot proceed: Labels are not loaded or empty.")
            runOnUiThread { Toast.makeText(this, "Error: Labels not loaded. Cannot classify.", Toast.LENGTH_LONG).show() }
            return
        }

        try {
            val bitmapToProcess: Bitmap
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && bitmap.config == Bitmap.Config.HARDWARE) {
                Log.d(TAG, "Input bitmap is HARDWARE, converting to software ARGB_8888.")
                bitmapToProcess = bitmap.copy(Bitmap.Config.ARGB_8888, true)

            } else {
                Log.d(TAG, "Input bitmap is already software or on an older API level.")
                bitmapToProcess = bitmap
            }

            val resizedBitmap = Bitmap.createScaledBitmap(bitmapToProcess, 224, 224, true)
            val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
                resizedBitmap,
                TensorImageUtils.TORCHVISION_NORM_MEAN_RGB,
                TensorImageUtils.TORCHVISION_NORM_STD_RGB
            )
            val outputTensor = currentModule.forward(IValue.from(inputTensor)).toTensor()
            val scores = outputTensor.dataAsFloatArray

            if (scores.isEmpty()) {
                Log.e(TAG, "Classification failed: scores array is empty.")
                runOnUiThread { Toast.makeText(this, "Classification produced no results.", Toast.LENGTH_LONG).show() }
                return
            }

            var maxIdx = -1
            var maxScore = -Float.MAX_VALUE
            for (i in scores.indices) {
                if (scores[i] > maxScore) {
                    maxScore = scores[i]
                    maxIdx = i
                }
            }

            if (maxIdx != -1 && maxIdx < currentLabels.size) {
                val plantName = currentLabels[maxIdx]
                Log.i(TAG, "Classification result: $plantName (Score: $maxScore)")
                runOnUiThread {
                    stopScanningAnimation { showPlantBottomSheet(plantName) }
                }
            } else {
                Log.e(TAG, "Classification failed: maxIdx is invalid ($maxIdx) or out of bounds for labels (size ${currentLabels.size}).")
                runOnUiThread { Toast.makeText(this, "Could not classify plant.", Toast.LENGTH_LONG).show() }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error during classification.", e)
            // Check if the exception is the specific IllegalStateException for more targeted logging
            if (e is IllegalStateException && e.message?.contains("Config#HARDWARE") == true) {
                Log.e(TAG, "Still encountered hardware bitmap issue despite conversion attempt. Original bitmap config: ${bitmap.config}", e)
            }
            runOnUiThread { Toast.makeText(this, "Error during plant classification.", Toast.LENGTH_LONG).show() }
        }
    }

    private fun showPlantBottomSheet(name: String) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        dialog.setContentView(view)

        val nameText = view.findViewById<TextView>(R.id.plantName)
        val infoText = view.findViewById<TextView>(R.id.plantInfo)
        val bottomSheetRootLayout = view.findViewById<LinearLayout>(R.id.bottomSheetRootLayout)

        nameText.text = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        infoText.text = plantDescriptions[name] ?: "No information available for ${name.replaceFirstChar { it.titlecase(Locale.getDefault()) }}."

        val bottomSheetInternal = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheetInternal != null) {
            val behavior = BottomSheetBehavior.from(bottomSheetInternal)
            val windowMetrics = androidx.window.layout.WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
            val screenHeight = windowMetrics.bounds.height()
            val desiredHeight = screenHeight / 3
            bottomSheetRootLayout.minimumHeight = desiredHeight
            behavior.peekHeight = desiredHeight
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        // Add dismissal listener to restore camera preview and hide imageView
        dialog.setOnDismissListener {
            imageView.setImageDrawable(null)
            imageView.visibility = View.GONE
            previewView.visibility = View.VISIBLE
        }

        dialog.show()
    }

    private fun startScanningAnimation() {
        scanningLineView.visibility = View.VISIBLE

        val parentHeight = previewView.height.toFloat()
        val endY = parentHeight - scanningLineView.height

        val scanning = ObjectAnimator.ofFloat(scanningLineView, "translationY", 0f, endY).apply {
            duration = 2000
            interpolator = LinearInterpolator()
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator) {
                    hasCompletedCycle = true
                }
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
            })
        }

        scanningAnimator = AnimatorSet().apply {
            play(scanning)
            start()
        }
    }

    private fun stopScanningAnimation(onFinished: () -> Unit) {
        if (!hasCompletedCycle) {
            scanningLineView.postDelayed({
                stopScanningAnimation(onFinished)
            }, 100)
            return
        }

        scanningAnimator?.cancel()
        scanningAnimator = null
        scanningLineView.visibility = View.GONE
        hasCompletedCycle = false
        onFinished()
    }

    private fun openGallery() {
        Log.d(TAG, "Opening gallery picker")
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            val uri: Uri? = data?.data
            if (uri != null) {
                try {
                    Log.d(TAG, "Image selected from gallery: $uri")
                    val bitmap = contentResolver.openInputStream(uri)?.use {
                        BitmapFactory.decodeStream(it)
                    }
                    if (bitmap != null) {
                        // Show the picked image in imageView and hide camera preview
                        runOnUiThread {
                            imageView.setImageBitmap(bitmap)
                            imageView.visibility = View.VISIBLE
                            previewView.visibility = View.GONE
                        }
                        startScanningAnimation()
                        classify(bitmap)
                    } else {
                        Toast.makeText(this, "Unable to decode image.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error loading image from gallery", e)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        Log.d(TAG, "Camera executor shutdown.")
    }
}

