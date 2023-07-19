package com.example.manualtestreport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.manualtestreport.databinding.ActivityMainBinding

open class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener(binding)
    }

    private fun setListener(binding: ActivityMainBinding) {
        binding.btnOne.setOnClickListener {
            executeOne()
        }
        binding.btnTwo.setOnClickListener {
            executeTwo()
        }
    }

    private fun executeOne() {
        Toast.makeText(this, "One Executed !!", Toast.LENGTH_SHORT).show()
    }

    private fun executeTwo() {
        Toast.makeText(this, "Two Executed !!", Toast.LENGTH_SHORT).show()
    }
}