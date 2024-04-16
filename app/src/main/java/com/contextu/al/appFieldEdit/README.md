## APPFIELD EDIT GUIDE

In this example, we show how to make a App Field Edit Guide

1. Create an account
   at [Contextual Dashboard](https://dashboard.contextu.al/ "Contextual Dashboard").
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
import com.contextu.al.appFieldEdit.ui.AppFieldEditGB
import com.contextu.al.core.CtxEventObserver
```

4. for the GuideBlock you wish to use, then Copy-Paste the instantiation of the Guide Component
   AFTER the Contextual SDK registration

```kotlin
Contextual.registerGuideBlock(AppFieldEditGB.GB_KEY).observeForever { contextualContainer ->

    if (contextualContainer.guidePayload.guide.guideBlock.contentEquals(AppFieldEditGB.GB_KEY))
    {
        AppFieldEditGB(
                this@MainActivity,
                contextualContainer
        ).setOnCompletedListener { 
        //TODO ON COMPLETE
        }.setOnDismissListener {
        //TODO ON DISMISS
        }.show() 
    }


}
```

5. to get validation regex for any step

```kotlin

 val validation: AppFieldEditValidation? = AppFieldValidator().getValidation(step)
 val regex=validation.regex

```

6. to set particular step valid
```kotlin

 AppFieldValidator().setValidValue(step,true)

``` 

7. Go to the Dashboard and create a guide:

* choose “Display the guides on any screen of your app” and
* pick one of the Contextual Tips Templates.
* Preview the Guide on your Phone - it should look similar to the template
8. Now go to the Extensibility section in the sidebar and paste in the JSON as follows:
```json
{
  "guideBlockKey": "AppFieldEdit",
  "validation": {
    "regex": "^.+$"
  }
}
```

* Match the name in the JSON to the name of your wrapper in the code
  JSON editing

9. If you are still in Preview Mode, then you should see the App field edit guide
10. Change the Title and Content and buttons. Play around with it and see the results.
11. Save the guide and show to your Product Team, once you release this version of the App they can
    launch App Field Edit Guide to whoever they want, whenever they want.

