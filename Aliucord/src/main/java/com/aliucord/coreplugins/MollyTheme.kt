/*
 * This file is part of Mollycord, an Android Discord client mod.
 * Based on Aliucord by Juby210 & Vendicated
 * Modified by Bubblegum @bubblegum4fun
 * Licensed under the Open Software License version 3.0
 */

package com.aliucord.coreplugins

import android.content.Context
import android.graphics.Color
import com.aliucord.entities.CorePlugin
import com.aliucord.patcher.*
import com.discord.utilities.color.ColorCompat

/**
 * Molly Theme - The default Mollycord theme
 * 
 * Colors:
 * - Background: Gold (#DAA520)
 * - Borders/Accents: Pink (#FE019A)
 * - Outgoing text: Blue (#0066CC)
 * - Incoming text: Black (#000000)
 * - Links: Red (#FF0000)
 */
internal class MollyTheme : CorePlugin(Manifest("MollyTheme", "The default Mollycord theme - Molly")) {
    
    companion object {
        // Molly Theme Colors
        const val BACKGROUND_PRIMARY = 0xFFDAA520.toInt()      // Gold
        const val BACKGROUND_SECONDARY = 0xFFB8860B.toInt()    // Dark Golden Rod  
        const val BACKGROUND_TERTIARY = 0xFFCD853F.toInt()     // Peru (lighter gold)
        const val ACCENT_COLOR = 0xFFFE019A.toInt()            // Pink
        const val TEXT_NORMAL = 0xFF000000.toInt()             // Black
        const val TEXT_MUTED = 0xFF333333.toInt()              // Dark gray
        const val LINK_COLOR = 0xFFFF0000.toInt()              // Red
        const val HEADER_PRIMARY = 0xFF000000.toInt()          // Black
        const val HEADER_SECONDARY = 0xFF222222.toInt()        // Near black
        const val INTERACTIVE_NORMAL = 0xFF000000.toInt()      // Black
        const val INTERACTIVE_ACTIVE = 0xFFFE019A.toInt()      // Pink
        const val CHANNEL_TEXT_AREA = 0xFFDAA520.toInt()       // Gold
        
        // Map of color attribute names to Molly colors
        val colorMap = mapOf(
            "primary_dark_600" to BACKGROUND_PRIMARY,
            "primary_dark_630" to BACKGROUND_SECONDARY,
            "primary_dark_660" to BACKGROUND_TERTIARY,
            "primary_dark_700" to BACKGROUND_PRIMARY,
            "primary_dark_800" to BACKGROUND_SECONDARY,
            "brand_new" to ACCENT_COLOR,
            "brand_new_360" to ACCENT_COLOR,
            "brand_new_500" to ACCENT_COLOR,
            "link" to LINK_COLOR,
            "link_light" to LINK_COLOR,
        )
    }
    
    override val isHidden = false
    override val isRequired = false  // Users can disable if they want

    override fun start(context: Context) {
        // Patch ColorCompat to return our custom colors
        patcher.after<ColorCompat>(
            "getThemedColor",
            Context::class.java,
            Int::class.javaPrimitiveType!!
        ) { (param, result: Int) ->
            val ctx = param.args[0] as Context
            val attrId = param.args[1] as Int
            
            // Try to get the attribute name
            try {
                val attrName = ctx.resources.getResourceEntryName(attrId)
                
                // Check if we have a custom color for this attribute
                colorMap[attrName]?.let { customColor ->
                    param.result = customColor
                }
            } catch (e: Exception) {
                // Resource not found, use default
            }
        }
        
        // Log that theme is active
        logger.info("Molly theme activated! 🎨")
    }

    override fun stop(context: Context) {
        patcher.unpatchAll()
        logger.info("Molly theme deactivated")
    }
}
