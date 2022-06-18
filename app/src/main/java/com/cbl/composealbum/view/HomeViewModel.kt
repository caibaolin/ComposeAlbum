package com.cbl.composealbum.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbl.base.MediaUtil
import com.cbl.base.bean.AlbumBean
import com.cbl.base.bean.MediaBean
import com.cbl.base.bean.allAlbumBean
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
                var sortAllAlbumBeanList = mutableListOf<AlbumBean>()
                sortAllAlbumBeanList.add(0,albumData.allAlbumBean)
                sortAllAlbumBeanList.addAll(albumData.alllist)
                sortAllAlbumBeanList= withContext(Dispatchers.IO){
                    MediaUtil.sortAlbumList(sortAllAlbumBeanList)
                }
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
        if(!viewStates.showLeftEdit){
            if(!bean.isCanEdit){
                return
            }
            leftEditSelect(bean)
        }else{
            if (viewStates.albumList.contains(bean)) {
                Timber.i("leftOnClick albumBean copy")
                viewStates = viewStates.copy(clickAlbumBean = bean)
            }
        }
    }

    private fun changeSort() {
        Timber.i("changeSort")
        val albumBeanList: MutableList<AlbumBean> = mutableListOf()
        var clickAlbumBeanTemp=AlbumBean()
        viewStates.albumList.forEach {albumBean->
            val mediaBeanList: MutableList<MediaBean> = mutableListOf()
            mediaBeanList.addAll(albumBean.list)
            val list=mediaBeanList.sortedByDescending {
                it._size
            }
            val temp=albumBean.copy(list = list.toMutableList())
            if(viewStates.clickAlbumBean===albumBean){
                Timber.i("changeSort clickAlbumBean viewStates.clickAlbumBean-->${viewStates.clickAlbumBean}")
                Timber.i("changeSort clickAlbumBean mediaBeanList-->${mediaBeanList}")

                clickAlbumBeanTemp=temp
            }
            albumBeanList.add(temp)
        }
        viewStates = viewStates.copy(
            albumList =albumBeanList,
            clickAlbumBean = clickAlbumBeanTemp
        )
    }

    private fun leftEdit() {
        val temp=!viewStates.showLeftEdit
        viewStates = viewStates.copy(showLeftEdit = temp)
    }

    private fun addAlbum(targetName:String) {

    }

    private fun reNameAlbum(bean: AlbumBean,targetName:String) {

    }

    private fun deleteAlbum(list: MutableList<AlbumBean>) {

    }

    fun dispatch(action: HomeViewAction) {
        Timber.i("dispatch action-->$action")
        when (action) {
            is HomeViewAction.RegisterMedia -> registerMedia()
            is HomeViewAction.LeftOnClick -> leftOnClick(action.bean)
            is HomeViewAction.ChangeSort -> changeSort()
            is HomeViewAction.LeftEdit -> leftEdit()
            is HomeViewAction.ReNameAlbumAction -> reNameAlbum(action.bean,action.targetName)
            is HomeViewAction.AddAlbumAction -> addAlbum(action.targetName)
            is HomeViewAction.DeleteAlbumAction -> deleteAlbum(action.list)
        }
    }
    private fun leftEditSelect( bean: AlbumBean){
        val temp: MutableList<AlbumBean> = mutableListOf<AlbumBean>()
        temp.addAll(viewStates.leftEditSelectList)
        if(temp.contains(bean)){
            temp.remove(bean)
        }else{
            temp.add(bean)
        }
        viewStates=viewStates.copy(leftEditSelectList = temp)
    }

}

data class HomeViewState(
    val albumList: List<AlbumBean> = emptyList<AlbumBean>(),
    val clickAlbumBean: AlbumBean = AlbumBean(),
    val showLeftEdit: Boolean = true,
    val leftEditSelectList: List<AlbumBean> = emptyList<AlbumBean>(),
)

sealed class HomeViewAction {
    object RegisterMedia : HomeViewAction()
    object LeftEdit : HomeViewAction()
    data class LeftOnClick(val bean: AlbumBean) : HomeViewAction()
    object ChangeSort : HomeViewAction()
    data class ReNameAlbumAction(val bean: AlbumBean, val targetName:String) : HomeViewAction()
    data class AddAlbumAction(val targetName:String) : HomeViewAction()
    data class DeleteAlbumAction(val list: MutableList<AlbumBean>) : HomeViewAction()
}