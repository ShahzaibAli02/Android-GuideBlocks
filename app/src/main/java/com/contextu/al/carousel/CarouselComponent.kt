package com.contextu.al.carousel

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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

    val heightModifier = if (carousel.baseView.height.contains("%")) {
        val height = carousel.baseView.height.split("%").firstOrNull()?.toIntOrNull()
        height?.let {
            Modifier.fillMaxHeight((it / 100f))
        } ?: Modifier
    } else {
        val height = carousel.baseView.height.toFloatOrNull()
        height?.let {
            Modifier.height(Dp(it))
        } ?: Modifier
    }

    val widthModifier = if (carousel.baseView.width.contains("%")) {
        val height = carousel.baseView.width.split("%").firstOrNull()?.toIntOrNull()
        height?.let {
            heightModifier.fillMaxWidth((it / 100f))
        } ?: heightModifier
    } else {
        val height = carousel.baseView.width.toFloatOrNull()
        height?.let {
            heightModifier.width(Dp(it))
        } ?: heightModifier
    }

    val cornerRadius = carousel.baseView.cornerRadius.toString().toFloatOrNull()?.let {
        widthModifier.clip(RoundedCornerShape(Dp(it)))
    } ?: widthModifier

    val border = carousel.baseView.border.width.let {
        if (it > 0)
            cornerRadius.border(Dp(it), Color(carousel.baseView.border.color))
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
                    top = Dp(carousel.baseView.padding.top.toFloat()),
                    bottom = Dp(carousel.baseView.padding.bottom.toFloat()),
                    start = Dp(carousel.baseView.padding.left.toFloat()),
                    end = Dp(carousel.baseView.padding.right.toFloat())
                )
                .background(Color(carousel.baseView.backGroundColor))
                .align(Alignment.Center),
        ) { page ->
            val item = carousel.items[page]
            Column(
                modifier = Modifier
                    .padding(
                        top = Dp(carousel.baseView.padding.top.toFloat()),
                        bottom = Dp(carousel.baseView.padding.bottom.toFloat()),
                        start = Dp(carousel.baseView.padding.left.toFloat()),
                        end = Dp(carousel.baseView.padding.right.toFloat())
                    )
                    .fillMaxSize()
                    .background(Color(item.baseView.backGroundColor)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .weight(0.3f, true)
                )

                Box(
                    Modifier
                        .fillMaxWidth()
                        .weight(0.7f, true)
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
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .border(BorderStroke(1.dp, Color.LightGray), shape = CircleShape)
                    )
                }

                Box(
                    Modifier
                        .fillMaxWidth()
                        .weight(0.5f, true)
                ) {
                    Column {
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
                                    .padding(top = 8.dp, start = 8.dp, end = 8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp)
                        .weight(0.3f)
                ) {
                    item.buttonModel?.let {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.BottomCenter),
                            verticalArrangement = Arrangement.Bottom
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
                    if (pagerState.currentPage == iteration) Color.White else Color.Black.copy(
                        alpha = 0.3f
                    )
                if (pagerState.currentPage == iteration) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .height(4.dp)
                            .width(20.dp)
                            .align(Alignment.CenterVertically)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}