package com.cbl.base.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cbl.base.bean.AlbumBean
import com.cbl.base.bean.MediaBean
import org.jetbrains.annotations.Async
import timber.log.Timber

/**
 * <pre>
 *     author : caibaolin
 *     e-mail : 401115406@qq.com
 *     time   : 2022/6/8 18:16
 *     desc   :
 * </pre>
 */
@Composable
fun LeftLazyItem(albumBean: AlbumBean, onClick: () -> Unit, clickSelect: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(if (clickSelect) Color.Red else Color.Transparent)
            .clickable {
                onClick.invoke()
            }) {
        Text(text = "${albumBean.getShowName()}  ${albumBean.list.size}")
    }
}

@Composable
fun RightLazyItem(mediaBean: MediaBean, onClick: () -> Unit) {
    AsyncImage(
        model = mediaBean.path,
        contentDescription = null,
        modifier = Modifier.aspectRatio(1f),
        contentScale = ContentScale.Crop
    )
}