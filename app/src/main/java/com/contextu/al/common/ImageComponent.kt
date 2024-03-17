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
import com.contextu.al.common.extensions.toBoxModifier
import com.contextu.al.model.ui.Image

@Composable
fun ImageComponent(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    image: Image
) {

    with(image) {
        val boxModifier = toBoxModifier(modifier = modifier)

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
            modifier = boxModifier
        )
    }
}