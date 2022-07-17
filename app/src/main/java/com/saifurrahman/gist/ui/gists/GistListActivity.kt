package com.saifurrahman.gist.ui.gists

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.saifurrahman.gist.api.ApiAdapter
import com.saifurrahman.gist.api.OnGistByUserDataCallback
import com.saifurrahman.gist.api.OnGistListDataCallback
import com.saifurrahman.gist.databinding.ActivityGistListBinding
import com.saifurrahman.gist.db.DatabaseAdapter
import com.saifurrahman.gist.db.OnDatabaseCallback
import com.saifurrahman.gist.model.Gist
import com.saifurrahman.gist.ui.gistDetails.GistDetailActivity

class GistListActivity : AppCompatActivity(), OnDatabaseCallback {
    lateinit var binding: ActivityGistListBinding

    private var gistAdapter: GistAdapter? = null

    private var apiAdapter: ApiAdapter? = null
    private var dataAdapter: DatabaseAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGistListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiAdapter = ApiAdapter()
        dataAdapter = DatabaseAdapter()

        initView()
    }

    override fun onResume() {
        super.onResume()

        dataAdapter?.onDatabaseCallback = this
        if ((gistAdapter?.itemCount?: 0) > 0) {
            dataAdapter?.getGists()
        }
    }

    override fun onGists(gists: List<Gist>) {
        super.onGists(gists)

        gistAdapter?.setData(gists)
    }

    private fun initView() {
        binding.gistRefreshLayout.setOnRefreshListener(onSwipeRefreshLayout)

        gistAdapter = GistAdapter(this)
        gistAdapter?.onGistClickListener = onGistOnGistClickListener
        binding.gistRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.gistRecyclerView.hasFixedSize()
        binding.gistRecyclerView.adapter = gistAdapter

        updateData()
    }

    private fun updateData() {
        binding.gistProgressBar.visibility = View.VISIBLE
        binding.gistTextView.text = ""

        apiAdapter?.getGistList(object : OnGistListDataCallback {
            override fun onData(gists: List<Gist>) {
                binding.gistProgressBar.visibility = View.GONE
                binding.gistRefreshLayout.isRefreshing = false

                gistAdapter?.setData(gists)
            }

            override fun onError(error: String, cachedGist: List<Gist>) {
                binding.gistProgressBar.visibility = View.GONE
                binding.gistRefreshLayout.isRefreshing = false

                if (cachedGist.isNotEmpty()) {
                    gistAdapter?.setData(cachedGist)
                } else {
                    binding.gistTextView.text = error
                }
            }
        })
    }

    private val onSwipeRefreshLayout = SwipeRefreshLayout.OnRefreshListener { updateData() }

    private val onGistOnGistClickListener = object : GistAdapter.OnGistClickListener{
        override fun onGistClick(gist: Gist) {
            val detailsIntent = Intent(this@GistListActivity, GistDetailActivity::class.java)
            detailsIntent.putExtra(GistDetailActivity.GIST_ID, gist.id)
            startActivity(detailsIntent)
        }

        override fun onFavouriteClick(gist: Gist) {
            dataAdapter?.changeFavouriteState(gist)
        }

        override fun onScrolledToItem(gist: Gist) {
            super.onScrolledToItem(gist)

            apiAdapter?.getGistListByUser(gist, object : OnGistByUserDataCallback {
                override fun onData(gist: Gist) {
                    gistAdapter?.updateGist(gist)
                }

                override fun onError(error: String, cached: Gist?) {
                    cached?.let {
                        gistAdapter?.updateGist(it)
                    }
                }
            })
        }

    }
}