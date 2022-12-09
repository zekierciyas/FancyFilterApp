package com.zekierciyas.fancyfilterapp.ui.filter_selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.zekierciyas.fancyfilterapp.R
import com.zekierciyas.fancyfilterapp.adapter.EffectSelectionAdapter
import com.zekierciyas.fancyfilterapp.databinding.FragmentFilterSelectorBinding
import com.zekierciyas.fancyfilterapp.di.LibraryModule
import com.zekierciyas.fancyfilterapp.model.SelectableEffects
import com.zekierciyas.fancyfilterapp.repository.FilterProcessorImp
import com.zekierciyas.fancyfilterapp.ui.FilterSelectorEvent
import com.zekierciyas.fancyfilterapp.ui.FilterSelectorUiState
import com.zekierciyas.fancyfilterapp.ui.common.activityViewModelBuilder
import com.zekierciyas.fancyfilterapp.util.*
import com.zekierciyas.fancyfilterlib.FancyFilter
import timber.log.Timber


class FilterSelectorFragment: Fragment(R.layout.fragment_filter_selector), EffectSelectionAdapter.ItemClickListener {

    private var _binding: FragmentFilterSelectorBinding? = null
    private val binding get() = _binding!!
    private var adapter: EffectSelectionAdapter? = null
    private val args: FilterSelectorFragmentArgs by navArgs()
    private val viewModel: FilterSelectorViewModel by activityViewModelBuilder {
        FilterSelectorViewModel(filterRepository = LibraryModule.filterProcessorImp)
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

        viewModel.uiState.observe(viewLifecycleOwner) {
            when(it) {
                is FilterSelectorUiState.OnSuccess ->{

                    when(it.event) {
                        is FilterSelectorEvent.FilteredBitmap -> {
                            Timber.i("Filtered bitmap is ready")
                            requireActivity().runOnUiThread {
                                binding.filteredImageView.setImageBitmap(it.event.filteredBitmap)
                            }
                        }

                        is FilterSelectorEvent.ListOfFilters -> {
                            Timber.i("Lis of filters to show on screen is ready")
                            requireActivity().runOnUiThread {
                                setupRecyclerView(it.event.listOfFilters)
                            }
                        }

                        is FilterSelectorEvent.AppliedFilterSaved -> {
                            Timber.i("Applied filter is saved")
                            requireActivity().runOnUiThread {
                                binding.progressLoader.hide()
                                binding.applyFiltering.enableClickable()
                            }
                            Toast.makeText(requireContext(), "Image is saved", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                is FilterSelectorUiState.OnError -> {
                    Timber.i("UI state is on error")
                    Toast.makeText(requireContext(), "Error: ${it.errorMessage}", Toast.LENGTH_LONG).show()
                }

                is FilterSelectorUiState.Loading -> {
                    Timber.i("UI state is loading")
                    requireActivity().runOnUiThread {
                        binding.progressLoader.show()
                        binding.applyFiltering.disableClickable()
                    }
                }
                else -> {

                }
            }
        }

        binding.closeFiltering.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.applyFiltering.setOnClickListener {
            viewModel.saveImage(requireContext())
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