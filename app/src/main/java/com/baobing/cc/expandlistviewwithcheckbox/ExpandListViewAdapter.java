package com.baobing.cc.expandlistviewwithcheckbox;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 *
 */

public class ExpandListViewAdapter extends BaseExpandableListAdapter {

    private static final int[] EMPTY_STATE_SET = {};
    /** State indicating the group is expanded. */
    private static final int[] GROUP_EXPANDED_STATE_SET = { android.R.attr.state_expanded };
    /** State indicating the group is empty (has no children). */
    private static final int[] GROUP_EMPTY_STATE_SET = { android.R.attr.state_empty };
    /** State indicating the group is expanded and empty (has no children). */
    private static final int[] GROUP_EXPANDED_EMPTY_STATE_SET = { android.R.attr.state_expanded,
            android.R.attr.state_empty };
    /** States for the group where the 0th bit is expanded and 1st bit is empty. */
    private static final int[][] GROUP_STATE_SETS = { EMPTY_STATE_SET, // 00
            GROUP_EXPANDED_STATE_SET, // 01
            GROUP_EMPTY_STATE_SET, // 10
            GROUP_EXPANDED_EMPTY_STATE_SET };// 11

    private List<FirstModel> mListData;
    private LayoutInflater mInflate;
    private Context context;

    public ExpandListViewAdapter(List<FirstModel> mListData, Context context) {
        this.mListData = mListData;
        this.context = context;
        this.mInflate = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return mListData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mListData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mListData.get(groupPosition).getListSecondModel().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public final void onGroupCollapsed(int groupPosition) {
        onGroupCollapsedEx(groupPosition);
        notifyDataSetChanged();
    }

    @Override
    public  final void onGroupExpanded(int groupPosition) {
        onGroupExpandedEx(groupPosition);
        notifyDataSetChanged();
    }

    protected void onGroupCollapsedEx(int groupPosition) {
    }

    protected void onGroupExpandedEx(int groupPosition) {
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        FirstHolder holder = null;
        if (convertView == null) {
            holder = new FirstHolder();
            convertView = mInflate.inflate(R.layout.item_expand_lv_first, parent, false);
            holder.tv = ((TextView) convertView.findViewById(R.id.tv));
            holder.cb = ((CheckBox) convertView.findViewById(R.id.cb));
            holder.indicator = (ImageView) convertView.findViewById(R.id.indicator);
            convertView.setTag(holder);
        } else {
            holder = (FirstHolder) convertView.getTag();
        }
        setIndicatorState(holder.indicator.getDrawable(),groupPosition,isExpanded);
        holder.tv.setText(mListData.get(groupPosition).getTitle());
        final FirstHolder finalHolder = holder;
        finalHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = finalHolder.cb.isChecked();
                Log.d("bigname", "onclick: first:" + groupPosition + "," + isChecked);
                mListData.get(groupPosition).setCheck(isChecked);
                for (int i = 0; i < mListData.get(groupPosition).getListSecondModel().size(); i++) {
                    SecondModel secondModel = mListData.get(groupPosition).getListSecondModel().get(i);
                    secondModel.setCheck(isChecked);
                    for (int j = 0; j < secondModel.getListThirdModel().size(); j++) {
                        ThirdModel thirdModel = secondModel.getListThirdModel().get(j);
                        thirdModel.setCheck(isChecked);
                    }
                }
                notifyDataSetChanged();
            }
        });
        finalHolder.cb.setChecked(mListData.get(groupPosition).isCheck());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        SecondHolder holder = null;
//        if (convertView == null) {
//            holder = new SecondHolder();
//            convertView = mInflate.inflate(R.layout.item_expand_lv_second, parent, false);
//            holder.tv = ((TextView) convertView.findViewById(R.id.tv));
//            holder.cb = ((CheckBox) convertView.findViewById(R.id.cb));
//            convertView.setTag(holder);
//        } else {
//            holder = (SecondHolder) convertView.getTag();
//        }
//        holder.tv.setText(mListData.get(groupPosition).getListSecondModel().get(childPosition).getTitle());
//        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                mListData.get(groupPosition).getListSecondModel().get(childPosition).setCheck(isChecked);
//            }
//        });
//        holder.cb.setChecked(mListData.get(groupPosition).getListSecondModel().get(childPosition).isCheck());
//        return convertView;
//        Object object= (Object) getChild(groupPosition, childPosition);
//        CustomExpandableListView subObjects= () convertView;;
//        if (convertView==null) {
//            subObjects= new CustomExpandableListView(activity);
//        }
//        Adapter2 adapter= new Adapter2(activity, object);
//        subObjects.setAdapter(adapter);
//
//        return subObjects
        CustomExpandableListView lv = ((CustomExpandableListView) convertView);
        if (convertView == null) {
            lv = new CustomExpandableListView(context);
        }
        lv.setGroupIndicator(null);
        lv.setDivider(null);
        Log.d("bigname", "getChildView-first:" + groupPosition);
        SecondAdapter secondAdapter = new SecondAdapter(groupPosition,context, mListData.get(groupPosition).getListSecondModel());
        lv.setAdapter(secondAdapter);
