package com.example.reciipiie.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.example.reciipiie.R
import com.example.reciipiie.data.SignInState

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(R.drawable.img),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)) // semi-transparent background
                .padding(start=15.dp,end=15.dp,bottom = 40.dp),
            contentAlignment = Alignment.BottomCenter
        )
        {
            Column {
                androidx.compose.material3.Text(
                    text = "Welcome to",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 60.sp,
                        fontStyle = FontStyle.Italic
                    )

                )
                Text(
                    text = buildAnnotatedString {
                        append("R")
                        addStyle(style = SpanStyle(fontWeight = FontWeight.Bold), start = 0, end = 1)

                        append("e")
                        addStyle(style = SpanStyle(fontWeight = FontWeight.Bold), start = 1, end = 2)

                        append("c")
                        addStyle(style = SpanStyle(fontWeight = FontWeight.Bold), start = 2, end = 3)

                        append("iip")
                        addStyle(style = SpanStyle(fontWeight = FontWeight.Bold), start = 5, end = 6)

                        append("iie")
                        addStyle(style = SpanStyle(fontWeight = FontWeight.Bold), start = 8, end = 9)
                    },
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 60.sp,
                        fontStyle = FontStyle.Italic
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                androidx.compose.material3.Text(
                    text = "Please sign in to continue",
                    color = Color.White,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(26.dp))
                Button(
                    onClick = onSignInClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(ContextCompat.getColor(context, R.color.signInOrange))), // Set button color to orange
                    modifier = Modifier
                        .fillMaxWidth() // Make the button full width
                        .padding(horizontal = 4.dp) // Add padding to the two ends
                ) {
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, // Center align the items vertically
                        modifier = Modifier.padding(10.dp) // Optional padding within the button
                    )
                    {
                        Image(
                            painter = painterResource(id = R.drawable.img_1), // Replace with your Google icon resource ID
                            contentDescription = "Google Icon",
                            modifier = Modifier.size(24.dp) // Set the size of the icon
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Continue with google",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                }
            }

        }

    }
}