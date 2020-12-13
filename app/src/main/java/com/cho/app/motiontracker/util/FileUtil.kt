package com.cho.app.motiontracker.util

import android.Manifest
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

private var REQUEST_PERMISSION = 1000

// タッチ情報をCSVファイルに保存
public fun touchInfoSaveCsv(fileName: String, intTargetX: Int, intTargetY: Int, intTargetWidth: Int, intTargetHeight: Int, intTouchX: Int, intTouchY: Int, hitFlg: String) {
    val d = Date() // 現在時刻
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    if (fileName.isNotEmpty()) {
        OutputStreamWriter(
            FileOutputStream("$fileName", true), Charset.forName("SJIS").displayName()
        ).use {
            it.write(sdf.format(d))
            it.write(",")
            it.write((intTargetX + intTargetWidth / 2).toString())
            it.write(",")
            it.write((intTargetY + intTargetHeight / 2).toString())
            it.write(",")
            it.write(intTouchX.toString())
            it.write(",")
            it.write(intTouchY.toString())
            it.write(",")
            it.write(hitFlg)
            it.write("\r\n")
        }
    }
}

// フォルダー、ファイル、ヘッダー生成
public fun csvFileCreate(path: String, fileName: String) {
    val dir = File("$path/MOTION/")
//  Log.d("dir.exists()", dir.exists().toString())
    // フォルダーが無い場合、フォルダーを生成
    if (!dir.exists()) {
        val result = dir.mkdirs();
//      Log.d("result", result.toString())
        if (result) {
            Log.d("フォルダー生成成功", "フォルダー生成成功")
        }
    }
//  Log.d("fileName", fileName)
    val file = File("$fileName")
    val isNewFileCreated :Boolean = file.createNewFile()
    if(isNewFileCreated){
        println("$file 生成成功")
    } else{
        println("$file が既に存在しています")
    }
    OutputStreamWriter(
        FileOutputStream("$fileName", true), Charset.forName("SJIS").displayName()
    ).use {
        it.write("time")
        it.write(",")
        it.write("object X")
        it.write(",")
        it.write("object Y")
        it.write(",")
        it.write("touch X")
        it.write(",")
        it.write("touch Y")
        it.write(",")
        it.write("Hit")
        it.write("\r\n")
    }
}

// 許可を求める
public fun requestLocationPermission(activity: Activity) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    ) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_PERMISSION
        )
    } else {
        val toast = Toast.makeText(activity, "許可してください", Toast.LENGTH_SHORT)
        toast.show()
        ActivityCompat.requestPermissions(
            activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_PERMISSION
        )
    }
}

// ファイル名を生成
public fun getFileName(name: String, sex: String, age: String, kbn: String, seq: String): String {
//    val d = Date() // 現在時刻
//    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val sb = StringBuilder().also {
//        it.append(sdf.format(d))
//        it.append("_")
        it.append(name)
        it.append("_")
        it.append(sex)
        it.append("_")
        it.append(age)
        it.append("_")
        it.append(kbn)
        it.append("_")
        it.append(seq)
        it.append(".")
        it.append("csv")
    }
    return sb.toString()
}