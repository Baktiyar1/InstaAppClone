package com.back4app.kotlin.back4appexample.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.back4app.kotlin.back4appexample.*
import com.back4app.kotlin.back4appexample.activities.api.RetrofitBuilder
import com.back4app.kotlin.back4appexample.databinding.FragmentAddPostBinding
import com.back4app.kotlin.back4appexample.entities.AddPostRequest
import com.back4app.kotlin.back4appexample.entities.AddPostResponse
import com.back4app.kotlin.back4appexample.utils.IMAGE_REQUEST
import com.back4app.kotlin.back4appexample.utils.toImage
import com.back4app.kotlin.back4appexample.utils.toast
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import com.parse.SaveCallback
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

class AddPostFragment : Fragment(), LocationListener {

    private val binding: FragmentAddPostBinding by lazy {
        FragmentAddPostBinding.inflate(layoutInflater)
    }
    private var imageUri: Uri? = null
    private var selectedImage: Bitmap? = null
    private var locationManager: LocationManager? = null
    private var imageFile: ParseFile? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            pickImageButton.setOnClickListener {
                openGallery()
            }

            pickLocationButton.setOnClickListener {
                getMyLocation()
            }

            createPostButton.setOnClickListener {
//                createPostParse()
                createPostRest()
            }
        }
    }

    private fun createPostRest() {
        binding.apply {
            if (imageFile != null) {
                progressBar.visibility = View.VISIBLE
                val postRequest = AddPostRequest()
                postRequest.location = localeTv.text.toString().trim()
                postRequest.description = editTextPostDescription.text.toString().trim()
                postRequest.title = editTextPostTitle.text.toString().trim()
                postRequest.userId = ParseUser.getCurrentUser().objectId
                postRequest.image = imageFile?.toImage()

                toast(imageFile.toString())

                val response = RetrofitBuilder.api.createPosts(postRequest)
                response.enqueue(object : retrofit2.Callback<AddPostResponse> {
                    override fun onResponse(
                        call: Call<AddPostResponse>,
                        response: Response<AddPostResponse>,
                    ) {
                        if (response.isSuccessful) {
                            toast("Post is created!")
                            editTextPostTitle.text.clear()
                            editTextPostDescription.text.clear()
                            imageAddPost.visibility = View.GONE
                            localeTv.visibility = View.GONE
                            localeTv.text = ""
                            progressBar.visibility = View.GONE
                            requireActivity().findViewById<ViewPager2>(R.id.view_pager).currentItem =
                                0
                        }
                    }

                    override fun onFailure(call: Call<AddPostResponse>, t: Throwable) {
                        Log.i("tag", t.message.toString())
                    }
                })
            } else {
                toast("Please, upload the image")
            }
        }
    }

    private fun uploadImage() {
        val steam = ByteArrayOutputStream()
        selectedImage?.compress(Bitmap.CompressFormat.PNG, 100, steam)
        val byteArray = steam.toByteArray()
        val parseFile = ParseFile("postImage.png", byteArray)
        parseFile.saveInBackground(SaveCallback { e ->
            if (e == null) {
                toast("Image is saved!")
                imageFile = parseFile
            } else {
                toast("Failed is image!")
                Log.i("EX", e.message.toString())
            }
        })
    }

    private fun createPostParse() {
        binding.apply {
            if (imageFile != null) {
                progressBar.visibility = View.VISIBLE
                val stream = ByteArrayOutputStream()
                selectedImage?.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()
                val parseFile = ParseFile("postImage.png", byteArray)
                parseFile.saveInBackground(SaveCallback { e ->
                    if (e == null) {
                        toast("Image is saved!")
                    }
                })

                val postObject = ParseObject("Post")
                postObject.put("title", editTextPostTitle.text.toString().trim())
                postObject.put("description", editTextPostDescription.text.toString().trim())
                postObject.put("location", localeTv.text.toString().trim())
                postObject.put("image", parseFile)
                postObject.put("userID", ParseUser.getCurrentUser().objectId)
                postObject.saveInBackground { e ->
                    if (e == null) {
                        toast("Post is saved!")
                        editTextPostTitle.text.clear()
                        editTextPostDescription.text.clear()
                        imageAddPost.visibility = View.GONE
                        localeTv.visibility = View.GONE
                        localeTv.text = ""
                        progressBar.visibility = View.GONE
                        requireActivity().findViewById<ViewPager2>(R.id.view_pager).currentItem = 0
                    } else Log.i("error", "" + e.message)
                }
            } else toast("Please, upload the image")
        }
    }

    private fun getMyLocation() {
        try {
            locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
                return
            }
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        } catch (e: Exception) {
            e.message
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            try {
                selectedImage =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
                binding.apply {
                    imageAddPost.visibility = View.VISIBLE
                    imageAddPost.setImageBitmap(selectedImage)
                    uploadImage()
                }
            } catch (e: IOException) {
                Log.i("error", "" + e.message)
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        toast(location.longitude.toString() + " " + location.longitude)
        val geocoder = Geocoder(requireActivity(), Locale.getDefault())
        try {
            val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            val address = addressList[0].getAddressLine(0) as String
            binding.apply {
                localeTv.text = address
                localeTv.visibility = View.VISIBLE
            }
        } catch (e: IOException) {
            e.message
        }
    }
}