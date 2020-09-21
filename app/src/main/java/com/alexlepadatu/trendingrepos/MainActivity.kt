package com.alexlepadatu.trendingrepos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alexlepadatu.trendingrepos.list.ListReposFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var reposFragment : Fragment? = supportFragmentManager.findFragmentById(R.id.container)
        if (reposFragment == null)
            reposFragment = ListReposFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, reposFragment)
            .commitNow()
    }
}