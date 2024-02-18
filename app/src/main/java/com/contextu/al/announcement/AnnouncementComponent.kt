package com.contextu.al.announcement

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.contextu.al.common.ContainerComponent
import com.contextu.al.common.ImageComponent
import com.contextu.al.common.AppTextView
import com.contextu.al.common.ButtonComponent
import com.contextu.al.model.customguide.GuidePayload
import com.contextu.al.common.model.toBoxAlignment

@SuppressLint("UnrememberedMutableState")
@Composable
fun AnnouncementComponent(
    guidePayload: GuidePayload
) {
    val showDialog = mutableStateOf(true)
    AnimatedVisibility(visible = showDialog.value) {
        Dialog(
            onDismissRequest = {
                showDialog.value = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {

            ContainerComponent(guidePayload = guidePayload) {
                Box {
                    guidePayload.guide.images.firstOrNull()?.let {
                        ImageComponent(
                            image = it,
                            modifier = Modifier.align(it.align.toBoxAlignment()),
                            contentScale = ContentScale.FillBounds
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AppTextView(
                            textProperties = guidePayload.guide.titleText
                        )
                        AppTextView(
                            textProperties = guidePayload.guide.contentText
                        )
                        guidePayload.guide.buttons.nextButton?.let {
                            ButtonComponent(buttonModel = it) {
                                
                            }
                        }
                    }
                }
            }
        }
    }
}