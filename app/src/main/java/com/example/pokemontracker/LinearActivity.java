package com.example.pokemontracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.*;


public class LinearActivity extends AppCompatActivity {

    View.OnClickListener resetListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            defaultColors();
            et_nationalNumber.setText(getString(R.string._896));
            et_name.setText(getString(R.string.glastrier));
            et_species.setText(getString(R.string.wild_horse_pokemon));
            et_height.setText(getString(R.string._2_2_m));
            et_weight.setText(getString(R.string._800_0_kg));
            et_hp.setText("0");
            et_attack.setText("0");
            et_defence.setText("0");
            splevel.setSelection(0);
        }
    };

    AdapterView.OnItemSelectedListener spinListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String message = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };


    TextView appName, regisCatch, nationalNumber, name, species, gender, height, weight,
            level, baseStats, hp, attack, defence;

    EditText et_nationalNumber, et_name, et_species, et_height, et_weight,
            et_hp, et_attack, et_defence;
    RadioButton female, male, unk;

    Button reset, save, delete;

    ListView listLV;
    Spinner splevel;

    SimpleCursorAdapter adapter;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear);

        //Titles
        appName = requireViewById(R.id.AppName);
        regisCatch = requireViewById(R.id.newCatch);
        baseStats = requireViewById(R.id.baseStats);

        // editText headers
        nationalNumber = requireViewById(R.id.natNumber);
        name = requireViewById(R.id.pokeName);
        species = requireViewById(R.id.speciesCat);
        gender = requireViewById(R.id.pokeGender);
        height = requireViewById(R.id.pokeHeight);
        weight = requireViewById(R.id.pokeWeight);
        level = requireViewById(R.id.pokeLevel);
        hp = requireViewById(R.id.pokeHP);
        attack = requireViewById(R.id.pokeAttack);
        defence = requireViewById(R.id.pokeDefence);

        //editText IDs
        et_nationalNumber = requireViewById(R.id.et_natNumber);
        et_name = requireViewById(R.id.et_pokeName);
        et_species = requireViewById(R.id.et_speciesCat);
        et_height = requireViewById(R.id.et_pokeHeight);
        et_weight = requireViewById(R.id.et_pokeWeight);
        et_hp = requireViewById(R.id.et_pokeHP);
        et_attack = requireViewById(R.id.et_pokeAttack);
        et_defence = requireViewById(R.id.et_pokeDefence);

        //radio Buttons
        female = requireViewById(R.id.radioFem);
        male = requireViewById(R.id.radioMale);
        unk = requireViewById(R.id.radioUnk);

        //Buttons
        reset = requireViewById(R.id.resetButton);
        save = requireViewById(R.id.saveButton);
        delete = requireViewById(R.id.deleteButton);

        listLV = findViewById(R.id.list_LV);

        //Spinner
        splevel = requireViewById(R.id.spinLevel);
        splevel.setOnItemSelectedListener(spinListener);

        ArrayList<Integer> valuesList = new ArrayList<>();
        for(int i = 1; i <= 50; i++){
            valuesList.add(i);
        }

        ArrayAdapter<Integer> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,valuesList);
        splevel.setAdapter(typeAdapter);

        reset.setOnClickListener(resetListener);
        save.setOnClickListener(savePokemonListener);
    }

    private boolean validateInputs() {
        boolean allValid = true;

        defaultColors();

        //checking for empty inputs
        if (et_nationalNumber.getText().toString().isEmpty() ||
                et_name.getText().toString().isEmpty() ||
                et_species.getText().toString().isEmpty() ||
                et_height.getText().toString().isEmpty() ||
                et_weight.getText().toString().isEmpty() ||
                et_hp.getText().toString().isEmpty() ||
                et_attack.getText().toString().isEmpty() ||
                et_defence.getText().toString().isEmpty()) {
            allValid = false;
            Toast.makeText(getApplicationContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
        }

        int nNumberValue = Integer.parseInt(et_nationalNumber.getText().toString());
        if(nNumberValue < 0 || nNumberValue > 1010){
            Toast.makeText(getApplicationContext(), "Number must be between 0-1010.", Toast.LENGTH_SHORT).show();
            nationalNumber.setTextColor(Color.parseColor("Red"));
            allValid = false;
        }

        String nameInput = et_name.getText().toString();
        if (!validName(nameInput)) {
            Toast.makeText(getApplicationContext(), "Name length must be 3-12 characters.", Toast.LENGTH_SHORT).show();
            name.setTextColor(Color.parseColor("Red"));
            allValid = false;
        }

        String speciesInput = et_species.getText().toString();
        if(!validSpecies(speciesInput)){
            Toast.makeText(getApplicationContext(), "Only Letters and Characters.", Toast.LENGTH_SHORT).show();
            species.setTextColor(Color.parseColor("Red"));
            allValid = false;
        }

        boolean isMale = male.isChecked();
        boolean isFemale = female.isChecked();
        boolean isUnk  = unk.isChecked();
        if (!isMale && !isFemale && !isUnk) {
            Toast.makeText(getApplicationContext(), "Please select a gender.", Toast.LENGTH_SHORT).show();
            gender.setTextColor(Color.parseColor("Red"));
            allValid = false;
        }

        double heightValue = Double.parseDouble(et_height.getText().toString());
        if(heightValue < 0.3 || heightValue > 19.99){
            Toast.makeText(getApplicationContext(), "Height must be between 0.3-19.99.", Toast.LENGTH_SHORT).show();
            height.setTextColor(Color.parseColor("Red"));
            allValid = false;
        }

        double weightValue = Double.parseDouble(et_weight.getText().toString());
        if(weightValue < 0.1 || weightValue > 820){
            Toast.makeText(getApplicationContext(), "Weight must be between 0.1-820.", Toast.LENGTH_SHORT).show();
            weight.setTextColor(Color.parseColor("Red"));
            allValid = false;
        }



        int hpValue = Integer.parseInt(et_hp.getText().toString());
        if (hpValue < 1 || hpValue > 362) {
            Toast.makeText(getApplicationContext(), "HP must be 1 to 362.", Toast.LENGTH_SHORT).show();
            hp.setTextColor(Color.parseColor("Red"));

            allValid = false;
        }

        int attackValue = Integer.parseInt(et_attack.getText().toString());
        if (attackValue < 5 || attackValue > 526) {
            Toast.makeText(getApplicationContext(), "Attack must be 5 to 562.", Toast.LENGTH_SHORT).show();
            attack.setTextColor(Color.parseColor("Red"));

            allValid = false;
        }


        int defenceValue = Integer.parseInt(et_defence.getText().toString());
        if (defenceValue < 5 || defenceValue > 614) {
            Toast.makeText(getApplicationContext(), "Defence must be 5 to 614.", Toast.LENGTH_SHORT).show();
            defence.setTextColor(Color.parseColor("Red"));

            allValid = false;
        }

        return allValid;
    }

    private void defaultColors(){
        nationalNumber.setTextColor(Color.parseColor("Black"));
        name.setTextColor(Color.parseColor("Black"));
        species.setTextColor(Color.parseColor("Black"));
        gender.setTextColor(Color.parseColor("Black"));
        height.setTextColor(Color.parseColor("Black"));
        weight.setTextColor(Color.parseColor("Black"));

        hp.setTextColor(Color.parseColor("Black"));
        attack.setTextColor(Color.parseColor("Black"));
        defence.setTextColor(Color.parseColor("Black"));
    }

    public boolean validName(String s){
        if(s.length() > 12 || s.length() < 3){
            return false;
        }

        for(int i = 0; i < s.length(); i++){
            char a = Character.toLowerCase(s.charAt(i));
            if(a > 'z' || a < 'a'){
                if(a != '.' && a != ' '){
                    return false;
                }
            }
        }

        return true;
    }
    public boolean validSpecies(String s){
        for(int i = 0; i < s.length(); i++){
            char a = Character.toLowerCase(s.charAt(i));
            if(a > 'z' || a < 'a'){
                if(a != ' '){
                    return false;
                }
            }
        }
        return true;
    }

    String[] fromColumns = {
            PokemonDB.COL1_NAME,
            PokemonDB.COL2_NAME,
            PokemonDB.COL3_NAME,
            PokemonDB.COL4_NAME,
            PokemonDB.COL5_NAME,
            PokemonDB.COL6_NAME,
            PokemonDB.COL7_NAME,
            PokemonDB.COL8_NAME
    };

    int[] toViews = {
            R.id.tv_pokemon_national_number,
            R.id.tv_pokemon_name,
            R.id.tv_pokemon_species,
            R.id.tv_pokemon_height,
            R.id.tv_pokemon_weight,
            R.id.tv_pokemon_hp,
            R.id.tv_pokemon_attack,
            R.id.tv_pokemon_defense
    };

    public void updateListUI() {
        adapter = new SimpleCursorAdapter(
                getApplicationContext(),
                R.layout.list_item_pokemon,
                cursor,
                fromColumns,
                toViews,
                0
        );

        listLV.setAdapter(adapter);
    }

    View.OnClickListener savePokemonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (validateInputs()) {
                ContentValues values = new ContentValues();

                try {
                    int natNumberValue = Integer.parseInt(et_nationalNumber.getText().toString());
                    values.put(PokemonDB.COL1_NAME, natNumberValue);

                    values.put(PokemonDB.COL2_NAME, et_name.getText().toString());
                    values.put(PokemonDB.COL3_NAME, et_species.getText().toString());

                    double heightValue = Double.parseDouble(et_height.getText().toString());
                    values.put(PokemonDB.COL4_NAME, heightValue);

                    double weightValue = Double.parseDouble(et_weight.getText().toString());
                    values.put(PokemonDB.COL5_NAME, weightValue);

                    int hpValue = Integer.parseInt(et_hp.getText().toString());
                    values.put(PokemonDB.COL6_NAME, hpValue);

                    int attackValue = Integer.parseInt(et_attack.getText().toString());
                    values.put(PokemonDB.COL7_NAME, attackValue);

                    int defenceValue = Integer.parseInt(et_defence.getText().toString());
                    values.put(PokemonDB.COL8_NAME, defenceValue);

                    Uri newUri = getContentResolver().insert(PokemonDB.CONTENT_URI, values);

                    updateListUI();
                    Toast.makeText(getApplicationContext(), "Pokemon Stored", Toast.LENGTH_LONG).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid numeric input.", Toast.LENGTH_SHORT).show();
                }
            }

        }
    };


}