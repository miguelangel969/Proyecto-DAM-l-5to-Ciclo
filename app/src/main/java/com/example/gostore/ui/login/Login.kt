package com.example.gostore.ui.login

import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gostore.data.model.request.UserRequest
import com.example.gostore.data.model.response.EResponseBase
import com.example.gostore.data.model.response.UserResponseV1
import com.example.gostore.data.remote.ApiClient
import com.example.gostore.dataStore.StoreUser
import com.example.gostore.navigations.Routes
import com.example.gostore.ui.theme.GoStoreTheme
import com.google.gson.JsonSerializer
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navigationController: NavHostController) {

    val context = LocalContext.current

    val services = ApiClient.goStoreServices()

    var enabledSpinner: Boolean by remember {
        mutableStateOf(false)
    }

    var passwordVisible by remember { mutableStateOf(false) }

    var username by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    val dataUser = StoreUser(context)


    fun onLogin() {

        if (username == "" || password == "") {

            return Toast.makeText(
                context,
                "Tiene que llenar los campos obligatorios",
                Toast.LENGTH_SHORT
            ).show()

        }

        enabledSpinner = true

        val body = UserRequest(username, password)

        val call = services.login(body)

        call.enqueue(object : Callback<EResponseBase<UserResponseV1>> {

            override fun onResponse(
                call: Call<EResponseBase<UserResponseV1>>,
                response: Response<EResponseBase<UserResponseV1>>
            ) {

                if (response.isSuccessful) {

                    val responseBody: EResponseBase<UserResponseV1> = response.body()!!

                    if (responseBody.code === 200) {

                        CoroutineScope(Dispatchers.Main).launch {

                            withContext(Dispatchers.IO) {
                                dataUser.setUserId(responseBody.item.id)
                            }

                            Toast.makeText(
                                context,
                                "Bienvenido ${responseBody.item.username}",
                                Toast.LENGTH_SHORT
                            ).show()

                            navigationController.navigate(Routes.ScreenCategory.route)

                        }

                    } else {

                        enabledSpinner = false;

                        Toast.makeText(
                            context,
                            responseBody.text,
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }

                enabledSpinner = false

            }

            override fun onFailure(call: Call<EResponseBase<UserResponseV1>>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                enabledSpinner = false
            }
        })

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp)
                .wrapContentSize(Alignment.Center)
        ) {

            if (enabledSpinner) {

                AlertDialog(
                    containerColor = Color.Transparent,
                    onDismissRequest = { },
                    title = {

                    },
                    text = {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(color = Color.Black)
                        }

                    },
                    confirmButton = {

                    },
                    dismissButton = {

                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(start = 20.dp, end = 20.dp),
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 60.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 10.dp),
                                text = "Proyecto de desarrollo de aplicación móviles I",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )

                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("https://cdn.glitch.global/d703169b-e9c7-4494-a66a-5d2d7db4a54a/WhatsApp%20Image%202023-07-02%20at%2002.31.25.jpeg?v=1688283114345")
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Image - Url",
                                contentScale = ContentScale.FillWidth,
                            )

                        }

                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        value = username,
                        onValueChange = { username = it },
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        placeholder = {
                            Text(text = "Usuario")
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.Person, contentDescription = "Usuario")
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.LightGray,
                            focusedIndicatorColor = Color.LightGray,
                            unfocusedIndicatorColor = Color.LightGray,
                            disabledIndicatorColor = Color.LightGray
                        )
                    )


                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        value = password,
                        onValueChange = { password = it },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        placeholder = {
                            Text(text = "Contraseña")
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.Lock, contentDescription = "Contraseña")
                        },
                        trailingIcon = {
                            val icon =
                                if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible },
                                modifier = Modifier
                                    .clickable { passwordVisible = !passwordVisible }
                                    .padding(4.dp)
                            ) {
                                Icon(icon, contentDescription = "Toggle password visibility")
                            }
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.LightGray,
                            focusedIndicatorColor = Color.LightGray,
                            unfocusedIndicatorColor = Color.LightGray,
                            disabledIndicatorColor = Color.LightGray
                        )
                    )


                    TextButton(modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF335EFF)
                        ),
                        onClick = { onLogin() }) {
                        Text(text = "Ingresar")
                    }

                    TextButton(modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray
                        ),
                        onClick = {

                            navigationController.navigate(Routes.ScreenRegister.route)

                        }) {
                        Text(text = "Registrar")
                    }

                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = "¿Olvidaste tu contraseña?",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                }

            }

        }
    }


}
