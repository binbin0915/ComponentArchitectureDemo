package com.wangkai.compose.ui.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wangkai.compose.ui.theme.MyApplicationTheme

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .width(150.dp)
            .clickable {


            })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}