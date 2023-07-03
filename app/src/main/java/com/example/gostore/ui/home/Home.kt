package com.example.gostore.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.protobuf.Empty
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gostore.data.model.request.CartRequest
import com.example.gostore.data.model.request.UserRequest
import com.example.gostore.data.model.response.EResponseBase
import com.example.gostore.data.model.response.ProductResponseV1
import com.example.gostore.data.model.response.UserResponseV1
import com.example.gostore.data.remote.ApiClient
import com.example.gostore.dataStore.StoreUser
import com.example.gostore.navigations.Routes
import com.example.gostore.ui.login.Login
import com.example.gostore.ui.theme.GoStoreTheme
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun Home() {

    var products: List<ProductResponseV1> by remember {
        mutableStateOf(emptyList())
    }

    val context = LocalContext.current

    val dataUser = StoreUser(context)

    val userId = runBlocking { dataUser.getUserId.first() }

    val services = ApiClient.goStoreServices()

    val call = services.getProducts()

    call.enqueue(object : Callback<EResponseBase<ProductResponseV1>> {

        override fun onResponse(
            call: Call<EResponseBase<ProductResponseV1>>,
            response: Response<EResponseBase<ProductResponseV1>>
        ) {

            if (response.isSuccessful) {

                val responseBody: EResponseBase<ProductResponseV1> = response.body()!!
                products = responseBody.list;

            }

        }

        override fun onFailure(call: Call<EResponseBase<ProductResponseV1>>, t: Throwable) {
            Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
        }

    })

    fun onAddItem(product_id: Int) {

        val body = CartRequest(userId!!, product_id, 1)

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
