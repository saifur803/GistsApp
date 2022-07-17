package com.saifurrahman.gist.ui.gistDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.saifurrahman.gist.R
import com.saifurrahman.gist.api.ApiAdapter
import com.saifurrahman.gist.databinding.ActivityGistDetailBinding
import com.saifurrahman.gist.db.DatabaseAdapter
import com.saifurrahman.gist.db.OnDatabaseCallback
import com.saifurrahman.gist.model.Gist
import com.saifurrahman.gist.util.Utils

class GistDetailActivity : AppCompatActivity(), OnDatabaseCallback {
    companion object {
        const val GIST_ID = "gist-id"
    }

    lateinit var binding: ActivityGistDetailBinding

    private var dataAdapter = DatabaseAdapter()
    private var gist: Gist? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGistDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(GIST_ID)?: run {
            finish()
            return
        }

        dataAdapter.onDatabaseCallback = this
        dataAdapter.getGist(id)
    }

    override fun onGist(gist: Gist) {
        super.onGist(gist)
        this.gist = gist
        initView()
    }

    private fun initView() {
        binding.gistFavouriteBtn.setOnClickListener(onClickListener)

        binding.gistFavouriteBtn.isSelected = gist!!.isFavourite
        binding.detailsGistFilenameTv.text = gist!!.filename
        binding.detailsGistUrlTv.text = getString(R.string.url, gist!!.url)

        gist?.createdAt?.let {
            binding.detailsCreatedTv.text = getString(
                R.string.created_at,
                Utils.getDesiredDateFormat(it, "dd MMM yyyy, hh:mm a")
            )
        }
        gist?.updatedAt?.let {
            binding.detailsUpdatedTv.text = getString(
                R.string.updated_at,
                Utils.getDesiredDateFormat(it, "dd MMM yyyy, hh:mm a")
            )
        }
        binding.detailsDescTv.text = gist?.description?: ""

        binding.gistUsernameTv.text = gist!!.username
        binding.gistCountTv.text = getString(R.string.count, gist!!.gistCount.toString())

        gist?.userImageUrl?.let {
            Glide.with(this)
                .load(it)
                .into(binding.detailsUserImageView)
        }
    }

    private val onClickListener = View.OnClickListener { p0 ->
        if (p0?.id == binding.gistFavouriteBtn.id) {
            binding.gistFavouriteBtn.isSelected = !binding.gistFavouriteBtn.isSelected
            gist?.isFavourite = binding.gistFavouriteBtn.isSelected

            dataAdapter.changeFavouriteState(gist!!)
        }
    }
}