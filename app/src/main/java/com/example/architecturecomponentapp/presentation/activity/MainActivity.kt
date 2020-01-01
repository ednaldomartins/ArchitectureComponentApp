package com.example.architecturecomponentapp.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager

import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter

import com.example.architecturecomponentapp.R
import com.example.architecturecomponentapp.presentation.fragment.AddFilmFragment
import com.example.architecturecomponentapp.presentation.fragment.ApiFilmListFragment
import com.example.architecturecomponentapp.presentation.fragment.FilmCategoryFragment
import com.example.architecturecomponentapp.presentation.fragment.FilmListFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentAdapter = FragmentPagerItemAdapter(
            supportFragmentManager, FragmentPagerItems.with(this)
                .add("Destaques", ApiFilmListFragment::class.java)
                .add("Categorias", FilmCategoryFragment::class.java)
                .add("Favoritos", FilmListFragment::class.java)
                .add("Adicionar Filme", AddFilmFragment::class.java)
                .create()
        )
        val viewPager: ViewPager = findViewById(R.id.main_view_pager)
        viewPager.adapter = fragmentAdapter

        val tabLayout: SmartTabLayout = findViewById(R.id.main_tab_layout)
        tabLayout.setViewPager(viewPager)

    }
}
