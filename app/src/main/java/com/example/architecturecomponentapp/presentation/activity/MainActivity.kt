package com.example.architecturecomponentapp.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.architecturecomponentapp.presentation.fragment.ApiFilmListFragment
import com.example.architecturecomponentapp.presentation.fragment.DataFilmListFragment

class MainActivity : AppCompatActivity() {

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
                .add("Favoritos", DataFilmListFragment::class.java)
                .create()
        )
        val viewPager: ViewPager = findViewById(R.id.main_view_pager)
        viewPager.adapter = fragmentAdapter

        val tabLayout: SmartTabLayout = findViewById(R.id.main_tab_layout)
        tabLayout.setViewPager(viewPager)
    }

}
