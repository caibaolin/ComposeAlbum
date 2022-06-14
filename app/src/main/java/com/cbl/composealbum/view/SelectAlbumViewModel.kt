package com.cbl.composealbum.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbl.base.MediaUtil
import com.cbl.base.bean.AlbumBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * <pre>
 *     author : caibaolin
 *     e-mail : 401115406@qq.com
 *     time   : 2022/6/14 11:49
 *     desc   :
 * </pre>
 */
class SelectAlbumViewModel : ViewModel() {
    var viewStates by mutableStateOf(SelectAlbumState())
        private set

    init {
        dispatch(SelectAlbumAction.RegisterMedia)
    }

    private fun registerMedia() {
        viewModelScope.launch {
            MediaUtil.albumData.collectLatest { albumData ->
                var sortAllAlbumBeanList = mutableListOf<AlbumBean>()
                sortAllAlbumBeanList.addAll(albumData.alllist)
                sortAllAlbumBeanList= withContext(Dispatchers.IO){
                    MediaUtil.sortAlbumList(sortAllAlbumBeanList)
                }
                viewStates = viewStates.copy(albumList = sortAllAlbumBeanList)

            }
        }
    }

    fun dispatch(action: SelectAlbumAction) {
        when (action) {
            is SelectAlbumAction.RegisterMedia -> registerMedia()
        }
    }
}

data class SelectAlbumState(
    val albumList: List<AlbumBean> = emptyList<AlbumBean>()
)

sealed class SelectAlbumAction {
    object RegisterMedia : SelectAlbumAction()
}