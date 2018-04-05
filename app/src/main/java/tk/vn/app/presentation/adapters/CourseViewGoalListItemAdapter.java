package tk.vn.app.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import tk.vn.app.R;
import tk.vn.app.model.CompactSubscriptionBean;
import tk.vn.app.model.GoalBean;

/**
 * Created by neelp on 01-04-2018.
 */

public class CourseViewGoalListItemAdapter extends ArrayAdapter<GoalBean> {

    Context context;
    List<GoalBean> list;

    public CourseViewGoalListItemAdapter(Context context, List<GoalBean> list) {
        super(context, -1, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.course_view_goal_item, parent, false);

        final GoalBean t = list.get(position);
        TextView goalName = (TextView) rowView.findViewById(R.id.goal_name_text);

        goalName.setText(t.getGoalName());

        // TODO verify details configured in view
        return rowView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

}