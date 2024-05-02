package com.vasilyev.kuickhackjvm.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.vasilyev.kuickhackjvm.model.Document
import com.vasilyev.kuickhackjvm.databinding.FragmentHomeBinding
import com.vasilyev.kuickhackjvm.model.RecentFile
import com.vasilyev.kuickhackjvm.ui.ViewModelFactory
import com.vasilyev.kuickhackjvm.ui.main.MainSharedViewModel


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = requireNotNull(_binding)

    private var bottomSheetCallback: ShowBottomSheetCallBack? = null

    private val viewModel: MainSharedViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ShowBottomSheetCallBack){
            bottomSheetCallback = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        Log.d("ViewModelCheck", viewModel.toString())
        setListeners()
        observeViewModel()

        return binding.root
    }

    private fun setListeners(){
        with(binding){
            cvIdCard.setOnClickListener {
                bottomSheetCallback?.onShow(Document.ID_CARD)
                viewModel.selectDocument(Document.ID_CARD)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.recentFiles.observe(viewLifecycleOwner) {
            stopLoading()
            if(it.size == 1){
                binding.cvRecentDocFirst.visibility = View.VISIBLE
                binding.cvRecentDocSecond.visibility = View.INVISIBLE
                displayFirstDocumentPreview(it[0])
            }else if(it.size > 1){
                binding.cvRecentDocFirst.visibility = View.VISIBLE
                binding.cvRecentDocSecond.visibility = View.VISIBLE
                displayFirstDocumentPreview(it[it.size - 1])
                displaySecondDocumentPreview(it[it.size - 2])
            }else{
                binding.cvRecentDocFirst.visibility = View.INVISIBLE
                binding.cvRecentDocSecond.visibility = View.INVISIBLE
            }
        }

        viewModel.mainSharedState.observe(viewLifecycleOwner){ state ->
            when(state){
                is MainSharedState.Loading ->{
                    showLoading()
                }
            }
        }
    }

    private fun showLoading(){
        binding.cvRecentDocFirst.visibility = View.INVISIBLE
        binding.cvRecentDocSecond.visibility = View.INVISIBLE
        binding.loadingProgressBar.isActivated = true
        binding.loadingProgressBar.visibility = View.VISIBLE
    }

    private fun stopLoading(){
        binding.loadingProgressBar.isActivated = false
        binding.loadingProgressBar.visibility = View.INVISIBLE
    }

    private fun displayFirstDocumentPreview(firstDocument: RecentFile){
        with(binding) {
            val docName = firstDocument.fileName
            if(docName.length >= MAX_DOCUMENT_TEXT_LENGTH){
                tvDocNameFirst.text = "${docName.substring(0, MAX_DOCUMENT_TEXT_LENGTH - 1)}..."
            }else{
                tvDocNameFirst.text = docName
            }
            tvDocDateFirst.text = "Today"
        }
    }

    private fun displaySecondDocumentPreview(secondDocument: RecentFile){
        with(binding) {
            val docName = secondDocument.fileName
            if(docName.length >= MAX_DOCUMENT_TEXT_LENGTH){
                tvDocNameFirst.text = "${docName.substring(0, MAX_DOCUMENT_TEXT_LENGTH - 1)}..."
            }else{
                tvDocNameSecond.text = docName
            }
            tvDocDateSecond.text = "Today"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    interface ShowBottomSheetCallBack{
        fun onShow(documentType: Document)
    }

    companion object{
        fun newInstance() = HomeFragment()
        private const val MAX_DOCUMENT_TEXT_LENGTH = 21
    }
}