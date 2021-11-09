package com.example.cupcake

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentSummaryBinding
import com.example.cupcake.model.OrderViewModel

class SummaryFragment : Fragment() {

    private val sharedViewModel : OrderViewModel by activityViewModels()

    private var binding: FragmentSummaryBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentSummaryBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            summaryFragment = this@SummaryFragment
        }
    }

    fun sendOrder() {
        val numberOfCupcakes = sharedViewModel.quantity.value ?: 0
        val orderSummary = getString(R.string.order_details,
            resources.getQuantityString(R.plurals.cupcakes, numberOfCupcakes, numberOfCupcakes),
            sharedViewModel.flavor.value,
            sharedViewModel.date.value,
            sharedViewModel.price.value.toString())

        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.new_cupcake_order))
            .putExtra(Intent.EXTRA_TEXT, orderSummary)
            .putExtra(Intent.EXTRA_EMAIL, getString(R.string.cupcake_shop_email))
        if(activity?.packageManager?.resolveActivity(intent, 0) != null){
            startActivity(intent)
        }
    }

    fun cancelOrder() {
        sharedViewModel.resetOrder()
        val action = SummaryFragmentDirections.actionSummaryFragmentToStartFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}