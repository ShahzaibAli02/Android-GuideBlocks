## circlevideo

Everyone loves Confetti! This is a simple example to get you started with Contextual Extensibility without needing to hard-code your changes every time you want to celebrate with the user.

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
import com.contextu.al.circlevideo.CircleVideoGuideBlocks
import com.contextu.al.core.CtxEventObserver
```

for the GuideBlock you wish to use, then add 

```
        val circleVideoGuideBlocks = "CircleVideo"
        Contextual.registerGuideBlock(circleVideoGuideBlocks).observe(this){
            contextualContainer ->
            if(contextualContainer.guidePayload.guide.guideBlock.contentEquals(circleVideoGuideBlocks)){
                val circleVideoView = CircleVideoGuideBlock(this@MainActivity)
                val contentUrl:String = contextualContainer.guidePayload.guide.contentText.text ?: ""
                circleVideoView.show(contentUrl)
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
   "guideBlockKey": "CircleVideo"
   }
   `
7. If you are still in Preview Mode, then you should see the Announcement will magically change to confetti
8. Change the Title and Content and buttons. Play around with it and see the results.
9. Save the guide and show to your Product Team, once you release this version of the App they can launch confetti to whoever they want, whenever they want.

