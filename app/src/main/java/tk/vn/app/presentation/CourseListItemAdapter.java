package tk.vn.app.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import tk.vn.app.R;
import tk.vn.app.model.CompactSubscriptionBean;

/**
 * Created by neelp on 01-04-2018.
 */

public class CourseListItemAdapter extends ArrayAdapter<CompactSubscriptionBean> {

    Context context;
    CompactSubscriptionBean[] list;

    CourseListItemAdapter(Context context, CompactSubscriptionBean[] list) {
        super(context, -1, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.course_list_item, parent, false);
        TextView subscriptionName = (TextView) rowView.findViewById(R.id.courseName);
        TextView subscriptionStatus = (TextView) rowView.findViewById(R.id.courseStatus);
        TextView subscriptionInfo = (TextView) rowView.findViewById(R.id.courseInfo);

        final CompactSubscriptionBean t = list[position];
//        rowView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        System.out.println("adapter "+t);
        subscriptionName.setText(t.getCourseName() != null ? t.getCourseName() : "");
        subscriptionInfo.setText(t.getInfo() != null ? t.getInfo() : "");
        subscriptionStatus.setText((t.getSubscriptionStatus() != null ? t.getSubscriptionStatus() : ""));
        if(t.getSubscriptionStatus()!=null&&!t.getSubscriptionStatus().isEmpty()){
            subscriptionStatus.setVisibility(View.VISIBLE);
        }
        // TODO verify details configured in view

        return rowView;
    }

    @Override
    public int getCount() {
        return list.length;
    }



}