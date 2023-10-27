package com.example.baeguard.presenter.view


import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.baeguard.R
import com.example.baeguard.data.model.Dispositivo
import com.example.baeguard.util.UiState
import com.example.baeguard.viewmodel.HomeViewModel
import com.google.firebase.firestore.DocumentReference

private const val TAG = "BAE HOME"

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val dispositivos = remember{ mutableStateListOf<Dispositivo>() }

    homeViewModel.getAllDispositivos()
    homeViewModel.alldispositivos.observe(LocalLifecycleOwner.current){ state ->
        when(state){
            is UiState.Loading -> {

            }
            is UiState.Failure -> {
                Log.e(TAG, state.error.toString())
            }
            is UiState.Success -> {
                dispositivos.clear()
                state.data.forEach{
                    dispositivos.add(it)
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        StatusCardList(navController, homeViewModel, dispositivos)

        FloatingActionButton(
            backgroundColor = colorResource(id = R.color.colorPrimary),
            onClick = { navController.navigate("add_dispo") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color(color = 0xffe7e0cf)
            )
        }
    }


}

@Composable
fun StatusCard(navController: NavController,id: String, status: String, temperature: Int, humidity: Int, environment: String) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(
                shape = RoundedCornerShape(6.dp),
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xffe05950), Color(0xffffb74d))
                )

            )
    ) {
        
        Card(
            backgroundColor = Color(color = 0xffe7e0cf),
            border = BorderStroke(2.dp, Color(0xffe05950)),
            modifier = Modifier
                .padding(16.dp)
                .border(
                    border = BorderStroke(2.dp, Color(0xffe05950)),
                    shape = RoundedCornerShape(6.dp)
                )
                .fillMaxWidth()
                .clickable { navController.navigate("disp_dt/$id") },
            elevation = 4.dp,

            ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status:",
                        style = MaterialTheme.typography.h6,
                        color = Color.Black
                    )



                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Humidity",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = environment,
                            style = MaterialTheme.typography.body1,
                            color = Color.Gray
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Center){
                    Text(
                        text = status,
                        style = MaterialTheme.typography.h6,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.temperatura),
                        contentDescription = stringResource(id = R.string.temperatura_content_description),
                        tint = Color.Gray,
                        modifier = Modifier.height(30.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Temperatura: $temperature",
                        style = MaterialTheme.typography.body1,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 16.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.ahumidade),
                        contentDescription = stringResource(id = R.string.humidade_content_description),
                        tint = Color.Gray,
                        modifier = Modifier.height(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Umidade: $humidity",
                        style = MaterialTheme.typography.body1,
                        color = Color.Gray
                    )
                }
            }



        }
    }
}

fun groupStatusByHouse(dispositivos: List<Dispositivo>): Map<DocumentReference?, List<Dispositivo>> {
    return dispositivos.groupBy { it.ambiente }
}

// Função com LazyColumn que exibe os cards
@Composable
fun StatusCardList(navController: NavController, homeViewModel: HomeViewModel, dispositivos: List<Dispositivo>) {
    val groupedStatus = groupStatusByHouse(dispositivos)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(colorResource(id = R.color.colorProfile), colorResource(id = R.color.colorProfile))
                )
            )
    ) {

        LazyColumn {
            item{
                  Box ( modifier = Modifier
                      .wrapContentSize(align = Alignment.Center)
                      .fillMaxWidth()
                  ) {

                      Column {


                      Text(
                          text = "Bem vindo ao",
                          color = Color(0xffe7e0cf),
                          textAlign = TextAlign.Center,
                          style = TextStyle(
                              fontSize = 30.sp
                          ),
                          modifier = Modifier
                              .fillMaxWidth()
                      )

                      Text(
                          text = "BAE Guard",
                          color = Color(0xffe7e0cf),
                          textAlign = TextAlign.Center,
                          style = TextStyle(
                              fontSize = 50.sp, fontWeight = FontWeight.Bold
                          ),
                          modifier = Modifier
                              .fillMaxWidth()
                      )
                          Spacer(modifier = Modifier.height(10.dp))

                      Text(
                          text = "Seus dispositivos",
                          color = Color(0xffe7e0cf),
                          textAlign = TextAlign.Center,
                          style = TextStyle(
                              fontSize = 26.sp
                          ),
                          modifier = Modifier
                              .fillMaxWidth()

                      )
                          Spacer(modifier = Modifier.height(10.dp))
                      }
                  }
            }
            groupedStatus.forEach { (house, dispositivos) ->
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        // Exibir o nome da casa
                        Row() {

                            if (house != null) {
                                AmbienteTitle(house, homeViewModel)
                            }


                            Spacer(modifier = Modifier.width(5.dp))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Fechar",
                                tint = Color(color = 0xffe7e0cf), // Cor do ícone
                                modifier = Modifier
                                    .padding(start = 8.dp) // Adicione um espaçamento à esquerda do ícone
                                    .size(30.dp)
                                    .align(Alignment.CenterVertically)// Tamanho do ícone
                            )
                        }
                    }
                }

                items(dispositivos) { dispositivo ->


                    val status:String
                    status = if (dispositivo.CO2){
                        "CO2 detectado!"
                    }else if(dispositivo.GLP){
                        "GLP detectado!"
                    }else if(dispositivo.chama){
                        "Chama detectada!"
                    }else{
                        "Tudo certo!"
                    }

                    StatusCard(
                        navController,
                        id = dispositivo.id,
                        status = status,
                        temperature = dispositivo.temperatura,
                        humidity = dispositivo.umidade,
                        environment = dispositivo.nome
                    )
                }
            }
        }

    }
}

@Composable
fun AmbienteTitle(ambiente: DocumentReference, homeViewModel: HomeViewModel) {
    var ambienteNome by remember { mutableStateOf("") }

    homeViewModel.getAmbiente(ambiente)

    homeViewModel.ambiente.observe(LocalLifecycleOwner.current) { state ->
        when (state) {
            is UiState.Loading -> {
                // Handle loading state
            }
            is UiState.Failure -> {
                Log.e(TAG, state.error.toString())
                // Handle failure state
            }
            is UiState.Success -> {
                ambienteNome = homeViewModel.getAmbienteTitle(ambiente)
            }
        }
    }

    Text(
        text = ambienteNome,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(16.dp),
        color = Color(color = 0xffe7e0cf),
    )
}

@Preview
@Composable
fun PrevStatusCard() {
    val navController = rememberNavController()
    StatusCard(navController = navController,id = "", status = "", temperature = 27, humidity = 25, environment = "Algum lugar")
}