//        lv.collapseGroup(10);
        return lv;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
    /*
  *   第二层的适配器
  * */
    class SecondAdapter extends BaseExpandableListAdapter {
        Context context;
        LayoutInflater inflater;
        List<SecondModel> listSecondModel;
        int position;

        public SecondAdapter(int position, Context context,List<SecondModel> listSecondModel) {
            this.context = context;
            this.position = position;
            this.listSecondModel = listSecondModel;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getGroupCount() {
            int size = listSecondModel.size();
            Log.d("bigname", "getGroupCount: "+size);
            return size;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return listSecondModel.get(groupPosition).getListThirdModel().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return listSecondModel.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return listSecondModel.get(groupPosition).getListThirdModel().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }



        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            SecondHolder holder = null;
            if (convertView == null) {
                holder = new SecondHolder();
                convertView = mInflate.inflate(R.layout.item_expand_lv_second, parent, false);
                holder.tv = ((TextView) convertView.findViewById(R.id.tv));
                holder.cb = ((CheckBox) convertView.findViewById(R.id.cb));
                holder.indicator = (ImageView) convertView.findViewById(R.id.indicator);
                convertView.setTag(holder);
            } else {
                holder = (SecondHolder) convertView.getTag();
            }
            setIndicatorState(holder.indicator.getDrawable(),groupPosition,isExpanded);
            holder.tv.setText(listSecondModel.get(groupPosition).getTitle());
            final SecondHolder secondHolder = holder;
            secondHolder.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = secondHolder.cb.isChecked();
                    Log.d("bigname", "onCheckedChanged: second:" + groupPosition + "," + isChecked);
                    listSecondModel.get(groupPosition).setCheck(isChecked);
                    for (int i = 0; i < listSecondModel.get(groupPosition).getListThirdModel().size(); i++) {
                        ThirdModel thirdModel = listSecondModel.get(groupPosition).getListThirdModel().get(i);
                        thirdModel.setCheck(isChecked);
                    }
                    //判断第一层是否需要选中
                    boolean oneCheck = false;
                    for (int j = 0; j < listSecondModel.size(); j++) {
                        if (listSecondModel.get(j).isCheck()) {
                            oneCheck = true;
                        } else {
                            oneCheck = false;
                            break;
                        }
                    }
                    Log.d("bigname", "onCheckedChanged: 第二层所有的都选中:" + oneCheck + "--" + position);
                    mListData.get(position).setCheck(oneCheck);
                    if (refreshUpdateData != null) {
                        refreshUpdateData.onRefreshData(position, oneCheck);
                    }
                    notifyDataSetChanged();
                }
            });
            secondHolder.cb.setChecked(listSecondModel.get(groupPosition).isCheck());
            notifyDataSetChanged();
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ThirdHolder holder = null;
            if (convertView == null) {
                holder = new ThirdHolder();
                convertView = mInflate.inflate(R.layout.item_expand_lv_third, parent, false);
                holder.tv = ((TextView) convertView.findViewById(R.id.tv));
                holder.cb = ((CheckBox) convertView.findViewById(R.id.cb));
                convertView.setTag(holder);
            } else {
                holder = (ThirdHolder) convertView.getTag();
            }
            holder.tv.setText(listSecondModel.get(groupPosition).getListThirdModel().get(childPosition).getTitle());
            final ThirdHolder thirdHolder = holder;
            thirdHolder.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = thirdHolder.cb.isChecked();
                    Log.d("bigname", "onCheckedChanged: third:" + groupPosition + "," + isChecked);
                    listSecondModel.get(groupPosition).getListThirdModel().get(childPosition).setCheck(isChecked);
                    //判断第二层是否需要选中
                    boolean secondAllCheck = false;
                    for (int i = 0; i < listSecondModel.get(groupPosition).getListThirdModel().size(); i++) {
                        if (listSecondModel.get(groupPosition).getListThirdModel().get(i).isCheck()) {
                            secondAllCheck = true;
                        }  else {
                            secondAllCheck = false;
                            break;
                        }
                    }
                    Log.d("bigname", "secondAllCheck: " + secondAllCheck );
                    if (secondAllCheck) {
                        listSecondModel.get(groupPosition).setCheck(true);
                    } else {
                        listSecondModel.get(groupPosition).setCheck(false);
                    }
                    //判断第一层是否需要选中  mListData.get(groupPosition).setCheck(isChecked);
                    Log.d("bigname", "onCheckedChanged: one:" + groupPosition );
                    Log.d("bigname", "onCheckedChanged: listSecondModel:" + listSecondModel.size() );
                    //判断第一层是否需要选中，即第二层全部被旋转
                    boolean oneCheck = false;
                    for (int i = 0; i < listSecondModel.size(); i++) {
                        //判断每一组的每一个元素是否被选中
                        if (listSecondModel.get(i).isCheck()) {
                            oneCheck = true;
                        } else {
                            oneCheck = false;
                        }
                    }
                    mListData.get(position).setCheck(oneCheck);
                    //刷新第一层
                    if (refreshUpdateData != null) {
                        refreshUpdateData.onRefreshData(position, oneCheck);
                    }
                    notifyDataSetChanged();
                }
            });
            thirdHolder.cb.setChecked(listSecondModel.get(groupPosition).getListThirdModel().get(childPosition).isCheck());

            return convertView;
        }


        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    //接口回调刷新第一层适配器
    public interface RefreshUpdateData{
        void onRefreshData(int position, boolean isRefresh);
    }

    private RefreshUpdateData refreshUpdateData;

    public void setRefreshUpdateData(RefreshUpdateData refreshUpdateData) {
        this.refreshUpdateData = refreshUpdateData;
    }

    protected void setIndicatorState(Drawable indicator, int groupPosition, boolean isExpanded) 	{
        final int stateSetIndex = (isExpanded ? 1 : 0) | // Expanded?
                (getChildrenCount(groupPosition) == 0 ? 2 : 0); // Empty?
        indicator.setState(GROUP_STATE_SETS[stateSetIndex]);
    }


    class FirstHolder {
        TextView tv;
        CheckBox cb;
        ImageView indicator;
    }

    class SecondHolder {
        TextView tv;
        CheckBox cb;
        ImageView indicator;
    }

    class ThirdHolder{
        TextView tv;
        CheckBox cb;
    }
}
