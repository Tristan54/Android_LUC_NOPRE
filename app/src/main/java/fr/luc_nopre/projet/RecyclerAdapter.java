package fr.luc_nopre.projet;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TodoHolder> implements ItemTouchHelperAdapter{

    private ArrayList<TodoItem> items;

    public RecyclerAdapter(ArrayList<TodoItem> items) {
        this.items = items;
    }

    @Override
    public TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new TodoHolder(inflatedView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(TodoHolder holder, int position) {
        final TodoItem it = items.get(position);
        holder.bindTodo(it);
    }



    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class TodoHolder extends RecyclerView.ViewHolder {
        private Resources resources;
        private ImageView image;
        private Switch sw;
        private TextView label;
        private TextView date;

        private LinearLayout l;

        @SuppressLint("NewApi")
        public TodoHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.imageView);
            sw = (Switch) itemView.findViewById(R.id.switch1);
            label = (TextView) itemView.findViewById(R.id.textView);
            resources = itemView.getResources();
            date = itemView.findViewById(R.id.dateView);

            l = itemView.findViewById(R.id.linearLayout);
        }



        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bindTodo(final TodoItem todo) {
            final LocalDateTime dateActuel = LocalDateTime.now();
            final LocalDateTime d = todo.getDate();
            int day = d.getDayOfMonth();
            int month = d.getMonthValue();
            int years = d.getYear();

            int hour = d.getHour();
            int minute = d.getMinute();

            date.setText("date d'échéance : "+day+"/"+month+"/"+years+" "+hour+":"+minute);
            label.setText(todo.getLabel());
            sw.setChecked(todo.isDone());

            if(todo.isDone()){
                if(d.isEqual(dateActuel) || d.isBefore(dateActuel)){
                    l.setBackgroundColor(resources.getColor(R.color.red));
                }else{
                    l.setBackgroundColor(resources.getColor(R.color.gray));
                }
            }else{
                l.setBackgroundColor(resources.getColor(R.color.white));
            }

            if(d.isEqual(dateActuel) || d.isBefore(dateActuel) && todo.isDone()){
                TodoDbHelper.deleteItem(todo,itemView.getContext());
            }else if(d.isEqual(dateActuel) || d.isBefore(dateActuel)){
                l.setBackgroundColor(resources.getColor(R.color.red));
            }


            switch (todo.getTag()) {
                case Faible:
                    image.setBackgroundColor(resources.getColor(R.color.faible));
                    break;
                case Normal:
                    image.setBackgroundColor(resources.getColor(R.color.normal));
                    break;
                case Important:
                    image.setBackgroundColor(resources.getColor(R.color.important));
                    break;

            }
            sw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(d.isEqual(dateActuel) || d.isBefore(dateActuel)){
                        l.setBackgroundColor(resources.getColor(R.color.red));
                        if(todo.isDone()){
                            todo.setDone(false);
                        }else{
                            todo.setDone(true);
                        }
                        TodoDbHelper.updateDone(todo, v.getContext());
                    }else{
                        if (todo.isDone()) {
                            todo.setDone(false);
                            l.setBackgroundColor(resources.getColor(R.color.white));
                        } else {
                            todo.setDone(true);
                            l.setBackgroundColor(resources.getColor(R.color.gray));
                        }
                        TodoDbHelper.updateDone(todo, v.getContext());
                    }
                }
            });
        }
    }
}