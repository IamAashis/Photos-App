package com.android.photosapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.photosapp.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var selectedImages: ArrayList<String> = ArrayList()
    private val REQUEST_CODE_SELECT_IMAGES = 1
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        imageAdapter = ImageAdapter(selectedImages)
        recyclerView.adapter = imageAdapter
        setup()
    }

    private fun setup() {
        binding.btnSelectImages.setOnClickListener {
            selectImages()
        }
    }


    private fun selectImages() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
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

            if (selectedImages.size < 2) {
                // Display a message to the user that at least 2 images need to be selected
                Toast.makeText(this, "Please select at least 2 images", Toast.LENGTH_SHORT).show()
                return
            }

//            if (selectedImages.size == 1) {
                val imageUri = selectedImages[0]
                for (i in 0 until 49) {
                    selectedImages.add(imageUri)
                }
//            }

            generateList()
        }
    }

    private fun generateList() {
        if (selectedImages.size < 2) {
            // Display a message to the user that at least 2 images need to be selected
            Toast.makeText(this, "Please select at least 2 images", Toast.LENGTH_SHORT).show()
            return
        }

        val triangularSequence = generateTriangularSequence(selectedImages.size)

        val imageList = mutableListOf<String>()
        for (i in 0 until 50) {
            val index = triangularSequence[i] % selectedImages.size
            imageList.add(selectedImages[index])
        }

        imageAdapter.images = imageList
        imageAdapter.notifyDataSetChanged()
    }

    private fun generateTriangularSequence(size: Int): List<Int> {
        val sequence = mutableListOf<Int>()
        var count = 0
        var number = 1

        while (count < size) {
            for (i in 1..number) {
                if (count < size) {
                    sequence.add(count)
                    count++
                } else {
                    break
                }
            }
            number++
        }

        return sequence
    }
}