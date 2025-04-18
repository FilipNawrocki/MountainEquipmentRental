package pl.example1.mountainequipmentrental.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import pl.example1.mountainequipmentrental.R
import pl.example1.mountainequipmentrental.ViewModel.MainViewModel
import pl.example1.mountainequipmentrental.databinding.FragmentQrScanRentBinding
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class QrScanRentFragment : Fragment() {

    private lateinit var mainView: MainViewModel

    private var _binding: FragmentQrScanRentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQrScanRentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var selectedDate: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = ViewModelProvider(this)[MainViewModel::class.java]
        val args = QrScanRentFragmentArgs.fromBundle(requireArguments())
        binding.txtGearId.text = args.gearId

        val name = binding.editText.text.toString()
        val surname = binding.editText2.text.toString()
        val telephone_number = binding.editText3.text.toString()

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->

            val formattedMonth = String.format("%02d", month + 1)
            val formattedDay = String.format("%02d", dayOfMonth)
            selectedDate = "$year-$formattedMonth-$formattedDay"
        }

        binding.button.setOnClickListener(){
            val fromDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val toDate = selectedDate

            if (toDate == null) {
                Toast.makeText(requireContext(), "Wybierz datę zakończenia", Toast.LENGTH_SHORT).show()
            }else if(name.isEmpty() || surname.isEmpty() || telephone_number.isEmpty()){
                Toast.makeText(requireContext(), "Wprowadź dane", Toast.LENGTH_SHORT).show()
            } else {
                mainView.isAvailable(args.gearId, fromDate, toDate, requireContext(), name, surname, telephone_number)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}