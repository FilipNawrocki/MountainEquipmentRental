package pl.example1.mountainequipmentrental.View.AdminEdit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import pl.example1.mountainequipmentrental.R
import pl.example1.mountainequipmentrental.databinding.FragmentRentalAdminEditBinding

class RentalAdminEditFragment : Fragment() {

    private var _binding: FragmentRentalAdminEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRentalAdminEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.EditCategoryButton.setOnClickListener {
            // znajdź odpowiednią akcję w nav_graph, jeśli chcesz przejść do edycji kategorii
            // findNavController().navigate(R.id.action_rentalAdminEditFragment_to_editCategoryAdminFragment)
        }

        binding.EditEquipmentButton.setOnClickListener {
            findNavController().navigate(R.id.action_rentalAdminEditFragment_to_editEquipmentAdminFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
