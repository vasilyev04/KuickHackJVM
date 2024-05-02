package com.vasilyev.kuickhackjvm.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.vasilyev.kuickhackjvm.model.Document
import com.vasilyev.kuickhackjvm.ui.documents.DocumentsFragment
import com.vasilyev.kuickhackjvm.ui.home.HomeFragment
import com.vasilyev.kuickhackjvm.R
import com.vasilyev.kuickhackjvm.databinding.ActivityMainBinding
import com.vasilyev.kuickhackjvm.databinding.UploadWayBottomSheetBinding
import com.vasilyev.kuickhackjvm.ui.ViewModelFactory
import com.vasilyev.kuickhackjvm.ui.checking.CheckingActivity

class MainActivity : AppCompatActivity(), HomeFragment.ShowBottomSheetCallBack {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = requireNotNull(_binding)

    private val viewModel: MainSharedViewModel by viewModels {
        ViewModelFactory(application)
    }

    private val dialog by lazy {
        BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
    }

    private var selectedDocument = Document.UNDEFINED

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ){ uri ->

        uri?.let {
            viewModel.createRecentFile(it, getDocumentName(selectedDocument))

            startActivity(
                CheckingActivity.newIntent(
                this@MainActivity, selectedDocument, it.toString())
            )
        }

        dialog.cancel()
    }

    private val scannerLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val scanningResult =
                    GmsDocumentScanningResult.fromActivityResultIntent(result.data)

                scanningResult?.pdf?.uri?.let { uri ->
                    viewModel.createRecentFile(uri, getDocumentName(selectedDocument))

                    startActivity(
                        CheckingActivity.newIntent(
                        this@MainActivity, selectedDocument, uri.toString())
                    )
                }
            }

            dialog.cancel()
        }

    private fun getDocumentName(documentType: Document): String{
        return when(documentType){
            Document.ID_CARD -> getString(R.string.id_card)
            Document.BIRTH_DOCUMENT -> getString(R.string.birth_document)
            Document.DRIVER_LICENSE -> getString(R.string.driver_license)
            Document.UNDEFINED -> getString(R.string.unknown)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("ViewModelCheck", viewModel.toString())

        changeFragment(HomeFragment.newInstance())
        setListeners()
        observeDialog()
        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.selectedDocument.observe(this){
            Log.d("ViewModelCheck", it.toString())
            selectedDocument = it
        }
    }

    private fun observeDialog(){
        dialog.setOnDismissListener {

        }

        dialog.setOnShowListener {

        }
    }

    private fun changeFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun launchScanner(pageLimit: Int){
        val options = GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(true)
            .setPageLimit(pageLimit)
            .setResultFormats(
                GmsDocumentScannerOptions.RESULT_FORMAT_JPEG,
                GmsDocumentScannerOptions.RESULT_FORMAT_PDF
            )
            .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
            .build()

        val scanner = GmsDocumentScanning.getClient(options)

        scanner.getStartScanIntent(this).addOnSuccessListener { intentSender ->
            scannerLauncher.launch(
                IntentSenderRequest.Builder(intentSender).build()
            )
        }.addOnFailureListener { exception ->
            Log.d(DEBUG_TAG, "ERROR during Scanning: $exception")
        }
    }

    private fun launchFilePick(){
        filePickerLauncher.launch("application/pdf")
    }

    private fun setListeners(){
        binding.bottomNavigationView.setOnItemSelectedListener {menuItem ->
            when(menuItem.itemId){
                R.id.home -> changeFragment(HomeFragment.newInstance())
                R.id.documents -> changeFragment(DocumentsFragment.newInstance())
            }

            false
        }
    }

    private fun showBottomSheet(documentType: Document) {
        val bindingDialog = UploadWayBottomSheetBinding.inflate(layoutInflater)
        selectedDocument = documentType

        val pageLimit = when(documentType){
            Document.ID_CARD -> 2
            Document.DRIVER_LICENSE -> 2
            Document.BIRTH_DOCUMENT -> 2

            Document.UNDEFINED -> return
        }

        bindingDialog.btnScanDocument.setOnClickListener {
            launchScanner(pageLimit)
        }

        bindingDialog.btnSelectDocument.setOnClickListener {
            launchFilePick()
        }

        bindingDialog.btnClose.setOnClickListener {
            dialog.cancel()
        }

        dialog.setContentView(bindingDialog.root)
        dialog.show()
    }

    companion object{
        private const val DEBUG_TAG = "MAIN_ACTIVITY_DEBUG"
    }

    override fun onShow(documentType: Document) {
        showBottomSheet(documentType)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}