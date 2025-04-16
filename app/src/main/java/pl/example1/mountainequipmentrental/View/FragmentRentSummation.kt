package pl.example1.mountainequipmentrental.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import pl.example1.mountainequipmentrental.Model.GearModel
import pl.example1.mountainequipmentrental.Model.RentalModel
import pl.example1.mountainequipmentrental.R
import pl.example1.mountainequipmentrental.ViewModel.MainViewModel
import java.nio.file.Files.find

class FragmentRentSummation : Fragment() {

    private val args: FragmentRentSummationArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rent_summation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameEditText = view.findViewById<EditText>(R.id.editText)
        val surnameEditText = view.findViewById<EditText>(R.id.editText2)
        val phoneEditText = view.findViewById<EditText>(R.id.editText3)

        val gearList = args.gearList?.toList() ?: emptyList()
        val dateFrom = args.dateFrom
        val dateTo = args.dateTo
        val container = view.findViewById<LinearLayout>(R.id.gearListContainer)
        val mainViewModel: MainViewModel by viewModels()

        gearList.forEach { gear ->
            val item = TextView(requireContext()).apply {
                text = "${gear.name}\n${gear.description}"
                textSize = 18f
                setPadding(16, 16, 16, 16)
                setBackgroundResource(R.drawable.default_item_background)
            }
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 24)
            }
            item.layoutParams = layoutParams
            container.addView(item)
        }

        view.findViewById<View>(R.id.button).setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val surname = surnameEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()

            if (name.isEmpty() || surname.isEmpty() || phone.isEmpty()) {
                Toast.makeText(requireContext(), "Uzupełnij wszystkie pola!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            for (gear in gearList) {
                val model = RentalModel(
                    Name = name,
                    Surname = surname,
                    telephone_number = phone,
                    dateFrom = dateFrom,
                    dateTo = dateTo,
                    gearId = gear.id,
                    returned = false
                )
                mainViewModel.rentGear(model)
            }

            Toast.makeText(requireContext(), "Zamówienie złożone!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_fragmentRentSummation_to_categoryEquipmentFragment)
        }
    }
}
