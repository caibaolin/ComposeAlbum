package com.cbl.composealbum.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cbl.base.bean.AlbumBean
import com.cbl.base.view.RightLazyItem
import timber.log.Timber

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
    var backHandlingEnabled by remember { mutableStateOf(true) }
    BackHandler(backHandlingEnabled) {
        // Handle back press
        Timber.i("do back")
        backCall.invoke()
        backHandlingEnabled = false
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp)
    ) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(6),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            viewModel.viewStates.albumList.forEach {
                if (it.list.size > 0) {
                    item { RightLazyItem(mediaBean = it.list[0], {}) }
                } else {
                    item { RightLazyItem(null, {}) }
                }
            }
        }
    }

}