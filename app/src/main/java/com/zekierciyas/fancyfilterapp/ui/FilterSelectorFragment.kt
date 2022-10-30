package com.zekierciyas.fancyfilterapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.zekierciyas.fancyfilterapp.R
import com.zekierciyas.fancyfilterapp.adapter.EffectSelectionAdapter
import com.zekierciyas.fancyfilterapp.databinding.FragmentFilterSelectorBinding
import com.zekierciyas.fancyfilterapp.model.SelectableEffects
import com.zekierciyas.fancyfilterapp.repository.FilterProcessorImp
import com.zekierciyas.fancyfilterapp.ui.common.activityViewModelBuilder
import com.zekierciyas.fancyfilterlib.FancyFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FilterSelectorFragment: Fragment(R.layout.fragment_filter_selector), EffectSelectionAdapter.ItemClickListener {

    private var _binding: FragmentFilterSelectorBinding? = null
    private val binding get() = _binding!!
    private var adapter: EffectSelectionAdapter? = null
    private val args: FilterSelectorFragmentArgs by navArgs()
    private val viewModel: FilterSelectorViewModel by activityViewModelBuilder {
        FilterSelectorViewModel(filterRepository = FilterProcessorImp(
            fancyFilter = FancyFilter
            .Builder()
            .withContext(requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterSelectorBinding.inflate(inflater, container, false)
        binding.arg = args.arg
        viewModel.applySelectableFilters(args.arg.uri, requireContext())
        return binding.root
    }

    private fun setupRecyclerView(listOfEffects: List<SelectableEffects> ) {
        val horizontalLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.layoutManager = horizontalLayoutManager
        adapter = EffectSelectionAdapter(requireContext(), listOfEffects)
        adapter!!.setClickListener(this)
        binding.recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.listOfFilters.observe(viewLifecycleOwner) {
                requireActivity().runOnUiThread {
                    setupRecyclerView(it)
                }
        }

        viewModel.filteredBitmap.observe(viewLifecycleOwner) {
            requireActivity().runOnUiThread {
                binding.filteredImageView.setImageBitmap(it)
            }
        }
    }

    override fun onItemClick(view: View?, position: Int) {
        viewModel.applySelectedFilter(position)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}