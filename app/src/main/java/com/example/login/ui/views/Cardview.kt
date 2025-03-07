package com.example.login.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.login.R
import com.example.login.databinding.ActivityRecyclerviewBinding
import com.example.login.ui.viewmodel.UserProfileViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Cardview : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityRecyclerviewBinding
    private val userProfileViewModel: UserProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.negro100))

        val drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        drawerToggle.drawerArrowDrawable.color = resources.getColor(R.color.blanco)
        binding.navigationView.setNavigationItemSelectedListener(this)

        val headerView = binding.navigationView.getHeaderView(0)
        val profileImage = headerView.findViewById<ImageView>(R.id.profileImage)
        val navUserName = headerView.findViewById<TextView>(R.id.navUserName)

        userProfileViewModel.userProfile.observe(this) { profile ->
            navUserName.text = profile.email
            if (profile.photoUri != null) {
                profileImage.setImageURI(profile.photoUri)
            } else {
                profileImage.setImageResource(R.drawable.perfil_100)
            }
        }

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            binding.navigationView.setCheckedItem(R.id.nav_home)
            binding.bottomNavigation.selectedItemId = R.id.bottom_profile
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> replaceFragment(AnunciosFragment())
                R.id.bottom_profile -> replaceFragment(HomeFragment())
            }
            true
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                replaceFragment(HomeFragment())
                binding.bottomNavigation.selectedItemId = R.id.bottom_profile
            }
            R.id.nav_profile -> {
                replaceFragment(ProfileFragment())
            }
            R.id.nav_logout -> logout()
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun logout() {
        // Ya no usamos firebaseAuth para el logout.
        // En su lugar, limpia el token almacenado y navega a la pantalla de login.
        com.example.login.data.network.TokenManager.saveToken("") // O elimina el token
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
