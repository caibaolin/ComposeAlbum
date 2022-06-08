package com.cbl.composealbum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontListFontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.core.view.WindowCompat
import com.cbl.base.MediaUtil
import com.cbl.composealbum.ui.theme.ComposeAlbumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            HomePage()
        }
    }
}

@Composable
fun Greeting(name: String) {
    /*BoxWithConstraints() {

    }*/
    val data = MediaUtil.albumData.collectAsState().value
    MediaUtil.sortAlbumList(data.alllist)
    var index by remember {
        mutableStateOf(0)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(0.29f)
            .fillMaxHeight()
    ) {
        data.alllist.forEach {
            item {
                Text(text = "${it.getShowName()}  size:${it.list.size}" /*modifier = Modifier.fillMaxSize().padding(20.dp)*/)
            }
        }
    }
}

@Composable
fun HomePage() {
    val constraints = ConstraintSet {
        val leftBox = createRefFor("leftBox")
        val rightBox = createRefFor("rightBox")
        val appName = createRefFor("appName")
        val bt_left_edit = createRefFor("bt_left_edit")
        val bt_new_album = createRefFor("bt_new_album")
        val left_box_bottom_fun = createRefFor("left_box_bottom_fun")

        constrain(leftBox) {
            start.linkTo(parent.start)
        }
        constrain(rightBox) {
            end.linkTo(parent.end)
        }
        constrain(appName){
            top.linkTo(leftBox.top,50.dp)
            start.linkTo(leftBox.start,50.dp)
        }
        constrain(bt_left_edit){
            top.linkTo(appName.top)
            bottom.linkTo(appName.bottom)
            end.linkTo(leftBox.end,20.dp)
        }
        constrain(bt_new_album){
            start.linkTo(leftBox.start)
            end.linkTo(leftBox.end)
            bottom.linkTo(leftBox.bottom)
        }
        constrain(left_box_bottom_fun){
            start.linkTo(leftBox.start)
            end.linkTo(leftBox.end)
            bottom.linkTo(leftBox.bottom)
        }
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize(), constraintSet = constraints
    ) {
        /*
        * left
        * */
        Box(
            modifier = Modifier
                .layoutId("leftBox")
                .fillMaxWidth(0.29f)
                .fillMaxHeight()
                .background(
                    Color(235, 235, 235)
                )
        ) {

        }
        Text(text = "相册", modifier = Modifier.layoutId("appName"), fontSize = 25.sp)
        Button(modifier = Modifier.layoutId("bt_left_edit"),onClick = { /*TODO*/ }) {
            Text(text = "编辑")
        }
        Text(text = "新建相册", modifier = Modifier.layoutId("bt_new_album"))
        Box(modifier = Modifier.layoutId("left_box_bottom_fun").height(50.dp).fillMaxWidth(0.29f).background(Color.Black))
        /*
      * right
      * */
        Box(
            modifier = Modifier
                .layoutId("rightBox")
                .fillMaxWidth(0.71f)
                .fillMaxHeight()
                .background(
                    Color(255, 255, 255, 0x4d)
                )
        ) {

        }
    }


}