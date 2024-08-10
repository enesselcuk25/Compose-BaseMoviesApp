package com.enesselcuk.moviesui.screens.searchScreen.views

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.enesselcuk.moviesui.screens.searchScreen.SearchViewModel
import com.enesselcuk.moviesui.util.Constant.IMAGE_BASE_W500
import java.math.RoundingMode


@Composable
fun Movies(
    @StringRes movies: Int?,
    @DrawableRes icons: Int?,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    content: @Composable () -> Unit
) {

    if (viewModel.isVisible.value) {
        Column() {
            ConstraintLayout(
                modifier = modifier
                    .padding(5.dp)
                    .fillMaxWidth()
            ) {
                val (movie, icon) = createRefs()
                Text(
                    text = stringResource(id = movies!!),
                    modifier = modifier.constrainAs(movie) {
                        top.linkTo(parent.top, 8.dp)
                        start.linkTo(parent.start, 8.dp)
                        bottom.linkTo(parent.bottom, 8.dp)
                        width = Dimension.fillToConstraints
                    }, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )

                Icon(
                    painter = painterResource(id = icons!!),
                    contentDescription = "",
                    modifier = modifier.constrainAs(icon) {
                        top.linkTo(parent.top, 8.dp)
                        bottom.linkTo(parent.bottom, 8.dp)
                        end.linkTo(parent.end, 8.dp)
                    }
                )
            }
            content()
        }
    }

}


@Composable
private fun ViewMovies(
    modifier: Modifier = Modifier,
    movieImage: String? = null,
    onCLick: (() -> Unit)? = null,
    name: String,
    vote: Double
) {
    ConstraintLayout(modifier = Modifier.clickable { onCLick?.invoke() }) {
        val (imageCons, voteCons, nameCons) = createRefs()

        AsyncImage(
            model = "${IMAGE_BASE_W500}$movieImage",
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .size(height = 150.dp, width = 120.dp)
                .constrainAs(imageCons) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = "IMDb ${vote.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()}",
            color = Color.White,
            modifier = Modifier
                .background(
                    Color.Red,
                    RoundedCornerShape(topStart = 8.dp, bottomEnd = 8.dp)
                )
                .wrapContentWidth()
                .padding(horizontal = 5.dp)
                .constrainAs(voteCons) {
                    start.linkTo(parent.start)
                    top.linkTo(imageCons.top)
                },
            style = TextStyle(fontWeight = FontWeight.W800)
        )

        Text(
            text = name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(nameCons) {
                top.linkTo(imageCons.bottom, 8.dp)
                start.linkTo(imageCons.start)
                end.linkTo(imageCons.end)
            }
        )
    }
}

@Composable
fun MoviesViewRow(
    modifier: Modifier = Modifier,
    navController: NavController,
    searchViewModel: SearchViewModel
) {

    val data = searchViewModel.searchMoviesFlow.collectAsStateWithLifecycle()

    Column(modifier = modifier) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(horizontal = 5.dp)
        ) {
            items(data.value?.results ?: emptyList()) {
                it.vote_average?.let { it1 ->
                    it.title?.let { movieName ->
                        if (movieName.count() >= 12) movieName.substring(0, 12).plus("...")
                        else movieName
                    }?.let { it2 ->
                        ViewMovies(
                            modifier,
                            it.poster_path,
                            onCLick = { navController.navigate("detail/${it.id}") },
                            vote = it1,
                            name = it2
                        )
                    }
                }
            }
        }
    }
}


