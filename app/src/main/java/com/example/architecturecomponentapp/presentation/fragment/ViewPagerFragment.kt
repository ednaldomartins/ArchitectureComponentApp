package com.example.architecturecomponentapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.architecturecomponentapp.R
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems


class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val toolbar: Toolbar = view.findViewById(R.id.main_toolbar)
        toolbar.title = resources.getString(R.string.app_name)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)

        val fragmentAdapter = FragmentPagerItemAdapter(
            (activity as AppCompatActivity?)!!.supportFragmentManager, FragmentPagerItems.with(context)
                .add("Destaques", ApiFilmListFragment::class.java)
                .add("Favoritos", DataFilmListFragment::class.java)
                .create()
        )
        val viewPager: ViewPager = view.findViewById(R.id.main_view_pager)
        viewPager.adapter = fragmentAdapter

        val tabLayout: SmartTabLayout = view.findViewById(R.id.main_tab_layout)
        tabLayout.setViewPager(viewPager)

        return view
    }

}
