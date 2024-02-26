package com.contextu.al.carousel

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.contextu.al.common.AppTextView
import com.contextu.al.common.ButtonComponent
import com.contextu.al.common.model.ButtonModel
import com.contextu.al.common.model.TextModel
import com.contextu.al.common.toSP
import com.contextu.al.extensions.toBoxModifier
import com.contextu.al.model.customguide.ContextualContainer
import com.contextu.al.model.ui.CarouselItem
import com.contextu.al.model.ui.Text
import com.google.gson.Gson

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CarouselComponent(
    contextualContainer: ContextualContainer,
    modifier: Modifier = Modifier,
    onActionClick: (CarouselAction) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = {
        contextualContainer.guidePayload.guide.carousel.items.size
    })

    val extraProperties = Gson()
        .fromJson(
            contextualContainer.guidePayload.guide.extraJson,
            CarouselExtraModel::class.java
        )

    with(contextualContainer.guidePayload.guide) {
        val boxModifier = baseView.toBoxModifier(modifier = modifier)

        Box(
            modifier = modifier
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = boxModifier
                    .align(Alignment.Center),
            ) { page ->
                val item = carousel.items[page]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(item.backgroundColor)),
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
                                .padding(bottom = 16.dp)
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
                        AddContent(item = item)
                    }

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 30.dp)
                            .weight(0.3f)
                    ) {
                        with(item) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.BottomCenter),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                ButtonComponent(
                                    buttonModel = ButtonModel(
                                        width = buttonWidth,
                                        height = buttonHeight,
                                        background = buttonBackground,
                                        textModel = TextModel(
                                            fontSize = buttonTextFontSize,
                                            fontName = buttonTextFontName,
                                            fontWeight = buttonTextFontWeight,
                                            text = buttonText,
                                            color = buttonTextColor,
                                            alignment = buttonTextAlignment
                                        )
                                    ),
                                ) {
                                    onActionClick.invoke(CarouselAction.OnButtonClick(item))
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row {
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

                extraProperties.skip?.let {
                    AppTextView(
                        textProperties = Text(
                            text = it.text,
                            color = android.graphics.Color.parseColor(it.color),
                            fontSize = it.size
                        ),
                        modifier = Modifier.clickable {
                            onActionClick.invoke(CarouselAction.OnSkip)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AddContent(item: CarouselItem) {
    Column {
        with(item) {
            AppTextView(
                size = titleFontSize,
                text = title,
                fontWeight = titleFontWeight,
                color = titleColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp),
                lineHeight = (titleFontSize + 10).toSP(),
                textAlign = TextAlign.Center
            )

            AppTextView(
                size = contentFontSize,
                text = content,
                fontWeight = contentFontWeight,
                color = contentColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

sealed class CarouselAction {
    data object OnSkip : CarouselAction()
    data class OnButtonClick(val carouselItem: CarouselItem): CarouselAction()
}