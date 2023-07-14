package com.example.stockmartetapptutorialandroid.presentation.company_info

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stockmartetapptutorialandroid.domain.model.IntradayInfo
import com.example.stockmartetapptutorialandroid.ui.theme.StockMartetAppTutorialAndroidTheme
import kotlin.math.round
import kotlin.math.roundToInt


@Composable
fun StockChart(
    infos: List<IntradayInfo> = emptyList(),
    modifier: Modifier = Modifier,
    graphColor : Color = Color.Green
){
    val spacing = 100F
    val transparentGraphColor = remember {
        graphColor.copy(
            alpha = 0.5F
        )
    }
    val upperValue = remember(infos) {
        infos.maxOfOrNull { it.close }?.plus(1)?.roundToInt() ?: 0
    }

    val lowerValue = remember(infos) {
        infos.minOfOrNull { it.close }?.toInt() ?: 0
    }

    val density = LocalDensity.current
    val textPlain = remember(density) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 16.sp.toPx() }

        }
    }

    Canvas(modifier = modifier){
        val spacePerHour = (size.width - spacing) / infos.size
        (0 until  infos.size step 2).forEach{ i ->
            val info = infos[i]
            val hour = info.timestamp.hour
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    hour.toString(),
                    spacing + (i * spacePerHour),
                    size.height - 5,
                    textPlain
                )
            }
        }

        val priceStep = (upperValue - lowerValue) / 5f
        (0 .. 4).forEach{ i ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    round(lowerValue +i * priceStep ).toString(),
                    30f,
                    size.height - spacing - i * (size.height / 5),
                    textPlain
                )
            }
        }
        var lastX = 0f
        val strokePath = Path().apply {
            val height = size.height
            for(i in infos.indices){
                val info = infos[i]
                val nextInfo = infos.getOrNull(i + 1) ?: infos.last()
                val leftRatio = (info.close - lowerValue)/ (upperValue - lowerValue)
                val rightRatio = (nextInfo.close - lowerValue)/ (upperValue - lowerValue)

                val x1 = spacing + i * spacePerHour
                val y1 = height - spacing - (leftRatio * height).toFloat()
                val x2 = spacing + (i + 1) * spacePerHour
                val y2 = height - spacing - (rightRatio * height).toFloat()
                if(i==0){
                    moveTo(x1,y1)
                }
                lastX = (x1 + x2 ) / 2f
                quadraticBezierTo(
                    x1, y1, lastX , (y1+y2) / 2f
                )
            }
        }

        val fillPath = android.graphics.Path(strokePath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(lastX,size.height - spacing)
                lineTo(spacing,size.height - spacing)
                close()
            }
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    transparentGraphColor,
                    Color.Transparent
                ),
                endY = size.height - spacing
            )
        )
        drawPath(
            path = strokePath,
            color = graphColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }

}

@Composable
@Preview
fun previewStockChart(){
    StockMartetAppTutorialAndroidTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
//            StockChart()
        }

    }
}