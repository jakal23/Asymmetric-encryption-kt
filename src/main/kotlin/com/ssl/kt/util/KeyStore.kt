package com.ssl.kt.util

import com.ssl.kt.Config
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

    fun writeWithNote(key: String, fileName: String, note: EndLineString){
        write(key, fileName, note)
    }

    fun write(key: String, fileName: String, note: EndLineString? = null){
        val dir = File(Config.dir)
        if (dir.exists() && dir.isDirectory){
            write(key, File(dir, fileName), note)
        }else if (!dir.exists()){
            dir.mkdir()
            write(key, File(dir, fileName), note)
        }else{
            throw RuntimeException("Please set a valid directory.")
        }
    }

    private fun write(key: String, file: File, note: EndLineString? = null, minLineLength: Int = 40){
        val bufferSize = computeBufferSize(key, Config.defLineLength)
            .takeIf { it in minLineLength..Config.defLineLength }
            ?:Config.defLineLength

        val buffer = CharArray(bufferSize)
        val writer = FileWriter(file)

        val reader = key.reader()
        var chars = reader.read(buffer)

        note?.let { writer.write(note.begin) }

        while (chars >= 0) {
            writer.write(buffer, 0, chars)
            writer.write("\n")
            chars = reader.read(buffer)
        }

        note?.let { writer.write(note.end) }

        writer.flush()
        writer.close()
    }

    private fun computeBufferSize(key: String, maxLineLength: Int): Int {
        var bufferSize = key.length

        SimpleNumber.allDividers(bufferSize){ number, _->
            val isValid = number < maxLineLength
            if (isValid){
                bufferSize = number
            }
            isValid
        }

        return bufferSize
    }


}