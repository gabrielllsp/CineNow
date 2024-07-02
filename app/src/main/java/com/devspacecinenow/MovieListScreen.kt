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
import com.devspacecinenow.data.MovieDto
import com.devspacecinenow.data.MovieResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun MovieListScreen(navController: NavHostController) {

    var nowPlayingMovies by remember { mutableStateOf<List<MovieDto>>(emptyList()) }
    var topRatedMovies by remember { mutableStateOf<List<MovieDto>>(emptyList()) }
    var popularMovies by remember { mutableStateOf<List<MovieDto>>(emptyList()) }
    var upcomingMovies by remember { mutableStateOf<List<MovieDto>>(emptyList()) }

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
                Log.d("mainActivity", "Request Error: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            Log.d("mainActivity", "Network Error: ${t.message}")
        }
    })

    val callTopRated = apiService.getTopRateMovies()
    callTopRated.enqueue(object : Callback<MovieResponse> {
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
                Log.d("mainActivity", "Request Error: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            Log.d("mainActivity", "Network Error: ${t.message}")
        }
    })

    val callPopularMovie = apiService.getPopularMovies()
    callPopularMovie.enqueue(object : Callback<MovieResponse> {
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
                Log.d("mainActivity", "Request Error: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            Log.d("mainActivity", "Network Error: ${t.message}")
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
                    upcomingMovies = movies
                }

            } else {
                Log.d("mainActivity", "Request Error: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            Log.d("mainActivity", "Network Error: ${t.message}")
        }
    })

    MovieListContent(
        nowPlayingMovies = nowPlayingMovies,
        topRatedMovies = topRatedMovies,
        popularMovies = popularMovies,
        upcomingMovies = upcomingMovies
    ) { itemClicked ->
        navController.navigate(route = "movieDetail/${itemClicked.id}")
    }
}

@Composable
private fun MovieListContent(
    modifier: Modifier = Modifier,
    nowPlayingMovies: List<MovieDto>,
    topRatedMovies: List<MovieDto>,
    popularMovies: List<MovieDto>,
    upcomingMovies: List<MovieDto>,
    onClick: (MovieDto) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "CineNow", modifier = modifier.padding(8.dp),
            fontSize = 40.sp,
            fontWeight = FontWeight.SemiBold
        )
        MovieSession(
            label = "Now Playing",
            movieList = nowPlayingMovies,
            onClick = onClick
        )

        MovieSession(
            label = "Top Rated",
            movieList = topRatedMovies,
            onClick = onClick
        )

        MovieSession(
            label = "Popular Movie",
            movieList = popularMovies,
            onClick = onClick
        )
        MovieSession(
            label = "Upcoming Movies",
            movieList = upcomingMovies,
            onClick = onClick
        )
    }
}

@Composable
private fun MovieList(
    movieList: List<MovieDto>,
    onClick: (MovieDto) -> Unit
) {
    LazyRow {
        items(movieList) { movieDto ->
            MovieItem(movieDto = movieDto, onClick = onClick)
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
        .clickable {
            onClick.invoke(movieDto)
        }) {
        AsyncImage(
            modifier = modifier
                .padding(end = 4.dp)
                .width(120.dp)
                .height(150.dp),
            contentScale = ContentScale.Crop,
            model = movieDto.posterFullPath,
            contentDescription = "${movieDto.title} Poster image"
        )
        Spacer(modifier = modifier.size(8.dp))
        Text(
            text = movieDto.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.SemiBold
        )
        Text(text = movieDto.overview, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
        Text(
            text = label,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = modifier.size(8.dp))
        MovieList(movieList = movieList, onClick = onClick)
    }

}