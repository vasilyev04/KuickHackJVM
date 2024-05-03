package com.vasilyev.kuickhackjvm.ui.documents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.vasilyev.kuickhackjvm.R
import com.vasilyev.kuickhackjvm.databinding.FragmentDocumentsBinding
import com.vasilyev.kuickhackjvm.model.CheckingResult
import com.vasilyev.kuickhackjvm.ui.ViewModelFactory
import com.vasilyev.kuickhackjvm.ui.documents.adapter.DocumentsListAdapter
import com.vasilyev.kuickhackjvm.ui.result.ActivityResult


class DocumentsFragment : Fragment(), DocumentsListAdapter.MenuActionListener {
    private var _binding: FragmentDocumentsBinding? = null
    private val binding: FragmentDocumentsBinding
        get() = requireNotNull(_binding)

    private val viewModel: DocumentsViewModel by viewModels{
        ViewModelFactory(requireActivity().application)
    }

    private val adapter: DocumentsListAdapter by lazy {
        DocumentsListAdapter(this)
    }

    private var resultList: List<CheckingResult> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDocumentsBinding.inflate(inflater, container, false)

        attachRecyclerView()
        observeViewModel()
        setupSwipeCallback()
        setListeners()
        return binding.root
    }


    private fun setListeners(){
        binding.tilSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                filter(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText ?: "")
                return true
            }

        })
    }

    private fun filter(text: String){
        if(text.isBlank()){
            adapter.submitList(resultList.reversed())
            return
        }

        val filteredList = mutableListOf<CheckingResult>()

        for(item in resultList){
            if(item.documentName.lowercase().contains(text.lowercase())){
                filteredList.add(item)
            }
        }

        adapter.submitList(filteredList.reversed())
    }

    private fun setupSwipeCallback(){
        val callback = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteRecentCheckingResult(item.id)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }
    private fun observeViewModel(){
        viewModel.resultsList.observe(viewLifecycleOwner){
            adapter.submitList(it.reversed())
            resultList = it

            binding.tvNoRecentFiles.visibility = if(it.isEmpty()){
                View.VISIBLE
            }else{
                View.INVISIBLE
            }
        }
    }

    private fun attachRecyclerView(){
        binding.recyclerView.adapter = adapter
        adapter.onClickListener = { resultId ->
            startActivity(ActivityResult.newIntent(requireActivity(), resultId))
        }
        registerForContextMenu(binding.recyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = DocumentsFragment()
    }

    override fun onMenuItemClick(position: Int, action: String) {
        viewModel.deleteRecentCheckingResult(adapter.currentList[position].id)
    }
}