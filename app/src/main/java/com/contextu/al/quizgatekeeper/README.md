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
import com.contextu.al.quizgatekeeper.QuizGatekeeperGuideBlock
import com.contextu.al.core.CtxEventObserver
```

4. for the GuideBlock you wish to use, then Copy-Paste the instantiation of the Guide Component AFTER the Contextual SDK registration.

```
        val myQuizGateKeeper = "QuizGateKeeper"
        Contextual.registerGuideBlock(myQuizGateKeeper).observe(this){ contextualContainer ->
            if (contextualContainer.guidePayload.guide.guideBlock.contentEquals(myQuizGateKeeper)) {
                mBinding.composeView.isVisible=true
                mBinding.composeView.apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        MaterialTheme {
                            QuizGatekeeperGuideBlock().show(
                                activity = this@MainActivity,
                                mContextualContainer = contextualContainer
                            )
                        }
                    }
                }
            }
        }
```
 
5. Build your App and Run it on a phone or
6. Go to the Dashboard and create a guide:
* choose “Display the guides on any screen of your app” and
* pick one of the “Standard” Contextual Announcement Templates.
* Preview the Announcement on your Phone - it should look similar to the template
7. Now go to the Extensibility section in the sidebar and paste in the JSON as follows:
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
   "key": "Quiz_fail_datetime",
   "value": "@now",
   "attempts": 2,
   "lockout_seconds": 600,
   "allow_screen_access": false
   }
   },
   "pass": {
   "action": "setTag",
   "action_data": {
   "key": "Quiz_pass_datetime",
   "value": "@now",
   "allow_screen_access": true
   }
   }
   }
   `
* Match the name in the JSON to the name of your wrapper in the code
JSON editing
8. If you are still in Preview Mode, then you should see the Announcement will magically change to Fancy Announcement
9. Change the Title and Content and buttons. Play around with it and see the results.
10. Save the guide and show to your Product Team, once you release this version of the App they can launch Fancy Announcement to whoever they want, whenever they want.

