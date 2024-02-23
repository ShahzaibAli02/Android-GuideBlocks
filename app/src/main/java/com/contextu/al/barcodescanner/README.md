## barcodeScanner

Everyone loves Scanner! This is a simple example to get you started with Contextual Extensibility without needing to hard-code your changes every time you want to celebrate with the user.

Barcode GB launches the barcode scanning screen and send the result back to the caller, 
additionally we can customize the option whether to show/hide the scanning result bottom sheet in the barcode code screen by using
Extensibility property "[show_qr_result]"

1. Create an account at [Contextual Dashboard](https://dashboard.contextu.al/ "Contextual Dashboard").
2. Install the Contextual SDK following the instructions for IOS or Android.
3. Copy-Paste the instantiation of the Guide Component AFTER the Contextual SDK registration.

**In your build.gradle add:**

```
implementation 'com.github.GuideBlocks-org:Android-GuideBlocks:0.0.4', {
        exclude group: 'com.google.android.material'
        exclude group: 'com.github.bumptech.glide'
    }
```

**In your activities where you want to use GuideBlocks add (for example):**

```
import com.contextu.al.barcodescanner.BarcodeScanningActivity
```

for the GuideBlock you wish to use, then add 

```
    
        val barCodeScanner = "BarCodeScanner"
        Contextual.registerGuideBlock(barCodeScanner).observe(this){
            contextualContainer ->
            if(contextualContainer.guidePayload.guide.guideBlock.contentEquals(barCodeScanner)){
               BarcodeScannerGuideBlock(
            (activity)
        ) { barcodeResult ->
            
        }.also {
            it.showGuideBlock(contextualContainer)
        }
       }
        }
```


4. Build your App and Run it on a phone or
5. Go to the Dashboard and create a guide:
* Use this [video]( https://vimeo.com/863886653#t=0m58s "Another Guide Creation How-to") to see the steps
* choose “Display the guides on any screen of your app” and
* pick one of the “Standard” Contextual Announcement Templates.
* Preview the Announcement on your Phone - it should look similar to the template
6. Now go to the Extensibility section in the sidebar and paste in the JSON as follows:
   `
   {
   "guideBlockKey": "BarCodeScanner",
   "properties": {
   "width": 70,
   "height": 80,
   "show_qr_result": true
   },
   "iconProperties":{
   "color":123
   },
   "tag":"last_qr_scan"
   }
   `

