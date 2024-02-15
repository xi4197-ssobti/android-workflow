package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todoapp.ui.theme.ToDoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel: MainViewModel by viewModels()
       // val taskDetailsViewModel: TaskDetailsViewModel by viewModels()
        
        setContent {
            ToDoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Main(mainViewModel)
                }
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun Main(mainViewModel: MainViewModel){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "MainScreen" ){
        composable(route = "MainScreen"
        ){entry ->
            val itemIndex = entry.savedStateHandle.get<Int>("index")
            val editName = entry.savedStateHandle.get<String>("newTaskName")
            val editDescription = entry.savedStateHandle.get<String>("newTaskDescription")
            MainScreen(mainViewModel, navController,editName,editDescription,itemIndex)
        }
        composable(route = "TaskDetails/{index}",
            arguments = listOf(
                navArgument(name = "index"){
                    type = NavType.IntType
                }
            )
        ){
                index ->
            TaskDetails(
                itemIndex = index.arguments?.getInt("index"),
                taskList = mainViewModel.list,
                descriptionList = mainViewModel.description,
                navController = navController)
        }
    }
}







