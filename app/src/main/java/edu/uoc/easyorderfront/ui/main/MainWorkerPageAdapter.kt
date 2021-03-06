package edu.uoc.easyorderfront.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import edu.uoc.easyorderfront.R
import edu.uoc.easyorderfront.ui.login.TabLoginFragment
import edu.uoc.easyorderfront.ui.register.TabRegisterWorkerFragment

class MainWorkerPageAdapter(fm: FragmentManager, context: Context) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val context = context

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {return TabLoginFragment()
            }
            1 -> {return TabRegisterWorkerFragment()
            }
            else -> {return TabLoginFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> {return context.getString(R.string.login)}
            1 -> {return context.getString(R.string.registrarse_trabajador)}
        }

        return super.getPageTitle(position)
    }

    override fun getCount(): Int {
        return 2
    }

}