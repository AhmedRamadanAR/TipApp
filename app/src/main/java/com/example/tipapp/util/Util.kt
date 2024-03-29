package com.example.tipapp.util

fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {
    return if (totalBill > 1 && totalBill.toString().isNotEmpty())
        (totalBill * tipPercentage)/ 100 else 0.0


}
fun totalPerPerson(totalBill:Double,split:Int,tipPercentage: Int):Double{
    val bill= calculateTotalTip(totalBill,tipPercentage)+totalBill
    return bill/split

}
