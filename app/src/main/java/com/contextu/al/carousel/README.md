## carousel

Everyone loves carousel! This is a simple example to get you started with Contextual Extensibility without needing to hard-code your changes every time you want to celebrate with the user.

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
import com.contextu.al.carousel.CarouselComponent
```

for the GuideBlock you wish to use, then add 

```
    
        val carousel = "Carousel"
        Contextual.registerGuideBlock(carousel).observe(this) { container ->
            CarouselComponent(contextualContainer = container) {
                    when (it) {
                        is CarouselAction.OnSkip -> {

                        }

                        is CarouselAction.OnButtonClick -> {

                        }
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
   "guideBlockKey": "Carousel",
   "skip": {
   "text":"Skip",
   "color": "#d3d3d3",
   "size": 12
   }
   }
   `

