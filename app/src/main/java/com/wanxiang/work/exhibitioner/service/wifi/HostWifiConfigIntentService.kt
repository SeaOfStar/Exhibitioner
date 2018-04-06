package com.wanxiang.work.exhibitioner.service.wifi

import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 *
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class HostWifiConfigIntentService : IntentService("HostWifiConfigIntentService") {

    var config = WifiConfiguration()
    val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private fun clearConfig() {
        config.allowedAuthAlgorithms.clear()
        config.allowedGroupCiphers.clear()
        config.allowedKeyManagement.clear()
        config.allowedPairwiseCiphers.clear()
        config.allowedProtocols.clear()
        config.SSID = "\"\""
    }

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            when(action) {
                ACTION_START_HOST_WIFI -> {
                    val ssid = intent.getStringExtra(EXTRA_PARAM_SSID)
                    val password = intent.getStringExtra(EXTRA_PARAM_PASSWORD)
                    val cypt = intent.getIntExtra(EXTRA_PARAM_CYPTTYPE, WifiConfiguration.KeyMgmt.NONE)
                    handleActionStart(ssid, password, cypt)
                }
                ACTION_STOP_HOST_WIFI -> {
                    val ssid = intent.getStringExtra(EXTRA_PARAM_SSID)
                    handleActionStop(ssid)
                }
                ACTION_CONNECT_WIFI -> {
                    val ssid = intent.getStringExtra(EXTRA_PARAM_SSID)
                    val password = intent.getStringExtra(EXTRA_PARAM_PASSWORD)
                    val cypt = intent.getIntExtra(EXTRA_PARAM_CYPTTYPE, WifiConfiguration.KeyMgmt.NONE)
                    handleActionConnect(ssid, password, cypt)
                }
            }
        }
    }

    private fun handleActionStart(ssid: String, password: String, cypt: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    @SuppressLint("MissingPermission")
    private fun handleActionConnect(ssid: String, password: String, cypt: Int) {
        clearConfig()

        config.SSID = "\"$ssid\""
        when (cypt) {
            WifiConfiguration.KeyMgmt.NONE -> {
                config.wepKeys[0] = ""
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
                config.wepTxKeyIndex = 0
            }

            WifiConfiguration.KeyMgmt.WPA_PSK -> {
                config.preSharedKey = "\"$password\""
                config.hiddenSSID = true
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
                //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
                config.status = WifiConfiguration.Status.ENABLED
            }

            WifiConfiguration.KeyMgmt.WPA_EAP -> {
                config.hiddenSSID = true
                config.wepKeys[0] = "\"$password\""
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40)
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104)
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
                config.wepTxKeyIndex = 0
            }
        }

        val configId = wifiManager.addNetwork(config)
        val b = wifiManager.enableNetwork(configId, true)
    }



    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionStop(ssid: String) {
        // TODO: Handle action Baz
        throw UnsupportedOperationException("Not yet implemented")
    }

    companion object {
        private val ACTION_START_HOST_WIFI = "com.wanxiang.work.exhibitioner.service.wifi.HostWifiConfigIntentService.action.start_host_wifi"
        private val ACTION_STOP_HOST_WIFI = "com.wanxiang.work.exhibitioner.service.wifi.HostWifiConfigIntentService.action.stop_host_wifi"
        private val ACTION_CONNECT_WIFI = "com.wanxiang.work.exhibitioner.service.wifi.HostWifiConfigIntentService.action.connect_wifi"

        private val EXTRA_PARAM_SSID = "com.wanxiang.work.exhibitioner.service.wifi.HostWifiConfigIntentService.extra.PARAM_SSID"
        private val EXTRA_PARAM_PASSWORD = "com.wanxiang.work.exhibitioner.service.wifi.HostWifiConfigIntentService.extra.PARAM_PASSWORD"
        private val EXTRA_PARAM_CYPTTYPE = "com.wanxiang.work.exhibitioner.service.wifi.HostWifiConfigIntentService.extra.PARAM_CYPT"

        /**
         * 启动热点服务
         *
         * @see IntentService
         */
        fun startActionStartHostWifi(context: Context, ssid: String, password: String, cypt: Int = WifiConfiguration.KeyMgmt.NONE) {
            val intent = Intent(context, HostWifiConfigIntentService::class.java)
            intent.action = ACTION_START_HOST_WIFI
            intent.putExtra(EXTRA_PARAM_SSID, ssid)
            intent.putExtra(EXTRA_PARAM_PASSWORD, password)
            intent.putExtra(EXTRA_PARAM_CYPTTYPE, cypt)
            context.startService(intent)
        }

        /**
         * 停止热点服务
         *
         * @see IntentService
         */
        fun startActionStopHostWifi(context: Context, ssid: String) {
            val intent = Intent(context, HostWifiConfigIntentService::class.java)
            intent.action = ACTION_STOP_HOST_WIFI
            intent.putExtra(EXTRA_PARAM_SSID, ssid)
            context.startService(intent)
        }

        /**
         * 连接指定的wifi
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        fun startActionConnectWifi(context: Context, ssid: String, password: String, cypt: Int = WifiConfiguration.KeyMgmt.NONE) {
            val intent = Intent(context, HostWifiConfigIntentService::class.java)
            intent.action = ACTION_CONNECT_WIFI
            intent.putExtra(EXTRA_PARAM_SSID, ssid)
            intent.putExtra(EXTRA_PARAM_PASSWORD, password)
            intent.putExtra(EXTRA_PARAM_CYPTTYPE, cypt)
            context.startService(intent)
        }
    }
}
