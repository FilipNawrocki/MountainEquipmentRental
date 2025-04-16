package pl.example1.mountainequipmentrental.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.example1.mountainequipmentrental.Model.GearModel
import pl.example1.mountainequipmentrental.R
import pl.example1.mountainequipmentrental.View.AdminRental
import pl.example1.mountainequipmentrental.ViewModel.MainViewModel
import pl.example1.mountainequipmentrental.databinding.GearRowBinding

class GearAdapter(
    gearList: List<GearModel>,
    private val onItemClick: (GearModel) -> Unit
) : RecyclerView.Adapter<GearAdapter.GearViewHolder>() {
    private var gearList: MutableList<GearModel> = gearList.toMutableList()
    private val selectedItems = mutableSetOf<Int>() // pozycje zaznaczonych elementów

    inner class GearViewHolder(val binding: GearRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GearViewHolder {
        val binding = GearRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GearViewHolder(binding)
    }

    override fun getItemCount(): Int = gearList.size

    override fun onBindViewHolder(holder: GearViewHolder, position: Int) {
        val gear = gearList[position]
        val isSelected = selectedItems.contains(position)

        with(holder.binding) {
            GearId.text = gear.id
            GearName.text = gear.name
            GearDescription.text = gear.description
            GearIsAvailable.text = gear.available.toString()
            GearPrice.text = gear.pricePerWeek.toString()

            root.setBackgroundResource(
                if (isSelected) R.drawable.selected_item_background
                else R.drawable.default_item_background
            )

            root.setOnClickListener {
                toggleSelection(position)
                onItemClick(gear)
            }
        }
    }

    private fun toggleSelection(position: Int) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(position)
        } else {
            selectedItems.add(position)
        }
        notifyItemChanged(position)
    }



    fun getSelectedItems(): List<GearModel> {
        return selectedItems.map { gearList[it] }
    }

    fun updateList(newList: List<GearModel>) {
        gearList.clear()
        gearList.addAll(newList)
        selectedItems.clear()
        notifyDataSetChanged()
    }
}
