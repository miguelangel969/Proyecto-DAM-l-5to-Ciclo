package com.example.gostore.ui.category

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.protobuf.Empty
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gostore.data.model.request.CartRequest
import com.example.gostore.data.model.response.EResponseBase
import com.example.gostore.data.model.response.ProductResponseV1
import com.example.gostore.data.remote.ApiClient
import com.example.gostore.dataStore.StoreUser
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun CategoryProduct(id: Int) {

    var products: List<ProductResponseV1> by remember {
        mutableStateOf(emptyList())
    }

    val context = LocalContext.current

    val dataUser = StoreUser(context)

    val user_id = runBlocking { dataUser.getUserId.first() }

    val services = ApiClient.goStoreServices()

    val call = services.getCategoryProducts(id)

    call.enqueue(object : Callback<List<ProductResponseV1>> {

        override fun onResponse(
            call: Call<List<ProductResponseV1>>,
            response: Response<List<ProductResponseV1>>
        ) {

            if (response.isSuccessful) {

                val responseBody: List<ProductResponseV1> = response.body()!!
                products = responseBody;

            }

        }

        override fun onFailure(call: Call<List<ProductResponseV1>>, t: Throwable) {
            Toast.makeText(context, "Error al traer los datos", Toast.LENGTH_SHORT).show()
        }
    })

    fun onAddItem(product_id: Int) {

        val body = CartRequest(user_id!!, product_id, 1)

        val call = services.CartInsert(body)

        call.enqueue(object : Callback<EResponseBase<Empty>> {

            override fun onResponse(
                call: Call<EResponseBase<Empty>>,
                response: Response<EResponseBase<Empty>>
            ) {

                if (response.isSuccessful) {

                    val responseBody: EResponseBase<Empty> = response.body()!!

                    Toast.makeText(context, responseBody.text, Toast.LENGTH_SHORT).show()


                }

            }

            override fun onFailure(call: Call<EResponseBase<Empty>>, t: Throwable) {
                Toast.makeText(context, "No se pudo agregar el producto", Toast.LENGTH_SHORT).show()
            }
        })

    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),

        ) {

        items(products) {

            Card(
                modifier = Modifier.padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            )

            {

                Column(modifier = Modifier.fillMaxWidth()) {

                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "${it.product_name} - ${it.product_brand}",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(4.dp)
                        )

                    }

                    Column(
                        modifier = Modifier
                            .height(200.dp)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(it.img_url)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Image - Url",
                            contentScale = ContentScale.FillBounds,
                        )

                    }

                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Precio: S/.${it.product_price}",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E6D09),
                            modifier = Modifier.weight(1f)
                        )

                        Button(
                            onClick = {
                                onAddItem(it.id)
                            },
                            modifier = Modifier.padding(start = 8.dp),
                        ) {
                            Text(text = "Agregar Producto")
                        }
                    }


                }
            }
        }

    }


}
