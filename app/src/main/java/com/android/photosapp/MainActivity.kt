package com.android.photosapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.photosapp.adapter.ImageAdapter
import com.android.photosapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private val selectedImages: ArrayList<String> = ArrayList()
    private val startImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                getImage(result)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()
    }

    private fun setup() {
        initRecyclerView()
        initListener()
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        imageAdapter = ImageAdapter(selectedImages)
        recyclerView.adapter = imageAdapter
    }

    private fun initListener() {
        binding.btnSelectImages.setOnClickListener {
            if (binding.edtSizeofList.text.toString().trim().isNotEmpty()) {
                selectImages()
            } else {
                showToast(getString(R.string.total_size))
            }
        }
    }

    private fun getImage(result: ActivityResult) {
        if (result.data?.clipData != null) {
            selectedImages.clear()
            val count = result.data?.clipData?.itemCount ?: 0
            for (i in 0 until count) {
                val imageUri = result.data?.clipData?.getItemAt(i)?.uri.toString()
                selectedImages.add(imageUri)
            }
        } else if (result.data?.data != null) {
            selectedImages.clear()
            val imageUri = result.data?.data.toString()
            selectedImages.add(imageUri)
        }

        if (selectedImages.size == 2) {
            generateList()
        } else {
            showToast(getString(R.string.select_image))
            selectedImages.clear()
        }
    }

    private fun selectImages() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startImageResult.launch(Intent.createChooser(intent, getString(R.string.select_pictures)))
    }

    private fun generateList() {
        val image1 = selectedImages[0]
        val image2 = selectedImages[1]

        val totalItems = binding.edtSizeofList.text.toString().trim().toInt()
        val triangularIndices = generateTriangularNumbers(totalItems)

        val images = mutableListOf<String>()

        for (i in 0..totalItems) {
            if (i in triangularIndices) {
                images.add(image1)
            } else {
                images.add(image2)
            }
        }
        selectedImages.clear()
        selectedImages.addAll(images)
        imageAdapter.notifyDataSetChanged()
    }

    private fun generateTriangularNumbers(limit: Int): Set<Int> {
        val triangularNumbers = mutableSetOf<Int>()
        var sum = 0
        var n = 1

        while (sum <= limit) {
            sum += n
            triangularNumbers.add(sum)
            n++
        }

        return triangularNumbers
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
