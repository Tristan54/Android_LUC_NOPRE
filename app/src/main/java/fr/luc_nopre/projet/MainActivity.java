package fr.luc_nopre.projet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<TodoItem> items;
    private RecyclerView recycler;
    private LinearLayoutManager manager;
    private RecyclerAdapter adapter;

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
        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
                // Non géré dans cet exemple (ce sont les drags) -> on retourne false
                return false;
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
                TodoDbHelper.updateDone(item, viewHolder.itemView.getContext());
                recycler.getAdapter().notifyItemChanged(position);
            }




        };

        recycler.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder dialogue = new AlertDialog.Builder(getApplicationContext());
                dialogue.setTitle("Delete entry");
                dialogue.setMessage("Are you sure you want to delete this entry?");

                dialogue.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                });

                dialogue.setNegativeButton(android.R.string.no, null);
                dialogue.setIcon(android.R.drawable.ic_dialog_alert);
                dialogue.show();

                return true;
            }
        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recycler);
    }
}
