package com.example.gostore.data.remote

import androidx.datastore.preferences.protobuf.Empty
import com.example.gostore.data.model.request.CartRequest
import com.example.gostore.data.model.request.UserRequest
import com.example.gostore.data.model.request.UserRequestV2
import com.example.gostore.data.model.response.CartUserResponseV1
import com.example.gostore.data.model.response.CategoryResponseV1
import com.example.gostore.data.model.response.EResponseBase
import com.example.gostore.data.model.response.ProductResponseV1
import com.example.gostore.data.model.response.UserResponseV1
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GoStoreInterface {

    @POST("Login")
    fun login(@Body userRequest: UserRequest): Call<EResponseBase<UserResponseV1>>

    @POST("User/Insert")
    fun userInsert(@Body userRequest: UserRequest): Call<EResponseBase<UserResponseV1>>

    @GET("Products/Get")
    fun getProducts(): Call<EResponseBase<ProductResponseV1>>

    @GET("Categories/Get")
    fun getCategories(): Call<List<CategoryResponseV1>>

    @GET("Products/GetByCategoryID/{id}")
    fun getCategoryProducts(@Path("id") id : Int): Call<List<ProductResponseV1>>

    @GET("CartByUser/{id}")
    fun getCartByUserId(@Path("id") id : Int): Call<List<CartUserResponseV1>>

    @POST("Cart/Insert")
    fun CartInsert(@Body cartRequest: CartRequest): Call<EResponseBase<Empty>>

    @POST("Cart/Delete")
    fun CartDelete(@Body userRequest: UserRequestV2): Call<EResponseBase<Empty>>


}