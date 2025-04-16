package pl.example1.mountainequipmentrental.View.AdminEdit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.firestore.FirebaseFirestore
import pl.example1.mountainequipmentrental.Model.GearModel
import pl.example1.mountainequipmentrental.ViewModel.MainViewModel
import pl.example1.mountainequipmentrental.databinding.FragmentAddNewGearBinding

class AddNewGearFragment : Fragment() {

    private var _binding: FragmentAddNewGearBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewGearBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.buttonSave.setOnClickListener {

            val id = binding.editTextId.text.toString().trim()
            val name = binding.editTextName.text.toString().trim()
            val description = binding.editTextDescription.text.toString().trim()
            val category = binding.editTextCategory.text.toString().trim()
            val priceText = binding.editTextPrice.text.toString().trim()
            val isAvailable = binding.checkBoxAvailable.isChecked

            mainViewModel.saveGearToFirebase(requireContext(), id, name, description, category, priceText, isAvailable)

            if (id.isNotEmpty()) {
                val qrBitmap = mainViewModel.generateQrCode(id)
                binding.imageViewQrCode.setImageBitmap(qrBitmap)
            }

            clearForm()
        }

        binding.buttonSaveQr.setOnClickListener {
            binding.imageViewQrCode.drawable?.let {
                val bitmap = (it as android.graphics.drawable.BitmapDrawable).bitmap
                mainViewModel.saveBitmapToGallery(requireContext(), bitmap)
            } ?: Toast.makeText(requireContext(), "Najpierw wygeneruj kod QR", Toast.LENGTH_SHORT).show()
        }
    }


    private fun clearForm() {
        binding.editTextName.text?.clear()
        binding.editTextDescription.text?.clear()
        binding.editTextCategory.text?.clear()
        binding.editTextPrice.text?.clear()
        binding.checkBoxAvailable.isChecked = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
