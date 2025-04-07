package pl.example1.mountainequipmentrental.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pl.example1.mountainequipmentrental.Model.CategoriesModel
import pl.example1.mountainequipmentrental.R
import pl.example1.mountainequipmentrental.databinding.CategoryRowBinding

class CategoriesAdapter(
    private val CategoryList: List<CategoriesModel>,
    private val onItemClick: (CategoriesModel) -> Unit
) :
    RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class CategoriesViewHolder( binding: CategoryRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.CategoryName
        val image = binding.CategoryImage

        init {
            binding.root.setOnClickListener{val previousPosition = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                onItemClick(CategoryList[adapterPosition])}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val binding = CategoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoriesViewHolder(binding)

    }

    override fun getItemCount(): Int = CategoryList.size

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = CategoryList[position]
        holder.name.text = category.Name

        Glide.with(holder.itemView.context)
            .load(category.Url)
            .into(holder.image)

        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.selected_item_background)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.default_item_background)
        }
    }
}