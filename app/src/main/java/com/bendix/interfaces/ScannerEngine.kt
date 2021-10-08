package com.bendix.interfaces

import com.symbol.emdk.barcode.ScanDataCollection

class ScannerEngine {
    interface EventsDelegate {
        fun scannerBarcodeEvent(type: ScanDataCollection.LabelType?, value: String?)
    }
}