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
import timber.log.Timber

/**
 * <pre>
 *     author : caibaolin
 *     e-mail : 401115406@qq.com
 *     time   : 2022/6/10 10:06
 *     desc   :
 * </pre>
 */
class HomeViewModel : ViewModel() {
    val initClickAlbumBean = AlbumBean()
    var viewStates by mutableStateOf(HomeViewState(clickAlbumBean = initClickAlbumBean))
        private set

    init {
        dispatch(HomeViewAction.RegisterMedia)
    }

    private fun registerMedia() {
        viewModelScope.launch {
            MediaUtil.albumData.collectLatest { albumData ->
                val sortAllAlbumBeanList = mutableListOf<AlbumBean>()
                sortAllAlbumBeanList.addAll(albumData.alllist)
                MediaUtil.sortAlbumList(sortAllAlbumBeanList)
                if (viewStates.clickAlbumBean == initClickAlbumBean) {
                    viewStates = viewStates.copy(
                        albumList = sortAllAlbumBeanList,
                        clickAlbumBean = sortAllAlbumBeanList[0]
                    )
                } else {
                    var temp: AlbumBean? = null
                    sortAllAlbumBeanList.forEach {
                        if (it.relative_path == viewStates.clickAlbumBean.relative_path) {
                            temp = it
                            return@forEach
                        }
                    }
                    viewStates = viewStates.copy(
                        albumList = sortAllAlbumBeanList,
                        clickAlbumBean = temp ?: sortAllAlbumBeanList[0]
                    )
                }
            }
        }
    }

    private fun leftOnClick(bean: AlbumBean) {
        Timber.i("leftOnClick albumBean")
        if (viewStates.albumList.contains(bean)) {
            Timber.i("leftOnClick albumBean copy")
            viewStates = viewStates.copy(clickAlbumBean = bean)
        }
    }

    fun dispatch(action: HomeViewAction) {
        Timber.i("dispatch action-->$action")
        when (action) {
            is HomeViewAction.RegisterMedia -> registerMedia()
            is HomeViewAction.LeftOnClick -> leftOnClick(action.bean)
        }
    }

}

data class HomeViewState(
    val albumList: List<AlbumBean> = emptyList<AlbumBean>(),
    val clickAlbumBean: AlbumBean = AlbumBean()
)

sealed class HomeViewAction {
    object RegisterMedia : HomeViewAction()
    object LeftEdit : HomeViewAction()
    data class LeftOnClick(val bean: AlbumBean) : HomeViewAction()
}