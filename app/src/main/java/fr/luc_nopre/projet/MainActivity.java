package fr.luc_nopre.projet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private ArrayList<TodoItem> items;
    private RecyclerView recycler;
    private LinearLayoutManager manager;
    private RecyclerAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newAct = new Intent(getBaseContext(), CreationListe.class);
                startActivity(newAct);
            }
        });
        Log.i("INIT", "Fin initialisation composantes");

        // Test d'ajout d'un item
//        TodoItem item = new TodoItem(TodoItem.Tags.Important, "Réviser ses cours");
//        TodoDbHelper.addItem(item, getBaseContext());
//        item = new TodoItem(TodoItem.Tags.Normal, "Acheter du pain");
//        TodoDbHelper.addItem(item, getBaseContext());

        // On récupère les items
        items = TodoDbHelper.getItems(this);
        Log.i("INIT", "Fin initialisation items");

        // On trie la liste pour avoir dans l'ordre en fonction de leur position

        Collections.sort(items, new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem ti1, TodoItem ti2) {
                return ti1.compareTo(ti2);
            }
        });

        // On initialise le RecyclerView
        recycler = (RecyclerView) findViewById(R.id.recycler);
        manager = new LinearLayoutManager(this);
        recycler.setLayoutManager(manager);

        adapter = new RecyclerAdapter(items);
        recycler.setAdapter(adapter);

        setRecyclerViewItemTouchListener();
        Log.i("INIT", "Fin initialisation recycler");
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
            Intent dbmanager = new Intent(getBaseContext(),AndroidDatabaseManager.class);
            startActivity(dbmanager);
        }

        if (id == R.id.action_clear) {
            TodoDbHelper.clearBDD(this.getBaseContext());
            items.clear();
            recycler.getAdapter().notifyDataSetChanged();

        }

        return super.onOptionsItemSelected(item);
    }

    private void setRecyclerViewItemTouchListener() {
        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                changerPosition(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return true;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                TodoItem item = items.get(position);
                switch(swipeDir) {
                    case ItemTouchHelper.RIGHT:
                        item.setDone(true);
                        break;
                    case ItemTouchHelper.LEFT:
                        item.setDone(false);
                        break;
                }
                recycler.getAdapter().notifyItemChanged(position);
            }
        };


        recycler.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, recycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder dialogue = new AlertDialog.Builder(MainActivity.this);
                dialogue.setTitle("Delete entry");
                dialogue.setMessage("Are you sure you want to delete this entry?");

                dialogue.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                dialogue.setNegativeButton(android.R.string.no, null);
                dialogue.setIcon(android.R.drawable.ic_dialog_alert);
                dialogue.show();
            }

            @Override
            public void onLongClick(View view) {


            }
        }));


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recycler);
    }

    private void changerPosition(int n1, int n2){
        // avant changement
        TodoItem item1 = this.items.get(n1);
        TodoItem item2 = this.items.get(n2);

        Log.i("Move", "changement de position entre : "+n1+" et : "+n2);

        int tmp = item1.getPosition();
        item1.setPosition(item2.getPosition());
        TodoDbHelper.updatePosition(item1,this.getBaseContext());

        item2.setPosition(tmp);
        TodoDbHelper.updatePosition(item2,this.getBaseContext());
    }
}
