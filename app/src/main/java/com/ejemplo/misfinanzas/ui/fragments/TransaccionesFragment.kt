package com.ejemplo.misfinanzas.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ejemplo.misfinanzas.ui.adapters.TransaccionAdapter
import com.ejemplo.misfinanzas.data.model.Transaccion
import com.ejemplo.misfinanzas.databinding.FragmentTransaccionesBinding
import com.ejemplo.misfinanzas.ui.AgregarActivity
import com.ejemplo.misfinanzas.ui.MainViewModel
import com.google.android.material.snackbar.Snackbar

class TransaccionesFragment : Fragment() {

    private var _binding: FragmentTransaccionesBinding? = null
    private val binding get() = _binding!!
    // Compartir el ViewModel con la Activity y los otros Fragments
    private val viewModel: MainViewModel by activityViewModels()

    // registerForActivityResult en un Fragment funciona igual que en una Activity
    private val lanzarAgregar = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { resultado ->
        if (resultado.resultCode == Activity.RESULT_OK) {
            val nueva = resultado.data?.getSerializableExtra("NUEVA_TRANSACCION") as? Transaccion
            if (nueva != null) {
                viewModel.agregarTransaccion(nueva)
                Toast.makeText(requireContext(), "Transacción guardada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransaccionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerView
        binding.rvTransacciones.layoutManager = LinearLayoutManager(requireContext())

        // Observar transacciones
        viewModel.transacciones.observe(viewLifecycleOwner) { lista ->
            val adapter = TransaccionAdapter(lista) { transaccion ->
                Toast.makeText(
                    requireContext(),
                    "${transaccion.descripcion}: ${transaccion.montoFormateado()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.rvTransacciones.adapter = adapter
            binding.tvCantidad.text = "${lista.size} movimientos"
        }

        // Configurar swipe para eliminar
        configurarSwipeEliminar()

        // Botón de agregar
        binding.btnAgregar.setOnClickListener {
            // requireContext() obtiene el contexto del Fragment
            val intent = Intent(requireContext(), AgregarActivity::class.java)
            lanzarAgregar.launch(intent)
        }
    }

    // Swipe a la izquierda para eliminar con opción de deshacer
    private fun configurarSwipeEliminar() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicion = viewHolder.adapterPosition
                val lista = viewModel.transacciones.value ?: return
                if (posicion < lista.size) {
                    val transaccion = lista[posicion]
                    viewModel.eliminarTransaccion(transaccion)

                    Snackbar.make(binding.root, "Eliminada", Snackbar.LENGTH_LONG)
                        .setAction("Deshacer") {
                            viewModel.agregarTransaccion(transaccion)
                        }.show()
                }
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.rvTransacciones)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}