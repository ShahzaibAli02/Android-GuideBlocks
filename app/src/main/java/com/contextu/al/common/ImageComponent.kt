package com.contextu.al.common

import android.os.Build
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.contextu.al.model.ui.Image

@Composable
fun ImageComponent(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    image: Image
) {

    with(image) {
        val heightModifier = if (height?.contains("%") == true) {
            val height = height?.split("%")?.firstOrNull()?.toIntOrNull()
            height?.let {
                modifier.fillMaxHeight((it / 100f))
            } ?: modifier
        } else {
            val height = height?.toFloatOrNull()
            height?.let {
                if (it >= 0)
                    modifier.height(Dp(it))
                else
                    modifier.height(Dp(60f))
            } ?: modifier
        }

        val widthModifier = if (width?.contains("%") == true) {
            val height = width?.split("%")?.firstOrNull()?.toIntOrNull()
            height?.let {
                heightModifier.fillMaxWidth((it / 100f))
            } ?: heightModifier
        } else {
            val height = width?.toFloatOrNull()
            height?.let {
                if (it >= 0)
                    heightModifier.width(Dp(it))
                else
                    heightModifier.fillMaxWidth()
            } ?: heightModifier
        }

        AsyncImage(
            imageLoader = ImageLoader.Builder(LocalContext.current)
                .components {
                    if (Build.VERSION.SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }
                .build(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(image.resource)
                .build(),

            contentDescription = null,
            contentScale = contentScale,
            modifier = widthModifier
        )
    }
}