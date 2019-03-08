package fr.luc_nopre.projet;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by phil on 07/02/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TodoHolder> {

    private ArrayList<TodoItem> items;

    public RecyclerAdapter(ArrayList<TodoItem> items) {
        this.items = items;
    }

    @Override
    public TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new TodoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(TodoHolder holder, int position) {
        TodoItem it = items.get(position);
        holder.bindTodo(it);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class TodoHolder extends RecyclerView.ViewHolder {
        private Resources resources;
        private ImageView image;
        private Switch sw;
        private TextView label;
        private TodoItem item;

        public TodoHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.imageView);
            sw = (Switch) itemView.findViewById(R.id.switch1);
            label = (TextView) itemView.findViewById(R.id.textView);
            resources = itemView.getResources();
        }

        public void bindTodo(final TodoItem todo) {
            label.setText(todo.getLabel());
            sw.setChecked(todo.isDone());
            switch(todo.getTag()) {
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
                    if(todo.isDone()){
                        todo.setDone(false);
                    }else{
                        todo.setDone(true);
                    }

                    TodoDbHelper.updateDone(todo,v.getContext());
                }
            });
        }

    }
}
