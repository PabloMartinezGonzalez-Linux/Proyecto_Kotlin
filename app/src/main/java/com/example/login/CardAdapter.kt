import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.login.CardItem
import com.example.login.databinding.ItemCardBinding

class CardAdapter(
    private val items: MutableList<CardItem>,
    private val onItemEdit: (Int) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CardItem) {
            // Mostrar la imagen de fondo
            binding.bgCard.setImageResource(item.bgImageRes)

            // Mostrar la imagen seleccionada o el recurso por defecto
            if (item.imageUri != null) {
                binding.productImage.setImageURI(item.imageUri)
            } else {
                item.imageRes?.let { binding.productImage.setImageResource(it) }
            }

            binding.Marca.text = item.brand
            binding.Modelo.text = item.model

            // Abrir diálogo al hacer clic
            binding.root.setOnClickListener {
                onItemEdit(adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

