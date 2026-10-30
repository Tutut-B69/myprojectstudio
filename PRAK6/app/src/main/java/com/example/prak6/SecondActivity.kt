package com.example.prak6
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LifecycleDemo"
    }

    private var namaDariMain: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        Log.d(TAG, "SecondActivity: onCreate()")

        val tvSapaan: TextView = findViewById(R.id.tvSapaan)
        val btnIsiForm: Button = findViewById(R.id.btnIsiForm)
        val btnKembali: Button = findViewById(R.id.btnKembali)

        namaDariMain = intent.getStringExtra("EXTRA_NAMA")
        tvSapaan.text = "Halo, $namaDariMain!"

        btnIsiForm.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            intent.putExtra("EXTRA_NAMA", namaDariMain)
            startActivity(intent)
        }

        btnKembali.setOnClickListener {
            finish() // kembali ke MainActivity, memicu onRestart() di MainActivity
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "SecondActivity: onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "SecondActivity: onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "SecondActivity: onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "SecondActivity: onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "SecondActivity: onDestroy()")
    }
}
