package fr.luc_nopre.projet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class creationListe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_liste);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newAct = new Intent(getBaseContext(),MainActivity.class);
                startActivity(newAct);
            }
        });


        Button valider = findViewById(R.id.valider);
        valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText text = (EditText)findViewById(R.id.textListe);
                String label = String.valueOf(text.getText());

                RadioGroup radio = findViewById(R.id.radio);
                int id = radio.getCheckedRadioButtonId();

                TodoItem.Tags t = null;

                switch (id) {
                    case R.id.radioButton  : {
                        t = TodoItem.Tags.Faible;
                        break;
                    }
                    case R.id.radioButton2 : {
                        t = TodoItem.Tags.Normal;
                        break;
                    }
                    case R.id.radioButton3 : {
                        t = TodoItem.Tags.Important;
                        break;
                    }
                }

                if(t == null){
                    t = TodoItem.Tags.Faible;
                }
                TodoItem item = new TodoItem(t,label);
                TodoDbHelper.addItem(item,getBaseContext());
                Intent newAct = new Intent(getBaseContext(),MainActivity.class);
                startActivity(newAct);
            }
        });
    }

}
