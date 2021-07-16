package com.baobing.cc.expandlistviewwithcheckbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ExpandListViewAdapter.RefreshUpdateData {
    List<FirstModel> mListData;
    private ExpandableListView mLv;
    private ExpandListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDate();
        mLv = ((ExpandableListView) findViewById(R.id.lv));

        mAdapter = new ExpandListViewAdapter(mListData, this);
        mAdapter.setRefreshUpdateData(this);
        mLv.setAdapter(mAdapter);
//        mLv.collapseGroup(10);
    }

    private void initDate() {
        mListData = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            FirstModel firstModel = new FirstModel();
            List<SecondModel> listSecondModel = new ArrayList<>();
            firstModel.setCheck(false);
            firstModel.setTitle("第一级" + i);
            firstModel.setListSecondModel(listSecondModel);
            mListData.add(firstModel);
            for (int j = 0; j < 4; j++) {
                SecondModel secondModel = new SecondModel();
                List<ThirdModel> thirdModelList = new ArrayList<>();
                secondModel.setCheck(false);
                secondModel.setTitle("第二级" + j);
                secondModel.setListThirdModel(thirdModelList);
                listSecondModel.add(secondModel);
                for (int k = 0; k < 2; k++) {
                    ThirdModel thirdModel = new ThirdModel();
                    thirdModel.setCheck(false);
                    thirdModel.setTitle("第三级" + k);
                    thirdModelList.add(thirdModel);
                }
            }
        }
    }

    public void btnOnclick(View view) {
        List<String> selectResult = new ArrayList<>();
        for (int i = 0; i < mListData.size(); i++) {
            List<SecondModel> listSecondModel = mListData.get(i).getListSecondModel();
            for (int j = 0; j < listSecondModel.size(); j++) {
                List<ThirdModel> listThirdModel = listSecondModel.get(j).getListThirdModel();
                for (int k = 0; k < listThirdModel.size(); k++) {
                    StringBuilder address = new StringBuilder();

                    address.append("name：");
                    if (listThirdModel.get(k).isCheck()) {
                        selectResult.add(address.append(listThirdModel.get(k).getTitle()).toString());
                    }
                }
            }
        }
        Log.d("bigname", "btnOnclick: "+selectResult);
    }

    @Override
    public void onRefreshData(int position, boolean isRefresh) {
        Log.e("--MainActivity--", "---" + position + "---" + isRefresh );
        mAdapter.notifyDataSetChanged();
    }
}
