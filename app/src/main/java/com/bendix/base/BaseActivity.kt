package com.bendix.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bendix.utility.LogUtil
import com.symbol.emdk.EMDKManager
import com.symbol.emdk.EMDKResults
import com.symbol.emdk.barcode.BarcodeManager
import com.symbol.emdk.barcode.ScanDataCollection
import com.symbol.emdk.barcode.Scanner
import com.symbol.emdk.barcode.ScannerConfig
import com.symbol.emdk.barcode.ScannerException
import com.symbol.emdk.barcode.ScannerInfo
import com.symbol.emdk.barcode.ScannerResults
import com.symbol.emdk.barcode.StatusData
import java.util.ArrayList

open class BaseActivity : AppCompatActivity(), EMDKManager.EMDKListener, Scanner.DataListener,
    Scanner.StatusListener, BarcodeManager.ScannerConnectionListener {

    var emdkManager: EMDKManager? = null
    private var barcodeManager: BarcodeManager? = null
    private var scanner: Scanner? = null

    private var deviceList: List<ScannerInfo>? = null

    private var scannerIndex = 0 // Keep the selected scanner

    private var defaultIndex = 0 // Keep the default scanner


    private var statusString = ""

    private var bSoftTriggerSelected = false
    private var bDecoderSettingsChanged = false
    private var bExtScannerDisconnected = false

    private val lock = Any()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        deviceList = ArrayList()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR

        val results = EMDKManager.getEMDKManager(applicationContext, this)
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            LogUtil.displayLogWarning("BA", "EMDKManager object request failed!")
        }

        // Active Scanner
        activeScanner()
    }

    override fun onResume() {
        super.onResume()
        // The application is in foreground
        if (emdkManager != null) {
            // Acquire the barcode manager resources
            initBarcodeManager()
            // Enumerate scanner devices
            enumerateScannerDevices()
            // Active default scanner
            activeScanner()
            // Initialize scanner
            initScanner()
        }
    }

    override fun onPause() {
        super.onPause()
        // The application is in background
        // Release the barcode manager resources
        deInitScanner()
        deInitBarcodeManager()
    }

    override fun onOpened(emdkManager: EMDKManager?) {
        LogUtil.displayLog("BA", "EMDK open success!")
        this.emdkManager = emdkManager
        // Acquire the barcode manager resources
        initBarcodeManager()
        // Enumerate scanner devices
        enumerateScannerDevices()
        // Active default scanner
        activeScanner()
    }

    override fun onClosed() {
        LogUtil.displayLogError("BA", "Base - onClosed - called")
        // Release all the resources
        if (emdkManager != null) {
            emdkManager!!.release()
            emdkManager = null
        }
        LogUtil.displayLogWarning(
            "BA",
            "EMDK closed unexpectedly! Please close and restart the application."
        )
    }

    override fun onConnectionChange(
        scannerInfo: ScannerInfo?,
        connectionState: BarcodeManager.ConnectionState?
    ) {
        val status: String
        var scannerName = ""
        val statusExtScanner = connectionState.toString()
        val scannerNameExtScanner = scannerInfo!!.friendlyName

        if (deviceList!!.isNotEmpty()) {
            scannerName = deviceList!![scannerIndex].friendlyName
        }

        if (scannerName.equals(scannerNameExtScanner, ignoreCase = true)) {
            when (connectionState) {
                BarcodeManager.ConnectionState.CONNECTED -> {
                    bSoftTriggerSelected = false
                    synchronized(lock) {
                        initScanner()
                        bExtScannerDisconnected = false
                    }
                }
                BarcodeManager.ConnectionState.DISCONNECTED -> {
                    bExtScannerDisconnected = true
                    synchronized(lock) { deInitScanner() }
                }
            }
            status = "$scannerNameExtScanner:$statusExtScanner"
            LogUtil.displayLog("BA ", status)
        } else {
            bExtScannerDisconnected = false
            status = "$statusString $scannerNameExtScanner:$statusExtScanner"
            LogUtil.displayLog("BA ", status)
        }
    }

    override fun onData(scanDataCollection: ScanDataCollection?) {
        if (scanDataCollection != null && scanDataCollection.result == ScannerResults.SUCCESS) {
            val scanData = scanDataCollection.scanData
            for (data in scanData) {
                Log.i("BA", "onData - Label : " + data.labelType)
                Log.i("BA", "onData - Data : " + data.data)
                LogUtil.displayLogInfo(
                    "BA",
                    "onData - Scan Label : " + data.labelType + " - Scan Data : " + data.data
                )
            }
        }
    }

    override fun onStatus(statusData: StatusData?) {
        val state: StatusData.ScannerStates = statusData!!.state
        when (state) {
            StatusData.ScannerStates.IDLE -> {
                statusString = statusData.friendlyName + " is enabled and idle..."
                LogUtil.displayLog("BA", "OnStatus : $statusString")
                // set trigger type
                if (bSoftTriggerSelected) {
                    scanner!!.triggerType = Scanner.TriggerType.SOFT_ONCE
                    bSoftTriggerSelected = false
                } else {
                    scanner!!.triggerType = Scanner.TriggerType.HARD
                }
                // set decoders
                if (bDecoderSettingsChanged) {
                    setDecoders()
                    bDecoderSettingsChanged = false
                }
                // submit read
                if (!scanner!!.isReadPending && !bExtScannerDisconnected) {
                    try {
                        scanner!!.read()
                    } catch (e: ScannerException) {
                        e.printStackTrace()
                    }
                }
            }
            StatusData.ScannerStates.WAITING -> {
                statusString = "Scanner is waiting for trigger press..."
                LogUtil.displayLog("BA", "OnStatus : $statusString")
            }
            StatusData.ScannerStates.SCANNING -> {
                statusString = "Scanning..."
                LogUtil.displayLog("BA", "OnStatus : $statusString")
            }
            StatusData.ScannerStates.DISABLED -> {
                statusString = statusData.friendlyName + " is disabled."
                LogUtil.displayLog("BA", "OnStatus : $statusString")
            }
            StatusData.ScannerStates.ERROR -> {
                statusString = "An error has occurred."
                LogUtil.displayLog("BA", "OnStatus : $statusString")
            }
            else -> {
            }
        }
    }

    open fun activeScanner() {
        val position = 1
        if (scannerIndex != position || scanner == null) {
            scannerIndex = position
            bSoftTriggerSelected = false
            bExtScannerDisconnected = false
            deInitScanner()
            initScanner()
        }
    }

    open fun initScanner() {
        if (scanner == null) {
            if (deviceList != null && deviceList!!.isNotEmpty()) {
                if (barcodeManager != null) scanner =
                    barcodeManager!!.getDevice(deviceList!![scannerIndex])
            } else {
                LogUtil.displayLogWarning(
                    "BA",
                    "Failed to get the specified scanner device! Please close and restart the application."
                )
                return
            }
            if (scanner != null) {
                scanner!!.addDataListener(this)
                scanner!!.addStatusListener(this)
                try {
                    scanner!!.enable()
                } catch (e: ScannerException) {
                    e.printStackTrace()
                    deInitScanner()
                }
            } else {
                LogUtil.displayLogWarning("BA", "Failed to initialize the scanner device.")
            }
        }
    }

    open fun deInitScanner() {
        if (scanner != null) {
            try {
                scanner!!.disable()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                scanner!!.removeDataListener(this)
                scanner!!.removeStatusListener(this)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                scanner!!.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            scanner = null
        }
    }

    open fun initBarcodeManager() {
        barcodeManager =
            emdkManager!!.getInstance(EMDKManager.FEATURE_TYPE.BARCODE) as BarcodeManager
        if (barcodeManager != null) {
            barcodeManager!!.addConnectionListener(this)
        }
    }

    open fun deInitBarcodeManager() {
        if (emdkManager != null) {
            emdkManager!!.release(EMDKManager.FEATURE_TYPE.BARCODE)
        }
    }

    open fun enumerateScannerDevices() {
        if (barcodeManager != null) {
            val friendlyNameList: MutableList<String> = ArrayList()
            var spinnerIndex = 0
            deviceList = barcodeManager!!.supportedDevicesInfo
            if (deviceList != null && deviceList!!.isNotEmpty()) {
                val it = deviceList!!.iterator()
                while (it.hasNext()) {
                    val scnInfo = it.next()
                    friendlyNameList.add(scnInfo.friendlyName)
                    if (scnInfo.isDefaultScanner) {
                        defaultIndex = spinnerIndex
                    }
                    ++spinnerIndex
                }
            } else {
                LogUtil.displayLogWarning(
                    "BA",
                    "Failed to get the list of supported scanner devices! Please close and restart the application."
                )
            }
        }
    }

    open fun setDecoders() {
        if (scanner != null) {
            try {
                val config = scanner!!.config
                // Set EAN8
                config.decoderParams.ean8.enabled = true
                // Set EAN13
                config.decoderParams.ean13.enabled = true
                // Set Code39
                config.decoderParams.code39.enabled = true
                //Set Code128
                config.decoderParams.code128.enabled = true
                scanner!!.config = config
            } catch (e: ScannerException) {
                e.printStackTrace()
            }
        }
    }

    open fun softScan(view: View?) {
        bSoftTriggerSelected = true
        cancelRead()
    }

    open fun cancelRead() {
        if (scanner != null) {
            if (scanner!!.isReadPending) {
                try {
                    scanner!!.cancelRead()
                } catch (e: ScannerException) {
                    e.printStackTrace()
                }
            }
        }
    }

    open fun destroyScanner() {
        // Release all the resources
        if (emdkManager != null) {
            emdkManager!!.release()
            emdkManager = null
        }
    }
}