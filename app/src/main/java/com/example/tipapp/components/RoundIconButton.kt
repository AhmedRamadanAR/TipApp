package com.example.tipapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun RoundIconButton(
    modifier:Modifier=Modifier,
    onClick:()->Unit,
    imageVector:ImageVector,
    tint:Color= Color.Black.copy(alpha =0.8f ),
    backgroundColor : CardColors=  CardDefaults.cardColors(  containerColor = MaterialTheme.colorScheme.background),
    elevation: CardElevation = CardDefaults.cardElevation(4.dp)




){
    Card(modifier = modifier
        .padding(4.dp)
        .clickable(onClick = onClick)
        .then(modifier.size(40.dp)),
        shape = CircleShape,
        elevation=elevation,
        colors = backgroundColor,

        ) {
        Icon(modifier=Modifier.align(alignment = Alignment.CenterHorizontally).size(100.dp),
            imageVector = imageVector, contentDescription = "Plus or minus icon",
            tint=tint)
    }
}