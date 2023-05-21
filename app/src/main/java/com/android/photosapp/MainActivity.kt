package com.android.photosapp;

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.photosapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private val selectedImages: ArrayList<String> = ArrayList()
    private val REQUEST_CODE_SELECT_IMAGES = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        imageAdapter = ImageAdapter(selectedImages)
        recyclerView.adapter = imageAdapter

        binding.btnSelectImages.setOnClickListener {
            if (binding.edtSizeofList.text.toString().trim().isNotEmpty()) {
                selectImages()
            } else {
                Toast.makeText(this, "Please insert total size of list", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectImages() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(
            Intent.createChooser(intent, "Select Pictures"),
            REQUEST_CODE_SELECT_IMAGES
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SELECT_IMAGES && resultCode == Activity.RESULT_OK) {
            if (data?.clipData != null) {
                selectedImages.clear()
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri.toString()
                    selectedImages.add(imageUri)
                }
            } else if (data?.data != null) {
                selectedImages.clear()
                val imageUri = data.data.toString()
                selectedImages.add(imageUri)
            }

            if (selectedImages.size == 2) {
                generateList()
            } else if (selectedImages.size < 2) {
                Toast.makeText(this, "Please select at least 2 images", Toast.LENGTH_SHORT).show()
                selectedImages.clear()
            }else{
                Toast.makeText(this, "Please select at least 2 images only", Toast.LENGTH_SHORT).show()
                selectedImages.clear()
            }
        }
    }

    fun generateList() {
        val image1 = selectedImages[0]
        val image2 = selectedImages[1]

        val totalItems = binding.edtSizeofList.text.toString().trim().toInt() ?: 0
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

}
