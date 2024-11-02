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
import com.rasel.calculator.bl.core.decorator.AdaptiveSpacingItemDecoration
import com.rasel.calculator.bl.data.ServiceModel
import com.rasel.calculator.databinding.ActivityHomeBinding
import com.rasel.calculator.ui.dashboard.HomeAdapter


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    val dataList = listOf(
        ServiceModel(
            title = "জন্ম নিবন্ধন",
            imageUrl = R.drawable.ic_launcher_background,
            webUrl = "https://everify.bdris.gov.bd/"
        ),
        ServiceModel(
            title = "ওয়াসা বিল",
            imageUrl = R.drawable.ic_launcher_background,
            webUrl = "http://app.dwasa.org.bd/index.php?type_name=member&page_name=acc_index&panel_index="
        ),
        ServiceModel(
            title = "NID",
            imageUrl = R.drawable.ic_launcher_background,
            webUrl = "https://services.nidw.gov.bd/nid-pub/"
        ),
        ServiceModel(
            title = "পাসপোর্ট",
            imageUrl = R.drawable.ic_launcher_background,
            webUrl = "https://www.epassport.gov.bd/authorization/login"
        ),
        ServiceModel(
            title = "Desco",
            imageUrl = R.drawable.ic_launcher_background,
            webUrl = "https://prepaid.desco.org.bd/customer/#/customer-info"
        ),
        ServiceModel(
            title = "ড্রাইভিং লাইসেন্স",
            imageUrl = R.drawable.ic_launcher_background,
            webUrl = "https://bsp.brta.gov.bd/login/"
        ),
        ServiceModel(
            title = "HSC/SSC/Diploma/Dakhil",
            imageUrl = R.drawable.ic_launcher_background,
            webUrl = "http://www.educationboardresults.gov.bd/result.php"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        setHomeAdapter()
    }

    private fun setHomeAdapter() {
        binding.rv.apply {
            adapter = lessonAdapter
            addItemDecoration(
                AdaptiveSpacingItemDecoration(
                    size = resources.getDimensionPixelSize(R.dimen.dimen_16),
                    edgeEnabled = false
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
            this.setShowTitle(true)
            setBookmarksButtonEnabled(false)
            setDownloadButtonEnabled(false)
//            this.setCloseButtonPosition(CustomTabsIntent.CLOSE_BUTTON_POSITION_END)
//            setCloseButtonIcon(toBitmap(myCustomCloseIcon))

            /* setStartAnimations(this@MainActivity, R.anim.slide_in_right, R.anim.slide_out_left)
             setExitAnimations(this@MainActivity, android.R.anim.slide_in_left, android.R.anim.slide_out_right)*/
        }.build()

        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    private fun openInCustomTabs2(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        CustomTabActivityHelper.openCustomTab(
            this, customTabsIntent, Uri.parse(url), WebviewFallback()
        )
    }


}
