package com.contextu.al.carousel

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.contextu.al.components.AppTextView
import com.contextu.al.components.ButtonComponent
import com.contextu.al.components.toSP
import com.contextu.al.model.CarouselModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CarouselComponent(
    carousel: CarouselModel,
    modifier: Modifier = Modifier,
    onSkipClick: () -> Unit
) {
    val pagerState = rememberPagerState()

    val heightModifier = if (carousel.height.contains("%")) {
        val height = carousel.height.split("%").firstOrNull()?.toIntOrNull()
        height?.let {
            Modifier.fillMaxHeight((it / 100f))
        } ?: Modifier
    } else {
        val height = carousel.height.toFloatOrNull()
        height?.let {
            Modifier.height(Dp(it))
        } ?: Modifier
    }

    val widthModifier = if (carousel.width.contains("%")) {
        val height = carousel.width.split("%").firstOrNull()?.toIntOrNull()
        height?.let {
            heightModifier.fillMaxWidth((it / 100f))
        } ?: heightModifier
    } else {
        val height = carousel.width.toFloatOrNull()
        height?.let {
            heightModifier.width(Dp(it))
        } ?: heightModifier
    }

    val cornerRadius = carousel.cornerRadius.toString().toFloatOrNull()?.let {
        widthModifier.clip(RoundedCornerShape(Dp(it)))
    } ?: widthModifier

    val border = carousel.border.width.let {
        if (it > 0)
            cornerRadius.border(Dp(it), Color(carousel.border.color))
        else
            cornerRadius
    }

    Box(
        modifier = modifier
    ) {
        HorizontalPager(
            state = pagerState,
            pageCount = carousel.items.size,
            modifier = border
                .padding(
                    top = Dp(carousel.padding.top.toFloat()),
                    bottom = Dp(carousel.padding.bottom.toFloat()),
                    start = Dp(carousel.padding.left.toFloat()),
                    end = Dp(carousel.padding.right.toFloat())
                )
                .background(Color(carousel.backGroundColor))
                .align(Alignment.Center),
        ) { page ->
            val item = carousel.items[page]
            Column(
                modifier = Modifier
                    .padding(
                        top = Dp(carousel.padding.top.toFloat()),
                        bottom = Dp(carousel.padding.bottom.toFloat()),
                        start = Dp(carousel.padding.left.toFloat()),
                        end = Dp(carousel.padding.right.toFloat())
                    )
                    .fillMaxSize()
                    .background(Color(item.backGroundColor)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .weight(if (item.buttonModel != null) 0.2f else 0.4f, true)
                ) {
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
                            .data(item.image)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .align(if (item.buttonModel != null) Alignment.BottomCenter else Alignment.Center)
                    )
                }

                Box(
                    Modifier
                        .fillMaxWidth()
                        .weight(if (item.buttonModel != null) 0.5f else 0.8f, true)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = if (item.buttonModel != null) Arrangement.Center else Arrangement.Top
                    ) {
                        with(item.title) {
                            AppTextView(
                                size = fontSize,
                                text = text,
                                fontWeight = fontWeight,
                                color = color,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp, end = 24.dp),
                                lineHeight = (fontSize + 10).toSP(),
                                textAlign = TextAlign.Center
                            )
                        }

                        item.content?.apply {
                            AppTextView(
                                size = fontSize,
                                text = text,
                                fontWeight = fontWeight,
                                color = color,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp),
                                lineHeight = (fontSize + 10).toSP(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                item.buttonModel?.let {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .weight(0.3f, true)
                    ) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                        ) {
                            ButtonComponent(
                                buttonModel = it,
                            ) {
                                onSkipClick.invoke()
                            }
                        }
                    }
                }

            }
        }

        Row(
            modifier = Modifier
                .height(25.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(carousel.items.size) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color.White else Color.Blue.copy(
                        alpha = 0.1f
                    )
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}