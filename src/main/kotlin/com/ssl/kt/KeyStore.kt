package com.ssl.kt

import java.io.File
import java.io.FileReader
import java.io.FileWriter

object KeyStore {

    fun isFileExist(fileName: String): Boolean{
        val dir = File(Config.dir)
        val file = File(dir, fileName)

        return file.exists() || file.canRead() || file.isFile
    }

    fun read(fileName: String): String{
        if (!isFileExist(fileName)){
            throw RuntimeException("Can not read file. File doesnt exist")
        }

        val reader = FileReader(File(Config.dir, fileName))
        val lines = reader.readLines()
        return lines.joinToString("")
    }

    fun write(key: String, fileName: String){
        val dir = File(Config.dir)
        if (dir.exists() && dir.isDirectory){
            write(key, File(dir, fileName))
        }else if (!dir.exists()){
            dir.mkdir()
            write(key, File(dir, fileName))
        }else{
            throw RuntimeException("Please set a valid directory.")
        }
    }

    private fun write(key: String, file: File){
        val buffer = CharArray(computeBufferSize(key))
        val writer = FileWriter(file)

        val reader = key.reader()

        while (reader.read(buffer) > 0){
            writer.write(buffer)
            writer.write("\n")
        }

        writer.flush()
        writer.close()
    }

    private fun computeBufferSize(key: String): Int {
        var bufferSize = key.length
        println(bufferSize)

        allDividers(bufferSize){ number, _->
            val isValid = number < 60
            if (isValid){
                bufferSize = number
            }
            isValid
        }

        return bufferSize
    }

    private fun allDividers(size: Int, block:(number: Int, coefficient: Int)->Boolean) {
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