package com.rasel.calculator

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.CATEGORY_BROWSABLE
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
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

    private val dataList = listOf(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        setHomeAdapter()
        setUpScanner()
    }

    private fun setUpScanner() {
        binding.btnScan.setOnClickListener {
            launchDefaultQRScanner()
        }
    }


    private fun setHomeAdapter() {
        binding.rvFeatureList.apply {
            adapter = lessonAdapter
            addItemDecoration(
                AdaptiveSpacingItemDecoration(
                    size = resources.getDimensionPixelSize(R.dimen.dimen_8),
                    edgeEnabled = true
                )
            )
        }
    }

    private val onClick: (ServiceModel) -> Unit = { step ->
        openUrl(step.webUrl)
    }
    private val lessonAdapter = HomeAdapter(onClick).apply {
        submitList(dataList)
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(ACTION_VIEW, Uri.parse(url)).apply {
                // The URL should either launch directly in a non-browser app (if it's
                // the default) or in the disambiguation dialog.
                addCategory(CATEGORY_BROWSABLE)
                flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_REQUIRE_NON_BROWSER
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Only browser apps are available, or a browser is the default.
            // So you can open the URL directly in your app, for example in a
            // Custom Tab.
            openInCustomTabs(url)
        }
    }

    private fun openInCustomTabs(url: String) {

//        val myCustomCloseIcon = getDrawable(R.drawable.ic_baseline_arrow_back_24));


        val customTabsIntent = CustomTabsIntent.Builder().apply {
            setUrlBarHidingEnabled(true)
            // shows the title of web-page in toolbar
            this.setShowTitle(true)
            setBookmarksButtonEnabled(false)
            setDownloadButtonEnabled(false)
//            this.setCloseButtonPosition(CustomTabsIntent.CLOSE_BUTTON_POSITION_END)
//            setCloseButtonIcon(toBitmap(myCustomCloseIcon))


            // setShareState(CustomTabsIntent.SHARE_STATE_ON) will add a menu to share the web-page
            setShareState(CustomTabsIntent.SHARE_STATE_OFF)

            /* setStartAnimations(this@MainActivity, R.anim.slide_in_right, R.anim.slide_out_left)
             setExitAnimations(this@MainActivity, android.R.anim.slide_in_left, android.R.anim.slide_out_right)*/
        }.build()

        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    private fun launchDefaultQRScanner() {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC)
            .enableAutoZoom()
            .build()
        val scanner = GmsBarcodeScanning.getClient(this, options)

        scanner.startScan()
            .addOnSuccessListener { barcode ->
                // Task completed successfully
                barcode.rawValue?.let {
                    openUrl(it)
                }

            }
            .addOnCanceledListener {
                // Task canceled
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
            }
    }
}
