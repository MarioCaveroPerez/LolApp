package com.example.lolapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lolapp.Adapters.SkinsAdapter
import com.example.lolapp.Data.Skins
import com.example.lolapp.databinding.FragmentSkinsBinding

class SkinsFragment(private val championId: String, private val skins: List<Skins>) : Fragment() {

    private var _binding: FragmentSkinsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSkinsBinding.inflate(layoutInflater, container, false)

        // Configurar RecyclerView
        binding.rvSkins.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSkins.adapter = SkinsAdapter(skins)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvSkins.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSkins.adapter = SkinsAdapter(skins)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}