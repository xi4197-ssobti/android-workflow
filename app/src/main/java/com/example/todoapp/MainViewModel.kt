package com.example.todoapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    // EventActionState
    var newTask = mutableStateOf("")
    var newdescription = mutableStateOf("")
    var showAddTask = mutableStateOf(false)


    val list = SnapshotStateList<String>()
    private val tasksList = mutableStateOf(list)
    val listOfTasks: State<List<String>> = tasksList

    val description = SnapshotStateList<String>()
    private val tasksDetail = mutableStateOf(description)

    fun fetchTask(){
        tasksList.value = list.apply {
            add("Task 1")
            add("Task 2")
        }

        tasksDetail.value = description.apply {
            add("Task 1 - Say Hello")
            add("Task 2 - Say Wait")
        }
    }

    fun addTasks(taskName :String,taskDescription: String){
        tasksList.value = list.apply{
            add(taskName)
        }
        tasksDetail.value= description.apply{
            add(taskDescription)
        }
    }

    fun editTask(old:String ,new:String){
        val itemIndex = list.indexOf(old)
        list[itemIndex]=new
        tasksList.value = list
    }

    fun editDescription(old:String , new:String){
        val itemIndex = description.indexOf(old)
        description[itemIndex]=new
        tasksDetail.value = description
    }
}

