package com.fajar.pratamalaundry.presentation.rules

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import com.fajar.pratamalaundry.R
import com.fajar.pratamalaundry.databinding.ActivityRulesBinding

class RulesActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityRulesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRulesBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        setActionBar()
    }

    private fun setActionBar() {
        setSupportActionBar(_binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.rules_asosiasi ->
                    if (checked) {
                        toRulesAsosiasi()
                    }
                R.id.rules_komplain ->
                    if (checked) {
                        toRulesKomplain()
                    }
            }
        }
    }

    private fun toRulesAsosiasi() {
        val toOneWeeks = Intent(this, RulesAsosiasiActivity::class.java)
        startActivity(toOneWeeks)

    }

    private fun toRulesKomplain() {
        val toOneMonth = Intent(this@RulesActivity, RulesKomplainActivity::class.java)
        startActivity(toOneMonth)
    }
}