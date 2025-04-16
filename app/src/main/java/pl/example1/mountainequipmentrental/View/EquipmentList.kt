package pl.example1.mountainequipmentrental.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import pl.example1.mountainequipmentrental.Adapter.GearAdapter
import pl.example1.mountainequipmentrental.Model.GearModel
import pl.example1.mountainequipmentrental.R
import pl.example1.mountainequipmentrental.ViewModel.MainViewModel
import pl.example1.mountainequipmentrental.databinding.FragmentEquipmentListBinding

class EquipmentList : Fragment() {
    private var _binding: FragmentEquipmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private lateinit var selectedGear: GearModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEquipmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val adapter = GearAdapter(mutableListOf()) { gear ->
            selectedGear = gear
            Toast.makeText(requireContext(), "Wybrano: ${gear.name}", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

// Ładuj dostępny sprzęt:
        val args = EquipmentListArgs.fromBundle(requireArguments())
        val categoryName = args.categoryName
        val dateFrom = args.dateFrom
        val dateTo = args.dateTo
        viewModel.loadAvailableGear(categoryName, dateFrom, dateTo)

        viewModel.gearList.observe(viewLifecycleOwner) { listaSprzetu ->
            adapter.updateList(listaSprzetu)
        }
        // Debug print
        Toast.makeText(requireContext(),"Kategoria: $categoryName, Od: $dateFrom, Do: $dateTo",Toast.LENGTH_SHORT).show()

        binding.RentBnt.setOnClickListener {
            if (::selectedGear.isInitialized) {
                val gearList = adapter.getSelectedItems()
                val action = EquipmentListDirections.actionEquipmentListToFragmentRentSummation(
                    dateTo,dateFrom,gearList.toTypedArray(),
                )
                findNavController().navigate(action)
                //findNavController().navigate(R.id.action_equipmentList_to_fragmentRentSummation)
            } else {
                Toast.makeText(requireContext(), "Wybierz sprzęt!", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
