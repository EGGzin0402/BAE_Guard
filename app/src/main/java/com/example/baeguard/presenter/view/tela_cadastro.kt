package com.example.baeguard.presenter.view

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.baeguard.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun tela_cadastro(
    navController: NavController
) {
    TextFieldWithIconsSignUp()
    ConfirmaSenha()
    SignUp(navController) {
        Text(" ")
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithIconsSignUp() {
    var value by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xffe7e0cf))
    ) {
        TextField(
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            value = value,
            onValueChange = { newText ->
                value = newText
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
fun ConfirmaSenha() {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showPassword2 by remember { mutableStateOf(false) }

    TextField(
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        value = password,
        onValueChange = { newText ->
            password = newText
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

    Spacer(modifier = Modifier.height(16.dp))
    Spacer(modifier = Modifier.height(16.dp))
    TextField(
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        value = confirmPassword,
        onValueChange = { newText ->
            confirmPassword = newText
        },
        label = { Text(text = "Confirme sua Senha", color = Color.Black) },
        placeholder = { Text(text = "Digite sua senha", color = Color.Black) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Lock Icon",
                tint = Color.Black
            )
        },
        trailingIcon = {
            IconButton(onClick = { showPassword2 = !showPassword2 }) {
                Icon(
                    painter = painterResource(id = if (showPassword2) R.drawable.baseline_close_24  else R.drawable.baseline_remove_red_eye_24 ),
                    tint = Color.Black,
                    contentDescription = if (showPassword2) "Hide Password" else "Show Password"
                )
            }
        },
        visualTransformation = if (showPassword2) VisualTransformation.None else PasswordVisualTransformation(),
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

    // You can add further validation and feedback here if needed.
}

@Composable
fun SignUp(navController: NavController, modifier: Modifier = Modifier, content: @Composable () -> Unit) {

    Spacer(modifier = Modifier.height(50.dp))
    Box(
        modifier = modifier
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
                    y = 85.dp
                )
                .padding(bottom = 100.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = "Cadastre-se",
                style = TextStyle(fontSize = 44.sp, fontWeight = FontWeight.Bold),
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 10.dp)
                    .padding(top = 50.dp)
            )
            // Substituir o TextField existente pela função TextFieldWithIcons
            TextFieldWithIconsSignUp()
            Spacer(modifier = Modifier.height(16.dp))
            ConfirmaSenha()

            Button(
                onClick = { /* Ação de login aqui */ },
                colors = ButtonDefaults.buttonColors(Color(color = 0xFC000000)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    "Cadastrar-se",
                    color = Color.White
                )
            }
            Text(
                text = "Já possui uma conta?",
                color = Color.Black,
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.clickable {
                    // Add your navigation logic here to navigate to the sign-up screen
                }
            )

            Text(
                text = "Login",
                color = Color.Black,
                style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.clickable {
                    navController.navigate("tela_login")
                }
            )
        }
        Divider(
            color = Color(0xff232526).copy(alpha = 0.5f),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 0.dp,
                    y = 650.dp
                )
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
            // Call the content lambda function passed to CircleBox
            content()
        }


    }
}
