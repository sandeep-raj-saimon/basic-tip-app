package com.nexaura.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class MainActivity2 : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var etTipAmount: TextView
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipLabel: TextView
    private lateinit var tvTotalAmount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById<EditText>(R.id.etBaseAmount)
        etTipAmount = findViewById<TextView>(R.id.tvTipAmount)
        seekBarTip = findViewById<SeekBar>(R.id.SeekBarTip)
        tvTipLabel = findViewById<TextView>(R.id.tvTipLabel)
        tvTotalAmount = findViewById<TextView>(R.id.tvTotalAmount)

        // Listener for SeekBar
        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvTipLabel.text = "Tip (" + progress + "%):"
                computeTipAndTotal()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Listener for EditText
        etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                computeTipAndTotal()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Initial calculation
        computeTipAndTotal()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun computeTipAndTotal() {
        val baseAmountStr = etBaseAmount.text.toString()
        if (baseAmountStr.isEmpty()) {
            etTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }
        val baseAmount = baseAmountStr.toDoubleOrNull() ?: 0.0
        val tipPercent = seekBarTip.progress
        val tip = baseAmount * tipPercent / 100
        val total = baseAmount + tip
        etTipAmount.text = String.format("%.2f", tip)
        tvTotalAmount.text = String.format("%.2f", total)
    }
}