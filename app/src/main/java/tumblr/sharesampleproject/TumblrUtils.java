package tumblr.sharesampleproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Tumblr sharing superclass that allows for deep linking into the Tumblr app and sharing content to create new
 * Tumblr posts.
 *
 * @author brandon
 */
public class TumblrUtils {
	/**
	 * The Tumblr package name.
	 */
	public static final String TUMBLR_PACKAGE_NAME = "com.tumblr";

	/**
	 * Starts the Tumblr application.
	 *
	 * @param activity
	 * 		the activity to use to start the Tumblr app.
	 * @param data
	 * 		the data Uri to open from within Tumblr. This should be a Uri created from this class.
	 */
	public static void startTumblr(final Activity activity, final Uri data) {
		// Defensive.
		if (activity == null || data == null) {
			return;
		}

		final Intent viewIntent = new Intent();
		viewIntent.setAction(Intent.ACTION_VIEW);
		viewIntent.addCategory(Intent.CATEGORY_BROWSABLE);
		viewIntent.setPackage(TumblrUtils.TUMBLR_PACKAGE_NAME);
		viewIntent.setData(data);
		activity.startActivity(viewIntent);
	}

	/**
	 * Tests whether the Tumblr app is installed.
	 *
	 * @param context
	 * 		the android context to use.
	 * @return true if the Tumblr app is installed, false otherwise.
	 */
	public static boolean isTumblrInstalled(final Context context) {
		// Defensive.
		if (context == null) {
			return false;
		}

		final PackageManager packageManager = context.getPackageManager();
		final List<ApplicationInfo> apps = packageManager.getInstalledApplications(0);
		boolean found = false;
		for (ApplicationInfo app : apps) {
			if (TumblrUtils.TUMBLR_PACKAGE_NAME.equals(app.packageName)) {
				found = true;
				break;
			}
		}
		return found;
	}

	/**
	 * Base format for all Tumblr URLs.
	 */
	private static final String URL_FORMAT_BASE = "tumblr://x-callback-url/";

	/**
	 * Creates a deep link URL to a blog inside the Tumblr Android app.
	 *
	 * @param blogName
	 * 		the name of the blog to open inside of the Tumblr app.
	 * @return the deep link URL.
	 */
	public static Uri createBlogUri(final String blogName) {
		return createBlogUri(blogName, 0);
	}

	/**
	 * Creates a deep link URL to a blog inside the Tumblr Android app, to a specific post.
	 *
	 * @param blogName
	 * 		the name of the blog to open inside of the Tumblr app.
	 * @param postId
	 * 		the post to open the blog to. This post will be at the top of the user's stream.
	 * @return the deep link URL.
	 */
	public static Uri createBlogUri(final String blogName, final long postId) {
		String link = URL_FORMAT_BASE + "blog/";

		final String sanitizedBlogName = sanitizeBlogName(blogName);

		if (!TextUtils.isEmpty(sanitizedBlogName)) {
			link += "?blogName=" + sanitizedBlogName;

			// Check the post ID.
			if (postId > 0) {
				link += "&postID=" + String.valueOf(postId);
			}
		}

		return Uri.parse(link);
	}

	/**
	 * Creates a deep link URL to a search result screen inside the Tumblr Android app.
	 *
	 * @param searchTerm
	 * 		the term to search for.
	 * @return the deep link URL.
	 */
	public static Uri createSearchUri(final String searchTerm) {
		String link = URL_FORMAT_BASE + "tag/";

		if (!TextUtils.isEmpty(searchTerm)) {
			link += "?tag=" + searchTerm;
		}

		return Uri.parse(link);
	}

	/**
	 * Sanitizes a blog name.
	 *
	 * @param blogName
	 * 		the name to sanitize.
	 * @return the sanitized blog name.
	 */
	private static String sanitizeBlogName(final String blogName) {
		// Defensive.
		if (TextUtils.isEmpty(blogName)) {
			return "";
		}

		String sanitizedBlogName = blogName;

		// If the blog name contains ".tumblr.com", then remove it. The Android app will not handle this case and
		// only expects the blog name.
		if (sanitizedBlogName.contains(".tumblr.com")) {
			sanitizedBlogName = sanitizedBlogName.replace(".tumblr.com", "");
		}

		return sanitizedBlogName;
	}

