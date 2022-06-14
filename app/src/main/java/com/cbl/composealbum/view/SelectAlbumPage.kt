package com.cbl.composealbum.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cbl.base.bean.AlbumBean
import com.cbl.base.view.CommonAlbum
import com.cbl.base.view.RightLazyItem
import timber.log.Timber
import com.cbl.composealbum.R

/**
 * <pre>
 *     author : caibaolin
 *     e-mail : 401115406@qq.com
 *     time   : 2022/6/13 18:02
 *     desc   :
 * </pre>
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectAlbumPage(viewModel: SelectAlbumViewModel = viewModel(), backCall: () -> Unit) {
 /*   var backHandlingEnabled by remember { mutableStateOf(true) }
    BackHandler(backHandlingEnabled) {
        // Handle back press
        Timber.i("do back")
        backCall.invoke()
        backHandlingEnabled = false
    }*/
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { backCall.invoke() }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_h_back),
                    contentDescription = null
                )
            }
            Text(text = "请选择要添加到的相册")
        }

        LazyVerticalGrid(
            cells = GridCells.Fixed(8),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {

            viewModel.viewStates.albumList.forEach {
                item { CommonAlbum(it) }
            }
        }
    }


}