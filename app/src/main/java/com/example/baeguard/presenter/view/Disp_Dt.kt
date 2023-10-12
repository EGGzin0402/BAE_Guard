package com.example.baeguard.presenter.view

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.baeguard.R
import com.example.baeguard.data.model.Ambiente
import com.example.baeguard.data.model.Dispositivo
import com.example.baeguard.presenter.viewmodel.DispDtViewModel
import com.example.baeguard.util.FirestoreTables
import com.example.baeguard.util.UiState
import com.google.firebase.firestore.FirebaseFirestore


private val TAG = "BAE DISP DT"

@Composable
fun Disp_DtScreen(
    dispId:String,
    dispDtViewModel: DispDtViewModel,
    navController: NavController
) {
    var dispositivo by remember { mutableStateOf(Dispositivo()) }

    dispDtViewModel.getDispositivo(dispId)
    dispDtViewModel.dispositivo.observe(LocalLifecycleOwner.current){ state ->
        when(state){
            is UiState.Loading -> {

            }
            is UiState.Failure -> {
                Log.e(TAG, state.error.toString())
            }
            is UiState.Success -> {
                dispositivo = state.data
            }
        }
    }

    var ambientes = remember { mutableStateListOf<Ambiente>() }

    dispDtViewModel.getAllAmbientes()
    dispDtViewModel.allambientes.observe(LocalLifecycleOwner.current){state ->
        when(state){
            is UiState.Loading -> {

            }
            is UiState.Failure -> {
                Log.e(TAG, state.error.toString())
            }
            is UiState.Success -> {
                ambientes.clear()
                state.data.forEach{
                    ambientes.add(it)
                }
            }
        }

    }


    Log.i(TAG, dispositivo.toString())

    val showDialog = remember { mutableStateOf(false) }
    var showDialog2 by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xff232526)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .background(
                                color = Color(0xffe7e0cf),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_camera2_alt_24),
                            contentDescription = "Camera Icon",
                            tint = Color(0xff232526),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredHeight(height = 50.dp)
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = dispositivo.nome,
                            color = Color(0xffe7e0cf),
                            style = TextStyle(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.width(140.dp))
                        IconButton(
                            onClick = { showDialog2 = true },

                            )

                        {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = Color(0xffe7e0cf),
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                        EditarDispositivo(
                            showDialog = showDialog2,
                            dispositivo = dispositivo,
                            dispDtViewModel = dispDtViewModel,
                            environments = ambientes,
                            onConfirm = { name, environment, isNew, newEnvironment->

                                Log.i(TAG, environment.toString())

                                if (isNew){
                                    val ambid = dispDtViewModel.addAmbiente(Ambiente(
                                        nome = newEnvironment
                                    ))
                                    dispDtViewModel.updateDispositivo(
                                        Dispositivo(
                                            id = dispositivo.id,
                                            nome = name,
                                            CO2 = dispositivo.CO2,
                                            GLP = dispositivo.GLP,
                                            URLcam = dispositivo.URLcam,
                                            temperatura = dispositivo.temperatura,
                                            umidade = dispositivo.umidade,
                                            ambiente = FirebaseFirestore.getInstance().collection(
                                                FirestoreTables.AMBIENTE).document(ambid)
                                        )
                                    )
                                }else{
                                    dispDtViewModel.updateDispositivo(
                                        Dispositivo(
                                            id = dispositivo.id,
                                            nome = name,
                                            CO2 = dispositivo.CO2,
                                            GLP = dispositivo.GLP,
                                            URLcam = dispositivo.URLcam,
                                            temperatura = dispositivo.temperatura,
                                            umidade = dispositivo.umidade,
                                            ambiente = FirebaseFirestore.getInstance().collection(
                                                FirestoreTables.AMBIENTE).document(environment.id)
                                        )
                                    )
                                    Log.i(TAG, Dispositivo(
                                        id = dispositivo.id,
                                        nome = name,
                                        CO2 = dispositivo.CO2,
                                        GLP = dispositivo.GLP,
                                        URLcam = dispositivo.URLcam,
                                        temperatura = dispositivo.temperatura,
                                        umidade = dispositivo.umidade,
                                        ambiente = dispositivo.ambiente
                                    ).toString())
                                }
                                showDialog2 = false
                            },
                            onCancel = { showDialog2 = false }
                        )
                        Card(
                            shape = RoundedCornerShape(6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .clickable { showDialog.value = true }
                        ) {
                            IconButton(
                                onClick = { showDialog.value = true }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_delete_24),
                                    contentDescription = "delete",
                                    tint = Color(0xffe7e0cf),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .align(Alignment.End)
                                )
                            }
                            DialogoDeConfirmacao(
                                showDialog = showDialog.value,
                                onConfirm = {
                                    dispDtViewModel.deleteDispositivo(dispositivo)
                                    navController.navigate("home")
                                },
                                onCancel = { showDialog.value = false }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            item {
                Column (
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    if (dispositivo.CO2){
                        AcionarBombeiros()
                    }else if(dispositivo.GLP){
                        AcionarBombeiros()
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Card_Humidade(dispositivo.umidade)
                    Spacer(modifier = Modifier.width(12.dp))
                    Card_Temperatura(dispositivo.temperatura)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Card_Status(
                    if (dispositivo.CO2){
                        "CO2 detectado!"
                    }else if(dispositivo.GLP){
                        "GLP detectado!"
                    }else{
                        "Tudo Certo!"
                    }
                )

            }

        }
    }

    dispDtViewModel.updateDispositivo.observe(LocalLifecycleOwner.current){ state ->
        when(state){
            is UiState.Loading -> {

            }
            is UiState.Failure -> {
                Log.e(TAG, state.error.toString())
            }
            is UiState.Success -> {
                Log.i(TAG, state.toString())
                Log.i(TAG, state.data)
            }
        }
    }

    dispDtViewModel.deleteDispositivo.observe(LocalLifecycleOwner.current){ state ->
        when(state){
            is UiState.Loading -> {

            }
            is UiState.Failure -> {
                Log.e(TAG, state.error.toString())
            }
            is UiState.Success -> {
                Log.i(TAG, state.toString())
                Log.i(TAG, state.data)
            }
        }
    }

}

@Composable
fun EditarDispositivo(
    showDialog: Boolean,
    dispositivo: Dispositivo,
    dispDtViewModel: DispDtViewModel,
    environments : List<Ambiente>,
    onConfirm: (name: String, environment: Ambiente, isNewEnvironment: Boolean, newEnvironment: String) -> Unit,
    onCancel: () -> Unit
) {
    var isNewEnvironment by remember { mutableStateOf(false) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var environmentText by remember { mutableStateOf("Novo Ambiente") }

    var environment by remember { mutableStateOf(Ambiente()) }

    dispositivo.ambiente?.let { dispDtViewModel.getAmbiente(it) }
    dispDtViewModel.ambiente.observe(LocalLifecycleOwner.current) { state ->
        when (state) {
            is UiState.Loading -> {
                // Handle loading state
            }
            is UiState.Failure -> {
                Log.e(TAG, state.error.toString())
                // Handle failure state
            }
            is UiState.Success -> {
                environment = state.data
            }
        }
    }

    Log.i(TAG, environment.toString())

    if (showDialog) {
        var name by remember { mutableStateOf(dispositivo.nome) }

        AlertDialog(
            modifier = Modifier
                .border(
                    border = BorderStroke(2.dp, Color(0xffe05950)),
                    shape = RoundedCornerShape(6.dp)
                ),
            onDismissRequest = onCancel,
            title = {
                Text(
                    text = "Editar dispositivo",
                    fontWeight = FontWeight.Bold,
                    color= Color.White,
                    fontSize = 20.sp,
                    textAlign = Center,
                    modifier = Modifier.fillMaxWidth()
                )

            },
            text = {


                Column() {

                    Box ( modifier = Modifier
                        .height(15.dp)
                        .background(color = Color.Transparent)){
                        Text(
                            text = "Editar dispositivo",
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 16.dp)

                        )
                    }
                    OutlinedTextField(
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xffffe98a),
                            unfocusedBorderColor = Color.White,
                            focusedLabelColor = Color.White,
                            cursorColor = Color.White,
                            textColor = Color.White),
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome do Dispositivo", color = Color.White) },

                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)

                    )



                    if (isNewEnvironment) {
                        OutlinedTextField(
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xffffe98a),
                                unfocusedBorderColor = Color.White,
                                focusedLabelColor = Color.White,
                                cursorColor = Color.White,
                                textColor = Color.White),
                            value = environmentText,
                            onValueChange = { environmentText = it },
                            label = { Text("Nome do Ambiente", color = Color.White) },

                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    } else {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
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
                                    modifier = Modifier
                                        .fillMaxWidth()
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
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Fechar",
                                            tint = Color.White, // Cor do ícone
                                            modifier = Modifier
                                                .padding(start = 8.dp) // Adicione um espaçamento à esquerda do ícone
                                                .size(24.dp) // Tamanho do ícone
                                        )

                                        Text(
                                            text = environment.nome,
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
                                                environment.id = item.id
                                                environment.nome = item.nome
                                                environment.usuario = item.usuario
                                                isDropdownExpanded = false
                                            }
                                        ) {
                                            Text(
                                                text = item.nome,
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
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(CenterHorizontally)

                    ) {
                        Spacer(modifier = Modifier.width(30.dp))
                        Checkbox(
                            checked = isNewEnvironment,
                            onCheckedChange = { isNewEnvironment = it },
                            colors = CheckboxDefaults.colors(
                                checkmarkColor = Color.White, // Cor do checkmark quando o checkbox está marcado
                                checkedColor = Color(0xffe05950), // Cor de fundo do checkbox quando está marcado
                                uncheckedColor = Color.White // Cor de fundo do checkbox quando não está marcado
                            ),


                            )
                        Text(
                            text = "Novo Ambiente",
                            color = Color.White,
                            style = LocalTextStyle.current.copy(fontSize = 14.sp)
                        )
                    }


                }
            },
            backgroundColor = colorResource(id = R.color.colorProfile),
            confirmButton = {
                Button(
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xffe05950),
                        contentColor = Color(0xffffb74d),
                        disabledBackgroundColor = Color(0xffe05950),
                        disabledContentColor = Color(0xffffb74d)
                    ),
                    onClick = {
                        onConfirm(name, environment, isNewEnvironment, environmentText)
                        onCancel()
                    },
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 0.dp)

                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Salvar",
                        tint = Color(0xFFFFFFFF)
                    )
                    Text(text = "Salvar", color = Color.White)
                }
            },
            dismissButton = {

                Button(
                    onClick = onCancel,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = Color.Transparent,
                        disabledBackgroundColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    ),
                    border = BorderStroke(2.dp, Color(0xFFFFFFFF)),
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(6.dp))
                        .padding(top = 8.dp, bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = Color(0xFFFFFFFF)
                    )
                    Text(text = "Cancelar", color = Color.White)
                }
            }
        )
    }
}
@Composable
fun AcionarBombeiros() {
    val phoneNumber = remember { mutableStateOf("193") }
    val context = LocalContext.current

    Column {
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${phoneNumber.value}")
                context.startActivity(intent)
            },
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xffe05950) ,contentColor = Color(0xffffb74d), disabledBackgroundColor = Color(0xffe05950), disabledContentColor =  Color(0xffffb74d)),
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
            Text("Acionar Bombeiros", fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun Card_Humidade(humidity: Int) {
    val c4 = Color(0xFF298DDD)
    val c5 = Color(0xFFF3E2B9)

    Card(
        modifier = Modifier
            .size(width = 150.dp, height = 180.dp)
            .padding(0.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(c5, c4)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .align(Alignment.Center)
            ) {
                Text(
                    text = "Umidade",
                    color = Color(0xff232526),
                    style = TextStyle(
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "$humidity%",
                    color = Color(0xff232526),
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Icon(
                    painter = painterResource(R.drawable.ahumidade),
                    contentDescription = stringResource(id = R.string.humidade_content_description),
                    tint = Color(0xff232526),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Composable
fun Card_Temperatura(temperature: Int) {
    val c4 = Color(0xFFFF5722)
    val c5 = Color(0xFFFFEB3B)

    Card(
        modifier = Modifier
            .size(width = 150.dp, height = 180.dp)
            .padding(0.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(c5, c4)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .align(Alignment.Center)
            ) {
                Text(
                    text = "Temperatura",
                    color = Color(0xff232526),
                    style = TextStyle(
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "$temperature °C",
                    color = Color(0xff232526),
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Icon(
                    painter = painterResource(R.drawable.temperatura),
                    contentDescription = stringResource(id = R.string.temperatura_content_description),
                    tint = Color(0xff232526),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}


@Composable
fun Card_Status(status: String) {
    val c2 = Color(0xff61ff5c)
    val c3 = Color(0xffffe98a)
    val textColor = if (status == "Tudo Certo!") Color(0xff232526) else Color.White
    val gradientColors = if (status == "Tudo Certo!") listOf(c3, c2) else listOf(Color(0xffe05950), Color(0xffe05950))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .wrapContentSize(Alignment.Center)
    ) {
        Card(
            modifier = Modifier
                .width(350.dp)
                .height(200.dp)
                .padding(10.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = gradientColors
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .align(Alignment.Center)
                ) {
                    Text(
                        text = "Status",
                        color = textColor,
                        style = TextStyle(
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .requiredWidth(width = 200.dp)
                            .requiredHeight(height = 45.dp)
                    )
                    Text(
                        text = status,
                        color = textColor,
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    )
                }
            }
        }
    }
}



@Composable
fun DialogoDeConfirmacao(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    if (showDialog) {
        val context = LocalContext.current
        AlertDialog(
            modifier = Modifier
                .border(
                    border = BorderStroke(2.dp, Color(0xffe05950)),
                    shape = RoundedCornerShape(6.dp)
                ),
            onDismissRequest = onCancel,
            title = {
                Text(
                    text = stringResource(R.string.confirmationdips_dialog_message),
                    fontWeight = FontWeight.Bold,
                    color= Color.White,
                    fontSize = 20.sp
                )
            },
            text = { Text(text = stringResource(R.string.confirmacaodisp_dialog_title),  color= Color.White,) },
            backgroundColor = colorResource(id = R.color.colorProfile),
            confirmButton = {
                Button(
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xffe05950),
                        contentColor = Color(0xffffb74d),
                        disabledBackgroundColor = Color(0xffe05950),
                        disabledContentColor = Color(0xffffb74d)
                    ),
                    onClick = {
                        onConfirm()
                        // Fechar o diálogo após a confirmação
                        onCancel()
                    },
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Fechar",
                        tint = Color(0xFFFFFFFF)
                    )
                    Text(text = "Excluir", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = onCancel,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = Color.Transparent,
                        disabledBackgroundColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    ),
                    border = BorderStroke(2.dp, Color(0xFFFFFFFF)),
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(6.dp))
                        .padding(top = 8.dp, bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = Color(0xFFFFFFFF)
                    )
                    Text(text = "Cancelar", color = Color.White)
                }
            }
        )
    }
}




