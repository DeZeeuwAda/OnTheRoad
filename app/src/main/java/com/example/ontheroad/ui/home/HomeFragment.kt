package com.example.ontheroad.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.ontheroad.MainActivity
import com.example.ontheroad.R
import com.example.ontheroad.ui.details.DetailsFragment
import com.google.gson.Gson
import org.json.JSONObject

class HomeFragment : Fragment() {

    // Aanmaken van variabelen
    private lateinit var textFieldKenteken: EditText
    private lateinit var btnZoek: Button
    private lateinit var listViewDetails: ListView
    private lateinit var autoDetailsAdapter: ArrayAdapter<String>
    private val autoDetailsList = ArrayList<String>()
    private val autoInfoMap = mutableMapOf<String, MainActivity.AutoInfo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        textFieldKenteken = view.findViewById(R.id.etKenteken)
        btnZoek = view.findViewById(R.id.btnZoek)
        listViewDetails = view.findViewById(R.id.lvAutoDetails)

        autoDetailsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, autoDetailsList)
        listViewDetails.adapter = autoDetailsAdapter

        // Aanmaken van de zoekknop, en het ophalen van de gegevens. Ook een melding maken als er geen geldig kenteken ingevuld is
        btnZoek.setOnClickListener {
            val kenteken = textFieldKenteken.text.toString().trim().uppercase()
            if (kenteken.isNotEmpty()) {
                haalAutoGegevensOp(kenteken)
            } else {
                textFieldKenteken.error = "Voer een geldig kenteken in."
            }
        }

        // Aanmaken van een listener voor het klikken op een item in de lijst
        listViewDetails.setOnItemClickListener { _, _, position, _ ->
            val geselecteerdKenteken = autoDetailsList[position].split(", ")[0]
            val autoInfo = autoInfoMap[geselecteerdKenteken]

            val detailsFragment = DetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("AUTO_INFO", autoInfo)
                }
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailsFragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    //Ophalen van de gegevens uit de API, en meldt het als er een fout is
    private fun haalAutoGegevensOp(kenteken: String) {
        val url = "https://opendata.rdw.nl/resource/m9d7-ebf2.json?kenteken=$kenteken"
        val requestQueue = Volley.newRequestQueue(activity)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                autoDetailsList.clear()
                autoInfoMap.clear()
                for (i in 0 until response.length()) {
                    val autoInfo = parseAutoDetails(response.getJSONObject(i))
                    autoDetailsList.add("${autoInfo.kenteken}, ${autoInfo.voertuigsoort}")
                    autoInfoMap[autoInfo.kenteken] = autoInfo
                }
                autoDetailsAdapter.notifyDataSetChanged()
            },
            {
                autoDetailsList.clear()
                autoDetailsList.add("Fout bij het ophalen van gegevens")
                autoDetailsAdapter.notifyDataSetChanged()
            })

        requestQueue.add(jsonArrayRequest)
    }

    private fun parseAutoDetails(jsonObject: JSONObject): MainActivity.AutoInfo {
        return Gson().fromJson(jsonObject.toString(), MainActivity.AutoInfo::class.java)
    }
}
