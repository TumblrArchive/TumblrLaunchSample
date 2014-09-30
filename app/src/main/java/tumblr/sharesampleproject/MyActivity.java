package tumblr.sharesampleproject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {

	private static final String mPackageName = "com.tumblr";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my, container, false);

	        TextView tv = (TextView)rootView.findViewById(R.id.create_text_post);
	        tv.setOnClickListener(mTextPostListener);

	        tv = (TextView)rootView.findViewById(R.id.create_photo_post);
	        tv.setOnClickListener(mPhotoPostListener);

	        tv = (TextView)rootView.findViewById(R.id.create_multiple_photo_post);
	        tv.setOnClickListener(mMultiPhotoPostListener);

	        tv = (TextView)rootView.findViewById(R.id.view_blog);
	        tv.setOnClickListener(mViewBlogListener);

	        tv = (TextView)rootView.findViewById(R.id.view_post);
	        tv.setOnClickListener(mViewPostListener);

	        tv = (TextView)rootView.findViewById(R.id.view_tag);
	        tv.setOnClickListener(mViewTagListener);

	        //Check for the Tumblr app
	        PackageManager pm = getActivity().getPackageManager();
	        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
	        boolean found = false;
	        for(ApplicationInfo app : apps){
		        if(app.packageName.equals(mPackageName)){
			        found = true;
			        break;
		        }
	        }

	        if(!found){
		        Toast.makeText(getActivity().getApplicationContext(), "Tumblr is not on your phone, " +
				        "you're gonna have a bad time", Toast.LENGTH_LONG).show();
	        }

            return rootView;
        }

	    private final View.OnClickListener mTextPostListener = new View.OnClickListener(){
		    public void onClick(View v){

			    //Create a text post
			    Intent textPost = new Intent();
			    textPost.setAction(Intent.ACTION_SEND);
			    textPost.setPackage(mPackageName);
			    textPost.setType("text/plain");
			    textPost.putExtra(Intent.EXTRA_SUBJECT, "This is the Post Title");
			    textPost.putExtra(Intent.EXTRA_TEXT, "Body text is here");
			    getActivity().startActivity(textPost);
		    }
	    };

	    private final View.OnClickListener mPhotoPostListener = new View.OnClickListener() {
		    public void onClick(View v) {
			    //Create a photo post (using a sample content Uri)
			    Intent photoPost = new Intent();
			    photoPost.setAction(Intent.ACTION_SEND);
			    photoPost.setPackage(mPackageName);
			    photoPost.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://media/external/images/media/1"));
			    photoPost.setType("image/jpeg");
			    getActivity().startActivity(photoPost);
		    }
	    };

	    private final View.OnClickListener mMultiPhotoPostListener = new View.OnClickListener() {
		    public void onClick(View v) {
			    //Create a photo post with several photos (using sample Uri's)
			    ArrayList<Uri> photoArray = new ArrayList<Uri>();
			    photoArray.add(Uri.parse("content://media/external/images/media/1"));
			    photoArray.add(Uri.parse("content://media/external/images/media/2"));
			    photoArray.add(Uri.parse("content://media/external/images/media/3"));

			    Intent multiPhotoPost = new Intent();
			    multiPhotoPost.setAction(Intent.ACTION_SEND_MULTIPLE);
			    multiPhotoPost.setPackage(mPackageName);
			    multiPhotoPost.setType("image/jpeg");
			    multiPhotoPost.putParcelableArrayListExtra(Intent.EXTRA_STREAM, photoArray);
			    getActivity().startActivity(multiPhotoPost);
		    }
	    };

	    private final View.OnClickListener mViewBlogListener = new View.OnClickListener() {
		    public void onClick(View v) {
			    //Deep link to a blog
			    Intent viewBlogIntent = new Intent();
			    viewBlogIntent.setAction(Intent.ACTION_VIEW);
			    viewBlogIntent.addCategory(Intent.CATEGORY_BROWSABLE);
			    viewBlogIntent.setPackage(mPackageName);
			    viewBlogIntent.setData(Uri.parse("tumblr://x-callback-url/blog?blogName=haseman"));
			    getActivity().startActivity(viewBlogIntent);
		    }
	    };

	    private final View.OnClickListener mViewPostListener = new View.OnClickListener() {
		    public void onClick(View v) {
			    //Deep link to a post
			    Intent viewPostIntent = new Intent();
			    viewPostIntent.setAction(Intent.ACTION_VIEW);
			    viewPostIntent.addCategory(Intent.CATEGORY_BROWSABLE);
			    viewPostIntent.setPackage(mPackageName);
			    viewPostIntent.setData(Uri.parse("tumblr://x-callback-url/blog?blogName=haseman&postID=94288096341"));
			    getActivity().startActivity(viewPostIntent);
		    }
	    };

	    private final View.OnClickListener mViewTagListener = new View.OnClickListener() {
		    public void onClick(View v) {
			    //Deep link to a tag
			    Intent viewTagIntent = new Intent();
			    viewTagIntent.setAction(Intent.ACTION_VIEW);
			    viewTagIntent.addCategory(Intent.CATEGORY_BROWSABLE);
			    viewTagIntent.setPackage(mPackageName);
			    viewTagIntent.setData(Uri.parse("tumblr://x-callback-url/tag?tag=cars"));
			    getActivity().startActivity(viewTagIntent);
		    }
	    };
    }
}
