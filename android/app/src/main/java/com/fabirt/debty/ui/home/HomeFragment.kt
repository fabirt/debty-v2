package com.fabirt.debty.ui.home

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.fabirt.debty.NavGraphDirections
import com.fabirt.debty.R
import com.fabirt.debty.constant.K
import com.fabirt.debty.databinding.FragmentHomeBinding
import com.fabirt.debty.domain.model.FinancialTransferMode
import com.fabirt.debty.ui.MainActivity
import com.fabirt.debty.ui.assistant.AssistantViewModel
import com.fabirt.debty.ui.chart.ChartFragment
import com.fabirt.debty.ui.common.showSnackBar
import com.fabirt.debty.ui.common.showSnackBarWithAction
import com.fabirt.debty.ui.people.home.PeopleFragment
import com.fabirt.debty.ui.summary.SummaryFragment
import com.fabirt.debty.util.sendUpdateAppWidgetBroadcast
import com.fabirt.debty.util.toCurrencyString
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: HomePagerAdapter
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private lateinit var createFileLauncher: ActivityResultLauncher<String>
    private lateinit var openFileLauncher: ActivityResultLauncher<Array<String>>
    private val viewModel: HomeViewModel by viewModels()
    private val assistantViewModel: AssistantViewModel by activityViewModels()
    private val backupViewModel: BackupViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            _binding?.tabLayout?.getTabAt(0)?.select()
        }

        onBackPressedCallback.isEnabled = false
        listenMovementChanges()
        listenBackupEvents()

        createFileLauncher =
            registerForActivityResult(ActivityResultContracts.CreateDocument()) { uri ->
                uri?.let { exportDatabase(it) }
            }

        openFileLauncher =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                uri?.let { importDatabase(it) }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val children = listOf(
            SummaryFragment(), PeopleFragment(), ChartFragment()
        )
        pagerAdapter = HomePagerAdapter(this, children)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener { navigateToCreateMovement() }

        binding.pager.apply {
            adapter = pagerAdapter
            (getChildAt(0) as? RecyclerView)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.tag = position
            tab.text = when (position) {
                0 -> getString(R.string.summary)
                1 -> getString(R.string.people)
                2 -> getString(R.string.chart)
                else -> ""
            }
        }.attach()

        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            binding.drawerLayout.close()

            when (menuItem.itemId) {
                R.id.item_export -> {
                    createFileLauncher.launch(K.DATABASE_NAME)
                }
                R.id.item_import -> {
                    val mimeTypes = arrayOf(
                        "application/x-sqlite3",
                        "application/vnd.sqlite3",
                        "application/octet-stream",
                        "application/x-trash"
                    )
                    openFileLauncher.launch(mimeTypes)
                }
                else -> Unit
            }

            false
        }

        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                onBackPressedCallback.isEnabled = position != 0

                val fabDrawable: Drawable?
                val fabClickAction: () -> Unit
                when (position) {
                    0 -> {
                        fabDrawable = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_round_attach_money_24
                        )
                        fabClickAction = ::navigateToCreateMovement
                    }
                    1 -> {
                        fabDrawable = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_round_person_add_24
                        )
                        fabClickAction = ::navigateToCreatePerson
                    }
                    else -> {
                        fabDrawable = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_round_attach_money_24
                        )
                        fabClickAction = ::navigateToCreateMovement
                    }
                }

                binding.fab.setImageDrawable(fabDrawable)
                binding.fab.setOnClickListener { fabClickAction() }
            }
        })

        listenToAssistantEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToCreatePerson() {
        val action = NavGraphDirections.actionGlobalCreatePersonFragment()
        findNavController().navigate(action)
    }

    private fun navigateToCreateMovement() {
        val action = HomeFragmentDirections.actionHomeToPersonSearch()
        findNavController().navigate(action)
    }

    private fun listenMovementChanges() {
        lifecycleScope.launch {
            viewModel.movements
                .catch { }
                .collect { sendUpdateAppWidgetBroadcast() }
        }
    }

    private fun listenToAssistantEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            assistantViewModel.eventFlow.collect { event ->
                when (event) {
                    is AssistantViewModel.Event.AssistantEvent -> {
                        val message = when (event.transferMode) {
                            FinancialTransferMode.ReceiveMoney -> getString(
                                R.string.money_transfer_action_receive,
                                event.transferAmount.toCurrencyString(),
                                event.transferDestinationName
                            )
                            FinancialTransferMode.SendMoney -> getString(
                                R.string.money_transfer_action_send,
                                event.transferAmount.toCurrencyString(),
                                event.transferDestinationName
                            )
                            FinancialTransferMode.AddMoney -> getString(
                                R.string.money_transfer_action_add,
                                event.transferAmount.toCurrencyString(),
                                event.transferDestinationName
                            )
                        }
                        showSnackBar(message, binding.contextView, binding.fab)
                    }
                }
            }
        }
    }

    private fun listenBackupEvents() {
        lifecycleScope.launch {
            backupViewModel.eventFlow.collect { event ->
                when (event) {
                    BackupEvent.DatabaseExported -> {
                        showSnackBarWithAction(
                            getString(R.string.save_to_storage_success),
                            binding.contextView,
                            getString(R.string.restart)
                        ) {
                            restartActivity()
                        }
                    }
                    BackupEvent.DatabaseImported -> {
                        showSnackBarWithAction(
                            getString(R.string.import_success),
                            binding.contextView,
                            getString(R.string.restart)
                        ) {
                            restartActivity()
                        }
                    }
                }
            }
        }
    }

    private fun exportDatabase(uri: Uri) {
        backupViewModel.exportDatabase(
            requireContext().getDatabasePath(K.DATABASE_NAME),
            uri,
            requireContext().contentResolver
        )
    }

    private fun importDatabase(uri: Uri) {
        backupViewModel.importDatabase(
            requireContext().getDatabasePath(K.DATABASE_NAME),
            uri,
            requireContext().contentResolver
        )
    }

    private fun restartActivity() {
        val activity = requireActivity()
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        activity.finish()
        startActivity(intent)
    }
}



