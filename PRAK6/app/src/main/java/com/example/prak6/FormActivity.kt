package com.example.prak6
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.Toast

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val etNamaForm = findViewById<EditText>(R.id.etNamaForm)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etNoHp = findViewById<EditText>(R.id.etNoHp)
        val radioGroupGender = findViewById<RadioGroup>(R.id.radioGroupGender)
        val cbOlahraga = findViewById<CheckBox>(R.id.cbOlahraga)
        val cbMembaca = findViewById<CheckBox>(R.id.cbMembaca)
        val cbCoding = findViewById<CheckBox>(R.id.cbCoding)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val btnKirim = findViewById<Button>(R.id.btnKirim)
        val btnJadwal = findViewById<Button>(R.id.btnJadwal)

        // Nama diteruskan dari SecondActivity agar kolom nama terisi otomatis
        val namaDikirim = intent.getStringExtra("EXTRA_NAMA")
        etNamaForm.setText(namaDikirim ?: "")

        // Tombol Kirim: validasi dan tampilkan ringkasan data
        btnKirim.setOnClickListener {
            val nama = etNamaForm.text.toString()
            val email = etEmail.text.toString()
            val noHp = etNoHp.text.toString()

            if (nama.isBlank() || email.isBlank()) {
                Toast.makeText(this, "Nama dan Email wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val genderId = radioGroupGender.checkedRadioButtonId
            val gender = if (genderId != -1) {
                findViewById<RadioButton>(genderId).text.toString()
            } else {
                "(belum dipilih)"
            }

            val hobiTerpilih = mutableListOf<String>()
            if (cbOlahraga.isChecked) hobiTerpilih.add("Olahraga")
            if (cbMembaca.isChecked) hobiTerpilih.add("Membaca")
            if (cbCoding.isChecked) hobiTerpilih.add("Coding")
            val hobi = if (hobiTerpilih.isEmpty()) "(tidak ada)" else hobiTerpilih.joinToString(", ")

            val rating = ratingBar.rating

            val ringkasan = "Nama: $nama\nEmail: $email\nNo HP: $noHp\n" +
                    "Jenis Kelamin: $gender\nHobi: $hobi\nRating: $rating"

            Toast.makeText(this, ringkasan, Toast.LENGTH_LONG).show()
        }

        // Tombol Booking Jadwal: buka JadwalActivity sambil meneruskan nama
        btnJadwal.setOnClickListener {
            val intent = Intent(this, JadwalActivity::class.java)
            intent.putExtra("EXTRA_NAMA", etNamaForm.text.toString())
            startActivity(intent)
        }
    }
}
