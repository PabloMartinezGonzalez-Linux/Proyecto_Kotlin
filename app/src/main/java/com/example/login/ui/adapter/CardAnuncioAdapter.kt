import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.login.R
import com.example.login.domain.models.CardAnuncio

class CardAnuncioAdapter(private val cards: List<CardAnuncio>) : RecyclerView.Adapter<CardAnuncioAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView? = itemView.findViewById(R.id.ivAdImage)
    }

    override fun getItemViewType(position: Int): Int {
        return cards[position].cardType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val layout = when (viewType) {
            0 -> R.layout.anuncio_card_azul
            1 -> R.layout.anuncio_card_gris
            2 -> R.layout.anuncio_card_naranja
            3 -> R.layout.anuncio_card_disney
            else -> R.layout.anuncio_card_azul
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards[position]
        holder.ivImage?.setImageResource(card.imageResId)
    }

    override fun getItemCount(): Int = cards.size
}
