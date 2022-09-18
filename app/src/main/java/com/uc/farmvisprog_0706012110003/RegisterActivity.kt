package com.uc.farmvisprog_0706012110003

import GlobalVar.DatabaseHewan.Companion.listDataHewan
import Model.Hewan
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.uc.farmvisprog_0706012110003.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private var position = -1
    private lateinit var hewan:Hewan
    private var tempUri=""

    private val GetResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode== Activity.RESULT_OK){
            val uri = it.data?.data
            binding.FotoHewanImageView.setImageURI(uri)
            if (uri!=null){
                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
                    baseContext.getContentResolver().takePersistableUriPermission(
                        uri, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
                tempUri = uri.toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        Listener()
        GetIntent()

        binding.judulregisterTextView.text = intent.getStringExtra("Title")
    }

    private fun Listener() {
        binding.backbuttonImageView.setOnClickListener{
            val myIntent = Intent(this, HomeActivity::class.java)
            startActivity(myIntent)
        }

        binding.FotoHewanImageView.setOnClickListener {
            val myIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            myIntent.type = "image/*"
            GetResult.launch(myIntent)
        }

        binding.saveButton.setOnClickListener {
            var namaHewan = binding.NamaHewanTextInputLayout.editText?.text.toString().trim()
            var jenisHewan = binding.JenisHewanTextInputLayout.editText?.text.toString().trim()
            var usiaHewan = binding.UsiaHewanTextInputLayout.editText?.text.toString().trim()

            hewan = Hewan(namaHewan, jenisHewan, usiaHewan)
            Checker()
        }
    }

    private fun Checker() {
        var isCompleted: Boolean = true
        if (hewan.nama!!.isEmpty()) {
            binding.NamaHewanTextInputLayout.error = "Mohon isi Nama Hewan"
            isCompleted = false
        } else {
            binding.NamaHewanTextInputLayout.error = ""
        }

        if (hewan.jenis!!.isEmpty()) {
            binding.JenisHewanTextInputLayout.error = "Mohon isi Jenis Hewan"
            isCompleted = false
        } else {
            binding.JenisHewanTextInputLayout.error = ""
        }

        if (hewan.usia!!.isEmpty()) {
            binding.UsiaHewanTextInputLayout.error = "Mohon isi Usia Hewan"
            isCompleted = false
        } else {
            binding.UsiaHewanTextInputLayout.error = ""
        }

        if (isCompleted) {
            if (position == -1) {
                hewan.imageUri = tempUri
                listDataHewan.add(hewan)
                Toast.makeText(this, "Hewan Sukses Ditambahkan", Toast.LENGTH_LONG).show()
                val myIntent = Intent(this, HomeActivity::class.java)
                startActivity(myIntent)
            } else {
                if (tempUri == listDataHewan[position].imageUri){
                    hewan.imageUri = listDataHewan[position].imageUri
                } else if (tempUri=="") {
                    hewan.imageUri = listDataHewan[position].imageUri
                } else{
                    hewan.imageUri = tempUri
                }
                listDataHewan[position] = hewan
                Toast.makeText(this, "Edit Berhasil", Toast.LENGTH_LONG).show()
                val myIntent = Intent (this, HomeActivity::class.java)
                startActivity(myIntent)
            }
            finish()
        }
    }

    private fun GetIntent() {
        position = intent.getIntExtra("position", -1)
        if (position != -1) {
            val hewan = listDataHewan[position]
            Display(hewan)
        }

    }

    private fun Display(hewan: Hewan) {
        binding.NamaHewanTextInputLayout.editText!!.setText(hewan!!.nama)
        binding.JenisHewanTextInputLayout.editText!!.setText(hewan!!.jenis)
        binding.UsiaHewanTextInputLayout.editText!!.setText(hewan!!.usia)
        binding.FotoHewanImageView.setImageURI(Uri.parse(hewan.imageUri))
    }
}