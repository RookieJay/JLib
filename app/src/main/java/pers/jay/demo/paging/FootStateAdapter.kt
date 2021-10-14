package pers.jay.demo.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import pers.jay.demo.databinding.LayoutRvRetryFooterBinding

class FootStateAdapter(val retry: () -> Unit): LoadStateAdapter<RetryViewHolder>() {

    override fun onBindViewHolder(holder: RetryViewHolder, loadState: LoadState) {
        holder.binding.apply {
            btRetry.isVisible = loadState is LoadState.Error
            progressBar.isVisible = loadState is LoadState.Loading
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): RetryViewHolder {
        val binding = LayoutRvRetryFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.btRetry.setOnClickListener {
            retry()
        }
        return RetryViewHolder(binding)
    }
}

class RetryViewHolder(val binding: LayoutRvRetryFooterBinding) : RecyclerView.ViewHolder(binding.root) {

}
