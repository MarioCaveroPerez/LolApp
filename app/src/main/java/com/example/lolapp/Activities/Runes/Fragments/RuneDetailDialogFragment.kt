package com.example.lolapp.Activities.Runes.Fragments

import android.app.Dialog
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.lolapp.R
import com.squareup.picasso.Picasso
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class RuneDetailDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_NAME = "rune_name"
        private const val ARG_DESC = "rune_desc"
        private const val ARG_ICON = "rune_icon"

        fun newInstance(name: String, desc: String, iconUrl: String): RuneDetailDialogFragment {
            val fragment = RuneDetailDialogFragment()
            val args = Bundle()
            args.putString(ARG_NAME, name)
            args.putString(ARG_DESC, desc)
            args.putString(ARG_ICON, iconUrl)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val params = dialog?.window?.attributes
        val margin = 50 // px
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params
        // Personaliza el tamaño del diálogo
        dialog?.window?.setLayout(resources.displayMetrics.widthPixels - margin * 2, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.setDimAmount(0.9f)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rune_detail_dialog, container, false)

        val ivIcon = view.findViewById<ImageView>(R.id.ivRuneIcon)
        val tvName = view.findViewById<TextView>(R.id.tvRuneName)
        val tvDesc = view.findViewById<TextView>(R.id.tvRuneDesc)

        val name = arguments?.getString(ARG_NAME)
        val desc = arguments?.getString(ARG_DESC)
        val iconUrl = arguments?.getString(ARG_ICON)

        tvName.text = name
        tvDesc.text = Html.fromHtml(desc, Html.FROM_HTML_MODE_LEGACY)
        Picasso.get().load(iconUrl).into(ivIcon)

        return view
    }
}
