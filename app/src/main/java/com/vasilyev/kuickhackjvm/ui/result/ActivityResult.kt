package com.vasilyev.kuickhackjvm.ui.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vasilyev.kuickhackjvm.R
import com.vasilyev.kuickhackjvm.databinding.ActivityResultBinding
import com.vasilyev.kuickhackjvm.model.CheckStatus
import com.vasilyev.kuickhackjvm.ui.ViewModelFactory

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
    }

    private fun observeViewModel(){
        viewModel.result.observe(this){
            with(binding){
                when(it.checkStatus){
                    CheckStatus.SUCCESS -> {
                        tvCheckResult.text = getString(R.string.check_status_success)
                        ivCheckResultStatus.setImageDrawable(getDrawable(R.drawable.success))
                    }
                    CheckStatus.WARNING -> {
                        tvCheckResult.text = getString(R.string.check_status_warning)
                        ivCheckResultStatus.setImageDrawable(getDrawable(R.drawable.warning))
                    }
                    CheckStatus.ERROR -> {
                        tvCheckResult.text = getString(R.string.check_status_error)
                        ivCheckResultStatus.setImageDrawable(getDrawable(R.drawable.error))
                    }
                }
            }
        }
    }
    companion object{
        fun newIntent(context: Context) = Intent(context, ActivityResult::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}