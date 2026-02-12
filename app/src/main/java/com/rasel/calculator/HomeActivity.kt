package com.rasel.calculator

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.CATEGORY_BROWSABLE
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER
import android.net.Uri
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
        return listOf(
            ServiceModel(
                title = "জন্ম নিবন্ধন",
                webUrl = "https://everify.bdris.gov.bd/"
            ),
            ServiceModel(
                title = "ওয়াসা বিল",
                webUrl = "http://app.dwasa.org.bd/index.php?type_name=member&page_name=acc_index&panel_index="
            ),
            ServiceModel(
                title = "NID",
                webUrl = "https://services.nidw.gov.bd/nid-pub/"
            ),
            ServiceModel(
                title = "পাসপোর্ট",
                webUrl = "https://www.epassport.gov.bd/authorization/login"
            ),
            ServiceModel(
                title = "Desco",
                webUrl = "https://prepaid.desco.org.bd/customer/#/customer-info"
            ),
            ServiceModel(
                title = "ড্রাইভিং লাইসেন্স",
                webUrl = "https://bsp.brta.gov.bd/login/"
            ),
            ServiceModel(
                title = "HSC/SSC/Diploma/Dakhil",
                webUrl = "http://www.educationboardresults.gov.bd/result.php"
            )
        )
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
                    size = resources.getDimensionPixelSize(R.dimen.dimen_8),
                    edgeEnabled = true
                )
            )
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(ACTION_VIEW, Uri.parse(url)).apply {
                addCategory(CATEGORY_BROWSABLE)
                flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_REQUIRE_NON_BROWSER
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            openInCustomTabs(url)
        }
    }

    private fun openInCustomTabs(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder().apply {
            setUrlBarHidingEnabled(true)
            setShowTitle(true)
            setBookmarksButtonEnabled(false)
            setDownloadButtonEnabled(false)
            setShareState(CustomTabsIntent.SHARE_STATE_OFF)
        }.build()

        customTabsIntent.launchUrl(this, Uri.parse(url))
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
                } ?: showErrorMessage("Null value received")
            }
            .addOnCanceledListener {
                // Task canceled
            }
            .addOnFailureListener { e ->
                e.message?.let { showErrorMessage(it) }
            }
    }

    private fun isValidUrl(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

    private fun showErrorMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
