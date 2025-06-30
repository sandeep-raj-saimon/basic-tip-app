package com.nexaura.myapplication

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
import org.w3c.dom.Text
private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var etTipAmount: TextView
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipLabel: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvPercentLabel: TextView
    private lateinit var tvTipDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById<EditText>(R.id.etBaseAmount)
        etTipAmount = findViewById<TextView>(R.id.tvTipAmount)
        seekBarTip = findViewById<SeekBar>(R.id.SeekBarTip)
        tvTipLabel = findViewById<TextView>(R.id.tvTipLabel)
        tvTotalAmount = findViewById<TextView>(R.id.tvTotalAmount)
        tvPercentLabel = findViewById<TextView>(R.id.tvPercentLabel)
        tvTipDescription = findViewById<TextView>(R.id.tvTipDescription)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTvTipDescription(INITIAL_TIP_PERCENT)
        // Initial calculation
//        computeTipAndTotal()
            seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    Log.i(TAG, "onProgressChanged $progress")
                    tvPercentLabel.text = "$progress%"
                    etTipAmount.text = progress.toString()
                    computeTipAndTotal()
                    updateTvTipDescription(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                   Log.i(TAG, "onStartTrackingTouch")
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    Log.i(TAG, "onStopTrackingTouch")
                }

            })

            etBaseAmount.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    Log.i(TAG, "afterTextChanged $s")
                    computeTipAndTotal()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    Log.i(TAG, "beforeTextChanged $s")
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    Log.i(TAG, "onTextChanged $s")
                }

            })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun updateTvTipDescription(tipPercent: Int) {
        Log.i(TAG, "updateTvTipDescription $tipPercent")
        val tipDescription = when (tipPercent) {
        in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
        in 15..19 -> "Good"
        in 20..24 -> "Great"
        else -> "Amazing"
        }
        tvTipDescription.text = tipDescription
    }
    private fun computeTipAndTotal() {
        // 1. get the value of the base and tip amount
        if (etBaseAmount.text.isEmpty()) {
            etTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress


        // 2. compute the tip and total
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        Log.i(TAG, "computeTipAndTotal afterTextChanged $tipPercent")
        Log.i(TAG, "computeTipAndTotal afterTextChanged $tipAmount")
        Log.i(TAG, "computeTipAndTotal afterTextChanged $totalAmount")
        // 3.update the UI
        etTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
    }

}