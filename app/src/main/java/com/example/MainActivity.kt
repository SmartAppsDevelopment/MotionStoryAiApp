package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.core.navigation.MotionStoryNavGraph
import com.example.core.theme.MotionBgDark
import com.example.core.theme.MotionStoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MotionStoryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MotionBgDark
                ) {
                    MotionStoryNavGraph()
                }
            }
        }
    }
}
