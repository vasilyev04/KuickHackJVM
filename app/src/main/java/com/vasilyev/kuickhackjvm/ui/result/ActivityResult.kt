package com.vasilyev.kuickhackjvm.ui.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.vasilyev.kuickhackjvm.R
import com.vasilyev.kuickhackjvm.databinding.ActivityResultBinding
import com.vasilyev.kuickhackjvm.model.CheckStatus
import com.vasilyev.kuickhackjvm.ui.ViewModelFactory
import com.vasilyev.kuickhackjvm.utils.base64ToBitmap

class ActivityResult : AppCompatActivity() {
    private var _binding: ActivityResultBinding? = null
    private val binding: ActivityResultBinding
        get() = requireNotNull(_binding)

    private val viewModel: ActivityResultViewModel by viewModels(){
        ViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()
        parseIntent()
        setListeners()
    }

    private fun setListeners(){
        binding.close.setOnClickListener {
            finish()
        }

        binding.btnComplete.setOnClickListener {
            finish()
        }
    }
    private fun observeViewModel(){
        viewModel.result.observe(this){
            with(binding){
                tvDocumentName.text = it.documentName
                ivResultDocument.setImageBitmap(base64ToBitmap(it.documentPreview))
                when(it.checkStatus){
                    CheckStatus.SUCCESS -> {
                        tvCheckResult.text = getString(R.string.check_status_success)
                        tvCheckResult.setTextColor(getColor(R.color.success))
                        ivCheckResultStatus.setImageDrawable(getDrawable(R.drawable.success))
                    }
                    CheckStatus.WARNING -> {
                        tvCheckResult.text = getString(R.string.check_status_warning)
                        tvCheckResult.setTextColor(getColor(R.color.warning))
                        ivCheckResultStatus.setImageDrawable(getDrawable(R.drawable.warning))
                    }
                    CheckStatus.ERROR -> {
                        tvCheckResult.setTextColor(getColor(R.color.error))
                        tvCheckResult.text = getString(R.string.check_status_error)
                        ivCheckResultStatus.setImageDrawable(getDrawable(R.drawable.error))
                    }
                }
            }
        }
    }

    private fun parseIntent(){
        if(!intent.hasExtra(EXTRA_RESULT_ID)) throw RuntimeException("EXTRA_RESULT_ID is absent")

        val id = intent.getIntExtra(EXTRA_RESULT_ID, 1)
        viewModel.getResult(id)
    }

    companion object{
        private const val EXTRA_RESULT_ID = "result_id"
        fun newIntent(context: Context, resultId: Int): Intent{
            return Intent(context, ActivityResult::class.java).apply {
                putExtra(EXTRA_RESULT_ID, resultId)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}