## Fancy Announcement

In this example, we show how to make a Fancy Announcement, just like the crazy ones your Designer comes up with 🤣. Its a simple example to get you started with Contextual Extensibility without needing to hard-code your changes every time you want to update the tip.

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
import com.contextu.al.fancyannouncement.FancyAnnouncementGuideBlocks
import com.contextu.al.core.CtxEventObserver
```

4. for the GuideBlock you wish to use, then Copy-Paste the instantiation of the Guide Component AFTER the Contextual SDK registration.

```
        val faGuideBlocks = "FancyAnnouncement"

        Contextual.registerGuideBlock(faGuideBlocks).observe(this){ contextualContainer ->
            if(contextualContainer.guidePayload.guide.guideBlock.contentEquals(faGuideBlocks)){
                FancyAnnouncementGuideBlocks(this@MainActivity).show()
            }
        }
```
 
5. Build your App and Run it on a phone or
6. Go to the Dashboard and create a guide:
* Use this [video]( https://vimeo.com/863886653#t=0m58s "Another Guide Creation How-to") to see the steps
* choose “Display the guides on any screen of your app” and
* pick one of the “Standard” Contextual Announcement Templates.
* Preview the Announcement on your Phone - it should look similar to the template
7. Now go to the Extensibility section in the sidebar and paste in the JSON as follows:
   `
   {
   "guideBlockKey": "FancyAnnouncement"
   }
   `
* Match the name in the JSON to the name of your wrapper in the code

JSON editing
 
<img src="https://raw.githubusercontent.com/contextu-al/FavDish/main/screenshots/FancyAnnouncementJSON.png" alt="Adding your Extra JSON" width="200"/>

or use the Tree method of editing

<img src="https://raw.githubusercontent.com/contextu-al/FavDish/main/screenshots/FancyAnnouncementTree.png" alt="Adding your Extra JSON" width="200"/>

8. If you are still in Preview Mode, then you should see the Announcement will magically change to Fancy Announcement
9. Change the Title and Content and buttons. Play around with it and see the results.
10. Save the guide and show to your Product Team, once you release this version of the App they can launch Fancy Announcement to whoever they want, whenever they want.

