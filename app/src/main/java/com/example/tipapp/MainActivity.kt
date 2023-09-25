@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.tipapp

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipapp.components.InputField
import com.example.tipapp.components.RoundIconButton
import com.example.tipapp.ui.theme.TipAppTheme
import com.example.tipapp.util.calculateTotalTip
import com.example.tipapp.util.totalPerPerson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp(content = { MainContent()})
        }
        }
    }

@Composable
fun MyApp(content: @Composable () -> Unit) {
    TipAppTheme {
        Surface(modifier = Modifier.padding(10.dp)) {
            Column {
                content()
            }

        }

        }

    }

@Preview
@Composable
fun TopHeader(totalPerson:Double = 0.00){
    Surface(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
        .height(150.dp)
        .clip(shape = CircleShape.copy(all = CornerSize(20.dp))),
    color =Color(0xFFE9D7F7)
    ) {
        val total="%.2f".format(totalPerson)
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment =CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = "Total Per Person", fontSize = 30.sp, style = MaterialTheme.typography.bodyMedium)
            Text(text = "$ $total", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold)
        }

    }
}

@Preview
@Composable
fun MainContent(){
    val splitState= remember {
        mutableIntStateOf(1)
    }
    val tipAmountState= remember {
        mutableDoubleStateOf(0.0)
    }
    val  totalPerPersonState= remember {
        mutableDoubleStateOf(0.0)
    }
    val range= IntRange(start = 1, endInclusive = 30)

    BillForm(range = range, splitState = splitState,
        tipAmountState = tipAmountState,
        totalPerPersonState = totalPerPersonState)
}
@Composable
fun BillForm(
    modifier: Modifier=Modifier,
    onValChanged:(String)->Unit={},
    range: IntRange=1..100,
    splitState : MutableIntState,
    tipAmountState: MutableDoubleState,
    totalPerPersonState:MutableDoubleState

){
    val totalBill = remember { mutableStateOf("") }
    val validState = remember (totalBill.value){
        totalBill.value.trim().isNotEmpty()
    }
    val sliderPositionState = remember {
        mutableFloatStateOf(0f)
    }

    val tipPercentage=(sliderPositionState.floatValue*100).toInt()
    val keyboardController= LocalSoftwareKeyboardController.current
TopHeader( totalPerPersonState.doubleValue)
    Surface(modifier = modifier
        .padding(10.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
        border = BorderStroke(1.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(valueState = totalBill,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    onValChanged(totalBill.value.trim())
                    keyboardController?.hide()


                })
            if (validState) {
                Row(
                    modifier = modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.Start
                )
                {
                    Text(
                        text = "Split",
                        modifier = modifier.align(alignment = CenterVertically)
                    )
                    Spacer(modifier = modifier.width(120.dp))
                    Row(
                        modifier = modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        RoundIconButton(onClick = {
                            splitState.intValue =
                                if (splitState.intValue > 1) {
                                    splitState.intValue - 1
                                } else {
                                    1
                                }
                            tipAmountState.doubleValue = calculateTotalTip(
                                totalBill = totalBill.value.toDouble(),
                                tipPercentage = tipPercentage
                            )
                            totalPerPersonState.doubleValue = totalPerPerson(
                                totalBill.value.toDouble(),
                                splitState.intValue,
                                tipPercentage
                            )
                        }, imageVector = Icons.Default.Remove)
                        Text(
                            text = "${splitState.intValue}",
                            modifier = modifier
                                .align(CenterVertically)
                                .padding(start = 9.dp, end = 9.dp)
                        )
                        RoundIconButton(onClick =
                        {
                            if (splitState.intValue < range.last) {
                                splitState.intValue += 1
                                tipAmountState.doubleValue = calculateTotalTip(
                                    totalBill = totalBill.value.toDouble(),
                                    tipPercentage = tipPercentage
                                )
                                totalPerPersonState.doubleValue = totalPerPerson(
                                    totalBill.value.toDouble(),
                                    splitState.intValue,
                                    tipPercentage
                                )
                            }
                        }, imageVector = Icons.Default.Add)
                    }
                }
                //tip Row
                Row(
                    modifier = modifier.padding(horizontal = 3.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.Start
                )
                {
                    Text(text = "Tip", modifier = Modifier.align(alignment = CenterVertically))
                    Spacer(modifier = Modifier.width(200.dp))
                    Text(
                        text = "$ ${tipAmountState.doubleValue}",
                        modifier = Modifier.align(alignment = CenterVertically)
                    )

                }
                //percentage Column
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = CenterHorizontally
                ) {

                    Text(text = "$tipPercentage %", modifier.align(alignment = CenterHorizontally))
                    Spacer(modifier = modifier.height(14.dp))
                    //Slider

                    Slider(
                        value = sliderPositionState.floatValue,
                        onValueChange = { newVal ->
                            sliderPositionState.floatValue = newVal
                            val newTipPercentage = (newVal * 100).toInt()
                            tipAmountState.doubleValue = calculateTotalTip(
                                totalBill = totalBill.value.toDouble(),
                                tipPercentage = newTipPercentage
                            )
                            totalPerPersonState.doubleValue = totalPerPerson(
                                totalBill.value.toDouble(),
                                splitState.intValue,
                                newTipPercentage
                            )
                        },
                        modifier.padding(start = 16.dp, end = 16.dp)
                    )
                }

            } else {
                Box {}
            }
        }

    }}

