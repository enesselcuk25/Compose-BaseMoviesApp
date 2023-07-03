package com.enesselcuk.moviesui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.enesselcuk.moviesui.R
import com.enesselcuk.moviesui.navigation.NavHostContainer
import com.enesselcuk.moviesui.screens.movie.SharedViewModel
import com.enesselcuk.moviesui.ui.theme.MoviesUiTheme
import com.enesselcuk.moviesui.util.PreferencesDataStoreStatus
import com.enesselcuk.moviesui.util.PreferencesDataStoreStatus.dataStore
import com.enesselcuk.moviesui.util.bottomNavItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val scope = rememberCoroutineScope()
            val scopeTheme = rememberSaveable { mutableStateOf(false) }

           // scopeTheme.value = isSystemInDarkTheme()

            scope.launch {
                baseContext.dataStore.data.collectLatest {
                    when (it[PreferencesDataStoreStatus.dataStoreDarkKey]) {
                        true -> scopeTheme.value = true
                        false -> scopeTheme.value = false
                        else ->  scopeTheme.value
                    }
                }
            }

            MoviesUiTheme(darkTheme = scopeTheme.value) {
                val navController = rememberNavController()
                Surface(color = Color.White) {
                    Scaffold(
                        topBar = {
                            TopBar(
                                backScreenClick = { navController.popBackStack() },
                                navController = navController
                            )
                        },
                        bottomBar = {
                            BottomNavigationBar(
                                navController = navController
                            )
                        }, content = { padding ->
                            NavHostContainer(
                                navController = navController,
                                paddingValues = padding
                            )
                        })
                }
            }
        }
    }
}


@Composable
fun BottomNavigationBar(
    navController: NavHostController? = NavHostController(LocalContext.current),
    sharedViewModel: SharedViewModel = hiltViewModel(),
) {

    if (sharedViewModel.isBottomNavVisible.value) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp, vertical = 2.dp)
                .clip(RoundedCornerShape(15.dp)),
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            // observe the backstack
            val navBackStackEntry by navController!!.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route


            bottomNavItem.onEach { navItem ->
                NavigationBarItem(
                    /**
                     * selected -> currentRoute seçilen route'aya eşittir.
                     */
                    /**
                     * selected -> currentRoute seçilen route'aya eşittir.
                     */
                    selected = currentRoute == navItem.route,
                    onClick = { navController!!.navigate(navItem.route) },

                    icon = {
                        Icon(
                            painter = painterResource(id = navItem.icon),
                            contentDescription = navItem.label,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    label = {
                        Text(
                            text = navItem.label,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    alwaysShowLabel = false
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavHostController,
    backScreenClick: (() -> Unit)? = null,
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val isMenu = rememberSaveable { mutableStateOf(false) }

    if (sharedViewModel.isTopVisible.value) {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .clip(shape = RoundedCornerShape(8.dp)),
            colors = topAppBarColors(MaterialTheme.colorScheme.surface),
            title = {
                Text(
                    text = sharedViewModel.isScreenName.value,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            actions = {
                if (sharedViewModel.isActionInTopBar.value) {
                    Icon(
                        painter = painterResource(id = R.drawable.settings),
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            isMenu.value = !isMenu.value
                        }
                    )

                    DropdownMenu(
                        expanded = isMenu.value,
                        onDismissRequest = { isMenu.value = false }
                    ) {
                        if (sharedViewModel.isVisibleSetting.value) {
                            DropdownMenuItem(onClick = {
                                isMenu.value = false
                                sharedViewModel.isGoSettings.value = true
                            }, text = { Text("Settings") })

                        } else {
                            DropdownMenuItem(onClick = {
                                isMenu.value = false
                                sharedViewModel.isChooseClick.value =
                                    !sharedViewModel.isChooseClick.value
                            }, text = {
                                if (sharedViewModel.isChooseClick.value) Text("Give Up")
                                else Text("Choose")
                            })

                            if (sharedViewModel.isChooseClick.value) {
                                DropdownMenuItem(onClick = {
                                    isMenu.value = false
                                    sharedViewModel.isChooseClick.value =
                                        !sharedViewModel.isChooseClick.value
                                    sharedViewModel.isOpenDialog.value =
                                        !sharedViewModel.isOpenDialog.value
                                }, text = {
                                    Text("Delete")
                                })
                            }
                        }
                    }
                }
            },
            navigationIcon = {
                if (sharedViewModel.isTopBackVisible.value) {
                    IconButton(onClick = { backScreenClick?.invoke() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = ""
                        )
                    }
                }
            }
        )
    }
}




