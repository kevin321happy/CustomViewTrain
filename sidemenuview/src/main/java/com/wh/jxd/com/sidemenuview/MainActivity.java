package com.wh.jxd.com.sidemenuview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 侧滑菜单控件
 */

public class MainActivity extends AppCompatActivity {

    private ListView mLv_menu;
    private ListView mLv_menu1;
    private int mLvCount = 5;
    private int mLv1Count = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLv_menu = (ListView) findViewById(R.id.lv_menu);
        mLv_menu1 = (ListView) findViewById(R.id.lv_menu1);
        mLv_menu.setAdapter(new MyAdapter("1"));
        mLv_menu1.setAdapter(new MyAdapter("2"));
    }


    public class MyAdapter extends BaseAdapter {
        private String type;

        public MyAdapter(String type) {
            this.type = type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public int getCount() {
            if (type.equals("1")) {
                return mLvCount;
            } else if ("2".equals(type)) {
                return mLv1Count;
            }else {
                return 0;
            }
        }


        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHodler hodler;
            if (convertView == null) {
                hodler = new ViewHodler();
                if ("1".equals(type)) {
                    convertView = View.inflate(MainActivity.this, R.layout.item_slid_menu, null);
                    hodler.tv_menu = convertView.findViewById(R.id.tv_delete);
                } else if ("2".equals(type)) {
                    convertView = View.inflate(MainActivity.this, R.layout.item_slid_menu1, null);
                    hodler.iv_menu = convertView.findViewById(R.id.tv_delete);
                }
                hodler.tv_content = convertView.findViewById(R.id.tv_content);
                convertView.setTag(hodler);
            } else {
                hodler = (ViewHodler) convertView.getTag();
            }
            hodler.tv_content.setText("社会我冲哥,人狠话不多:" + position);
            final View finalConvertView = convertView;
            if (hodler.tv_menu != null) {
                hodler.tv_menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLvCount--;
                        if (mLvCount < 0) {
                            mLvCount = 0;
                        }
//                        mLv_menu.removeView(finalConvertView);
                        notifyDataSetChanged();
                    }
                });
            }
            if (hodler.iv_menu != null) {
                hodler.iv_menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLv1Count--;
                        if (mLv1Count < 0) {
                            mLv1Count = 0;
                        }
//                        mLv_menu1.removeView(finalConvertView);
                        notifyDataSetChanged();
                    }
                });
            }
            return convertView;
        }

        private class ViewHodler {
            //内容视图
            private TextView tv_content;
            //菜单视图
            private TextView tv_menu;

            private ImageView iv_menu;

        }
    }
}
