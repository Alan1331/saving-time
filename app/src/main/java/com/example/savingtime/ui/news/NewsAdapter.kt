package com.example.savingtime.ui.news

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.savingtime.databinding.ListNewsBinding
import com.example.savingtime.model.News

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val data = mutableListOf<News>()

    fun updateData(newData: List<News>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ListNewsBinding,
        private val packageManager: PackageManager
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(news: News) = with(binding) {
            // set and load the poster
            val posterUrl = news.poster
            Glide.with(poster.context).load(posterUrl).into(poster)

            // set news title
            newsTitle.text = news.judul

            // bind link of the news into the news poster
            root.setOnClickListener {
                val webpage: Uri = Uri.parse(news.link)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                try {
                    root.context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(root.context, "Link berita gagal dibuka", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListNewsBinding.inflate(inflater, parent, false)
        val packageManager = parent.context.packageManager
        return ViewHolder(binding, packageManager)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
}