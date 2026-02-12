package com.rasel.calculator

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.rasel.calculator.bl.core.decorator.AdaptiveSpacingItemDecoration
import com.rasel.calculator.bl.data.ServiceModel
import com.rasel.calculator.databinding.ActivityHomeBinding
import com.rasel.calculator.ui.dashboard.HomeAdapter


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val homeAdapter = HomeAdapter { service -> openUrl(service.webUrl) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        setHomeAdapter()
        homeAdapter.submitList(getServiceListData())
        setUpScanner()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left
                bottomMargin = insets.bottom
                topMargin = insets.top
                rightMargin = insets.right
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun getServiceListData(): List<ServiceModel> {
        val titles = resources.getStringArray(R.array.service_titles)
        val urls = resources.getStringArray(R.array.service_urls)
        return titles.zip(urls).map { (title, url) ->
            ServiceModel(title = title, webUrl = url)
        }
    }

    private fun setUpScanner() {
        binding.btnScan.setOnClickListener {
            launchDefaultQRScanner()
        }
    }

    private fun setHomeAdapter() {
        binding.rvFeatureList.apply {
            adapter = homeAdapter
            addItemDecoration(
                AdaptiveSpacingItemDecoration(
                    size = resources.getDimensionPixelSize(R.dimen.grid_spacing),
                    edgeEnabled = true
                )
            )
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                intent.addFlags(Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER)
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            openInCustomTabs(url)
        }
    }

    private fun openInCustomTabs(url: String) {
        CustomTabsIntent.Builder().build().launchUrl(this, Uri.parse(url))
    }

    private fun launchDefaultQRScanner() {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC
            )
            .enableAutoZoom()
            .build()
        val scanner = GmsBarcodeScanning.getClient(this, options)

        scanner.startScan()
            .addOnSuccessListener { barcode ->
                barcode.rawValue?.let {
                    if (isValidUrl(it)) {
                        openUrl(it)
                    } else {
                        showErrorMessage(it)
                    }
                } ?: showErrorMessage(getString(R.string.error_null_qr_value))
            }
            .addOnCanceledListener {
                // Task canceled
            }
            .addOnFailureListener { 
                showErrorMessage(getString(R.string.error_qr_scan_failed))
            }
    }

    private fun isValidUrl(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

    private fun showErrorMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
