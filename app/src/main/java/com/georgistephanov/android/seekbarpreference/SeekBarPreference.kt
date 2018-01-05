package com.georgistephanov.android.seekbarpreference

import android.content.Context
import android.content.res.TypedArray
import android.preference.Preference
import android.preference.PreferenceManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView

class SeekBarPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {
    private val DEFAULT_VALUE = 20
    private val DEFAULT_MIN = 1
    private val DEFAULT_MAX = 60

    private val attrs = attrs
    private lateinit var seekBar: SeekBar
    private lateinit var label: TextView
    private lateinit var labelUnit: String

    private var seekBarValue = 0
    private var seekBarMax = 0

    override fun onCreateView(parent: ViewGroup?): View {
        super.onCreateView(parent)

        layoutResource = R.layout.seekbar_preference
        val layoutInflater = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        val layout = layoutInflater.inflate(R.layout.seekbar_preference, parent, false)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        seekBar = layout.findViewById(R.id.seekbar)
        seekBar.progress = sharedPreferences.getInt(this.key, DEFAULT_VALUE)
        seekBar.max = seekBarMax - DEFAULT_MIN
        seekBar.setOnSeekBarChangeListener(DurationSeekBarListener())

        label = layout.findViewById(R.id.seekbar_label)
        displayLabelText(seekBar.progress)

        return layout
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        if (restorePersistedValue) {
            // Restore existing state
            seekBarValue = getPersistedInt(DEFAULT_VALUE)
        } else {
            seekBarValue = defaultValue as Int
            persistInt(defaultValue)
        }

        // Get the custom attributes
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SeekBarPreference, 0, 0)
        labelUnit = ta.getString(R.styleable.SeekBarPreference_label)
        seekBarMax = ta.getInteger(R.styleable.SeekBarPreference_max, DEFAULT_MAX)
        ta.recycle()
    }

    override fun onGetDefaultValue(a: TypedArray?, index: Int): Any {
        if (a != null)
            return a.getInteger(index, DEFAULT_VALUE)
        else
            return DEFAULT_VALUE
    }

    private fun displayLabelText(progress: Int) {
        when (progress) {
            0 -> label.text = "${progress + DEFAULT_MIN} ${labelUnit.substring(0, labelUnit.length - 1)}"
            else -> label.text = "${progress + DEFAULT_MIN} ${labelUnit}"
        }
    }

    inner class DurationSeekBarListener : SeekBar.OnSeekBarChangeListener {

        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            displayLabelText(p1)
            persistInt(p1)
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {}
        override fun onStopTrackingTouch(p0: SeekBar?) {}
    }
}