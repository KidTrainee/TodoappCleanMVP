package vn.bfc.todoappcleanmvp.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import vn.bfc.todoappcleanmvp.R;
import vn.bfc.todoappcleanmvp.domain.model.Task;

import static com.google.common.base.Preconditions.checkNotNull;

public class TasksAdapter extends BaseAdapter {

    private List<Task> mTasks;
    private TasksItemListener mItemListener;

    public TasksAdapter(List<Task> tasks, TasksItemListener itemListener) {
        this.mTasks = tasks;
        this.mItemListener = itemListener;
    }

    public void replaceData(List<Task> tasks) {
        setList(tasks);
        notifyDataSetChanged();
    }

    private void setList(List<Task> tasks) {
        mTasks = checkNotNull(tasks);
    }

    @Override
    public int getCount() {
        return mTasks.size();
    }

    @Override
    public Task getItem(int pos) {
        return mTasks.get(pos);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.task_item, viewGroup, false);
        }

        final Task task = getItem(i);

        TextView titleTV = rowView.findViewById(R.id.title);
        titleTV.setText(task.getTitleForList());

        CheckBox completeCB = rowView.findViewById(R.id.complete);

        // Active / completed task UI
        completeCB.setChecked(task.isCompleted());
        if (task.isCompleted()) {
            rowView.setBackgroundDrawable(viewGroup.getContext()
            .getResources().getDrawable(R.drawable.list_completed_touch_feedback));
        } else {
            rowView.setBackgroundDrawable(viewGroup.getContext()
                    .getResources().getDrawable(R.drawable.touch_feedback));
        }

        completeCB.setOnClickListener((v) -> {
            if (!task.isCompleted()) {
                mItemListener.onCompleteTaskClick(task);
            } else {
                mItemListener.onActivateTaskClick(task);
            }
        });

        rowView.setOnClickListener((v) -> {
            mItemListener.onTaskClick(task);
        });
        return rowView;
    }
}
