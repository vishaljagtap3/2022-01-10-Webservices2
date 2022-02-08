package com.bitcode.webservices2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bitcode.webservices2.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import java.lang.reflect.Method

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var volleyRequestQueue: RequestQueue
    lateinit var userModel: UserModel
    var gson = Gson()
    var users = ArrayList<UserModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.activity_main,
            null,
            false
        )
        setContentView(binding.root)

        volleyRequestQueue = Volley.newRequestQueue(this)

        binding.btnGetUserData.setOnClickListener {

            var jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                "https://reqres.in/api/users/${binding.edtUserId.text.toString()}",
                null,
                {

                    userModel = gson.fromJson(
                        it.getJSONObject("data").toString(),
                        UserModel::class.java
                    )

                    /*userModel = gson.fromJson(
                        it.toString(),
                        UserModelData::class.java
                    ).data*/

                    /*var jsonObjectData = it.getJSONObject("data")

                    userModel  = UserModel(
                        jsonObjectData.getInt("id"),
                        jsonObjectData.getString("email"),
                        jsonObjectData.getString("first_name"),
                        jsonObjectData.getString("last_name"),
                        jsonObjectData.getString("avatar")
                    )*/

                    binding.userModel = userModel
                },
                {

                }
            )

            volleyRequestQueue.add(jsonObjectRequest)

            /*var stringRequest = StringRequest(
                Request.Method.GET,
                "https://reqres.in/api/users/${binding.edtUserId.text.toString()}",
                MyResponseListener(),
                MyErrorListener()
            )

            volleyRequestQueue.add(stringRequest)*/
        }

        binding.btnGetUserList.setOnClickListener {
            var jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                "https://reqres.in/api/users?page=${binding.edtUserId.text.toString()}",
                null,
                {

                    /*users.addAll(
                        gson.fromJson<UserModel>(
                            it.getJSONArray("data").toString(),
                            UserModel::class.java
                        )
                    )*/

                    /*var jsonArray = it.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        var jsonObject = jsonArray.getJSONObject(i)

                        users.add(
                            UserModel(
                                jsonObject.getInt("id"),
                                jsonObject.getString("email"),
                                jsonObject.getString("first_name"),
                                jsonObject.getString("last_name"),
                                jsonObject.getString("avatar")
                            )
                        )
                    }*/

                    for (user in users) {
                        Log.e("tag", user.toString())
                    }
                },
                {

                }
            )

            volleyRequestQueue.add(jsonObjectRequest)
        }

        binding.btnRegisterUser.setOnClickListener {

            var inputJsonObject = JSONObject()
            inputJsonObject.put("name", "Vishal Jagtap")
            inputJsonObject.put("job", "Android Trainer")

            var jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST,
                "https://reqres.in/api/users",
                inputJsonObject,
                {
                    Log.e("tag-res", it.toString())
                },
                {

                }
            )

            volleyRequestQueue.add(jsonObjectRequest)
        }

    }

    inner class MyResponseListener : Response.Listener<String> {
        override fun onResponse(response: String?) {
            Log.e("tag", "REs: $response")
            var jsonObject = JSONObject(response)
            var jsonObjectData = jsonObject.getJSONObject("data")

            userModel = UserModel(
                jsonObjectData.getInt("id"),
                jsonObjectData.getString("email"),
                jsonObjectData.getString("first_name"),
                jsonObjectData.getString("last_name"),
                jsonObjectData.getString("avatar")
            )

            binding.userModel = userModel

        }
    }

    inner class MyErrorListener : Response.ErrorListener {
        override fun onErrorResponse(error: VolleyError?) {
            Log.e("tag", "Error: ${error?.networkResponse?.statusCode}")
        }
    }
}

@BindingAdapter("web_url")
fun setWebUrlToImageView(imageView: ImageView, url: String?) {
    if (url != null) {
        Glide.with(imageView)
            .load(url)
            .into(imageView)
    }
}