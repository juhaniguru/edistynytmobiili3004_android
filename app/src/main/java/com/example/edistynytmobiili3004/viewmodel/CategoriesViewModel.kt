package com.example.edistynytmobiili3004.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiili3004.api.categoriesService
import com.example.edistynytmobiili3004.model.CategoriesState
import com.example.edistynytmobiili3004.model.CategoryItem
import com.example.edistynytmobiili3004.model.DeleteCategoryState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {



    private val _categoriesState = mutableStateOf(CategoriesState())
    val categoriesState: State<CategoriesState> = _categoriesState

    private val _deleteCategoryState = mutableStateOf(DeleteCategoryState())
    val deleteCategoryState: State<DeleteCategoryState> = _deleteCategoryState

    init {
        getCategories()
    }

    fun verifyCategoryRemoval(categoryId: Int) {
        _deleteCategoryState.value = _deleteCategoryState.value.copy(id=categoryId)
    }
    fun deleteCategoryById(categoryId: Int) {

        viewModelScope.launch {
            try {
                categoriesService.removeCategory(categoryId)
                val listOfCategories = _categoriesState.value.list.filter {
                    // jos tämä ehto on totta, menee vuorossa oleva item listaan
                    // jos tämä ehto ei ole totta, jää itemi listasta pois
                    categoryId != it.id
                }


                _categoriesState.value = _categoriesState.value.copy(list=listOfCategories)
            } catch(e: Exception) {
                Log.d("juhani", e.toString())
            } finally {
                //
            }
        }

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