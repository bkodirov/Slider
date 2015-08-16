package com.crispkeys.slider;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnimatedView animatedView = new AnimatedView(this);

        List<Integer> resId = new ArrayList<Integer>(){
            {
                add(R.drawable.cheese_1);
                add(R.drawable.cheese_4);
            }
        };
        animatedView.setAdapter(new Adapter(this, resId));

        setContentView(animatedView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class Adapter  extends BaseAdapter{

        private final Context mContext;
        private final List<Integer> mResList;

        public Adapter(Context context, List<Integer> resList) {
            mContext = context;
            mResList = resList;
        }

        @Override
        int getCount() {
            return mResList.size();
        }

        @Override
        View getView(int position) {
            int resId = mResList.get(position);
            View root = LayoutInflater.from(mContext).inflate(R.layout.row_animated_view, null);
            ImageView imageView = (ImageView) root.findViewById(R.id.imageView);
            imageView.setImageResource(resId);
            return root;
        }
    }
}
