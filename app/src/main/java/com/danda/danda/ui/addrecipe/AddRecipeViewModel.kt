package com.danda.danda.ui.addrecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.danda.danda.helper.Event
import com.google.firebase.firestore.FirebaseFirestore

class AddRecipeViewModel : ViewModel() {

    private val _addSuccess = MutableLiveData<Event<Boolean>>()
    val addSuccess: LiveData<Event<Boolean>> = _addSuccess

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    private val _isFailed = MutableLiveData<Event<Boolean>>()
    val isFailed: LiveData<Event<Boolean>> = _isFailed

   fun saveRecipeFireStore(nameRecipe: String, ingredients: String, tools: String, howToCook: String) {
       _isLoading.value = Event(true)
       val db = FirebaseFirestore.getInstance()
       val recipe: MutableMap<String, Any> = HashMap()
       recipe["nameRecipe"] = nameRecipe
       recipe["ingredients"] = ingredients
       recipe["tools"] = tools
       recipe["howToCook"] = howToCook

       db.collection("recipe")
           .add(recipe)
           .addOnSuccessListener {
               _isLoading.value = Event(false)
               _addSuccess.value = Event(true)
           }
           .addOnFailureListener {
               _isLoading.value = Event(false)
               _isFailed.value = Event(true)
           }
    }

}