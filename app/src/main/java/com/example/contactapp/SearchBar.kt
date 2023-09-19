package com.example.contactapp


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.tools.build.jetifier.core.utils.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    label: String = "",
    searchQueries: MutableState<String>,
    hideKeyboard: Boolean = false,
    onFocusClear: () -> Unit = {},
    onSearch: (String) -> Unit = {},
) {
    val isHintDisplayed = remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = searchQueries.value,
        onValueChange = {
            searchQueries.value = it
            onSearch(searchQueries.value)
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            focusManager.clearFocus()
            onSearch(searchQueries.value)
        }),
        maxLines = 1,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .onFocusChanged {
                isHintDisplayed.value = !it.hasFocus
            },
        shape = CircleShape,
        label = { Text(label) },
        leadingIcon = {Icon(Icons.Filled.Search,"SearchIcon", tint = Color.White) },
        trailingIcon = {Icon(Icons.Filled.Settings,"SearchIcon", tint = Color.White) }
    )
    if (hideKeyboard) {
        focusManager.clearFocus()
        onFocusClear()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactTextFields(
    label: String,
    text: MutableState<String>,
    numberKeyBoard: Boolean,
    leadingIcon: ImageVector?,
    hideKeyboard: Boolean = false,
) {
    var keyboardOptions =  KeyboardOptions(imeAction = ImeAction.Done)
    if (numberKeyBoard){
       keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search, keyboardType = KeyboardType.Number)
    }
    val isHintDisplayed = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = text.value,
        onValueChange = {
            text.value = it
        },
        keyboardOptions = keyboardOptions ,
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
        }),
        maxLines = 1,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(10.dp)
            .onFocusChanged {
                isHintDisplayed.value = !it.hasFocus
            },
        shape = CircleShape,
        label = { Text(label) },
        leadingIcon = {
            leadingIcon?.let {
                Icon(imageVector = it, contentDescription = null)
            }
        },
    )
    if (hideKeyboard) {
        focusManager.clearFocus()
    }
}