package com.alexlepadatu.trendingrepos.base

import androidx.fragment.app.Fragment
import com.alexlepadatu.trendingrepos.R

open class BaseFragment: Fragment() {

    protected fun addFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.nav_enter_right_to_left_anim,
                R.anim.nav_exit_right_to_left_anim,
                R.anim.nav_enter_left_to_right_anim,
                R.anim.nav_exit_left_to_right_anim)
            .replace(R.id.container, fragment)
            .addToBackStack(fragment::class.java.getSimpleName())
            .commit()
    }
}