# TumblrLaunchSample

:warning: :warning: :warning: **TUMBLRLAUNCHSAMPLE IS NO LONGER BEING ACTIVELY MAINTAINED.**

#Launching the Tumblr Android application

Our application supports two deep launching approaches.  We use Android's share mechanism (which also allows developer's to share directly into our application) as well as a URL schema.  This document will give example of both.

##Deep linking into the Tumblr app

The application supports a 'tumblr' URL schema and a slightly unconventional x-callback-url structure.  See a few url examples below

```
tumblr://x-callback-url/blog?blogName=haseman

tumblr://x-callback-url/blog?blogName=haseman&postID=94288096341

tumblr://x-callback-url/tag?tag=cars
```
As you might imagine, the first url open's a blog, the second a post from a blog, and the last a tag.

##Sharing with Tumblr
Tumblr supports generic photo, text, and video sharing as you might expect, however, by specifying our package you can tell Android you'd like to fire our application directly.  See the below code sampe (which is also in the sample project)

```
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
The sample code above will create a new text post in the Tumblr App (if it's installed) be careful, because if it isn't installed the following code will throw an exception.  For examples on creating photo and link posts please see the project itself.
