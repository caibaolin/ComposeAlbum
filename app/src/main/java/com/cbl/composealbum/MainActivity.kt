package com.cbl.composealbum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cbl.base.MediaUtil
import com.cbl.composealbum.ui.theme.ComposeAlbumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            Greeting("Android")
        }
    }
}

@Composable
fun Greeting(name: String) {
    val data = MediaUtil.albumData.collectAsState().value
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        data.alllist.forEach {
            item {
                Text(text = "${it.getShowName()}  size:${it.list.size}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeAlbumTheme {
        Greeting("Android")
    }
}