# Miriam's Recipes

This app lists recipes, and their respective steps, that were retrieved via the network with Retrofit and parsed with GSON. Some steps of the various recipes included videos, video playback is within the app using [Google’s ExoPlayer library](https://github.com/google/ExoPlayer).

I implemented the following and aforesaid from a blank slate:

- Ensure that there were no memory leaks from the network request nor video playback.
  - In Android, the GUI is destroyed and re-created whenever the device is rotated. This can easily lead to memory leaks due to the callbacks in the View (I have since greatly improved how I handle this).
- The user’s position within a video was restored when they rotated their device.
- Created separate layouts for a phone and a tablet.
- Write instrumentation tests for the UI with Google’s Android UI test suite, Espresso.
- Created a home screen widget that allowed the user view and cycle through the steps of a selected recipe

# Resources

- **Icons**
  - https://material.io/tools/icons/?style=round

- **Generic cooking picture**
  - Photo by [Chinh Le Duc](https://unsplash.com/photos/vuDXJ60mJOA?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText) on [Unsplash](https://unsplash.com/?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText).
