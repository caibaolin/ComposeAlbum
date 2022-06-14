package com.cbl.composealbum.view

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * <pre>
 *     author : caibaolin
 *     e-mail : 401115406@qq.com
 *     time   : 2022/6/14 12:03
 *     desc   :
 * </pre>
 */
@Composable
fun MainPage() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "HomePage") {
        composable("HomePage") {
            HomePage(docall = {
                navController.navigate("SelectAlbumPage")
            })
        }
        composable("SelectAlbumPage") {
            SelectAlbumPage(backCall = {navController.popBackStack()})
        }
    }

}