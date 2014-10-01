package tumblr.sharesampleproject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Sample activity. This could be YOUR app!
 *
 * @author haseman
 */
public class SampleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new SampleFragment())
					.commit();
		}
	}

	/**
	 * A sample fragment containing a few text views.
	 *
	 * @author haseman
	 */
	public static class SampleFragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_my, container, false);

			TextView tv = (TextView) rootView.findViewById(R.id.create_text_post);
			tv.setOnClickListener(mTextPostListener);

			tv = (TextView) rootView.findViewById(R.id.create_link_post);
			tv.setOnClickListener(mLinkPostListner);

			tv = (TextView) rootView.findViewById(R.id.create_photo_post);
			tv.setOnClickListener(mPhotoPostListener);

			tv = (TextView) rootView.findViewById(R.id.create_multiple_photo_post);
			tv.setOnClickListener(mMultiPhotoPostListener);

			tv = (TextView) rootView.findViewById(R.id.view_blog);
			tv.setOnClickListener(mViewBlogListener);

			tv = (TextView) rootView.findViewById(R.id.view_post);
			tv.setOnClickListener(mViewPostListener);

			tv = (TextView) rootView.findViewById(R.id.view_tag);
			tv.setOnClickListener(mViewTagListener);

			// Check for the Tumblr app
			if (!TumblrUtils.isTumblrInstalled(getActivity())) {
				Toast.makeText(getActivity().getApplicationContext(), "Tumblr is not on your phone, " +
						"you're gonna have a bad time", Toast.LENGTH_LONG).show();
			}

			return rootView;
		}

		private final View.OnClickListener mTextPostListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Create the text post intent.
				final Intent textPostIntent = TumblrUtils.createShareTextIntent(
						"This is the post title.", "This is the body text.");

				// Share!
				getActivity().startActivity(textPostIntent);
			}
		};

		private final View.OnClickListener mLinkPostListner = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Create the link post intent.
				final Intent linkPostIntent = TumblrUtils.createShareLinkIntent(
						"Title of the link post", Uri.parse("http://tumblr.com"));

				// Share!
				getActivity().startActivity(linkPostIntent);
			}
		};

		private final View.OnClickListener mPhotoPostListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Create the photo post intent.
				final Intent photoPostIntent = TumblrUtils.createSharePhotoIntent(
						Uri.parse("content://media/external/images/media/1"));

				// Share!
				getActivity().startActivity(photoPostIntent);
			}
		};

		private final View.OnClickListener mMultiPhotoPostListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Create a photo post with several photos (using sample Uri's).
				final List<Uri> photoList = new ArrayList<Uri>();
				photoList.add(Uri.parse("content://media/external/images/media/1"));
				photoList.add(Uri.parse("content://media/external/images/media/2"));
				photoList.add(Uri.parse("content://media/external/images/media/3"));

				// Create the photoset post intent.
				final Intent photosetPostIntent = TumblrUtils.createSharePhotosetIntent(photoList);

				// Share!
				getActivity().startActivity(photosetPostIntent);
			}
		};

		private final View.OnClickListener mViewBlogListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Deep link to a blog.
				final Uri blogDataUri = TumblrUtils.createBlogUri("haseman");

				// Start the Tumblr app.
				TumblrUtils.startTumblr(getActivity(), blogDataUri);
			}
		};

		private final View.OnClickListener mViewPostListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Deep link to a specific post.
				final Uri postDataUri = TumblrUtils.createBlogUri("haseman", 94288096341L);

				// Start the Tumblr app.
				TumblrUtils.startTumblr(getActivity(), postDataUri);
			}
		};

		private final View.OnClickListener mViewTagListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Deep link to a search results page.
				final Uri searchDataUri = TumblrUtils.createSearchUri("cars");

				// Start the Tumblr app.
				TumblrUtils.startTumblr(getActivity(), searchDataUri);
			}
		};
	}
}
