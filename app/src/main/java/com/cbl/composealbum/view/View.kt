package com.cbl.base.view

import android.R
import android.os.Build.VERSION.SDK_INT
import android.widget.CheckBox
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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


/**
 * <pre>
 *     author : caibaolin
 *     e-mail : 401115406@qq.com
 *     time   : 2022/6/8 18:16
 *     desc   :
 * </pre>
 */
@Composable
fun LeftLazyItem(albumBean: AlbumBean, onClick: () -> Unit, clickSelect: Boolean=false,leftEdit: Boolean=false,leftEditSelect: Boolean=false) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(if (clickSelect) Color.Red else Color.Transparent)
            .clickable {
                onClick.invoke()
            }) {
        Text(text = "${albumBean.getShowName()}  ${albumBean.list.size}", modifier = Modifier.align(
            Alignment.Center))
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
    if(mediaBean!=null){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(mediaBean.path)
                .videoFrameMillis(0L)
                .crossfade(true)
                .apply {
                    if (mediaBean.mimeType?.contains("video") == true){
                        decoderFactory(VideoFrameDecoder.Factory())
                    }else if(mediaBean.mimeType?.contains("gif") == true){
                        decoderFactory(GifDecoder.Factory())
                    }
                }
                .build(),
//        placeholder = painterResource(R.drawable.placeholder),
            contentDescription=null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.aspectRatio(1f),
        )
    }else{
        Box(modifier = Modifier
            .background(Color.Red)
            .aspectRatio(1f)) {
            
        }
    }
 
}
@Composable
fun CommonAlbum(albumBean: AlbumBean) {
    Column() {
        RightLazyItem(mediaBean = albumBean.list.firstOrNull(),{})
        Text(text = albumBean.getShowName())
        Text(text = "${albumBean.list.size}")
    }

}