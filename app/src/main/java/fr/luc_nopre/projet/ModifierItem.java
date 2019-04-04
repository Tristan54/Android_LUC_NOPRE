package fr.luc_nopre.projet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.time.LocalDateTime;

public class ModifierItem extends AppCompatActivity {

    private TodoItem todo;


    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_item);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        todo = (TodoItem) intent.getSerializableExtra("item");


        // On initilaise les editText à la date et l'heure actuelle
        EditText dateD = findViewById(R.id.date);
        EditText heureD = findViewById(R.id.heure);

        LocalDateTime dateCour = todo.getDate();
        dateD.setText(dateCour.getDayOfMonth() + "/" + dateCour.getMonthValue() + "/" + dateCour.getYear());
        if (dateCour.getMinute() < 10) {
            heureD.setText(dateCour.getHour() + ":0" + dateCour.getMinute());
        } else {
            heureD.setText(dateCour.getHour() + ":" + dateCour.getMinute());
        }
        EditText text = (EditText) findViewById(R.id.textListe);
        text.setText(todo.getLabel());

        RadioGroup radio = findViewById(R.id.radio);
        if(todo.getTag() == TodoItem.Tags.Faible){
            radio.check(R.id.radioButton);
        }else if(todo.getTag() == TodoItem.Tags.Normal){
            radio.check(R.id.radioButton2);
        }else{
            radio.check(R.id.radioButton3);
        }

        TextView pos = (TextView) findViewById(R.id.position);
        pos.setText("Position : "+todo.getPosition());


        Button valider = findViewById(R.id.valider);
        valider.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {

                EditText text = (EditText) findViewById(R.id.textListe);
                String label = String.valueOf(text.getText());
                if (label.equals("")) {
                    label = "item";
                }

                RadioGroup radio = findViewById(R.id.radio);
                int id = radio.getCheckedRadioButtonId();

                TodoItem.Tags t = null;

                switch (id) {
                    case R.id.radioButton: {
                        t = TodoItem.Tags.Faible;
                        break;
                    }
                    case R.id.radioButton2: {
                        t = TodoItem.Tags.Normal;
                        break;
                    }
                    case R.id.radioButton3: {
                        t = TodoItem.Tags.Important;
                        break;
                    }
                }

                if (t == null) {
                    t = TodoItem.Tags.Normal;
                }

                EditText date = findViewById(R.id.date);
                EditText heure = findViewById(R.id.heure);

                final String SEPARATEUR = "/";
                String dateSep[] = date.getText().toString().split(SEPARATEUR);
                int day = Integer.parseInt(dateSep[0]);
                int month = Integer.parseInt(dateSep[1]);
                int years = Integer.parseInt(dateSep[2]);

                final String SEPARATEURH = ":";
                String heureSep[] = heure.getText().toString().split(SEPARATEURH);
                int hours = Integer.parseInt(heureSep[0]);
                int minutes = Integer.parseInt(heureSep[1]);

                LocalDateTime dateEcheance = LocalDateTime.of(years, month, day, hours, minutes);

                todo.setLabel(label);
                todo.setTag(t);
                todo.setDate(dateEcheance);

                TodoDbHelper.updateItem(todo, getBaseContext());

                finish();
            }
        });
    }

}
