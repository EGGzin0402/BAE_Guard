package com.example.baeguard.presenter.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baeguard.R
import com.example.baeguard.data.model.UserData
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.EnumMap




@Composable
fun Add_DispoScreen_Qr(
    userData: UserData?,
    nome: String,
    ambiente: String,
    onBackPressed: () -> Unit = {}
) {

    val uid = userData?.userId

    LazyColumn(
        modifier = Modifier
            .background(colorResource(id = R.color.colorProfile))
            .fillMaxSize()
    ){
        item{  Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "Adicionando Dispositivo",
                color = Color(0xFFFFFFFF),
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(height = 50.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espaçamento entre o texto e a imagem

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Exibir a imagem do código QR
                Row(modifier = Modifier
                    .background(color = Color.White)
                    .height(300.dp)
                    .width(300.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    QRCode(content = "$nome, $ambiente, $uid"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp)) // Espaçamento entre o texto e a imagem
                Text(
                    text = "Escaneie esse QR Code com a câmera do seu dispositivo",
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium),

                    modifier = Modifier.requiredWidth(width = 291.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                androidx.compose.material.Button(
                    onClick = onBackPressed ,
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
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Cancelar")

                    }
                }
            }
        }
        }
    }
}

@Composable
fun QRCode(content: String) {

    Canvas(modifier = Modifier.fillMaxSize()) {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

        try {
            val matrix: BitMatrix = MultiFormatWriter().encode(
                content,
                BarcodeFormat.QR_CODE,
                size.width.toInt(),
                size.height.toInt(),
                hints
            )

            val paint = Paint().apply {
                color = Color.Black
                style = PaintingStyle.Fill
            }

            for (y in 0 until matrix.height) {
                for (x in 0 until matrix.width) {
                    if (matrix[x, y]) {
                        drawRect(
                            color = paint.color,
                            topLeft = Offset(x.toFloat(), y.toFloat()),
                            size = Size(1f, 1f)
                        )
                    }
                }
            }
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}

