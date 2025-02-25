package com.example.appeventdicoding.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.appeventdicoding.R
import com.example.appeventdicoding.data.local.entity.EventDicodingEntity
import com.example.appeventdicoding.databinding.ActivityDetailBinding
import com.example.appeventdicoding.ui.viewModelFactory.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private var binding:ActivityDetailBinding? = null

    @SuppressLint("ResourceType", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.setBackgroundDrawable(findViewById(R.color.transparent))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""


        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel:DetailViewModel by viewModels<DetailViewModel> {
            factory
        }

        val eventId = intent.getIntExtra(EVENT_ID, 0)
        Log.d("Detail Activity", "Cek Data EventId ${eventId}")
        if(eventId != 0) {
            viewModel.getDetailEvent(eventId)

            viewModel.eventDetail.observe(this) { data ->

                binding?.namaEvent?.text = data.event.name
                binding?.namaPenyelenggara?.text = data.event.ownerName
                val dataQuota = data.event.quota?.toInt() ?: 0
                val dataRegistrants = data.event.registrants?.toInt() ?: 0

                val sisaQuota = dataQuota - dataRegistrants

                binding?.sesiRegister?.text = "Sisa Kuota: $sisaQuota Ticket"
                binding?.beginTime?.text = "${data.event.beginTime}"
                binding?.descriptionDetail?.text = HtmlCompat.fromHtml(
                    data.event.description.toString(),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )

                binding?.toUrlEventDicoding?.setOnClickListener {
                    val dataUrl = data.event.link
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataUrl))
                    startActivity(intent)
                }
                Glide.with(this)
                    .load(data.event.imageLogo)
                    .into(binding?.imageViewEvent!! )

                var isFavorite = binding?.favoriteBookmark

                isFavorite?.setOnClickListener {
                    val eventDataLocal = EventDicodingEntity(
                        data.event.id ?:0,
                        data.event.endTime ?: "",
                        data.event.name ?: "",
                        data.event.imageLogo ?: "",
                        true
                    )
//                    if (eventDataLocal.isFavorite != null && eventDataLocal.isFavorite) {
//                        viewModel.deteleteFavorite(eventDataLocal.id)
//                        isFavorite.apply {
//                            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.heart_not_full))
//                        }
//                    }

                        viewModel.insertFavorite(eventDataLocal)
                        isFavorite.apply {
                            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.heart))
                        }


                }
            }

            viewModel.isLoading.observe(this) {
                showLoading(it)
            }
        }
    }

    private fun showLoading(isLoading:Boolean) {
        if (isLoading) {
            binding?.progresbar?.visibility = View.VISIBLE
            binding?.progresbar?.visibility = View.GONE
        } else {
            binding?.progresbar?.visibility = View.GONE
            binding?.toUrlEventDicoding?.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EVENT_ID = "event_id"
    }
}