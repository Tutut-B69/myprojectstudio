package com.example.prak6


import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class JadwalActivity : AppCompatActivity() {

    private var gambarPertama = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jadwal)

        val tvPeserta = findViewById<TextView>(R.id.tvPeserta)
        val ivFoto = findViewById<ImageView>(R.id.ivFoto)
        val btnGantiGambar = findViewById<Button>(R.id.btnGantiGambar)
        val spLayanan = findViewById<Spinner>(R.id.spLayanan)
        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val btnRingkasan = findViewById<Button>(R.id.btnRingkasan)
        val btnKalender = findViewById<Button>(R.id.btnKalender)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        // Nama diestafetkan dari FormActivity lewat Intent
        val nama = intent.getStringExtra("EXTRA_NAMA") ?: "(tanpa nama)"
        tvPeserta.text = "Peserta: $nama"

        // ImageView: ganti gambar bergantian
        btnGantiGambar.setOnClickListener {
            gambarPertama = !gambarPertama
            ivFoto.setImageResource(if (gambarPertama) R.drawable.gambar else R.drawable.img2)
            Toast.makeText(this, "Gambar diganti", Toast.LENGTH_SHORT).show()
        }

        // TimePicker: format 24 jam
        timePicker.setIs24HourView(true)

        // Simpan jadwal: tampilkan ringkasan lewat Toast
        btnSimpan.setOnClickListener {
            if (!tanggalValid(datePicker)) return@setOnClickListener

            val layanan = spLayanan.selectedItem.toString()
            val ringkasan = "Peserta: $nama\nLayanan: $layanan\n" +
                    "Tanggal: ${teksTanggal(datePicker)}\nPukul: ${teksWaktu(timePicker)}"
            Toast.makeText(this, ringkasan, Toast.LENGTH_LONG).show()
        }

        // EXPLICIT INTENT: pindah ke RingkasanActivity sambil membawa data
        btnRingkasan.setOnClickListener {
            if (!tanggalValid(datePicker)) return@setOnClickListener

            val intent = Intent(this, RingkasanActivity::class.java)
            intent.putExtra("EXTRA_NAMA", nama)
            intent.putExtra("EXTRA_LAYANAN", spLayanan.selectedItem.toString())
            intent.putExtra("EXTRA_TANGGAL", teksTanggal(datePicker))
            intent.putExtra("EXTRA_WAKTU", teksWaktu(timePicker))
            startActivity(intent)
        }

        // IMPLICIT INTENT: minta aplikasi kalender membuat acara baru
        btnKalender.setOnClickListener {
            if (!tanggalValid(datePicker)) return@setOnClickListener

            val layanan = spLayanan.selectedItem.toString()
            val mulai = Calendar.getInstance().apply {
                set(datePicker.year, datePicker.month, datePicker.dayOfMonth,
                    timePicker.hour, timePicker.minute, 0)
            }
            val selesai = Calendar.getInstance().apply {
                timeInMillis = mulai.timeInMillis + 60 * 60 * 1000   // durasi 1 jam
            }

            val intent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, "Konsultasi: $layanan")
                putExtra(CalendarContract.Events.DESCRIPTION, "Peserta: $nama")
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, mulai.timeInMillis)
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, selesai.timeInMillis)
            }
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Tidak ada aplikasi kalender", Toast.LENGTH_SHORT).show()
            }
        }

        btnKembali.setOnClickListener {
            finish()
        }
    }

    // Validasi: tanggal tidak boleh sebelum hari ini
    private fun tanggalValid(datePicker: DatePicker): Boolean {
        val dipilih = Calendar.getInstance().apply {
            set(datePicker.year, datePicker.month, datePicker.dayOfMonth, 23, 59, 59)
        }
        if (dipilih.before(Calendar.getInstance())) {
            Toast.makeText(this, "Tanggal tidak boleh sebelum hari ini", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun teksTanggal(datePicker: DatePicker): String =
        "%02d/%02d/%d".format(datePicker.dayOfMonth, datePicker.month + 1, datePicker.year)

    private fun teksWaktu(timePicker: TimePicker): String =
        "%02d:%02d".format(timePicker.hour, timePicker.minute)
}
