package mozilla.components.feature.accounts.push.intent

import android.content.Intent
import mozilla.components.concept.sync.TabData
import mozilla.components.feature.accounts.push.SendTabUseCases
import mozilla.components.feature.intent.processing.IntentProcessor

class SendToDeviceIntentProcessor(
    private val sendTabUseCases: SendTabUseCases,
) : IntentProcessor {

    companion object {
        const val SEND_TAB_TO_DEVICE_ACTION = "org.mozilla.fenix.SEND_TAB_TO_DEVICE_ACTION"
        const val SEND_TAB_TO_DEVICE_CATEGORY = "org.mozilla.fenix.SEND_TAB_TO_DEVICE_CATEGORY"
    }

    override fun process(intent: Intent): Boolean {
        if (intent.action != SEND_TAB_TO_DEVICE_ACTION) return false
        if (!intent.hasCategory(SEND_TAB_TO_DEVICE_CATEGORY)) return false

        val title = intent.getStringExtra(Intent.EXTRA_SUBJECT) ?: return false
        val url = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return false
        val deviceId = intent.getStringExtra(Intent.EXTRA_SHORTCUT_ID) ?: return false

        val tabData = TabData(title, url)
        if (deviceId == "<all>") {
            sendTabUseCases.sendToAllAsync(tabData)
        } else {
            sendTabUseCases.sendToDeviceAsync(deviceId, tabData)
        }
        return true
    }

}
