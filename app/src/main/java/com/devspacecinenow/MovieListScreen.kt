package com.devspacecinenow

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun MovieListScreen(navController: NavHostController) {
    var nowPlayingMovies by remember { mutableStateOf(emptyList<MovieDto>()) }
    var topRatedMovies by remember { mutableStateOf(emptyList<MovieDto>()) }
    var upComingMovies by remember { mutableStateOf(emptyList<MovieDto>()) }
    var popularMovies by remember { mutableStateOf(emptyList<MovieDto>()) }

    val apiService = RetrofitClient.retrofitInstance.create(ApiService::class.java)

    val callNowPlaying = apiService.getNowPlayingMovies()
    callNowPlaying.enqueue(object : Callback<MovieResponse> {
        override fun onResponse(
            call: Call<MovieResponse>,
            response: Response<MovieResponse>
        ) {
            if (response.isSuccessful) {
                val movies = response.body()?.results
                if (movies != null) {
                    nowPlayingMovies = movies
                }
            } else {
                Log.d("MainActivity", "Response Error: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            Log.d("MainActivity", "Error: ${t.message}")
        }
    })

    val callTopRatedMovies = apiService.getTopRatedMovies()
    callTopRatedMovies.enqueue(object : Callback<MovieResponse> {
        override fun onResponse(
            call: Call<MovieResponse>,
            response: Response<MovieResponse>
        ) {
            if (response.isSuccessful) {
                val movies = response.body()?.results
                if (movies != null) {
                    topRatedMovies = movies
                }
            } else {
                Log.d("MainActivity", "Response Error: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            Log.d("MainActivity", "Error: ${t.message}")
        }

    })

    val callUpcomingMovies = apiService.getUpcomingMovies()
    callUpcomingMovies.enqueue(object : Callback<MovieResponse> {
        override fun onResponse(
            call: Call<MovieResponse>,
            response: Response<MovieResponse>
        ) {
            if (response.isSuccessful) {
                val movies = response.body()?.results
                if (movies != null) {
                    upComingMovies = movies
                }
            } else {
                Log.d("MainActivity", "Response Error: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            Log.d("MainActivity", "Error: ${t.message}")
        }

    })

    val callPopularMovies = apiService.getPopularMovies()
    callPopularMovies.enqueue(object : Callback<MovieResponse> {
        override fun onResponse(
            call: Call<MovieResponse>,
            response: Response<MovieResponse>
        ) {
            if (response.isSuccessful) {
                val movies = response.body()?.results
                if (movies != null) {
                    popularMovies = movies
                }
            } else {
                Log.d("MainActivity", "Response Error: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            Log.d("MainActivity", "Error: ${t.message}")
        }

    })

    MovieListContent(
        nowPlayingMovies = nowPlayingMovies,
        topRatedMovies = topRatedMovies,
        upComingMovies = upComingMovies,
        popularMovies = popularMovies
    ) {itemClicked ->
        navController.navigate("movieDetail/${itemClicked.id}")
    }
}


@Composable
private fun MovieListContent(
    modifier: Modifier = Modifier,
    nowPlayingMovies: List<MovieDto>,
    topRatedMovies: List<MovieDto>,
    upComingMovies: List<MovieDto>,
    popularMovies: List<MovieDto>,
    onClick: (MovieDto) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "CineNow",
            modifier = Modifier.padding(8.dp),
            fontSize = 40.sp,
            fontWeight = FontWeight.SemiBold
        )
        MovieSession(
            label = "Top rated",
            movieList = topRatedMovies,
            onClick = onClick
        )

        MovieSession(
            label = "Now Playing",
            movieList = nowPlayingMovies,
            onClick = onClick
        )

        MovieSession(
            label = "Upcoming",
            movieList = upComingMovies,
            onClick = onClick
        )

        MovieSession(
            label = "Popular",
            movieList = popularMovies,
            onClick = onClick
        )
    }
}

@Composable
private fun MovieSession(
    modifier: Modifier = Modifier,
    label: String,
    movieList: List<MovieDto>,
    onClick: (MovieDto) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = label, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.size(8.dp))
        MovieList(movieList = movieList, onClick = onClick)
    }
}

@Composable
private fun MovieList(
    modifier: Modifier = Modifier,
    movieList: List<MovieDto>,
    onClick: (MovieDto) -> Unit
) {
    LazyRow(modifier = modifier) {
        items(movieList) {
            MovieItem(
                movieDto = it,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun MovieItem(
    modifier: Modifier = Modifier,
    movieDto: MovieDto,
    onClick: (MovieDto) -> Unit
) {
    Column(modifier = modifier
        .width(IntrinsicSize.Min)
        .clickable { onClick.invoke(movieDto) }) {
        AsyncImage(
            modifier = Modifier
                .padding(end = 4.dp)
                .width(120.dp)
                .height(150.dp),
            contentScale = ContentScale.Crop,
            model = movieDto.posterFullPath,
            contentDescription = "${movieDto.title} Poster Image",
        )
        Spacer(modifier = modifier.size(4.dp))
        Text(
            text = movieDto.title,
            fontWeight = FontWeight.SemiBold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Text(
            text = movieDto.overview,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

//@Preview(name = "Light Mode", showBackground = true)
//@Preview(name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
//@Preview(name = "Full Preview", showSystemUi = true)
//@Composable
//private fun MovieListPreview() {
//
//}