package com.example.baeguard.presenter.view


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.baeguard.R
import com.example.baeguard.data.model.UserData
import com.example.baeguard.util.FirestoreTables
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore


@ExperimentalMaterial3Api
@Composable
fun PerfilScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
    onDelete: () -> Unit,
    navController: NavController
) {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    val showDialog = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
        .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.colorProfile))
                .padding(16.dp)
        ) {
            LazyColumn() {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)

                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(6.dp))

                        ) {
                            if (userData?.profilePictureUrl != null){
                                AsyncImage(
                                    model = userData.profilePictureUrl,
                                    contentDescription = "profile picture",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .fillMaxWidth()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )

                            }else {
                                Image(
                                    painter = painterResource(id = R.drawable.logov31),
                                    contentDescription = stringResource(id = R.string.logov31_content_description),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .fillMaxWidth()
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            val user = auth.currentUser

                            if (user != null) {
                                val providerData = user.providerData
                                for (userInfo in providerData) {
                                    when (userInfo.providerId) {
                                        GoogleAuthProvider.PROVIDER_ID -> {
                                            if (userData?.username != null){
                                                Text(
                                                    text = userData.username,
                                                    color = Color(0xFFFFFFFF),
                                                    fontSize = 24.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }else{
                                                Text(
                                                    text = "Nome do Cliente",
                                                    color = Color(0xFFFFFFFF),
                                                    fontSize = 24.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            if (userData?.email != null){
                                                Text(
                                                    text = userData.email,
                                                    color = Color(0xFFFFFFFF),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Normal
                                                )
                                            }else{
                                                Text(
                                                    text = "cliente@exemplo.com",
                                                    color = Color(0xFFFFFFFF),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Normal
                                                )
                                            }

                                            Card(
                                                shape = RoundedCornerShape(6.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = Color.Transparent
                                                ),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { showDialog.value = true }
                                                    .padding(16.dp)
                                                    .height(40.dp)


                                            ) {

                                                RowWithIcon(label = "Deletar Conta", icon = Icons.Default.Delete)
                                                Divider(
                                                    color = Color(0xFFFFFFFF).copy(alpha = 0.5f),
                                                    modifier = Modifier
                                                        .align(alignment = Alignment.Start)

                                                )
                                                ConfirmationDialog(
                                                    showDialog = showDialog.value,
                                                    onConfirm = { /* Lógica para excluir a conta */ },
                                                    onCancel = { showDialog.value = false }
                                                )
                                            }

                                        }



                                        EmailAuthProvider.PROVIDER_ID -> {
                                            if (userData?.username != null){
                                                Text(
                                                    text = userData.username,
                                                    color = Color(0xFFFFFFFF),
                                                    fontSize = 24.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }else{
                                                Text(
                                                    text = "B.A.E. Profile",
                                                    color = Color(0xFFFFFFFF),
                                                    fontSize = 24.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            if (userData?.email != null){
                                                Text(
                                                    text = userData.email,
                                                    color = Color(0xFFFFFFFF),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Normal
                                                )
                                            }else{
                                                Text(
                                                    text = "cliente@exemplo.com",
                                                    color = Color(0xFFFFFFFF),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Normal
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(16.dp))
                                            Column{
                                                Card(
                                                    shape = RoundedCornerShape(6.dp),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = Color.Transparent
                                                    ),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable { navController.navigate("ed_senha") }
                                                        .padding(16.dp)
                                                        .height(40.dp)


                                                ) {
                                                    RowWithIcon(label = "Editar Senha", icon = Icons.Default.KeyboardArrowRight)
                                                    Divider(
                                                        color = Color(0xFFFFFFFF).copy(alpha = 0.5f),
                                                        modifier = Modifier
                                                            .align(alignment = Alignment.Start)
                                                            .fillMaxWidth()
                                                    )
                                                }

                                                Spacer(modifier = Modifier.height(16.dp))


                                                Card(
                                                    shape = RoundedCornerShape(6.dp),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = Color.Transparent
                                                    ),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable { navController.navigate("ed_email") }
                                                        .padding(16.dp)
                                                        .height(40.dp)

                                                ) {
                                                    RowWithIcon(label = "Editar Email", icon = Icons.Default.KeyboardArrowRight)
                                                    Divider(
                                                        color = Color(0xFFFFFFFF).copy(alpha = 0.5f),
                                                        modifier = Modifier
                                                            .align(alignment = Alignment.Start)
                                                            .fillMaxWidth()
                                                    )
                                                }

                                                Spacer(modifier = Modifier.height(16.dp))

                                                Card(
                                                    shape = RoundedCornerShape(6.dp),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = Color.Transparent
                                                    ),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable { navController.navigate("ed_nome") }
                                                        .padding(16.dp)
                                                        .height(40.dp)

                                                ) {
                                                    RowWithIcon(label = "Editar Nome", icon = Icons.Default.KeyboardArrowRight)
                                                    Divider(
                                                        color = Color(0xFFFFFFFF).copy(alpha = 0.5f),
                                                        modifier = Modifier
                                                            .align(alignment = Alignment.Start)
                                                            .fillMaxWidth()
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(16.dp))

                                            Card(
                                                shape = RoundedCornerShape(6.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = Color.Transparent
                                                ),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { showDialog.value = true }
                                                    .padding(16.dp)
                                                    .height(40.dp)


                                            ) {

                                                RowWithIcon(label = "Deletar Conta", icon = Icons.Default.Delete)
                                                Divider(
                                                    color = Color(0xFFFFFFFF).copy(alpha = 0.5f),
                                                    modifier = Modifier
                                                        .align(alignment = Alignment.Start)

                                                )
                                                ConfirmationDialog(
                                                    showDialog = showDialog.value,
                                                    onConfirm = {
                                                        FirebaseFirestore.getInstance().collection(FirestoreTables.USUARIO).document(user.uid).delete()
                                                        user.delete()
                                                            .addOnCompleteListener { task ->
                                                                if (task.isSuccessful) {
                                                                    onDelete()
                                                                } else {
                                                                    val error = task.exception?.message
                                                                    showToast(context,"Erro: $error")
                                                                }
                                                            }
                                                                },
                                                    onCancel = { showDialog.value = false }
                                                )




                                            }
                                        }
                                    }
                                }
                            }


                        }
                    }



                }
            }
        }
        FloatingActionButton(
            backgroundColor = colorResource(id = R.color.colorPrimary),
            onClick = { onSignOut() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Add",
                tint = Color(color = 0xffe7e0cf)
            )
        }
    }
}

@Composable
fun ConfirmationDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    if (showDialog) {
        AlertDialog(

            modifier = Modifier
                .border(
                    border = BorderStroke(2.dp, Color(0xffe05950)),
                    shape = RoundedCornerShape(6.dp)
                )

            ,
            onDismissRequest = onCancel,
            title = { Text(text = stringResource(R.string.confirmacao_dialog_title), fontWeight = FontWeight.Bold, fontSize = 20.sp, color=Color.White) },
            text = { Text(text = stringResource(R.string.confirmation_dialog_message), color=Color.White) },
            backgroundColor = colorResource(id = R.color.colorProfile),
            confirmButton = {
                Button(
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xffe05950) ,contentColor = Color(0xffffb74d), disabledBackgroundColor = Color(0xffe05950), disabledContentColor =  Color(0xffffb74d)),

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
                    Text(text = "Excluir", color= Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = onCancel,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent ,contentColor = Color.Transparent, disabledBackgroundColor = Color.Transparent, disabledContentColor =  Color.Transparent),
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
                    Text(text = "Cancelar", color= Color.White)
                }
            }

        )

    }
}


@Composable
fun RowWithIcon(label: String, icon: ImageVector) {
    Row(
        verticalAlignment = CenterVertically,
        modifier = Modifier
            .padding(top = 8.dp)



    ) {

        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color(0xFFFFFFFF),
            modifier = Modifier.padding(start = 4.dp)

        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(24.dp)

        ) {
            Icon(
                painter = rememberVectorPainter(image = icon),
                contentDescription = "Ícone",
                tint = Color(0xFFFFFFFF),
                modifier = Modifier.align(CenterEnd)
            )
        }


    }
}


