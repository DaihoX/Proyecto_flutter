// EstadisticasFragment.kt
// Fragment que muestra estadísticas de gastos por categoría
package com.ejemplo.misfinanzas

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ejemplo.misfinanzas.databinding.FragmentEstadisticasBinding
import com.ejemplo.misfinanzas.api.ResultadoApi

class EstadisticasFragment : Fragment() {

    private var _binding: FragmentEstadisticasBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        _binding = FragmentEstadisticasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observar las transacciones para calcular estadísticas
        viewModel.transacciones.observe(viewLifecycleOwner) { lista ->
            mostrarEstadisticas(lista)
        }
        // Agregar en onViewCreated de EstadisticasFragment:

        // Observar el resultado de la consulta de tasas
        viewModel.tasasCambio.observe(viewLifecycleOwner) { resultado ->
            // "when" con sealed class: el compilador verifica que cubramos todos los casos
            when (resultado) {
                is ResultadoApi.Cargando -> {
                    // Mostrar el indicador de carga
                    binding.progressTasas.visibility = View.VISIBLE
                    binding.tvTasas.text = ""
                }
                is ResultadoApi.Exito -> {
                    // Ocultar el indicador de carga
                    binding.progressTasas.visibility = View.GONE
                    // Mostrar las tasas relevantes
                    val tasas = resultado.datos.rates
                    val texto = buildString {
                        // buildString construye un String de forma eficiente
                        appendLine("1 USD = ${tasas["COP"] ?: "N/A"} COP")
                        appendLine("1 USD = ${tasas["EUR"] ?: "N/A"} EUR")
                        appendLine("1 USD = ${tasas["GBP"] ?: "N/A"} GBP")
                        appendLine("1 USD = ${tasas["BRL"] ?: "N/A"} BRL")
                        append("1 USD = ${tasas["MXN"] ?: "N/A"} MXN")
                    }
                    binding.tvTasas.text = texto
                }
                is ResultadoApi.Error -> {
                    binding.progressTasas.visibility = View.GONE
                    binding.tvTasas.text = resultado.mensaje
                    binding.tvTasas.setTextColor(
                        requireContext().getColor(R.color.rojo_gasto)
                    )
                }
            }
        }

        // Botón para consultar tasas
        binding.btnConsultarTasas.setOnClickListener {
            viewModel.consultarTasas()
        }
    }

    // Calcula y muestra las estadísticas por categoría
    // Función mejorada usando operaciones funcionales
    private fun mostrarEstadisticas(transacciones: List<Transaccion>) {
        // Top 3 categorías de gasto usando encadenamiento funcional
        val topGastos = transacciones
            .filter { !it.esIngreso() }
            .groupBy { it.categoria }
            .mapValues { (_, lista) -> lista.sumOf { Math.abs(it.monto) } }
            .toList()
            .sortedByDescending { it.second }
            .take(3)

        // Construir el texto del resumen usando buildString
        val resumen = buildString {
            appendLine("📊 Top categorías de gasto:")
            topGastos.forEachIndexed { index, (categoria, total) ->
                appendLine("${index + 1}. ${categoria.emoji} ${categoria.etiqueta}: ${total.formatearCOP()}")
            }

            appendLine()
            appendLine("📈 Resumen general:")
            appendLine("Total transacciones: ${transacciones.size}")

            // Calcular promedio de gastos usando let para manejar lista vacía
            transacciones
                .filter { !it.esIngreso() }
                .takeIf { it.isNotEmpty() }  // takeIf retorna null si la condición es false
                ?.let { gastos ->
                    val promedio = gastos.sumOf { Math.abs(it.monto) } / gastos.size
                    appendLine("Promedio por gasto: ${promedio.formatearCOP()}")
                }

            // Mayor ingreso
            transacciones
                .filter { it.esIngreso() }
                .maxByOrNull { it.monto }
                ?.let { mayor ->
                    appendLine("Mayor ingreso: ${mayor.descripcion} (${mayor.montoFormateado()})")
                }

            // Mayor gasto
            transacciones
                .filter { !it.esIngreso() }
                .minByOrNull { it.monto }
                ?.let { mayor ->
                    append("Mayor gasto: ${mayor.descripcion} (${mayor.montoFormateado()})")
                }
        }

        binding.tvTotalTransacciones.text = resumen
    }

    private fun formatearMonto(monto: Double): String {
        return "$ ${String.format("%,.0f", monto)}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}