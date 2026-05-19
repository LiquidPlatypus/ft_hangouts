package com.example.ft_hangouts

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ColorPickerDialogFragment: DialogFragment() {

	companion object {
		const val RESULT_KEY = "color_picker"
		const val ARG_PREF_KEY = "pref_key"
		fun newInstance(): ColorPickerDialogFragment = ColorPickerDialogFragment()
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val inflater = LayoutInflater.from(requireContext())
		val view = inflater.inflate(R.layout.dialog_color_hsv, null)

		val preview = view.findViewById<View>(R.id.color_preview)
		val hexText = view.findViewById<TextView>(R.id.hex_text)
		val seekH = view.findViewById<SeekBar>(R.id.seek_h)
		val seekS = view.findViewById<SeekBar>(R.id.seek_s)
		val seekV = view.findViewById<SeekBar>(R.id.seek_v)

		val prefs = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
		val defaultColor = resources.getColor(R.color.header_background, requireActivity().theme)
		val startColor = prefs.getInt("header_color", defaultColor)

		val hsv = FloatArray(3)
		Color.colorToHSV(startColor, hsv)
		seekH.progress = hsv[0].toInt()
		seekS.progress = (hsv[1] * 100).toInt()
		seekV.progress = (hsv[2] * 100).toInt()

		fun updatePreview() {
			val h = seekH.progress.toFloat()
			val s = seekS.progress.toFloat() / 100f
			val v = seekV.progress.toFloat() / 100f
			val color = Color.HSVToColor(floatArrayOf(h, s, v))
			preview.setBackgroundColor(color)
			hexText.text = String.format("Hex: #%06X", 0xFFFFFF and color)
		}

		val listener = object : SeekBar.OnSeekBarChangeListener {
			override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
				updatePreview()
			}

			override fun onStartTrackingTouch(seekBar: SeekBar?) {}
			override fun onStopTrackingTouch(seekBar: SeekBar?) {}
		}

		seekH.setOnSeekBarChangeListener(listener)
		seekS.setOnSeekBarChangeListener(listener)
		seekV.setOnSeekBarChangeListener(listener)

		updatePreview()

		return AlertDialog.Builder(requireContext())
			.setTitle(getString(R.string.choose_color))
			.setView(view)
			.setPositiveButton("OK") { _, _ ->
				val color = Color.HSVToColor(floatArrayOf(
					seekH.progress.toFloat(),
					seekS.progress.toFloat() / 100f,
					seekV.progress.toFloat() / 100f
				))
				parentFragmentManager.setFragmentResult(RESULT_KEY, Bundle().apply {
					putInt("color", color)
				})
			}
			.setNegativeButton(android.R.string.cancel, null)
			.create()
	}
}