package com.example.login

import CardAdapter
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.login.databinding.ActivityRecyclerviewBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class Cardview : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityRecyclerviewBinding
    private val cardItems = mutableListOf<CardItem>()
    private lateinit var adapter: CardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Toolbar
        setSupportActionBar(binding.toolbar)

        // Configurar DrawerLayout y NavigationView
        val drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener(this)

        // Configurar RecyclerView
        cardItems.add(CardItem(R.drawable.img_1, "KAWASAKI", "777"))
        cardItems.add(CardItem(R.drawable.img_1, "YAMAHA", "555"))

        adapter = CardAdapter(cardItems) { position ->
            editCard(position)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Configurar el helper para deslizamiento
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                cardItems.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        // Configurar el botón para añadir nueva tarjeta
        binding.addCardButton.setOnClickListener {
            val dialogFragment = EditCardDialogFragment(null, cardItems) { newCard ->
                cardItems.add(newCard)
                adapter.notifyItemInserted(cardItems.size - 1)
            }
            dialogFragment.show(supportFragmentManager, "AddCardDialog")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Opción de "Inicio" (No hace nada porque ya estamos en home)
            }
            R.id.nav_profile -> {
                // Aquí puedes abrir una nueva actividad cuando agregues el perfil
            }
            R.id.nav_logout -> {
                logout() // 🔥 Ahora funciona correctamente el logout
            }
        }

        // Cerrar el Navigation Drawer después de seleccionar una opción
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()

        // Borrar las SharedPreferences para eliminar el estado de sesión
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        // Redirigir a la pantalla de login y evitar que pueda volver atrás
        val intent = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Cierra la actividad actual
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun editCard(position: Int) {
        val dialogFragment = EditCardDialogFragment(position, cardItems) { updatedCard ->
            cardItems[position] = updatedCard
            adapter.notifyItemChanged(position)
        }
        dialogFragment.show(supportFragmentManager, "EditCardDialog")
    }
}
