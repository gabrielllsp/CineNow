package com.devspacecinenow

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.devspacecinenow.data.MovieDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun MovieDetailScreen(movieId: String, navHostController: NavHostController) {
    var movieDto by remember { mutableStateOf<MovieDto?>(null) }

    val apiService = RetrofitClient.retrofitInstance.create(ApiService::class.java)
    apiService.getMovieById(movieId).enqueue(
        object : Callback<MovieDto> {
            override fun onResponse(call: Call<MovieDto>, response: Response<MovieDto>) {
                if (response.isSuccessful) {
                    movieDto = response.body()
                } else {
                    Log.d("mainActivity", "Request Error: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<MovieDto>, t: Throwable) {
                Log.d("mainActivity", "Network Error: ${t.message}")
            }
        }
    )

    movieDto?.let {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        navHostController.popBackStack()
                    }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back Button")
                }
                Text(modifier = Modifier.padding(start = 4.dp), text = it.title, fontSize = 16.sp)
            }
            MovieDetailScreenContent(modifier = Modifier, it)
        }
    }
}

@Composable
fun MovieDetailScreenContent(modifier: Modifier = Modifier, movie: MovieDto) {
    Column(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            modifier = modifier
                .height(200.dp)
                .fillMaxSize(),
            contentScale = ContentScale.Crop,

            model = movie.posterFullPath,
            contentDescription = "${movie.title} Poster image"
        )
        Text(modifier = modifier.padding(16.dp),
            text = movie.overview,
            fontSize = 16.sp)
    }
}