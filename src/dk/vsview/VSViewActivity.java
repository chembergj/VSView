package dk.vsview;

import dk.vsview.domain.ServerData;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class VSViewActivity extends Activity {

	private final String tagATCFragment = "tagATC";
	private final String tagFriendsFragment = "tagFriends";
	private ServerDataProviderTask dataProviderTask;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		getResources();
		
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab tabATC = actionBar.newTab();
		tabATC.setText(getResources().getString(R.string.title_tab_atc));

		TabListener<TabATCFragment> tabListenerATC = new TabListener<TabATCFragment>(
				this, tagATCFragment, TabATCFragment.class);
		tabATC.setTabListener(tabListenerATC);
		actionBar.addTab(tabATC);
		
		Tab tabFriends = actionBar.newTab();
		tabFriends.setText(getResources().getString(R.string.title_tab_friends));

		TabListener<TabFriendsFragment> tabListenerFriends = new TabListener<TabFriendsFragment>(
				this, tagFriendsFragment, TabFriendsFragment.class);

		tabFriends.setTabListener(tabListenerFriends);
		actionBar.addTab(tabFriends);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_menu, menu);
		return true;
	}

	public void onRefreshClick(MenuItem item) {
		// One of the group items (using the onClick attribute) was clicked
		// The item parameter passed here indicates which item it is
		// All other menu item clicks are handled by onOptionsItemSelected()

		TabATCFragment atcFragment = (TabATCFragment) getFragmentManager()
				.findFragmentByTag(tagATCFragment);
		atcFragment.loadATCData();
	}
}