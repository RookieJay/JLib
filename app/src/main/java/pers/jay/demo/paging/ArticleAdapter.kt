package pers.jay.demo.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pers.jay.demo.data.Article
import pers.jay.demo.databinding.LayoutItemArticleBinding
import pers.jay.library.base.ext.showToast


class ArticleAdapter : PagingDataAdapter<Article, ArticleAdapter.ArticleHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val article = getItem(position)
        holder.binding.apply {
            article?.apply {
                tvTitle.text = title
                tvDesc.text = niceDate
            }
        }
        holder.itemView.setOnClickListener {
            article?.title?.let { it1 -> showToast(it1 + holder.itemView.layoutParams.width) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val binding =
            LayoutItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleHolder(binding)
    }

    class ArticleHolder(rootView: LayoutItemArticleBinding) :
        RecyclerView.ViewHolder(rootView.root) {

        val binding = rootView
    }


}