package com.example.tippy

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {

    private lateinit var baseAmount: EditText
    private lateinit var seekbar: SeekBar
    private lateinit var tipPercentage: TextView
    private lateinit var tipAmount: TextView
    private lateinit var totalAmount: TextView
    private lateinit var tipDesc: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        baseAmount = findViewById(R.id.etbase)
        seekbar = findViewById(R.id.seekBar)
        tipPercentage = findViewById(R.id.percent)
        tipAmount = findViewById(R.id.ettip)
        totalAmount = findViewById(R.id.ettotal)
        tipDesc = findViewById(R.id.tvTipDesc)
        seekbar.progress = INITIAL_TIP_PERCENT
        tipPercentage.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tipPercentage.text = "$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
        baseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }

        })
    }

    private fun updateTipDescription(progress: Int) {
        var tip= when(progress){
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tipDesc.text = tip
    }

    private fun computeTipAndTotal() {
        if (baseAmount.text.isEmpty()) {
            tipAmount.text = ""
            totalAmount.text = ""
            return
        }
        val base = baseAmount.text.toString().toDouble()
        val tipPercent = seekbar.progress

        val tip = base * tipPercent / 100
        val total = base + tip

        tipAmount.text = "%.2f".format(tip)
        totalAmount.text = "%.2f".format(total)
    }
}