package com.example.edistynytmobiili3004.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiili3004.api.categoriesService
import com.example.edistynytmobiili3004.model.CategoriesState
import com.example.edistynytmobiili3004.model.CategoryItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {



    private val _categoriesState = mutableStateOf(CategoriesState())
    val categoriesState: State<CategoriesState> = _categoriesState

    init {
        getCategories()
    }

    private suspend fun waitForCategories() {
        delay(2000)
    }

    private fun getCategories() {

        viewModelScope.launch {
            try {
                Log.d("juhani", "in getCategories:: starting to fetch data")
                _categoriesState.value = _categoriesState.value.copy(loading = true)
                val response = categoriesService.getCategories()
                _categoriesState.value = categoriesState.value.copy(

                    list = response.categories

                )

                Log.d("juhani", "in getCategories:: done  fetching data")
            } catch(e: Exception) {
                _categoriesState.value = _categoriesState.value.copy(err=e.message)
            } finally {
                _categoriesState.value = _categoriesState.value.copy(loading = false)
            }

        }
    }


}