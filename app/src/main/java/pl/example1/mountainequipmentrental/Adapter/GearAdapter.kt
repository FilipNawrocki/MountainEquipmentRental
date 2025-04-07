package pl.example1.mountainequipmentrental.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.example1.mountainequipmentrental.Model.CategoriesModel
import pl.example1.mountainequipmentrental.Model.GearModel
import pl.example1.mountainequipmentrental.databinding.CategoryRowBinding
import pl.example1.mountainequipmentrental.databinding.GearRowBinding

class GearAdapter(private val GearList: List<GearModel>,
                  private val onItemClick: (GearModel) -> Unit
): RecyclerView.Adapter<GearAdapter.GearViewHolder>() {
    inner class GearViewHolder( binding: GearRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.GearName
        val description = binding.GearDescription
        val isAvailable = binding.GearIsAvailable
        val price = binding.GearPrice
        val Id = binding.GearId

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GearViewHolder {
        val binding = GearRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GearViewHolder(binding)
    }

    override fun getItemCount(): Int = GearList.size

    override fun onBindViewHolder(holder: GearViewHolder, position: Int) {
        val gear = GearList[position]
        holder.Id.text = gear.id
        holder.name.text = gear.Name
        holder.description.text = gear.description
        holder.isAvailable.text = gear.isAvailable.toString()
        holder.price.text = gear.pricePerWeek.toString()
    }

    fun updateList(newList: List<GearModel>) {
        (GearList as MutableList).clear()
        GearList.addAll(newList)
        notifyDataSetChanged()
    }
}