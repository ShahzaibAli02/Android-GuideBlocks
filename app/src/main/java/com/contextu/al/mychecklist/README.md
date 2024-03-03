## NPS RATING BAR

In this example, we show how to make a  Check List Guide
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
import com.contextu.al.mychecklist.MyCheckListGuideBlocks
import com.contextu.al.core.CtxEventObserver
```

4. for the GuideBlock you wish to use, then Copy-Paste the instantiation of the Guide Component AFTER the Contextual SDK registration. [XML BASED ANDROID PROJECT]

```
         val guideName = "OpenChecklist"
        Contextual.registerGuideBlock(guideName).observe(this) { contextualContainer ->
            if (contextualContainer.guidePayload.guide.guideBlock.contentEquals(guideName))
            {
                val mComposeView: ComposeView = (mBinding.root.children.find { it.tag == "myComposeView" }?: ComposeView(this)) as ComposeView
                mComposeView.tag = "myComposeView";
                mComposeView.apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        MaterialTheme {
                            MyCheckListGuideBlocks().show(
                                deepLinkListener = { link ->
                                    runCatching {
                                        mNavController.navigate(Uri.parse(link))
                                    }
                                }, activity = this@MainActivity, contextualContainer = contextualContainer
                            )
                        }
                    }
                } //ADD VIEW ONLY IF ITS NOT ALREADY ADDED
                if (mBinding.root.contains(mComposeView).not())
                {
                    mBinding.root.addView(mComposeView)
                }
            }
        }
```


5. for the GuideBlock you wish to use, then Copy-Paste the instantiation of the Guide Component AFTER the Contextual SDK registration. [COMPOSE PROJECT]

```
               MaterialTheme {
                            val activity = context as? AppCompatActivity
                            val lifecycleOwner = LocalLifecycleOwner.current
                            val guideName = "NPSRatingBar"
                            var mContextualContainer: ContextualContainer? by remember { mutableStateOf(null)}
                            SideEffect {
                                Contextual.registerGuideBlock(guideName).observe(lifecycleOwner) { contextualContainer ->
                                    mContextualContainer=contextualContainer
                                }
                            }
                            if(mContextualContainer!=null && activity!=null)
                            {
                             MyCheckListGuideBlocks().show(
                                deepLinkListener = { link ->
                                    runCatching {
                                     //   mNavController.navigate(Uri.parse(link))
                                    }
                                }, activity = this@MainActivity, contextualContainer = mContextualContainer
                            )
                            }

                        }
```
 
6. Build your App and Run it on a phone or
7. Go to the Dashboard and create a guide:
* choose “Display the guides on any screen of your app” and
* pick one of the “Standard” Contextual Announcement Templates.
* Preview the Check List Guide on your Phone - it should look similar to the template
8. Now go to the Extensibility section in the sidebar and paste in the JSON as follows:
   `
{
   "guideBlockKey": "OpenChecklist",
   "tasks": [
   {
   "name": "Set My Tag",
   "action": "SetTag",
   "action_data": {
   "key": "mytag",
   "value": "1234"
   }
   },
   {
   "name": "Visit Inbox",
   "action": "gotoScreen",
   "action_data": {
   "deeplink": "airbnb_contextual://screen/inbox"
   }
   },
   {
   "name": "Visit Profile",
   "action": "gotoScreen",
   "action_data": {
   "deeplink": "airbnb_contextual://screen/profile"
   }
   }
   ]
}
   `
* Match the name in the JSON to the name of your wrapper in the code
JSON editing
9. If you are still in Preview Mode, then you should see the Nps Rating Bar
10. Change the Title and Content and buttons. Play around with it and see the results.
11. Save the guide and show  to your Product Team, once you release this version of the App they can launch   Check List Guide  to whoever they want, whenever they want.

