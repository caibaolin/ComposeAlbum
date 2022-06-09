package com.cbl.base.view

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import com.cbl.base.bean.AlbumBean

/**
 * <pre>
 *     author : caibaolin
 *     e-mail : 401115406@qq.com
 *     time   : 2022/6/8 18:16
 *     desc   :
 * </pre>
 */
@Composable
fun LeftLazyItem(albumBean: AlbumBean,onClick: () -> Unit){
    Text(text = "${albumBean.getShowName()}  ${albumBean.list.size}")
}
