package com.ejemplo.misfinanzas.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ejemplo.misfinanzas.ui.fragments.EstadisticasFragment
import com.ejemplo.misfinanzas.ui.fragments.InicioFragment
import com.ejemplo.misfinanzas.R
import com.ejemplo.misfinanzas.ui.fragments.TransaccionesFragment
import com.ejemplo.misfinanzas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    // ViewModel compartido entre la Activity y todos los Fragments
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar el Fragment inicial solo si es la primera vez
        // savedInstanceState es null la primera vez, pero tiene datos si se recreó
        // Sin esta verificación, al rotar se duplicaría el Fragment
        if (savedInstanceState == null) {
            cargarFragment(InicioFragment())
        }

        // Cargar datos de prueba si la base de datos está vacía
        viewModel.transacciones.observe(this) { lista ->
            if (lista.isEmpty()) {
                viewModel.insertarDatosDePrueba()
            }
        }

        // Configurar la navegación inferior
        // setOnItemSelectedListener se ejecuta cuando el usuario toca una pestaña
        binding.bottomNav.setOnItemSelectedListener { item ->
            // item.itemId es el id definido en bottom_menu.xml
            when (item.itemId) {
                R.id.nav_inicio -> cargarFragment(InicioFragment())
                R.id.nav_transacciones -> cargarFragment(TransaccionesFragment())
                R.id.nav_estadisticas -> cargarFragment(EstadisticasFragment())
            }
            // Retornar true indica que el item fue manejado
            true
        }
    }

    // Reemplaza el Fragment actual por uno nuevo
    private fun cargarFragment(fragment: Fragment) {
        // supportFragmentManager maneja los Fragments de esta Activity
        // beginTransaction inicia una transacción (un conjunto de cambios)
        supportFragmentManager.beginTransaction()
            // replace reemplaza el contenido del contenedor por el nuevo Fragment
            .replace(R.id.fragmentContainer, fragment)
            // commit ejecuta la transacción
            .commit()
    }
}