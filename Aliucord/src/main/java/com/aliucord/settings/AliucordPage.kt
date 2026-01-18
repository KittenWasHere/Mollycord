/*
 * This file is part of Mollycord, an Android Discord client mod.
 * Based on Aliucord by Juby210 & Vendicated
 * Modified by Bubblegum @bubblegum4fun
 * Licensed under the Open Software License version 3.0
 */

package com.aliucord.settings

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.aliucord.*
import com.aliucord.fragments.SettingsPage
import com.discord.stores.StoreStream
import com.discord.views.CheckedSetting
import com.lytefast.flexinput.R

// These keys aren't consistent because they were originally part of different modules
const val AUTO_DISABLE_ON_CRASH_KEY = "autoDisableCrashingPlugins"
const val AUTO_UPDATE_PLUGINS_KEY = "AC_plugins_auto_update_enabled"
const val AUTO_UPDATE_ALIUCORD_KEY = "AC_aliucord_auto_update_enabled"
const val ALIUCORD_SAFE_MODE_KEY = "AC_aliucord_safe_mode_enabled"
const val ALIUCORD_FROM_STORAGE_KEY = "AC_from_storage"

class AliucordPage : SettingsPage() {
    override fun onViewBound(view: View) {
        super.onViewBound(view)

        setActionBarTitle("Mollycord")
        setActionBarSubtitle("Mollycord Settings")

        val ctx = view.context

        addHeader(ctx, "Mollycord Settings")
        addSwitch(ctx, AUTO_UPDATE_ALIUCORD_KEY, "Automatically update Mollycord", null)
        addSwitch(ctx, AUTO_UPDATE_PLUGINS_KEY, "Automatically update plugins", null)
        addSwitch(ctx,
            AUTO_DISABLE_ON_CRASH_KEY,
            "Automatically disable plugins on crash",
            "When a plugin is found to be causing crashes, it will automatically be disabled",
            true
        )
        addSwitch(ctx, ALIUCORD_SAFE_MODE_KEY, "Safe mode", "Disables all plugins and optional coreplugins.", false) { Utils.promptRestart() }

        if (StoreStream.getUserSettings().isDeveloperMode) {
            addDivider(ctx)
            addHeader(ctx, "Developer Settings")
            addSwitch(
                ctx,
                ALIUCORD_FROM_STORAGE_KEY,
                "Use Mollycord core from storage",
                "Meant for developers. Do not enable unless you know what you're doing. " +
                    "Uses a custom core bundle that was pushed to the device.",
            ) {
                Utils.promptRestart()
            }
        }

        addDivider(ctx)
        addHeader(ctx, "About Mollycord")
        addLink(ctx, "Made by @bubblegum4fun", R.e.ic_heart_24dp) {
            android.app.AlertDialog.Builder(it.context)
                .setTitle("About Mollycord")
                .setMessage("Welcome to Mollycord!\n\nA modded Discord client for Android with many new upcoming features and themes!\n\nThe goal is to make Discord YOURS, while maintaining a safe and reliable client.\n\nMade by Bubblegum @bubblegum4fun\nMore changes coming soon! <3")
                .setPositiveButton("OK", null)
                .show()
        }
        addLink(ctx, "Source Code", R.e.ic_account_github_white_24dp) {
            Utils.launchUrl(Constants.MOLLYCORD_GITHUB_REPO)
        }
    }

    private fun addSwitch(
        ctx: Context,
        setting: String,
        title: String,
        subtitle: String?,
        default: Boolean = false,
        onToggled: ((checked: Boolean) -> Unit)? = null,
    ) {
        Utils.createCheckedSetting(ctx, CheckedSetting.ViewType.SWITCH, title, subtitle).run {
            isChecked = Main.settings.getBool(setting, default)
            setOnCheckedListener {
                Main.settings.setBool(setting, it)
                onToggled?.invoke(it)
            }
            linearLayout.addView(this)
        }
    }

    private fun addLink(ctx: Context, text: String, @DrawableRes drawable: Int, action: View.OnClickListener) {
        TextView(ctx, null, 0, R.i.UiKit_Settings_Item_Icon).run {
            this.text = text
            val drawableEnd = ContextCompat.getDrawable(ctx, R.e.ic_open_in_new_white_24dp)?.run {
                mutate()
                Utils.tintToTheme(this)
            }
            val drawableStart = ContextCompat.getDrawable(ctx, drawable)?.run {
                mutate()
                Utils.tintToTheme(this)
            }
            setCompoundDrawablesRelativeWithIntrinsicBounds(drawableStart, null, drawableEnd, null)
            setOnClickListener(action)
            linearLayout.addView(this)
        }
    }
}
