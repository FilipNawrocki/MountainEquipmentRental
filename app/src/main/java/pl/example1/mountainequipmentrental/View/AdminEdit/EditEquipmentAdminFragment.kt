package pl.example1.mountainequipmentrental.View.AdminEdit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import pl.example1.mountainequipmentrental.R
import pl.example1.mountainequipmentrental.databinding.FragmentEditEquipmentAdminBinding


class EditEquipmentAdminFragment : Fragment() {

    private var _binding: FragmentEditEquipmentAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
    _binding = FragmentEditEquipmentAdminBinding.inflate(inflater, container, false)
    return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.EquipmentListButton.setOnClickListener {
            val fragment = ShowGearListEditAdminFragment()
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit()
        }

        binding.AddEquipmentButton.setOnClickListener {
            val fragment = AddNewGearFragment()
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit()
        }

        binding.DeleteEquipmentButton.setOnClickListener {
            val fragment = delete_GearFragment()
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit()
        }
        binding.fragmentContainerView



    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}