package com.example.todoapp

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.todoapp.ui.theme.crimson

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel : MainViewModel,
               navController: NavController,
               editName :String?,
               editDescription :String?,
               itemIndex :Int?) {

    Scaffold (
        topBar = {
            MainTopBar(
                refresh = {viewModel.fetchTask()},
                addTask ={ viewModel.showAddTask.value = true }
            )
        })
    {innerPadding ->

        editName?.let {
            viewModel.editTask(viewModel.list[itemIndex!!],editName)
        }
        editDescription?.let {
            viewModel.editDescription(viewModel.description[itemIndex!!],editDescription)
        }

        Column(Modifier.padding(innerPadding)) {
            Box{
                Column(
                ){
                    TasksList(viewModel.listOfTasks, {index ->  TaskNavigator(navController,index) })

                    if (viewModel.showAddTask.value) {
                        AddNewTask(viewModel)
                    }
                }
            }
        }
    }
}

fun TaskNavigator(navController: NavController,index: Int){
    navController.navigate(route = "TaskDetails/$index")
}

@Composable
fun MainTopBar(refresh : ()->Unit,addTask : ()->Unit){
    Row (
        Modifier
            .height(50.dp)
            .background(crimson)
            .fillMaxWidth()
    ){
        Column(horizontalAlignment = Alignment.End){
            Button(onClick = refresh,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh")
            }
        }
        Column {
            Button(
                onClick = addTask,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    }
}

@Composable
fun TasksList(listOfTasks: State<List<String>>, onClick: (index:Int) -> Unit){
    Text(
        text = "List Of Tasks",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(10.dp)
    )

    LazyColumn(){
        itemsIndexed(listOfTasks.value)
        { index,task ->
            Card(
                Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .clickable(onClick = { onClick(index) }))
            {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text= task,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(10.dp),
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun AddNewTask(viewModel : MainViewModel){
    var isError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { viewModel.showAddTask.value= false }
    ) {
        Box(
            modifier = Modifier
                .padding(top = 100.dp, bottom = 80.dp)
                .fillMaxSize()
        ) {
            Card(
                Modifier.wrapContentHeight(),
                shape = RoundedCornerShape(15.dp),
                border = BorderStroke(2.dp, crimson)
            ) {
                // Task Bar
                Row(
                    Modifier
                        .padding(
                            top = 20.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 10.dp
                        )
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.add_task),
                        contentDescription = "taskIcon",
                        Modifier.size(40.dp),
                        alignment = Alignment.CenterStart
                    )
                    TextField(
                        modifier = Modifier
                            .size(width = 180.dp, height = 80.dp)
                            .align(alignment = Alignment.CenterVertically),
                        value = viewModel.newTask.value,
                        onValueChange = { viewModel.newTask.value = it },
                        label = { Text(text = "Task Name") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = crimson,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedLabelColor = crimson,
                            unfocusedLabelColor = crimson,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent
                        ),
                        supportingText = {
                            if (isError) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Invalid Task name",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )

                }

                Row {
                    Box(
                        Modifier
                            .padding(start = 15.dp, end = 15.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .height(90.dp)
                            .border(1.dp, Color.LightGray)
                    ) {
                        TextField(
                            modifier = Modifier
                                .background(Color.White)
                                .fillMaxSize(),
                            value = viewModel.newdescription.value,
                            onValueChange = { viewModel.newdescription.value = it },
                            label = { Text(text = "") },
                            singleLine = false,
                            placeholder = { Text(text = "Description") },
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedLabelColor = Color.Transparent,
                                unfocusedLabelColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            )
                        )
                    }
                }
                Row(
                    Modifier.wrapContentSize(Alignment.BottomEnd)
                ) {

                    ThemeButton(onClick = {
                        viewModel.showAddTask.value = false
                        viewModel.newdescription.value = ""
                        viewModel.newTask.value = "" },
                        text = "Cancel",
                        color = Color.Black)

                    ThemeButton(onClick = {
                        if (viewModel.newTask.value != "") {
                            viewModel.addTasks(viewModel.newTask.value,viewModel.newdescription.value)
                            viewModel.showAddTask.value = false
                            viewModel.newdescription.value = ""
                            viewModel.newTask.value = ""
                        } else {
                            isError = true
                        }
                    },
                        text = "Done",
                        color = crimson,)

                }
            }
        }
    }
}

@Composable
fun ThemeButton(onClick : ()->Unit, text:String, color: Color){
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
    ) {
        Text(
            text = text,
            color = color,
            textAlign = TextAlign.End
        )
    }
}