package com.example.gostore.ui.cart

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.protobuf.Empty
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gostore.data.model.request.CartRequest
import com.example.gostore.data.model.request.UserRequestV2
import com.example.gostore.data.model.response.CartUserResponseV1
import com.example.gostore.data.model.response.CategoryResponseV1
import com.example.gostore.data.model.response.EResponseBase
import com.example.gostore.data.model.response.ProductResponseV1
import com.example.gostore.data.remote.ApiClient
import com.example.gostore.dataStore.StoreUser
import com.example.gostore.navigations.Routes
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import java.math.RoundingMode

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Cart(navigationController: NavHostController) {

    var products: List<CartUserResponseV1> by remember {
        mutableStateOf(emptyList())
    }
    
    var enabledAlert : Boolean by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val services = ApiClient.goStoreServices()

    val dataUser = StoreUser(context)

    val id = runBlocking { dataUser.getUserId.first() }

    val call = services.getCartByUserId(id!!)

    call.enqueue(object : Callback<List<CartUserResponseV1>> {

        override fun onResponse(
            call: Call<List<CartUserResponseV1>>,
            response: Response<List<CartUserResponseV1>>
        ) {

            if (response.isSuccessful) {

                val responseBody: List<CartUserResponseV1> = response.body()!!
                products = responseBody;

            }

        }

        override fun onFailure(call: Call<List<CartUserResponseV1>>, t: Throwable) {
            Toast.makeText(context, "Error al traer los datos", Toast.LENGTH_SHORT).show()
        }
    })

    val totalPrice = products.sumOf { it.totalPrice }

    var totalPriceDecimal = BigDecimal(totalPrice).setScale(2, RoundingMode.HALF_UP)

    fun onDelete(type: String) {

        val body = UserRequestV2(id)

        val call = services.CartDelete(body)

        call.enqueue(object : Callback<EResponseBase<Empty>> {

            override fun onResponse(
                call: Call<EResponseBase<Empty>>,
                response: Response<EResponseBase<Empty>>
            ) {

                if (response.isSuccessful) {

                    val responseBody: EResponseBase<Empty> = response.body()!!

                    if(type === "pay"){
                        Toast.makeText(context, "Compra realizada exitosamente", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, responseBody.text, Toast.LENGTH_SHORT).show()
                    }

                    products = emptyList()
                    totalPriceDecimal = BigDecimal.ZERO

                }

            }

            override fun onFailure(call: Call<EResponseBase<Empty>>, t: Throwable) {
                Toast.makeText(context, "No se pudo eliminar la lista", Toast.LENGTH_SHORT).show()
            }
        })


    }
    
    if(enabledAlert){

        AlertDialog(
            onDismissRequest = { enabledAlert = false },
            title = { Text(text = "Confirmar compra") },
            text = {
                Text(text = "¿Estás seguro de realizar la compra?")
            },
            confirmButton = {
                Button(
                    onClick = {

                        onDelete("pay")
                        enabledAlert = false
                        navigationController.navigate(Routes.ScreenCategory.route)

                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(text = "Aceptar")
                }
            },
            dismissButton = {
                Button(
                    onClick = { enabledAlert = false },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(text = "Cancelar")
                }
            }
        )
        
    }

    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { onDelete("") }) {

                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar items",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black,
                )
            }

            Text(
                text = "Total: S/.${totalPriceDecimal}",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E6D09),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )

            Button(
                enabled = products.isNotEmpty(),
                onClick = { enabledAlert = true },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(text = "Pagar productos")
            }
        }
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

                    Row(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = "${it.product_name} - ${it.brand_product}",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(4.dp)
                        )

                    }

                    Column(
                        modifier = Modifier
                            .height(200.dp)
                            .padding(16.dp)
                    ) {

                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(it.image_url)
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
                            text = "Cantidad: ${it.quantity}",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E6D09),
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "Precio total: S/.${
                                BigDecimal(it.totalPrice).setScale(
                                    2,
                                    RoundingMode.HALF_UP
                                )
                            }",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E6D09),
                            modifier = Modifier.padding(start = 8.dp)
                        )

                    }


                }
            }

        }

    }


}