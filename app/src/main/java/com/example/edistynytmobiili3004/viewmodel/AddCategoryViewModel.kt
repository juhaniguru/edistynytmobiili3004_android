package com.example.edistynytmobiili3004.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiili3004.api.categoriesService
import com.example.edistynytmobiili3004.model.AddCategoryReq
import com.example.edistynytmobiili3004.model.CategoryState
import kotlinx.coroutines.launch

class AddCategoryViewModel: ViewModel() {
    private val _addCategoryState = mutableStateOf(CategoryState())
    val categoryState: State<CategoryState> = _addCategoryState

    fun setName(name: String) {
        var item = _addCategoryState.value.item
        item = item.copy(name=name)
        _addCategoryState.value = _addCategoryState.value.copy(item=item)

    }

    fun setAddCateogyOk(ok: Boolean) {
        _addCategoryState.value = _addCategoryState.value.copy(ok=ok)
    }


    fun create() {
        viewModelScope.launch {
            try {
                _addCategoryState.value = _addCategoryState.value.copy(loading = true)
                categoriesService.createCategory(AddCategoryReq(
                    name=_addCategoryState.value.item.name
                ))
                setAddCateogyOk(true)
            } catch(e: Exception) {
                _addCategoryState.value = _addCategoryState.value.copy(err = e.toString())
            } finally {
                _addCategoryState.value = _addCategoryState.value.copy(loading = false)
            }
        }
    }
}