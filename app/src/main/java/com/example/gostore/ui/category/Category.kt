package com.example.gostore.ui.category

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gostore.data.model.request.UserRequest
import com.example.gostore.data.model.response.CategoryResponseV1
import com.example.gostore.data.model.response.ProductResponseV1
import com.example.gostore.data.model.response.UserResponseV1
import com.example.gostore.data.remote.ApiClient
import com.example.gostore.navigations.Routes
import com.example.gostore.ui.login.Login
import com.example.gostore.ui.theme.GoStoreTheme
import com.skydoves.landscapist.coil.CoilImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun Category(navigationController: NavHostController) {

    var categories: List<CategoryResponseV1> by remember {
        mutableStateOf(emptyList())
    }

    val context = LocalContext.current

    val services = ApiClient.goStoreServices()

    val call = services.getCategories()

    call.enqueue(object : Callback<List<CategoryResponseV1>> {

        override fun onResponse(
            call: Call<List<CategoryResponseV1>>,
            response: Response<List<CategoryResponseV1>>
        ) {

            if (response.isSuccessful) {

                val responseBody: List<CategoryResponseV1> = response.body()!!
                categories = responseBody;

            }

        }

        override fun onFailure(call: Call<List<CategoryResponseV1>>, t: Throwable) {
            Toast.makeText(context, "Error al traer los datos", Toast.LENGTH_SHORT).show()
        }
    })

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),

        ) {

        items(categories) {


            Card(
                modifier = Modifier.padding(8.dp).clickable {

                    navigationController.navigate(Routes.ScreenCategoryProduct.createRoute(it.id))

                },
                colors = CardDefaults.cardColors(containerColor = Color.White)
            )

            {

                Column(modifier = Modifier.fillMaxWidth()) {

                    Row(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = "${it.category_name}",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(4.dp)
                        )

                    }

                }
            }

        }

    }


}
