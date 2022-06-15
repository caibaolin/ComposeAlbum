package com.cbl.composealbum.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cbl.base.MediaUtil
import com.cbl.base.view.LeftAddAlbum
import com.cbl.base.view.LeftLazyItem
import com.cbl.base.view.RightLazyItem
import com.cbl.composealbum.ui.theme.app_name
import com.cbl.composealbum.R
import timber.log.Timber

/**
 * <pre>
 *     author : caibaolin
 *     e-mail : 401115406@qq.com
 *     time   : 2022/6/9 18:41
 *     desc   :
 * </pre>
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePage(viewModel: HomeViewModel = viewModel(), docall: () -> Unit) {
    val constraints = ConstraintSet {
        val leftBox = createRefFor("leftBox")
        val rightBox = createRefFor("rightBox")
        val appName = createRefFor("appName")
        val bt_left_edit = createRefFor("bt_left_edit")
        val left_box_bottom_fun = createRefFor("left_box_bottom_fun")
        val lc_left = createRefFor("lc_left")
        val bt_right_edit = createRefFor("bt_right_edit")
        val rightgrid = createRefFor("rightgrid")
        constrain(leftBox) {
            width = Dimension.percent(0.29f)
            height = Dimension.fillToConstraints
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }
        constrain(rightBox) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            end.linkTo(parent.end)
            start.linkTo(leftBox.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }
        constrain(appName) {
            top.linkTo(leftBox.top, 30.dp)
            start.linkTo(leftBox.start, 20.dp)
        }
        constrain(bt_left_edit) {
            top.linkTo(appName.top)
            bottom.linkTo(appName.bottom)
            end.linkTo(leftBox.end, 10.dp)
        }
        constrain(left_box_bottom_fun) {
            width = Dimension.fillToConstraints
            height = Dimension.ratio("288:127")
            start.linkTo(leftBox.start)
            end.linkTo(leftBox.end)
            bottom.linkTo(leftBox.bottom)
        }
        constrain(lc_left) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
//            linkTo(top =appName.bottom )
            top.linkTo(appName.bottom)
            bottom.linkTo(left_box_bottom_fun.top)
            start.linkTo(leftBox.start)
            end.linkTo(leftBox.end)
        }
        constrain(bt_right_edit) {
            top.linkTo(appName.top)
            bottom.linkTo(appName.bottom)
            end.linkTo(rightBox.end, 10.dp)
        }
        constrain(rightgrid) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            top.linkTo(bt_right_edit.bottom, 50.dp)
            bottom.linkTo(rightBox.bottom)
            start.linkTo(rightBox.start)
            end.linkTo(rightBox.end)
        }
    }
    Timber.i("ConstraintLayout compose")
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize(), constraintSet = constraints
    ) {
        Box(
            modifier = Modifier
                .layoutId("leftBox")
                .background(
                    Color(235, 235, 235)
                )
        )
        Text(
            text = "相册", color = app_name, fontWeight = FontWeight(500), modifier = Modifier
                .layoutId("appName")
                .clickable {

                }, fontSize = 28.sp
        )
        AnimatedVisibility(visible = viewModel.viewStates.showLeftEdit, modifier = Modifier.layoutId("bt_left_edit")) {
            IconButton(
               /* modifier = Modifier.layoutId("bt_left_edit"),*/
                onClick = {
                    viewModel.dispatch(HomeViewAction.LeftEdit)
                }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_h_edit2),
                    contentDescription = "",
                    modifier = Modifier.size(50.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .layoutId("left_box_bottom_fun"),
            contentAlignment = Alignment.Center
        ){
            Spacer(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                        ),
                        alpha = 0.1f,
                    )
            )
            LeftAddAlbum()
        }

        LazyColumn(
            modifier = Modifier
                .layoutId("lc_left")

        ) {
            Timber.i("LazyColumn compose")

            viewModel.viewStates.albumList.forEach {
                item(key = it.relative_path) {
                    LeftLazyItem(
                        it,
                        onClick = { viewModel.dispatch(HomeViewAction.LeftOnClick(it)) },
                        clickSelect = (it == viewModel.viewStates.clickAlbumBean),
                        leftEdit = !viewModel.viewStates.showLeftEdit&&it.isCanEdit,
                        leftEditSelect = viewModel.viewStates.leftEditSelectList.contains(it)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .layoutId("rightBox")
                .background(
                    Color(255, 255, 255, 0x4d)
                )
        ) {

        }
        IconButton(
            modifier = Modifier.layoutId("bt_right_edit"),
            onClick = { viewModel.dispatch(HomeViewAction.ChangeSort) }) {
            Image(
                painter = painterResource(id = R.drawable.ic_h_edit2),
                contentDescription = "",
                modifier = Modifier.size(50.dp)
            )
        }
        LazyVerticalGrid(
            modifier = Modifier.layoutId("rightgrid"),
            columns = GridCells.Fixed(6),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Timber.i("LazyVerticalGrid compose")
            viewModel.viewStates.clickAlbumBean.list.forEach {
                item {
                    RightLazyItem(it, {})
                }
            }
        }
    }


}