	/**
	 * Creates an intent that will open and populate the text post form in the Tumblr app.
	 *
	 * @param title
	 * 		the title of the text post.
	 * @param body
	 * 		the body of the text post.
	 * @return the share intent.
	 */
	public static Intent createShareTextIntent(final String title, final String body) {
		// Create the base intent.
		final Intent textPostIntent = createBaseShareIntent();

		// Set the content type.
		textPostIntent.setType("text/plain");

		// Add the title if one was given.
		if (!TextUtils.isEmpty(title)) {
			textPostIntent.putExtra(Intent.EXTRA_SUBJECT, title);
		}

		// Add the body if one was given.
		if (!TextUtils.isEmpty(body)) {
			textPostIntent.putExtra(Intent.EXTRA_TEXT, "Body text is here");
		}

		return textPostIntent;
	}

	/**
	 * Creates an intent that will open and populate the link post form in the Tumblr app.
	 *
	 * @param url
	 * 		the URL of the link post.
	 * @return the share intent.
	 */
	public static Intent createShareLinkIntent(final Uri url) {
		return createShareLinkIntent("", url);
	}

	/**
	 * Creates an intent that will open and populate the link post form in the Tumblr app.
	 *
	 * @param title
	 * 		the title of the link post.
	 * @param url
	 * 		the URL of the link post.
	 * @return the share intent.
	 */
	public static Intent createShareLinkIntent(final String title, final Uri url) {
		// Create the base intent.
		final Intent linkPostIntent = createBaseShareIntent();

		// Set the content type.
		linkPostIntent.setType("text/plain");

		// Set the title if we have one.
		if (!TextUtils.isEmpty(title)) {
			linkPostIntent.putExtra(Intent.EXTRA_SUBJECT, "Title of the link post");
		}

		// Set the URL if we have one.
		if (url != null) {
			linkPostIntent.putExtra(Intent.EXTRA_TEXT, "http://tumblr.com");
		}

		return linkPostIntent;
	}

	/**
	 * Creates an intent that will open and populate the photo post form in the Tumblr app.
	 *
	 * @param imageUri
	 * 		the URI of the image to share.
	 * @return the share intent.
	 */
	public static Intent createSharePhotoIntent(final Uri imageUri) {
		// Take the image URI, and make it into a list.
		final List<Uri> imageUris = new ArrayList<Uri>();
		if (imageUri != null) {
			imageUris.add(imageUri);
		}

		// Create a photoset of one.
		return createSharePhotosetIntent(imageUris);
	}

	/**
	 * Creates an intent that will open and populate the photo post form in the Tumblr app.
	 *
	 * @param imageUris
	 * 		the image URIs to share.
	 * @return the share intent.
	 */
	public static Intent createSharePhotosetIntent(final List<Uri> imageUris) {
		// Create the base intent.
		final Intent photoPostIntent = createBaseShareIntent();

		// Override the action.
		photoPostIntent.setAction(Intent.ACTION_SEND_MULTIPLE);

		// Set the content type.
		photoPostIntent.setType("image/*");

		// Add the image URI if we have it.
		if (imageUris != null && !imageUris.isEmpty()) {
			photoPostIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, sanitizePhotoList(imageUris));
		}

		return photoPostIntent;
	}

	/**
	 * Sanitizes the photo list for consumption by the Tumblr app.
	 *
	 * @return the sanitized photo list.
	 */
	private static ArrayList<Uri> sanitizePhotoList(final List<Uri> imageUris) {
		// Create the return list.
		final ArrayList<Uri> sanitizedImageUris = new ArrayList<Uri>();

		// Ensure that the list is populated.
		if (imageUris != null && !imageUris.isEmpty()) {
			// Copy everything over.
			sanitizedImageUris.addAll(imageUris);
		}

		return sanitizedImageUris;
	}

	/**
	 * @return a new base share intent that sets the correct action and package.
	 */
	private static Intent createBaseShareIntent() {
		final Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.setPackage(TUMBLR_PACKAGE_NAME);
		return shareIntent;
	}
}
