package com.example.efimsokolov_shoppingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.efimsokolov_shoppingapp.ui.theme.EfimSokolovShoppingAppTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EfimSokolovShoppingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShoppingApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ShoppingApp(modifier: Modifier = Modifier) {
    val products = listOf(
        Product("Product A", "$100", "This is a great product A."),
        Product("Product B", "$150", "This is product B with more features."),
        Product("Product C", "$200", "Premium product C."),
        Product("Product D", "$1000", "Extremely premium product D.")
    )
    val windowInfo = calculateCurrentWindowInfo()
    var i by rememberSaveable  { mutableIntStateOf(-1) }

    val makeProductSelector: (Int) -> () -> Unit = { { i = it } } // Second order function factory for onClick
    val handleBackButtonClick: () -> Unit = { i = -1 }

    if (windowInfo.isWideScreen){
        LandScapeView(
            products, i = i,
            makeProductSelector = makeProductSelector
        )
    }
    else{
        PortraitView(
            products,
            i = i,
            makeProductSelector = makeProductSelector,
            handleBackButtonClick = handleBackButtonClick
        )
    }
}

class Product(
    val name: String,
    val price: String,
    val description: String
)

data class WindowInfo(
    val isWideScreen: Boolean
)


@Composable
fun PortraitView(products: List<Product>, i: Int, makeProductSelector: (Int) -> () -> Unit, handleBackButtonClick: () -> Unit){

    if (i == -1){
        ProductList(
            products,
            makeProductSelector=makeProductSelector
        )
    }
    else{
        ProductDetails(
            products[i],
            portraitOrientation = true,
            handleBackButtonClick = handleBackButtonClick,
            modifier = Modifier.fillMaxHeight()
        )
    }
}

@Composable
fun LandScapeView(products: List<Product>, i: Int,  makeProductSelector: (Int) -> () -> Unit){
    val defaultProduct = Product("", "Please Select a Product", "")

    Row {
        ProductList(products,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            makeProductSelector = makeProductSelector)
        ProductDetails(
            portraitOrientation = false,
            handleBackButtonClick = { },
            product = if (i == -1){
                defaultProduct
            }
            else{
                products[i]
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
    }
}


@Composable
fun ProductList(products: List<Product>, modifier: Modifier = Modifier, makeProductSelector:  (Int) -> () -> Unit){
    Column (modifier = modifier){
        Text(
            "Product List",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 40.sp
            ),
            modifier = Modifier.padding(top = 30.dp, start = 9.dp)

        )
        LazyColumn (
            modifier = Modifier.padding(horizontal = 5.dp)
        ) {
            itemsIndexed(products) { index, product ->
                Card(
                    onClick = makeProductSelector(index),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardDefaults.cardColors().containerColor
                    )
                ){
                    Column(
                        modifier = Modifier
                            .padding(14.dp)
                    ) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 25.sp,
                            )
                        )
                        Text(
                            text = "${product.price} | ${product.description}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 18.sp,
                                color = Color.DarkGray
                            )
                        )

                    }
                }
            }

        }
    }
}

@Composable
fun ProductDetails(product: Product, modifier: Modifier = Modifier, handleBackButtonClick: () -> Unit, portraitOrientation: Boolean){
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardDefaults.cardColors().containerColor
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                horizontalAlignment =  if (portraitOrientation) Alignment.Start else Alignment.CenterHorizontally,
                verticalArrangement = if (portraitOrientation) Arrangement.Top else Arrangement.Center,
                modifier = Modifier
                    .align( if (portraitOrientation) Alignment.TopStart else Alignment.Center)
                    .padding(top = 30.dp)
            ) {
                if (portraitOrientation){
                    TextButton(
                        onClick = handleBackButtonClick,
                    )
                    {
                        Text("Back", style = TextStyle(
                            color = Color.Black,
                            fontSize = 25.sp
                        )
                        )
                    }
                }
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 40.sp
                    ),
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = product.price,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 33.sp,
                        color = Color.DarkGray
                    ),
                    modifier = Modifier.padding(start = 12.dp, bottom = 6.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 30.sp,
                        color = Color.Gray,
                        lineHeight = 35.sp
                    ),
                    modifier = Modifier.padding(start = 12.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun calculateCurrentWindowInfo(): WindowInfo {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val isWideScreen = screenWidth >= 600
    return WindowInfo(
        isWideScreen = isWideScreen
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EfimSokolovShoppingAppTheme {
        ShoppingApp()
    }
}