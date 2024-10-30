package com.example.rasel.calculator

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.CATEGORY_BROWSABLE
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import com.example.rasel.calculator.databinding.ActivityDashboardBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        binding.btnBirthCertificate.setOnClickListener {
            openUrl("https://everify.bdris.gov.bd/")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {  //saving the value when screen is retated
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val temp = savedInstanceState.getString("screenValue")
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
