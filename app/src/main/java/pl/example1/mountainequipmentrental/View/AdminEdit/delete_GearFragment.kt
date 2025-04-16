package pl.example1.mountainequipmentrental.View.AdminEdit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import pl.example1.mountainequipmentrental.R
import pl.example1.mountainequipmentrental.ViewModel.MainViewModel
import pl.example1.mountainequipmentrental.databinding.FragmentDeleteGearBinding



class delete_GearFragment : Fragment() {

    private var _binding: FragmentDeleteGearBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDeleteGearBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTextGearId = view.findViewById<EditText>(R.id.editTextGearId)
        val buttonDeleteGear = view.findViewById<Button>(R.id.buttonDeleteGear)

        buttonDeleteGear.setOnClickListener {
            mainViewModel.deleteGear(editTextGearId.text.toString(), requireContext())
            editTextGearId.text.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}