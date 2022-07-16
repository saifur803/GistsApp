package com.saifurrahman.gist.ui.gists

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.saifurrahman.gist.api.ApiAdapter
import com.saifurrahman.gist.api.OnGistListDataCallback
import com.saifurrahman.gist.databinding.ActivityGistListBinding
import com.saifurrahman.gist.db.DatabaseAdapter
import com.saifurrahman.gist.model.Gist

class GistListActivity : AppCompatActivity() {
    lateinit var binding: ActivityGistListBinding

    private var gistAdapter: GistAdapter? = null

    private var apiAdapter: ApiAdapter? = null
    private var dataAdapter: DatabaseAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGistListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiAdapter = ApiAdapter()
        dataAdapter = DatabaseAdapter(this)

        initView()
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

                gistAdapter?.gistList = gists as ArrayList<Gist>
                gistAdapter?.notifyDataSetChanged()
            }

            override fun onError(error: String) {
                gistAdapter?.clear()

                binding.gistProgressBar.visibility = View.GONE
                binding.gistRefreshLayout.isRefreshing = false

                binding.gistTextView.text = error
            }

        })
    }

    private val onSwipeRefreshLayout = SwipeRefreshLayout.OnRefreshListener { updateData() }

    private val onGistOnGistClickListener = object : GistAdapter.OnGistClickListener{
        override fun onGistClick(gist: Gist) {

        }

        override fun onFavouriteClick(gist: Gist) {
            dataAdapter?.changeFavouriteState(gist)
        }

        override fun onScrolledToItem(gist: Gist) {
            super.onScrolledToItem(gist)

            apiAdapter?.getGistListByUser(gist.username, object : OnGistListDataCallback {
                override fun onData(gists: List<Gist>) {
                    gist.gistCount = gists.size

                    gistAdapter?.updateGist(gist)
                }

                override fun onError(error: String) {

                }

            })
        }

    }
}