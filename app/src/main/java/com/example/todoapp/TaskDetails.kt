package com.example.todoapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.todoapp.ui.theme.crimson

@Composable
fun TaskDetails(
    itemIndex :Int?,
    taskList : SnapshotStateList<String>,
    descriptionList : SnapshotStateList<String>,
    navController : NavHostController
){

    Column(
        Modifier.fillMaxSize()
    ){
        HandlerBar(onClick = {
            navController.popBackStack()})
        TaskHeader(taskList[itemIndex!!],
            saveOnClick = {newName ->
            navController.previousBackStackEntry?.savedStateHandle
                ?.set(key = "newTaskName",value = newName )
                navController.previousBackStackEntry?.savedStateHandle
                    ?.set(key = "index",value = itemIndex)

        })
        Divider(Modifier.padding(start = 10.dp, end = 10.dp, bottom = 15.dp),color = crimson)

        TaskDescription(descriptionList[itemIndex],
            saveOnClick = {newDescription->
                navController.previousBackStackEntry?.savedStateHandle
                    ?.set(key = "newTaskDescription",value = newDescription)
            }  )

    }
}


@Composable
fun HandlerBar(onClick: () -> Unit){
    Row (
        Modifier
            .height(50.dp)
            .background(crimson)
            .fillMaxWidth()
    ){
        Button(onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back")
        }
    }
}

@Composable
fun TaskHeader(listItem :String ,saveOnClick :(newName:String)->Unit){
    var newName by remember { mutableStateOf(listItem) }
    var showNameEdit by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    // Task Details
    Row (
        Modifier
            .padding(20.dp)
            .fillMaxWidth()
    ){
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "taskIcon" ,
            Modifier.size(40.dp)
        )
        if(!showNameEdit) {
            Row(modifier = Modifier.fillMaxWidth()) {

                Text(
                    text = newName,
                    Modifier.padding(start = 20.dp),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = crimson,
                    textAlign = TextAlign.Left
                )

                Button(
                    onClick = {
                        showNameEdit = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        tint = Color.Blue,
                        contentDescription = "Edit"
                    )
                }

            }
        }else {
            // On Edit
            Row {
                TextField(
                    modifier = Modifier
                        .size(width = 180.dp, height = 80.dp),
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text(text = "Edit Name") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = crimson,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = crimson,
                        unfocusedLabelColor = crimson,
                        focusedContainerColor  = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    ),
                    supportingText = {
                        if (isError) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Invalid Task name",
                                color = Color.Red
                            )
                        }
                    }
                )

                CancelButton (onClick = {
                    showNameEdit = false
                })

               SaveButton (onClick = {
                   if (newName != "") {
                       saveOnClick(newName)
                       showNameEdit = false
                   }else{
                       isError = true
                   }
               })
            }
        }
    }
}

@Composable
fun TaskDescription(descriptionItem:String,saveOnClick :(newDescription:String)->Unit){
    var newDescription by remember { mutableStateOf(descriptionItem) }
    var showDescriptionEdit by remember { mutableStateOf(false) }
    Box {
        Column {
            Text(
                text = "Description",
                Modifier.padding(start = 20.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                textAlign = TextAlign.Left
            )

            if (!showDescriptionEdit) {
                Text(
                    text = newDescription,
                    fontSize = 18.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(15.dp)
                        .clickable {
                            showDescriptionEdit = true
                        }
                )
            } else {
                TextField(
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth()
                        .height(140.dp)
                        .border(1.dp, Color.LightGray)
                        .clip(RoundedCornerShape(18.dp)),
                    value = newDescription,
                    onValueChange = { newDescription = it },
                    label = { Text(text = "") },
                    singleLine = false,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color.Transparent,
                        unfocusedLabelColor = Color.Transparent,
                        focusedContainerColor  = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    )
                )
            }

            Row(horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .alpha(if (showDescriptionEdit) 1f else 0f)
                    .fillMaxWidth()) {

                CancelButton(onClick = {
                    showDescriptionEdit = false
                })

                SaveButton(onClick = {
                    saveOnClick(newDescription)
                    showDescriptionEdit = false
                })


            }
        }
    }
}

@Composable
fun SaveButton(onClick: () -> Unit){
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
    ) {
        Icon(
            imageVector = Icons.Default.Done,
            tint = Color.Green,
            contentDescription = "Save"
        )
    }

}

@Composable
fun CancelButton(onClick: () -> Unit){
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
    ) {
        Icon(
            imageVector = Icons.Default.Clear,
            tint = Color.Red,
            contentDescription = "Cancel"
        )
    }

}

