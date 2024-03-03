## Fancy Announcement

In this example, we show how to make a  Quiz Dialog, just like the crazy ones your Designer comes up with 🤣. Its a simple example to get you started with Contextual Extensibility without needing to hard-code your changes every time you want to update the tip.

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
import com.contextu.al.quizgatekeeper.QuizGatekeeperGuideBlock
import com.contextu.al.core.CtxEventObserver
```

4. for the GuideBlock you wish to use, then Copy-Paste the instantiation of the Guide Component AFTER the Contextual SDK registration. [XML BASED ANDROID PROJECT]

```
       val myQuizGateKeeperGuide = "QuizGateKeeper"
        Contextual.registerGuideBlock(myQuizGateKeeperGuide).observe(this) { contextualContainer ->
            if (contextualContainer.guidePayload.guide.guideBlock.contentEquals(myQuizGateKeeperGuide))
            {
                val mComposeView: ComposeView = (mBinding.root.children.find { it.tag == "myComposeView" }?: ComposeView(this)) as ComposeView
                mComposeView.tag = "myComposeView";
                mComposeView.apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        MaterialTheme {
                            QuizGatekeeperGuideBlock().show(
                                activity = this@MainActivity, mContextualContainer = contextualContainer
                            ) { result ->
                                mBinding.root.removeView(mComposeView)
                            }
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
                            val guideName = "QuizGateKeeper"
                            var mContextualContainer: ContextualContainer? by remember { mutableStateOf(null)}
                            SideEffect {
                                Contextual.registerGuideBlock(guideName).observe(lifecycleOwner) { contextualContainer ->
                                    mContextualContainer=contextualContainer
                                }
                            }
                            if(mContextualContainer!=null && activity!=null)
                            {
                                QuizGatekeeperGuideBlock().show(
                                    activity = activity, mContextualContainer = mContextualContainer
                                ) { result ->

                                  // QUIZ FINISHED Any task to do here

                                }
                            }

                        }
```
 
6. Build your App and Run it on a phone or
7. Go to the Dashboard and create a guide:
* choose “Display the guides on any screen of your app” and
* pick one of the “Standard” Contextual Announcement Templates.
* Preview the Announcement on your Phone - it should look similar to the template
8. Now go to the Extensibility section in the sidebar and paste in the JSON as follows:
   `
   {
   "guideBlockKey": "QuizGateKeeper",
   "questions": [
   {
   "question": "How would you do X?",
   "answers": [
   {
   "label": "By clicking the edit profile",
   "correct": false
   },
   {
   "label": "By praying to my fave deity",
   "correct": false
   },
   {
   "label": "By entering the dish and selecting Fave",
   "correct": true
   }
   ]
   },
   {
   "question": "What planet are you on?",
   "answers": [
   {
   "label": "Earth",
   "correct": true
   },
   {
   "label": "Betelgeuse Seven",
   "correct": false
   },
   {
   "label": "Golgafrincham",
   "correct": false
   }
   ]
   }
   ],
   "fail": {
   "action": "restartQuiz",
   "action_data": {
   "key": "any_key",
   "value": "any_value",
   "attempts": 2,
   "lockout_seconds": 600,
   "allow_screen_access": false
   }
   },
   "pass": {
   "action": "setTag",
   "action_data": {
   "key": "any_key",
   "value": "any_value",
   "allow_screen_access": true
   }
   }
   }
   `
* Match the name in the JSON to the name of your wrapper in the code
JSON editing
9. If you are still in Preview Mode, then you should see the Announcement will magically change to Quiz Dialog
10. Change the Title and Content and buttons. Play around with it and see the results.
11. Save the guide and show to your Product Team, once you release this version of the App they can launch Quiz Dialog  to whoever they want, whenever they want.

