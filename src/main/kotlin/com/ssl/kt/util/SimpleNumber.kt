package com.ssl.kt.util

object SimpleNumber {

    fun allDividers(size: Int, block:(number: Int, coefficient: Int)->Boolean) {
        var coefficient = 1
        var number = size

        while(true){
            val firstDivider = findFirstDivider(number)
            if (firstDivider == 1)
                break
            coefficient *= (number / firstDivider)
            number = firstDivider
            if (block(number, coefficient))
                break
        }

    }

    private fun findFirstDivider(bufferSize: Int): Int {
        for ( i in 2..bufferSize){
            if (bufferSize % i == 0){
                return bufferSize / i
            }
        }
        return 1
    }
}