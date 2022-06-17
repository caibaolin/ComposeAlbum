package com.cbl.base.view

import android.os.Build.VERSION.SDK_INT
import android.widget.CheckBox
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import coil.request.videoFrameMicros
import coil.request.videoFrameMillis
import coil.transition.Transition
import com.cbl.base.bean.AlbumBean
import com.cbl.base.bean.MediaBean
import com.cbl.composealbum.ui.theme.h_black_70
import com.cbl.composealbum.ui.theme.h_red_9
import com.cbl.composealbum.ui.theme.theme_color
import timber.log.Timber
import com.cbl.composealbum.R

/**
 * <pre>
 *     author : caibaolin
 *     e-mail : 401115406@qq.com
 *     time   : 2022/6/8 18:16
 *     desc   :
 * </pre>
 */
val brush = Brush.horizontalGradient(colors = listOf(h_red_9, Color.Transparent))
val brush2 = Brush.horizontalGradient(colors = listOf(Color.Transparent, Color.Transparent))

@Composable
fun LeftLazyItem(
    albumBean: AlbumBean,
    onClick: () -> Unit,
    clickSelect: Boolean,
    leftEdit: Boolean,
    leftEditSelect: Boolean
) {
    val hasbg = when {
        leftEdit && leftEditSelect -> true
        !leftEdit && clickSelect -> true
        else -> false
    }
    val fontColor = if (hasbg) theme_color else Color.Black
    Timber.i("LeftLazyItem hasbg-->$hasbg")
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(432f / 116f)
            .background(brush = if (hasbg) brush else brush2)
            .padding(start = 20.dp, end = if(leftEdit) 8.dp else 20.dp)
            .clickable {
                onClick.invoke()
            }) {
        Text(
            text = "${albumBean.getShowName()}",
            modifier = Modifier.weight(1f),
            color = if(hasbg) fontColor else fontColor.copy(alpha = 0.7f),
            fontSize = 21.sp,
            fontWeight = FontWeight(500),
            overflow=TextOverflow.Ellipsis,
            maxLines = 1,
        )
        Text(
            text = "${albumBean.list.size}",
            color = if(hasbg) fontColor.copy(alpha = 0.9f) else fontColor.copy(alpha = 0.5f),
            fontWeight = FontWeight(400),
            fontSize = 21.sp,
            modifier = Modifier,
            maxLines = 1,
        )
        AnimatedVisibility(visible = leftEdit, modifier = Modifier) {
            Checkbox(
                checked = leftEditSelect,
                onCheckedChange = { onClick.invoke() },
                colors = CheckboxDefaults.colors(checkedColor = theme_color)
            )
        }
    }
}

