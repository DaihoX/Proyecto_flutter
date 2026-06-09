// TransaccionAdapter.kt
// Adapter que conecta la lista de transacciones con el RecyclerView
package com.ejemplo.misfinanzas

// LayoutInflater convierte XML en objetos View
import android.view.LayoutInflater
// ViewGroup es el contenedor padre donde se insertan las vistas
import android.view.ViewGroup
// RecyclerView es el componente de lista
import androidx.recyclerview.widget.RecyclerView
// Importamos el binding generado para el layout del item
import com.ejemplo.misfinanzas.databinding.ItemTransaccionBinding

// El Adapter recibe:
// - transacciones: la lista de datos a mostrar
// - onItemClick: una lambda que se ejecuta cuando el usuario toca un item
//   (recibe la Transaccion tocada como parámetro)
class TransaccionAdapter(
    private val transacciones: List<Transaccion>,
    private val onItemClick: (Transaccion) -> Unit
) : RecyclerView.Adapter<TransaccionAdapter.TransaccionViewHolder>() {
    // RecyclerView.Adapter requiere un tipo ViewHolder como parámetro genérico

    // ViewHolder: contenedor que guarda las referencias a las vistas de UN item
    // Evita llamar a findViewById repetidamente (optimización)
    // "inner class" puede acceder a los miembros de la clase externa
    inner class TransaccionViewHolder(
        val binding: ItemTransaccionBinding  // binding del layout item_transaccion.xml
    ) : RecyclerView.ViewHolder(binding.root)
    // RecyclerView.ViewHolder requiere la vista raíz como parámetro

    // onCreateViewHolder: se llama cuando RecyclerView necesita una vista NUEVA
    // Esto pasa pocas veces: solo crea las vistas visibles + unas pocas extra
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaccionViewHolder {
        // Inflar el XML del item: convierte item_transaccion.xml en objetos View
        // parent = el RecyclerView, false = no adjuntar al padre todavía
        val binding = ItemTransaccionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        // Retornar un nuevo ViewHolder con el binding
        return TransaccionViewHolder(binding)
    }

    // onBindViewHolder: se llama para VINCULAR datos a una vista existente
    // Esto pasa cada vez que un item aparece en pantalla (incluyendo reciclados)
    // position = índice del elemento en la lista
    override fun onBindViewHolder(holder: TransaccionViewHolder, position: Int) {
        // Obtener la transacción correspondiente a esta posición
        val transaccion = transacciones[position]

        // Vincular los datos a las vistas del item
        // holder.binding da acceso a todos los elementos del XML por su id
        holder.binding.tvEmoji.text = transaccion.categoria.emoji
        holder.binding.tvDescripcion.text = transaccion.descripcion
        holder.binding.tvCategoria.text = transaccion.categoria.etiqueta
        holder.binding.tvMonto.text = transaccion.montoFormateado()

        // Cambiar el color del monto según si es ingreso (verde) o gasto (rojo)
        // itemView.context da acceso al contexto para obtener colores
        val colorMonto = if (transaccion.esIngreso()) {
            holder.itemView.context.getColor(R.color.verde_claro)
        } else {
            holder.itemView.context.getColor(R.color.rojo_gasto)
        }
        holder.binding.tvMonto.setTextColor(colorMonto)

        // Configurar el click en todo el item
        // Cuando el usuario toca la tarjeta, se ejecuta la lambda onItemClick
        holder.itemView.setOnClickListener {
            onItemClick(transaccion)
        }
    }

    // getItemCount: retorna el número total de elementos
    // RecyclerView lo usa para saber cuántos items hay
    override fun getItemCount(): Int = transacciones.size
}