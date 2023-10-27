package com.example.baeguard.presenter.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baeguard.R
import com.example.baeguard.data.model.UserData
import com.example.baeguard.util.FirestoreTables
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun EdEmailScreen(onBackPressed: () -> Unit = {}) {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    var currentPassword by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }
    var confirmEmail by remember { mutableStateOf("") }
    var showPassword1 by remember { mutableStateOf(false) }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorProfile)),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(colorResource(id = R.color.colorProfile))
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Alterar E-mail",
                color = Color(0xFFFFFFFF),
                style = TextStyle(
                    fontSize = 50.sp
                )
            )


            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xffffe98a),
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = Color.White,
                    cursorColor = Color.White,
                    textColor = Color.White
                ),
                value = currentPassword,
                onValueChange = { currentPassword = it },
                label = { Text("Senha Atual", color = Color.White) },
                visualTransformation = if (showPassword1) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (currentPassword.isNotEmpty()) {
                        IconButton(
                            onClick = { showPassword1 = !showPassword1 }
                        ) {
                            Icon(
                                imageVector = if (showPassword1) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (showPassword1) "Esconder senha" else "Mostrar senha",
                                tint = Color.White
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xffffe98a),
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = Color.White,
                    cursorColor = Color.White,
                    textColor = Color.White
                ),
                value = newEmail,
                onValueChange = { newEmail = it },
                label = { Text("Novo E-mail", color = Color.White) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(

                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xffffe98a),
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = Color.White,
                    cursorColor = Color.White,
                    textColor = Color.White
                ),
                value = confirmEmail,
                onValueChange = { confirmEmail = it },
                label = { Text("Confirmar novo E-mail", color = Color.White) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(80.dp))
            Button(
                onClick = {

                    val user = auth.currentUser

                    val credential = user?.email?.let { EmailAuthProvider.getCredential(it, currentPassword) }

                    if (user != null) {
                        FirebaseFirestore.getInstance().document(FirestoreTables.USUARIO+"/"+ user.uid)
                            .set(
                                UserData(
                                userId = user.uid,
                                username = user.displayName,
                                email = newEmail,
                                profilePictureUrl = user.photoUrl.toString()
                            )
                            )
                    }

                    auth.signInWithCredential(credential!!)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful && newEmail == confirmEmail) {
                                user.updateEmail(newEmail)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            showToast(context,"Email alterado com sucesso")
                                            onBackPressed
                                        } else {
                                            val error = task.exception?.message
                                            showToast(context,"Erro: $error")
                                        }
                                    }
                            } else {
                                val error = task.exception?.message
                                showToast(context,"Erro: $error")
                            }
                        }

                },
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xffe05950),
                    contentColor = Color(0xffffb74d),
                    disabledBackgroundColor = Color(0xffe05950),
                    disabledContentColor = Color(0xffffb74d)
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier
                    .requiredWidth(width = 309.dp)
                    .requiredHeight(height = 44.dp)
                    .background(
                        shape = RoundedCornerShape(6.dp),
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xffe05950), Color(0xffffb74d))
                        )
                    )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        10.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .requiredWidth(width = 309.dp)
                        .requiredHeight(height = 44.dp)
                ) {
                    Text(
                        text = "Alterar E-mail",
                        color = Color(0xFFFFFFFF),
                        style = TextStyle(
                            fontSize = 16.sp
                        )
                    )

                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.clickable(onClick = onBackPressed)
            ) {
                IconButton(
                    onClick = onBackPressed,

                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(6.dp))
                        .requiredWidth(width = 309.dp)
                        .requiredHeight(height = 44.dp)
                        .border(
                            border = BorderStroke(2.dp, Color(0xFFFFFFFF)),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(
                            horizontal = 108.dp,
                            vertical = 12.dp
                        )

                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            10.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = Color(0xFFFFFFFF)
                        )
                        Text(
                            text = "Cancelar",
                            color = Color(0xFFFFFFFF),
                            style = TextStyle(
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
        }
    }
}