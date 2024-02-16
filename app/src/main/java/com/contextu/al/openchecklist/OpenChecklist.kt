package com.contextu.al.openchecklist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.contextu.al.R
import com.contextu.al.openchecklist.model.OpenChecklistTask
import com.contextu.al.openchecklist.model.parsJSONtoTaskList
import org.json.JSONException
import org.json.JSONObject


@Composable
fun OpenChecklist(
    title: String = "",
    payload: String = "",
    setTag: (String,String)->Unit,
    dismiss: (()->Unit)? = null
) {
    val tasks: List<OpenChecklistTask> =  parsJSONtoTaskList(payload)

    Dialog(
        onDismissRequest = { dismiss?.invoke() },
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Column(
            modifier = Modifier
                .width(600.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(title!=""){
                Text(
                    text = title,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFf2f2f8)),
                contentAlignment = Alignment.Center
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {

                    Spacer(modifier = Modifier.height(5.dp))


                    tasks.forEachIndexed { index, it ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp, vertical = 15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ){
                            Text(
                                text = it.name,
                                style = TextStyle(fontSize = 16.sp),
                                color = if(index % 2 == 0) Color.Gray else Color.Black
                            )

                            if(index % 2 == 0){
                                Image(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(id = R.drawable.ico_checkbox_checked),
                                    contentDescription = "icon_checked"
                                )
                            } else {
                                Image(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable { setTag(it.checkedKey(), "true") },
                                    painter = painterResource(id = R.drawable.ico_checkbox_unchecked),
                                    contentDescription = "icon_checked"
                                )
                            }
                        }

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp),
                            thickness = 1.dp,
                            color = Color.LightGray
                        )

                    }


                    Spacer(modifier = Modifier.height(15.dp))

                }
            }


        }
    }
}


