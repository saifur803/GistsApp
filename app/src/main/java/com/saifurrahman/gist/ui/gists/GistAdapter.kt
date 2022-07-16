package com.saifurrahman.gist.ui.gists

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saifurrahman.gist.R
import com.saifurrahman.gist.databinding.ViewholderGistBinding
import com.saifurrahman.gist.model.Gist

class GistAdapter(private val context: Context) : RecyclerView.Adapter<GistAdapter.ViewHolder>() {
    var gistList = ArrayList<Gist>()
    var onGistClickListener: OnGistClickListener? = null

    fun updateGist(gist: Gist) {
        val pos = gistList.indexOfFirst { it.id == gist.id }
        if (pos == -1) {
            return
        }

        gistList[pos] = gist
        notifyItemChanged(pos)
    }

    fun clear() {
        gistList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.viewholder_gist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gist = gistList[position]

        if (!gist.gistByUserIsLoading) {
            onGistClickListener?.onScrolledToItem(gist)
            gist.gistByUserIsLoading = true
        }

        holder.binding.gistFilenameTv.text = gist.filename
        holder.binding.gistUrlTv.text = context.getString(R.string.url, gist.url)

        if (gist.gistCount >= 5) {
            holder.binding.gistSharedView.visibility = View.VISIBLE
            holder.binding.gistUsernameTv.text = context.getString(R.string.user, gist.username)
            holder.binding.gistCountTv.text = context.getString(R.string.count, gist.gistCount.toString())
        } else {
            holder.binding.gistSharedView.visibility = View.GONE
        }

        holder.binding.gistFavouriteBtn.isSelected = gist.isFavourite

        holder.itemView.setOnClickListener {
            onGistClickListener?.onGistClick(gist)
        }
        holder.binding.gistFavouriteBtn.setOnClickListener {
            holder.binding.gistFavouriteBtn.isSelected = ! holder.binding.gistFavouriteBtn.isSelected
            gist.isFavourite = holder.binding.gistFavouriteBtn.isSelected
            onGistClickListener?.onFavouriteClick(gist)
        }
    }

    override fun getItemCount(): Int {
        return gistList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ViewholderGistBinding.bind(itemView)
    }

    interface OnGistClickListener {
        fun onGistClick(gist: Gist)
        fun onFavouriteClick(gist: Gist)

        fun onScrolledToItem(gist: Gist) {}
    }
}