@Composable
fun RightLazyItem(mediaBean: MediaBean?, onClick: () -> Unit) {
    /* val imageLoader = ImageLoader.Builder(LocalContext.current)
         .components {
             if (SDK_INT >= 28) {
                 add(ImageDecoderDecoder.Factory())
             } else {
                 add(GifDecoder.Factory())
             }
         }
         .build()*/
/*    AsyncImage(
        model = mediaBean.path,
        contentDescription = null,
        modifier = Modifier.aspectRatio(1f),
        contentScale = ContentScale.Crop
    )*/
/*    Image(
        modifier = Modifier
            .size(300.dp),
        painter = rememberAsyncImagePainter(
            model = "https://img-blog.csdnimg.cn/e6bb95f89399404195212196fdaf3964.gif",
            builder = {
                decoder(GifDecoder())//添加GIF解码
                placeholder(R.drawable.head_icon)//占位图
                crossfade(true)//淡出效果
            }),
        contentDescription = null
    )*/
    if (mediaBean != null) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(mediaBean.path)
                .videoFrameMillis(0L)
                .crossfade(true)
                .apply {
                    if (mediaBean.mimeType?.contains("video") == true) {
                        decoderFactory(VideoFrameDecoder.Factory())
                    } else if (mediaBean.mimeType?.contains("gif") == true) {
                        decoderFactory(GifDecoder.Factory())
                    }
                }
                .build(),
//        placeholder = painterResource(R.drawable.placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.aspectRatio(1f),
        )
    } else {
        Box(
            modifier = Modifier
                .background(Color.Red)
                .aspectRatio(1f)
        ) {

        }
    }

}
@Composable
fun LeftAddAlbum( onClick: () -> Unit={}) {
/*    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
       *//* Button(
            onClick = { onClick.invoke() },
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(221f / 288)
                .aspectRatio(221f / 52), colors = ButtonDefaults.buttonColors(backgroundColor = Color(233,208,214)),
            shape = RoundedCornerShape(percent = 50)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_h_add), contentDescription =null )
            Text(text = "新建相册",color = theme_color,
            fontSize = 21.sp,
            fontWeight = FontWeight(500))
        }*//*
    }*/
    CommonBox(modifier = Modifier.fillMaxSize(), onClick = { onClick }) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.ic_h_add),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(35f / 328)
                    .aspectRatio(1f)
            )
            Text(text = "新建相册",color = theme_color,
                fontSize = 21.sp,
                fontWeight = FontWeight(500))
        }
    }
}
@Composable
fun LeftFun( deleteEnable:Boolean=true,renameEnable:Boolean=true,onCancelClick: () -> Unit={},onRenameClick: () -> Unit={},onDeleteClick: () -> Unit={},onAllSelect: () -> Unit={}) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxSize()) {

        CommonBox(modifier = Modifier.fillMaxHeight(), onClick = onCancelClick) {
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(id = R.drawable.ic_h_cancel),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .aspectRatio(1f)
                )
                Text(text = "取消",color = theme_color,
                    fontSize = 15.sp,
                    fontWeight = FontWeight(500))
            }
        }
        CommonBox(enable = renameEnable, modifier = Modifier.fillMaxHeight(), onClick = {}) {
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.ic_h_rename),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .aspectRatio(1f)
                )
                Text(text = "重命名",color = theme_color,
                    fontSize = 15.sp,
                    fontWeight = FontWeight(500))
            }
        }
        CommonBox(enable = deleteEnable,modifier = Modifier.fillMaxHeight(), onClick = {}) {
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(id = R.drawable.ic_h_delete),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .aspectRatio(1f)
                )
                Text(text = "删除",color = theme_color,
                    fontSize = 15.sp,
                    fontWeight = FontWeight(500))
            }
        }
        CommonBox(modifier = Modifier.fillMaxHeight(), onClick = {}) {
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(id = R.drawable.ic_h_all_select),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .aspectRatio(1f)
                )
                Text(text = "全选",color = theme_color,
                    fontSize = 15.sp,
                    fontWeight = FontWeight(500))
            }
        }

    }
}

@Composable
fun CommonAlbum(albumBean: AlbumBean) {
    Column() {
        RightLazyItem(mediaBean = albumBean.list.firstOrNull(), {})
        Text(text = albumBean.getShowName())
        Text(text = "${albumBean.list.size}")
    }

}
@Composable
fun CommonTxt(txt:String,onClick: () -> Unit){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressedAsState = interactionSource.collectIsPressedAsState()
    Text(
        text = txt,
        fontSize = 24.sp,
        fontWeight = FontWeight(500),
        color = theme_color,
        modifier = Modifier
            .scale(if (isPressedAsState.value) 0.9f else 1f)
            .alpha(if (isPressedAsState.value) 0.8f else 1f)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    )
}
@Composable
fun CommonBox( enable: Boolean=true,modifier: Modifier = Modifier,onClick: () -> Unit,content: @Composable () -> Unit){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressedAsState = interactionSource.collectIsPressedAsState()
    val click: () -> Unit={}
    Box(
        modifier = modifier
            .scale(if (isPressedAsState.value&&enable) 0.9f else 1f)
            .alpha(when{
                !enable->0.4f
                isPressedAsState.value->0.8f
                else->1f
            })
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = if(enable) onClick else click
            )
    ){
        content()
    }
}
