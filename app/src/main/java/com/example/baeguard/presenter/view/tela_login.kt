package com.example.baeguard.presenter.view

import android.app.Activity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.baeguard.R
import com.example.baeguard.presenter.viewmodel.SignInViewModel
import com.example.baeguard.util.GoogleSignInState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun tela_login(
    googleState: GoogleSignInState,
    onGoogleSignInClick: () -> Unit,
    navController: NavController,
    mainScreen: () -> Unit,
    signInViewModel: SignInViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    val state by signInViewModel.signInState.collectAsStateWithLifecycle()

    if(state.isSuccess){
        mainScreen()
    }
    LaunchedEffect(key1 = googleState.signInError ){
        googleState.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    AndroidView(factory = { context ->
        View(context).apply {
            setBackgroundColor(0xffe05950.toInt())
            val window = (context as Activity).window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = 0xffe05950.toInt()
        }
    }, update = { androidView ->

    })

    Spacer(modifier = Modifier.height(50.dp))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                val width = size.width
                val height = size.height
                drawRect(Color.Black)

                scale(scaleX = 3f, scaleY = 1f) {
                    drawOval(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xffe05950), Color(0xffffe98a)),
                            start = Offset(0f, height * .50f),
                            end = Offset(width, height * .20f)
                        ),
                        topLeft = Offset(2f, height * .27f),
                        size = Size(width, height)
                    )
                }
            }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .offset(
                    x = 0.dp,
                    y = 50.dp
                )
                .padding(bottom = 100.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = "LOGIN",
                style = TextStyle(fontSize = 44.sp, fontWeight = FontWeight.Bold),
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 10.dp)
                    .padding(top = 40.dp)
            )

            TextFieldWithIcons { newValue ->
                email = newValue
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextfieldSenha{ newValue ->
                senha = newValue
            }

            Button(
                onClick = {
                    scope.launch{
                        signInViewModel.loginUser(email, senha)
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(color = 0xFC000000)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    "Login",
                    color = Color.White
                )
            }
            Text(
                text = "Não possui uma conta?",
                color = Color.Black,
                style = TextStyle(fontSize = 16.sp),

                )

            Text(
                text = "Cadastrar-se",
                color = Color.Black,
                style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.clickable {
                    navController.navigate("tela_cadastro")
                }
            )
        }
        Divider(
            color = Color(0xff232526).copy(alpha = 0.5f),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 0.dp,
                    y = 600.dp
                )
        )
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 136.dp,
                    y = 620.dp
                )
                .size(size = 78.dp)
                .clip(shape = CircleShape)
                .background(color = Color(0xffe7e0cf))){

        }
        Image(
            painter = painterResource(R.drawable.image1),
            contentDescription = stringResource(id = R.string.image1_content_description),

            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 136.dp,
                    y = 620.dp
                )
                .size(size = 77.dp)
                .clickable { onGoogleSignInClick() }
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier.padding(top = 7.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logov31),
                    contentDescription = stringResource(id = R.string.logov31_content_description),
                    modifier = Modifier
                        .size(180.dp)
                        .padding(10.dp)
                )
            }
        }


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithIcons(onValueChanged: (String) -> Unit) {
    var value by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xffe7e0cf))
    ) {
        TextField(
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            value = value,
            onValueChange = { newText ->
                value = newText
                onValueChanged(newText)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon",
                    tint = Color.Black
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Person Icon",
                    tint = Color.Black
                )
            },
            label = { Text(text = "E-mail", color = Color.Black) },
            placeholder = { Text(text = "Digite seu e-mail", color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color(0xFF1D1D1D),
                textColor = Color(0xFF1D1D1D),
                placeholderColor = Color(0xFF1D1D1D),
                containerColor = Color(0xffe7e0cf)
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextfieldSenha(onValueChanged: (String) -> Unit) {
    var value by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    TextField(
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        value = value,
        onValueChange = { newText ->
            value = newText
            onValueChanged(newText)
        },
        label = { Text(text = "Senha", color = Color.Black) },
        placeholder = { Text(text = "Digite sua senha", color = Color.Black) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Lock Icon",
                tint = Color.Black
            )
        },
        trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(
                    painter = painterResource(id = if (showPassword) R.drawable.baseline_close_24  else R.drawable.baseline_remove_red_eye_24 ),
                    tint = Color.Black,
                    contentDescription = if (showPassword) "Hide Password" else "Show Password"
                )
            }
        },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xffe7e0cf)),
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.Black,
            textColor = Color.Black,
            placeholderColor = Color.Black,
            containerColor = Color(color = 0xffe7e0cf)
        )
    )
}