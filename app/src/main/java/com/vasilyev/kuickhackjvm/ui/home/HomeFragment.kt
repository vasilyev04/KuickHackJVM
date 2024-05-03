package com.vasilyev.kuickhackjvm.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import com.vasilyev.kuickhackjvm.R
import com.vasilyev.kuickhackjvm.model.Document
import com.vasilyev.kuickhackjvm.databinding.FragmentHomeBinding
import com.vasilyev.kuickhackjvm.model.CheckingResult
import com.vasilyev.kuickhackjvm.ui.main.MainSharedViewModel
import com.vasilyev.kuickhackjvm.ui.result.ActivityResult
import com.vasilyev.kuickhackjvm.utils.base64ToBitmap


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = requireNotNull(_binding)

    private var bottomSheetCallback: ShowBottomSheetCallBack? = null
    private var seeAllFilesClicked: ButtonShowAllFilesCallback? = null

    private val viewModel: MainSharedViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ShowBottomSheetCallBack){
            bottomSheetCallback = context
        }

        if(context is ButtonShowAllFilesCallback){
            seeAllFilesClicked = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel.getUpdatedList()
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

            tvSeeAllFiles.setOnClickListener {
                seeAllFilesClicked?.onClicked()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.recentResults.observe(viewLifecycleOwner) {
            stopLoading()
            if(it.size == 1){
                binding.tvNoRecentFiles.visibility = View.INVISIBLE
                binding.cvRecentDocFirst.visibility = View.VISIBLE
                binding.cvRecentDocSecond.visibility = View.INVISIBLE
                displayFirstDocumentPreview(it[0])
            }else if(it.size > 1){
                binding.tvNoRecentFiles.visibility = View.INVISIBLE
                binding.cvRecentDocFirst.visibility = View.VISIBLE
                binding.cvRecentDocSecond.visibility = View.VISIBLE
                displayFirstDocumentPreview(it[it.size - 1])
                displaySecondDocumentPreview(it[it.size - 2])
            }else{
                binding.cvRecentDocFirst.visibility = View.INVISIBLE
                binding.cvRecentDocSecond.visibility = View.INVISIBLE
                binding.tvNoRecentFiles.visibility = View.VISIBLE
            }
        }

        viewModel.mainSharedState.observe(viewLifecycleOwner){ state ->
            when(state){
                is MainSharedState.Loading -> { showLoading() }
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

    private fun displayFirstDocumentPreview(firstDocument: CheckingResult){
        with(binding) {
            val docName = firstDocument.documentName
            if(docName.length >= MAX_DOCUMENT_TEXT_LENGTH){
                tvDocNameFirst.text = "${docName.substring(0, MAX_DOCUMENT_TEXT_LENGTH - 1)}..."
            }else{
                tvDocNameFirst.text = docName
            }

            btnOptionFirst.setOnClickListener {
                showPopupMenu(it, firstDocument)
            }

            cvRecentDocFirst.setOnClickListener {
                startActivity(ActivityResult.newIntent(requireActivity(), firstDocument.id))
            }

            ivPreviewFirst.setImageBitmap(base64ToBitmap(firstDocument.documentPreview))
            tvDocDateFirst.text = firstDocument.uploadDate
        }
    }

    private fun showPopupMenu(view: View, checkingResult: CheckingResult) {
        val popup = PopupMenu(view.context, view)
        popup.menuInflater.inflate(R.menu.item_context_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            val action = when (item.itemId) {
                R.id.delete -> "delete"
                else -> ""
            }

            if(action.isNotEmpty()){
                viewModel.deleteCheckingResult(checkingResult)
                true
            }else{
                false
            }
        }
        popup.show()
    }

    private fun displaySecondDocumentPreview(secondDocument: CheckingResult){
        with(binding) {
            val docName = secondDocument.documentName
            if(docName.length >= MAX_DOCUMENT_TEXT_LENGTH){
                tvDocNameSecond.text = "${docName.substring(0, MAX_DOCUMENT_TEXT_LENGTH - 1)}..."
            }else{
                tvDocNameSecond.text = docName
            }

            btnOptionSecond.setOnClickListener {
                showPopupMenu(it, secondDocument)
            }

            cvRecentDocSecond.setOnClickListener {
                startActivity(ActivityResult.newIntent(requireActivity(), secondDocument.id))
            }

            ivPreviewSecond.setImageBitmap(base64ToBitmap(secondDocument.documentPreview))
            tvDocDateSecond.text = secondDocument.uploadDate
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    interface ShowBottomSheetCallBack{
        fun onShow(documentType: Document)
    }

    interface ButtonShowAllFilesCallback{
        fun onClicked()
    }

    companion object{
        fun newInstance() = HomeFragment()
        private const val MAX_DOCUMENT_TEXT_LENGTH = 28
    }
}