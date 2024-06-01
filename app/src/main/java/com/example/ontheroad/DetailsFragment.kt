package com.example.ontheroad.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ontheroad.MainActivity
import com.example.ontheroad.R

class DetailsFragment : Fragment() {

    private lateinit var tvAutoDetails: TextView
    private var autoInfo: MainActivity.AutoInfo? = null

    // Maken van de functie welke de gegevens uit de API haalt en toont
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        tvAutoDetails = view.findViewById(R.id.tvAutoDetails)

        autoInfo = arguments?.getSerializable("AUTO_INFO") as? MainActivity.AutoInfo

        autoInfo?.let {
            val autoDetails = """
                Kenteken: ${it.kenteken}
                Voertuigsoort: ${it.voertuigsoort}
                Merk: ${it.merk}
                Handelsbenaming: ${it.handelsbenaming}
                Kleur: ${it.eerste_kleur}
                Taxi-indicator: ${it.taxi_indicator}
                WAM Verzekerd: ${it.wam_verzekerd}
            """.trimIndent()
            tvAutoDetails.text = autoDetails
        }

        return view
    }
}
