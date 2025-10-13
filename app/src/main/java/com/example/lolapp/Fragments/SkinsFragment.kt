package com.example.lolapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lolapp.Adapters.SkinsAdapter
import com.example.lolapp.Data.Skins
import com.example.lolapp.databinding.FragmentSkinsBinding


class SkinsFragment : Fragment() {

    private var _binding: FragmentSkinsBinding? = null
    private val binding get() = _binding!!

    private var championId: String? = null
    private var skins: List<Skins> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            championId = it.getString("champion_id")
            skins = it.getParcelableArrayList<Skins>("skins_list") ?: emptyList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSkinsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSkins.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvSkins.adapter = SkinsAdapter(championId ?: "", skins)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
