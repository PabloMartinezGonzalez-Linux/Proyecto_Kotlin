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
        fun bind(item: CardItem, position: Int) {
            binding.productImage.setImageResource(item.imageRes)
            binding.Marca.text = item.brand
            binding.Modelo.text = item.model

            // Al hacer clic en la tarjeta, abrir el diálogo para editar
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
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size
}


