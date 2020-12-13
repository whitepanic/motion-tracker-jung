package com.cho.app.motiontracker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.preference.PreferenceManager
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import com.cho.app.motiontracker.util.csvFileCreate
import com.cho.app.motiontracker.util.requestLocationPermission
import com.cho.app.motiontracker.util.touchInfoSaveCsv
import kotlinx.android.synthetic.main.activity_circle_speed.*

@Suppress("DEPRECATION")
class CircleSpeedActivity : AppCompatActivity() {

    var repeatCount: Int = 0
    var durationTime: Long = 10000
//    val tmpCountDownInterval: Long = durationTime

    inner class CircleAnimation1(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {
            repeatCount -= 1
            root.setTransition(R.id.start, R.id.end)
            root.setTransitionDuration(durationTime.toInt())
            root.transitionToEnd()
        }

        override fun onFinish() {
            if (repeatCount > 0) {
                val recursiveActionTimer = CircleAnimation1(durationTime, durationTime)
                recursiveActionTimer.start()
            }
        }
    }

    inner class MyCountDownTimer(millisInFuture: Long,
                                 countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {
            val second = millisUntilFinished / 1000L % 60L
//            Log.d("second : ", second.toString())
            if ("2".equals(second.toString())) {
                timerText.text = "3"
            }
            if ("1".equals(second.toString())) {
                timerText.text = "2"
            }
            if ("0".equals(second.toString())) {
                timerText.text = "1"
            }

        }

        override fun onFinish() {
            timerText.text = ""
            repeatCount = 3  // set repeat count
            val actionTimer = CircleAnimation1(durationTime, durationTime)
            actionTimer.start()
        }

    }

    var intViewStartY: Int = 0
    var intTargetWidth: Int = 0
    var intTargetHeight: Int = 0
    var intTargetX: Int = 0
    var intTargetY: Int = 0
    var fileName: String = "";
    var path = Environment.getExternalStorageDirectory().path

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_speed)
        val timer = MyCountDownTimer(3 * 10 * 100, 100)
        timerText.text = ""
        button_start.setOnClickListener {
            timer.start()
            val tmp = IntArray(2)
            root.getLocationOnScreen(tmp)
            intViewStartY = tmp[1]
            intTargetWidth = red_star.width
            intTargetHeight = red_star.height
            val pref = PreferenceManager.getDefaultSharedPreferences(this)
            var addSeq = 0

            // 外部ストレージへの読み書きのパーミッションが既に許可している
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
                val pref = PreferenceManager.getDefaultSharedPreferences(this)
                val name : String? = pref.getString("NAME", "")
                val sex : String? = pref.getString("SEX", "")
                val age : String? = pref.getString("YEAR", "")
                val seq : String? = pref.getString("SEQ", "")
                val title : String = pref.getString("TITLE", "")!!
                durationTime = pref.getLong("DURATION", 10000)
                if (seq.toString().isEmpty()) {
                    addSeq = 1
                } else {
                    addSeq = seq.toString().toInt() + 1
                }
                // ファイル名を取得
//                val finameStr = com.cho.app.motiontracker.util.getFileName(name.toString(), sex.toString(), age.toString(), title, addSeq.toString())
////              Log.d("|||||||finameStr|||||||||||||| ", finameStr)
//                fileName = "$path/MOTION/$finameStr"
//
//                // フォルダー、ファイル、ヘッダー生成
//                csvFileCreate(path, fileName)
            } else {
                // 外部ストレージへの読み書きのパーミッションが拒否していた場合
                requestLocationPermission(this)
            }
            pref.edit {
                putString("SEQ", addSeq.toString())
            }
        }
        back.setOnClickListener {
            // 戻るボタンが押された際に各値を設定
            val pref = PreferenceManager.getDefaultSharedPreferences(this)
            pref.edit {
                putString("NAME", "")
                putString("YEAR", "")
                putString("SEX", "")
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        root.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val animationPosition = root.progress.toString()
//                Log.d("||||||transtionState||||||||", animationPosition)
                if ("0.0".equals(animationPosition)) {
                    return true
                }
                if ("1.0".equals(animationPosition)) {
                    timer.cancel()
                    return true
                }

                val intTouchX: Int = event!!.getX(event.actionIndex).toInt()
                val intTouchY: Int = event.getY(event.actionIndex).toInt() + intViewStartY
                var hitFlg: String

                text_result.text = "touch axis X : ${intTouchX}, Y : ${intTouchY}"
                text_result2.text =
                    "object axis X : ${intTargetX + intTargetWidth / 2}, Y : ${intTargetY + intTargetHeight / 2}"

                if ((intTargetX <= intTouchX && intTouchX <= intTargetX + intTargetWidth) &&
                    (intTargetY <= intTouchY && intTouchY <= intTargetY + intTargetHeight)
                ) {
                    red_star.alpha = 1.0f
                    hitFlg = "1"
                } else {
                    red_star.alpha = 0.5f
                    hitFlg = "0"
                }
                // タッチ情報をCSVファイルに保存
                touchInfoSaveCsv(fileName, intTargetX, intTargetY, intTargetWidth, intTargetHeight, intTouchX, intTouchY, hitFlg)

                return true
            }
        })

        root.viewTreeObserver.addOnGlobalLayoutListener {
            val location = IntArray(2)
            red_star.getLocationOnScreen(location)
            intTargetX = location[0]
            intTargetY = location[1]
        }
    }
}