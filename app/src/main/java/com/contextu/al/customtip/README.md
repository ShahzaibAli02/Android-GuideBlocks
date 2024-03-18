## Custom Guide Block Tip

In this example, we show how to make a  Custom GuideBlock Tip.Its a simple example to get you started with Contextual Extensibility without needing to hard-code your changes every time you want to update the tip.

1. Create an account at [Contextual Dashboard](https://dashboard.contextu.al/ "Contextual Dashboard").
2. Install the Contextual SDK following the instructions for IOS or Android.
3. Copy-Paste the instantiation of the Guide Component AFTER the Contextual SDK registration.

**In your build.gradle add:**

```
implementation 'com.github.GuideBlocks-org:Android-GuideBlocks:LATEST_VERSION', {
        exclude group: 'com.google.android.material'
        exclude group: 'com.github.bumptech.glide'
    }
```

**In your activities where you want to use GuideBlocks add (for example):**

```
import com.contextu.al.customtip.CustomGBTip
import com.contextu.al.core.CtxEventObserver
```

4. for the GuideBlock you wish to use, then Copy-Paste the instantiation of the Guide Component AFTER the Contextual SDK registration.

```
       Contextual.registerGuideBlock(CustomGBTip.GB_KEY).observe(this) { contextualContainer ->

            if ( contextualContainer.guidePayload.guide.guideBlock.contentEquals(CustomGBTip.GB_KEY))
            {
                CustomGBTip(this@MainActivity,contextualContainer).showCustomTip()
            }


        }
```
5. Build your App and Run it on a phone or
6. Go to the Dashboard and create a guide:
   * choose “Screen To Display Tip” and
   * pick one of the “Standard” Contextual Tip Templates.
   * Select area to display the tip 
7. Now go to the Extensibility section in the sidebar and paste in the JSON as follows:
     `
   {
   "guideBlockKey": "TestTip",
   "message":"Test this is test custom guideblock with one textview and button",
   "buttonText":"Dismiss"
   }
     `
* Match the name in the JSON to the name of your wrapper in the code
JSON editing
8. If you are still in Preview Mode, then you should see the custom guide block tip
9.  Change the Title and Content and buttons. Play around with it and see the results.
10. Save the guide and show to your Product Team, once you release this version of the App they can launch Custom GB Tip to whoever they want, whenever they want.

