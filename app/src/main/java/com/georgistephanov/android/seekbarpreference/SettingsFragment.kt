package com.georgistephanov.android.seekbarpreference

import android.os.Bundle
import android.preference.PreferenceFragment

class SettingsFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the seekbar_preference from an XML resource
        addPreferencesFromResource(R.xml.preferences)
    }
}