package com.vasilyev.kuickhackjvm.ui.checking

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.vasilyev.kuickhackjvm.R
import com.vasilyev.kuickhackjvm.databinding.ActivityCheckingBinding
import com.vasilyev.kuickhackjvm.model.Document
import com.vasilyev.kuickhackjvm.ui.ViewModelFactory
import com.vasilyev.kuickhackjvm.ui.result.ActivityResult
import com.vasilyev.kuickhackjvm.utils.uriToFile

class CheckingActivity: AppCompatActivity() {
    private var _binding: ActivityCheckingBinding? = null
    private val binding: ActivityCheckingBinding
        get() = requireNotNull(_binding)

    private val viewModel: CheckingViewModel by viewModels{
        ViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCheckingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        parseIntent()
        setListeners()
        observeState()
    }

    private fun observeState(){
        viewModel.checkingState.observe(this){ state ->
            when(state){
                is CheckingState.Loading -> {}
                is CheckingState.CheckingCompleted -> {
                    startActivity(ActivityResult.newIntent(this, state.id))
                    finish()
                }
            }
        }
    }

    private fun setListeners(){
        binding.close.setOnClickListener {
            finish()
        }
    }

    private fun parseIntent(){
        if(!intent.hasExtra(EXTRA_FILE_URI)) throw RuntimeException("EXTRA_FILE_URI is absent")
        if(!intent.hasExtra(EXTRA_DOCUMENT_TYPE)) throw RuntimeException("EXTRA_DOCUMENT_TYPE is absent")

        val documentName = getDocumentName(intent.getSerializableExtra(EXTRA_DOCUMENT_TYPE) as Document)

        val uri = Uri.parse(intent.getStringExtra(EXTRA_FILE_URI))
        viewModel.checkFile(uriToFile(this, uri, documentName), documentName)
    }

    private fun getDocumentName(documentType: Document): String{
        return when(documentType){
            Document.ID_CARD -> getString(R.string.id_card)
            Document.BIRTH_DOCUMENT -> getString(R.string.birth_document)
            Document.DRIVER_LICENSE -> getString(R.string.driver_license)
            Document.UNDEFINED -> getString(R.string.unknown)
        }
    }

    companion object {
        private const val EXTRA_DOCUMENT_TYPE = "document_type"
        private const val EXTRA_FILE_URI = "file_uri"

        fun newIntent(context: Context, documentType: Document, uri: String): Intent {
            return Intent(context, CheckingActivity::class.java).apply {
                putExtra(EXTRA_DOCUMENT_TYPE, documentType)
                putExtra(EXTRA_FILE_URI, uri)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}