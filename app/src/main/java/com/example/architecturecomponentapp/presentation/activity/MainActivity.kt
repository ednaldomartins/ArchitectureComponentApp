package com.example.architecturecomponentapp.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager

import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.data.database.local.FilmDatabase
import com.example.architecturecomponentapp.model.FilmViewModel
import com.example.architecturecomponentapp.model.FilmViewModelFactory
import com.example.architecturecomponentapp.presentation.fragment.AddFilmFragment
import com.example.architecturecomponentapp.presentation.fragment.ApiFilmListFragment
import com.example.architecturecomponentapp.presentation.fragment.FilmCategoryFragment
import com.example.architecturecomponentapp.presentation.fragment.FilmListFragment

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var filmViewModel: FilmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // recuperar fonte de dados
        val application = requireNotNull(this).application
        val dataSource = FilmDatabase.getInstance(application).filmDao
        val filmViewModelFactory = FilmViewModelFactory(dataSource, application)
        filmViewModel = ViewModelProviders.of(this, filmViewModelFactory).get(FilmViewModel::class.java)

        val toolbar: Toolbar = findViewById(R.id.main_toolbar)
        toolbar.title = resources.getString(R.string.app_name)
        setSupportActionBar(toolbar)

        val fragmentAdapter = FragmentPagerItemAdapter(
            supportFragmentManager, FragmentPagerItems.with(this)
                .add("Destaques", ApiFilmListFragment::class.java)
                .add("Favoritos", FilmListFragment::class.java)
                .create()
        )
        val viewPager: ViewPager = findViewById(R.id.main_view_pager)
        viewPager.adapter = fragmentAdapter

        val tabLayout: SmartTabLayout = findViewById(R.id.main_tab_layout)
        tabLayout.setViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val menuSearchItem: MenuItem? = menu?.findItem(R.id.search_menu)
        val searchView: SearchView = menuSearchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Toast.makeText(this, "buscando por $query", Toast.LENGTH_LONG ).show()
        filmViewModel.searchFilmListApiService(query!!)
        return true
    }

}
