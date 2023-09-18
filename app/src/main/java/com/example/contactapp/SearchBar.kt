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
    numberKeyBoard: Boolean,
    leadingIcon: ImageVector?,
    isRequired: Boolean,
    isValidInput: MutableState<Boolean>,
    hideKeyboard: Boolean = false,
    onFocusClear: (String) -> Unit = {}
) {
    var keyboardOptions =  KeyboardOptions(imeAction = ImeAction.Done)
    if (numberKeyBoard){
       keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search, keyboardType = KeyboardType.Number)
    }
    val text = remember { mutableStateOf("") }
    val isHintDisplayed = remember { mutableStateOf(false) }
    val isErrorDisplayed = text.value.isEmpty()
    val focusManager = LocalFocusManager.current
    val textFieldColors = if (isRequired) {
        TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = if (isErrorDisplayed) Color.Red else Color.Green,
            focusedIndicatorColor = if (isErrorDisplayed) Color.Red else Color.Green
        )
    } else {
        TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Green,
            focusedIndicatorColor = Color.Green
        )
    }
    if (label == "Name") {
        isValidInput.value = text.value.isNotBlank()
    }
    OutlinedTextField(
        value = text.value,
        onValueChange = {
            text.value = it
            onFocusClear("$label ${text.value}")
        },
        keyboardOptions = keyboardOptions ,
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
            onFocusClear("$label ${text.value}")
        }),
        maxLines = 1,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(10.dp)
            .onFocusChanged {
                isHintDisplayed.value = !it.hasFocus
            },
        isError = isErrorDisplayed && isRequired,
        shape = CircleShape,
        label = { Text(label) },
        colors = textFieldColors,
        leadingIcon = {
            leadingIcon?.let {
                Icon(imageVector = it, contentDescription = null)
            }
        },
        trailingIcon = { if (isErrorDisplayed && isRequired) Text(text = "Required  ", textAlign = TextAlign.Center) else Icon(Icons.Filled.Check ,"")}
    )
    if (hideKeyboard) {
        focusManager.clearFocus()
        onFocusClear("$label ${text.value}")
    }
}