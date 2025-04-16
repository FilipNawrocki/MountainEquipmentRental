package pl.example1.mountainequipmentrental.View.AdminEdit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import pl.example1.mountainequipmentrental.Adapter.GearAdapter
import pl.example1.mountainequipmentrental.R
import pl.example1.mountainequipmentrental.ViewModel.MainViewModel
import pl.example1.mountainequipmentrental.databinding.FragmentShowGearListEditAdminBinding


class ShowGearListEditAdminFragment : Fragment() {

    private var _binding: FragmentShowGearListEditAdminBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: GearAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowGearListEditAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val adapter = GearAdapter(mutableListOf()) { gear ->
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.loadGear()

        viewModel.gearList.observe(viewLifecycleOwner) { listaSprzetu ->
            adapter.updateList(listaSprzetu)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}