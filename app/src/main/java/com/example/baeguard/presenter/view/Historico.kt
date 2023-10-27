package com.example.baeguard.presenter.view

import android.content.Context
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.baeguard.R
import com.example.baeguard.data.model.Historico
import com.example.baeguard.presenter.viewmodel.HistoricoViewModel
import com.example.baeguard.util.UiState

private val TAG = "BAE HISTORICO"

@Composable
fun HistoricoScreen(
    historicoViewModel: HistoricoViewModel = hiltViewModel()
) {

    val ssid = getWifiSSID(LocalContext.current)
    val listState = rememberLazyListState()
    val historico = remember{ mutableStateListOf<Historico>() }

    historicoViewModel.getAllHistorico()
    historicoViewModel.allhistorico.observe(LocalLifecycleOwner.current){ state ->
        when(state){
            is UiState.Loading -> {

            }
            is UiState.Failure -> {
                Log.e(TAG, state.error.toString())
            }
            is UiState.Success -> {
                historico.clear()
                state.data.forEach{
                    historico.add(it)
                }
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorProfile)),
    ) {

        items(historico) { fireInfo ->
            FireInfoCard(fireInfo = fireInfo)
        }
    }
}

@Composable
fun FireInfoCard(fireInfo: Historico) {
    androidx.compose.material.Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(6.dp),
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xffe05950), Color(0xffffb74d))
                )
            )
            .clickable { },
        elevation = 4.dp,
        backgroundColor = Color(color = 0xffe7e0cf),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color(color = 0xffe7e0cf))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = fireInfo.dispositivo,
                    style = typography.headlineLarge,
                    color = colorResource(id = R.color.colorProfile)
                )

                Text(
                    text = fireInfo.ambiente,
                    style = typography.labelLarge,
                    color = colorResource(id = R.color.colorProfile)
                )
            }
            Text(
                text = fireInfo.hora.toString(),
                style = typography.bodyLarge,
                color = colorResource(id = R.color.colorProfile)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {

                Icon(
                    painter = painterResource(R.drawable.ahumidade),
                    contentDescription = stringResource(id = R.string.humidade_content_description),
                    tint = colorResource(id = R.color.colorProfile),
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Umidade: ${fireInfo.umidade}",
                    color = colorResource(id = R.color.colorProfile),
                    style = typography.bodyLarge
                    )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row{
                Icon(
                    painter = painterResource(R.drawable.chart),
                    contentDescription = stringResource(id = R.string.humidade_content_description),
                    tint = colorResource(id = R.color.colorProfile),
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp)

                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Detecção de CO2: ${fireInfo.CO2}",
                    color = colorResource(id = R.color.colorProfile),
                    style = typography.bodyLarge
                    )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row{
                Icon(
                    painter = painterResource(R.drawable.warn),
                    contentDescription = stringResource(id = R.string.humidade_content_description),
                    tint = colorResource(id = R.color.colorProfile),
                    modifier = Modifier.height(20.dp)

                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Detecção de Gás: ${fireInfo.GLP}",
                    color = colorResource(id = R.color.colorProfile),
                    style = typography.bodyLarge
                    )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(){
                Icon(
                    painter = painterResource(R.drawable.temperatura),
                    contentDescription = stringResource(id = R.string.temperatura_content_description),
                    tint = colorResource(id = R.color.colorProfile),
                    modifier = Modifier.height(30.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = "Temperatura: ${fireInfo.temperatura}",
                    color = colorResource(id = R.color.colorProfile),
                    style = typography.bodyLarge
                    )
            }

        }
    }
}

@RequiresPermission(android.Manifest.permission.ACCESS_WIFI_STATE)
fun getWifiSSID(context: Context): String {
    val wifiManager = ContextCompat.getSystemService(context, WifiManager::class.java)
    val wifiInfo: WifiInfo? = wifiManager?.connectionInfo

    if (wifiInfo != null && wifiInfo.ssid.isNotEmpty()) {
        val ssid = wifiInfo.ssid
        // Lidere com o SSID aqui
        return ssid
    }

    return ""
}

@Preview
@Composable
fun PreviewInfoCard() {

    FireInfoCard(fireInfo = Historico())
    
}
