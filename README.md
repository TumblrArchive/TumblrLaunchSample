# Launching the Tumblr Android application

Our application supports two deep linking approaches.  We use Android's [share mechanism](http://developer.android.com/guide/components/intents-filters.html) (which also allows developer's to share directly into our application) as well as a URL scheme.  This document will give examples of both.

## Deep linking into the Tumblr app

The application supports a `tumblr` URL scheme and a slightly unconventional [x-callback-url](http://x-callback-url.com) structure (so that our [Android](https://play.google.com/store/apps/details?id=com.tumblr&hl=en) and [iOS](https://itunes.apple.com/us/app/tumblr/id305343404?mt=8) apps both support the same URLs).  See a few URL examples below:

```
tumblr://x-callback-url/blog?blogName=haseman

tumblr://x-callback-url/blog?blogName=haseman&postID=94288096341

tumblr://x-callback-url/tag?tag=cars
```

As you might imagine, the first URL opens a blog, the second a post from a blog, and the last a tag.

## Sharing with Tumblr

Tumblr supports generic photo, text, and video sharing as you might expect, however, by specifying our package you can tell Android you'd like to fire our application directly.  See the below code sample (which is also in the sample project):

```java
try {
  Intent textPost = new Intent();
  textPost.setAction(Intent.ACTION_SEND);
  textPost.setPackage("com.tumblr");
  textPost.setType("text/plain");
  textPost.putExtra(Intent.EXTRA_SUBJECT, "This is the Post Title");
  textPost.putExtra(Intent.EXTRA_TEXT, "Body text is here");
  getActivity().startActivity(textPost);
}catch(ActivityNotFoundException ex){
  Log.d("ShareExample", "Unable to find tumblr application", ex);
}
```

The sample code above will create a new text post in the [Tumblr app](https://play.google.com/store/apps/details?id=com.tumblr&hl=en) (if it's installed) be careful, because if it isn't installed the following code will throw an exception.  For examples on creating photo and link posts please see the project itself.
