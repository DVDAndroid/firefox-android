package org.mozilla.fenix.shortcutest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.Person
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import mozilla.components.concept.sync.Device
import mozilla.components.concept.sync.DeviceCapability
import mozilla.components.concept.sync.DeviceType
import mozilla.components.feature.accounts.push.intent.SendToDeviceIntentProcessor
import org.mozilla.fenix.R

class ShortcutTest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val device = Device(
            id = "device-id",
            displayName = "device-name",
            deviceType = DeviceType.MOBILE,
            isCurrentDevice = false,
            lastAccessTime = null,
            capabilities = listOf(DeviceCapability.SEND_TAB),
            subscriptionExpired = false,
            subscription = null,
        )

        ShortcutManagerCompat.removeAllDynamicShortcuts(this)
        val shortcuts = listOf(
            buildShortcut(device),
            buildShortcut(null),
        )
        ShortcutManagerCompat.setDynamicShortcuts(
            this,
            shortcuts
        )
    }

    private fun buildShortcut(device: Device?): ShortcutInfoCompat {
        val intent = Intent(SendToDeviceIntentProcessor.SEND_TAB_TO_DEVICE_ACTION)

        val icon = when (device?.deviceType) {
            null -> R.drawable.mozac_ic_select_all
            DeviceType.MOBILE -> R.drawable.mozac_ic_device_mobile_24
            else -> R.drawable.mozac_ic_device_desktop_24
        }

        return ShortcutInfoCompat.Builder(this, device?.id ?: "<all>")
            .setShortLabel(device?.displayName ?: this.getString(R.string.sync_send_to_all))
            .setIcon(IconCompat.createWithResource(this, icon))
            .setIntent(intent)
            .setPerson(
                Person.Builder()
                    .setName(device?.id ?: "<all>")
//                .setKey(device?.id ?: "<all>")
                    .build(),
            )
            .setCategories(setOf(SendToDeviceIntentProcessor.SEND_TAB_TO_DEVICE_CATEGORY))
//            .setExcludedFromSurfaces(ShortcutInfoCompat.SURFACE_LAUNCHER)
            .build()
    }
}
