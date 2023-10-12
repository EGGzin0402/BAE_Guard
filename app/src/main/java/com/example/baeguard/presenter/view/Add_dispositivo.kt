package com.example.baeguard.presenter.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.baeguard.R

@Composable
fun Add_DispoScreen(navController: NavHostController) {

    var deviceName by remember { mutableStateOf(TextFieldValue()) }
    var environmentName by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var isNewEnvironment by remember { mutableStateOf(false) } // Checkbox state

    val environments = listOf("Casa em Guarujá", "Casa em São Paulo", "Casa em Atibaia")
    LazyColumn(
                modifier = Modifier
                .background(colorResource(id = R.color.colorProfile))
            .fillMaxSize()
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.colorProfile))
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "Adicione seu dispositivo:",
                    color = Color(0xFFFFFFFF),
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(height = 50.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xffffe98a),
                        unfocusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                        cursorColor = Color.White
                    ),
                    value = deviceName,
                    onValueChange = { deviceName = it },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = {
                        Text(text = "Nome do Dispositivo", color = Color(0xFFFFFDFD))
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))


                // Either show the dropdown or the text field based on the checkbox
                if (isNewEnvironment) {
                    OutlinedTextField(
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xffffe98a),
                            unfocusedBorderColor = Color.White,
                            focusedLabelColor = Color.White,
                            cursorColor = Color.White
                        ),
                        value = environmentName,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        onValueChange = { environmentName = it },
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        placeholder = { Text(text = "Novo Ambiente", color = Color(0xFFFFFFFF)) }
                    )
                } else {

                    Box(
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Nome do Ambiente",
                                color = Color.White,
                                style = LocalTextStyle.current.copy(fontSize = 14.sp)
                            )

                            Box(
                                modifier = Modifier.fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = if (isDropdownExpanded) Color(0xffffe98a) else Color.White,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .background(Color.Transparent)
                                    .clickable { isDropdownExpanded = true }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowLeft,
                                        contentDescription = "Fechar",
                                        tint = Color.White, // Cor do ícone
                                        modifier = Modifier
                                            .padding(start = 8.dp) // Adicione um espaçamento à esquerda do ícone
                                            .size(24.dp) // Tamanho do ícone
                                    )

                                    Text(
                                        text = if (environmentName.isNotEmpty()) environmentName else "Selecione um Ambiente",
                                        color = Color.White,
                                        style = LocalTextStyle.current.copy(fontSize = 16.sp),
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }

                            DropdownMenu(
                                expanded = isDropdownExpanded,
                                onDismissRequest = { isDropdownExpanded = false }
                            ) {
                                environments.forEach { item ->
                                    DropdownMenuItem(
                                        onClick = {
                                            environmentName = item
                                            isDropdownExpanded = false
                                        }
                                    ) {
                                        Text(
                                            text = item,
                                            color = Color.Black, // Customize the text color of the dropdown items
                                            style = LocalTextStyle.current.copy(fontSize = 16.sp)
                                        )
                                    }
                                }
                            }
                        }

                    }

                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Checkbox(
                        checked = isNewEnvironment,
                        onCheckedChange = { isNewEnvironment = it },
                        colors = CheckboxDefaults.colors(
                            checkmarkColor = Color.White, // Cor do checkmark quando o checkbox está marcado
                            checkedColor = Color(0xffe05950), // Cor de fundo do checkbox quando está marcado
                            uncheckedColor = Color.White // Cor de fundo do checkbox quando não está marcado
                        ),
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = "Novo Ambiente",
                        color = Color(0xFFFFFFFF),
                        style = LocalTextStyle.current.copy(fontSize = 14.sp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    androidx.compose.material.Button(
                        onClick = { navController.navigate("add_qr") },
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
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_qr_code_scanner_24),
                                contentDescription = null // You can add a proper content description here
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Adicionar Dispositivo")

                        }
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun Dispo() {
    val navController = rememberNavController() // Declare e inicialize a variável navController
    Add_DispoScreen(navController) // Passe o navController para a função MainScreen
}
