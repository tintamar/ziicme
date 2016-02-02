package org.tselex.ziicme;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Zcontactenvoi extends Activity  implements AdapterView.OnItemClickListener ,AdapterView.OnItemSelectedListener {
    private static final String TAG = "Zreceptionliste --->";
    private static final String GLOBAL_NUMBER = "GLOBAL_NUMBER";
    ZJSONvalues jsvl;
    List<String> name1 = new ArrayList<String>();
    List<String> phno1 = new ArrayList<String>();
    ArrayList<Zcontact> listecontact;
    ArrayList<Zcontact> tempArrayList;
    boolean booltempArrayList=false;
    ZcontactAdapter ma ;
    private String spinnerprefix;
    private String spinnercountry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecran_envoi);
        jsvl = getIntent().getParcelableExtra("JSVL");
        if(jsvl != null) {
            final TextView numorigine = (TextView) findViewById(R.id.numorigine);
            final TextView nom = (TextView) findViewById(R.id.nom);
            final EditText numgsm = (EditText) findViewById(R.id.numgsm);
            final Button boutonok = (Button) findViewById(R.id.buttonok);
            final Button boutonout = (Button) findViewById(R.id.buttonout);
            final Button boutonreset = (Button) findViewById(R.id.buttonreset);
            final EditText searchfield = (EditText)findViewById(R.id.search);
            spinnergest();
            numgsm.setHint(R.string.destnumber);
            numgsm.setHintTextColor(getResources().getColor(R.color.redcolor));
            searchfield.setHint(R.string.search);
            searchfield.setHintTextColor(getResources().getColor(R.color.redcolor));
            searchfield.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    booltempArrayList=true;
                    int textlength = cs.length();
                    tempArrayList = new ArrayList<Zcontact>();
                    for(Zcontact c: listecontact){
                        if (textlength <= c.getNom().length()) {
                            if (c.getNom().toLowerCase().contains(cs.toString().toLowerCase())) {
                                tempArrayList.add(c);
                            }
                        }
                    }
                    ma = new ZcontactAdapter(jsvl.GLOBAL_CONTEXT, R.layout.ecran_envoi_rubrique, tempArrayList);
                    ListView lv= (ListView) findViewById(R.id.listViewContact);
                    lv.setAdapter(ma);
                    //lv.setOnItemClickListener(this);
                    lv.setItemsCanFocus(false);
                    lv.setTextFilterEnabled(true);
                    lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub

                }
            });
            numorigine.setText(getString(R.string.yournumber) + jsvl.GLOBAL_NUMBER);
            if (rechsqliteSecure(jsvl.GLOBAL_NUMBER)) {
                nom.setText(getString(R.string.yourname) + jsvl.GLOBAL_NOM);
            }
//************************************************************************************
            getAllContacts();
            listecontact = new ArrayList<>();
            for (int i = 0; i < name1.size(); i++) {
                listecontact.add(new Zcontact(name1.get(i), phno1.get(i), false));
            }
            ma = new ZcontactAdapter(jsvl.GLOBAL_CONTEXT, R.layout.ecran_envoi_rubrique, listecontact);
            ListView lv= (ListView) findViewById(R.id.listViewContact);
            lv.setAdapter(ma);
            lv.setOnItemClickListener(this);
            lv.setItemsCanFocus(false);
            lv.setTextFilterEnabled(true);
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

//*************************************************************************************

            boutonok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                //recherrche au moins true en listecontact
                jsvl.numeroenvoi = numgsm.getText().toString();
                if (!jsvl.numeroenvoi.equals("")) {
                    jsvl.numeroenvoi=numbergest(jsvl.numeroenvoi);
                    jsvl.numeroenvoi=spinnerprefix+jsvl.numeroenvoi;
                    if (!rechsqliteStock("2", jsvl.GLOBAL_NUMBER, jsvl.numeroenvoi)) {
                        cresqliteStock("2", jsvl.GLOBAL_NUMBER,  jsvl.numeroenvoi);
                    }
                    traitlistecontact();
                    InternetConnectionDetector cd = new InternetConnectionDetector(jsvl.GLOBAL_CONTEXT);
                    if (cd.checkMobileInternetConn()) {
                       Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, ZMyIntentService.class);
                       intent.putExtra(GLOBAL_NUMBER, jsvl.GLOBAL_NUMBER);
                       jsvl.GLOBAL_CONTEXT.startService(intent);
                    }
                    Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("JSVL", jsvl);
                    jsvl.GLOBAL_CONTEXT.startActivity(intent);
                    finish();
                } else {
                    if(!traitlistecontact()) {
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.ecran_toast,
                                (ViewGroup) findViewById(R.id.toast_layout_root));
                        TextView text = (TextView) layout.findViewById(R.id.texttoast);
                        text.setText(R.string.selnumber);
                        Toast toast = new Toast(getApplicationContext());
                        toast.setView(layout);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.show();
                    }else{
                        InternetConnectionDetector cd = new InternetConnectionDetector(jsvl.GLOBAL_CONTEXT);
                        if (cd.checkMobileInternetConn()) {
                            Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, ZMyIntentService.class);
                            intent.putExtra(GLOBAL_NUMBER, jsvl.GLOBAL_NUMBER);
                            jsvl.GLOBAL_CONTEXT.startService(intent);
                        }
                        Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("JSVL", jsvl);
                        jsvl.GLOBAL_CONTEXT.startActivity(intent);
                        finish();
                    }
                }
            }});
            boutonout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("JSVL", jsvl);
                jsvl.GLOBAL_CONTEXT.startActivity(intent);
                finish();
            }});
            boutonreset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numgsm.setText("");
                    searchfield.setText("");
                    numgsm.setHint(R.string.destnumber);
                    numgsm.setHintTextColor(getResources().getColor(R.color.redcolor));
                    searchfield.setHint(R.string.search);
                    searchfield.setHintTextColor(getResources().getColor(R.color.redcolor));

                    resetlistecontact();
                    spinnergest();
            }});
        }
    }



    private class ZcontactAdapter extends ArrayAdapter<Zcontact> {

        private ArrayList<Zcontact> listecontact;
        public ZcontactAdapter(Context context,int type,ArrayList<Zcontact> listecontact) {
            super(context, type,listecontact);
            this.listecontact = new ArrayList<Zcontact>();
            this.listecontact.addAll(listecontact);
        }
        private class ViewHolder
        {
            private TextView enom;
            private TextView enumgsm;
            private CheckBox echeck;
        }
        @Override
        public View getView( int position, View convertView, ViewGroup parent) {
            final int pos= position;
            ViewHolder holder = null;
            if (convertView == null) {
                 LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                //convertView = LayoutInflater.from(getContext()).inflate(R.layout.ecran_envoi_rubrique, parent);
                convertView = vi.inflate(R.layout.ecran_envoi_rubrique, null);
                //holder = new ViewHolder();
                TextView enom = (TextView) convertView.findViewById(R.id.nom);
                TextView enumgsm = (TextView) convertView.findViewById(R.id.numgsm);
                CheckBox echeck = (CheckBox) convertView.findViewById(R.id.checkBox1);
                //holder.enom = (TextView) convertView.findViewById(R.id.nom);
                //holder.enumgsm = (TextView) convertView.findViewById(R.id.numgsm);
                //holder.echeck = (CheckBox) convertView.findViewById(R.id.checkBox1);
                echeck.setTag(position);
                //holder.echeck.setOnClickListener(new View.OnClickListener() {
                echeck.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        //le pb est de se positionner sur la bonne occurence de listecontact
                        // position (ou pos) indique d'où on part  et où on va = ne pas s'en servir
                        //int intag=(Integer)v.getTag();//marche pas
                        int intag = Integer.parseInt(cb.getTag().toString());
                        Zcontact contact = listecontact.get(intag);
                        if(contact.isSelected())
                            {contact.setSelected(false);}
                        else
                            {contact.setSelected(true);}
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            TextView enom = (TextView) convertView.findViewById(R.id.nom);
            TextView enumgsm = (TextView) convertView.findViewById(R.id.numgsm);
            CheckBox echeck = (CheckBox) convertView.findViewById(R.id.checkBox1);
            Zcontact contact = listecontact.get(position);
            enom.setText(contact.getNom());

            enumgsm.setText(contact.getNumgsm());
            echeck.setChecked(contact.isSelected());
            echeck.setTag(position);
            return convertView;
        }

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
    public  void getAllContacts() {

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
                "upper("+ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    // This inner cursor is for contacts that have multiple numbers.
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
                    while (pCur.moveToNext()) {
                        String number = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
                        if(number!=null){
                            name1.add(name);
                            phno1.add(number);
                            break;
                        }
                    }
                    pCur.close();
                }
            }
        }
        cur.close();
    }
  private void  cresqliteStock(String oper,String num, String numdest)
  {
        ZSTOCKziicmeGest db = new ZSTOCKziicmeGest(this);
        db.insertSTOCKziicmeOper2(oper, num, numdest);
  }
  private boolean rechsqliteStock(String oper,String numgsm,String numdest) {
        boolean trouve = false;
        ZSTOCKziicmeGest db = new ZSTOCKziicmeGest(this);
        ZSTOCKziicme zstock = db.selectSTOCKziicmeOper2unic(oper, numgsm, numdest);
        if (!zstock.getNumgsm().equals("")) {     //indique pas trouvé
            trouve = true;
        }
        return trouve;
  }

  private boolean rechsqliteSecure(String numgsm) {
            boolean trouve = false;
            ZSECUREziicmeGest db = new ZSECUREziicmeGest(this);
            ZSECUREziicme zsecure = db.selectNumSECUREziicme(numgsm);
            if (!zsecure.getNumGSM().equals("")) {     //indique pas trouvé

                trouve =true;
            }
            return trouve;
  }
    private boolean traitlistecontact() {
        boolean trouve = false;
        for (int i = 0; i < listecontact.size(); i++) {
            Zcontact contact = listecontact.get(i);
            if(contact.isSelected()) {
                trouve=true;
                String numero = contact.getNumgsm().toString();
                if (!rechsqliteStock("2", jsvl.GLOBAL_NUMBER, numero)) {
                    cresqliteStock("2", jsvl.GLOBAL_NUMBER, numero);
                }
            }
        }
        if (booltempArrayList) {
            for (int i = 0; i < tempArrayList.size(); i++) {
                Zcontact contact = tempArrayList.get(i);
                if (contact.isSelected()) {
                    trouve = true;
                    String numero = contact.getNumgsm().toString();
                    if (!rechsqliteStock("2", jsvl.GLOBAL_NUMBER, numero)) {
                        cresqliteStock("2", jsvl.GLOBAL_NUMBER, numero);
                    }
                }
            }
        }
        return trouve;
    }
    private void resetlistecontact() {
        for (int i = 0; i < listecontact.size(); i++) {
            Zcontact contact = listecontact.get(i);
            if(contact.isSelected()) {
                contact.setSelected(false);
            }
        }
        ma = new ZcontactAdapter(jsvl.GLOBAL_CONTEXT, R.layout.ecran_envoi_rubrique, listecontact);
        ListView lv= (ListView) findViewById(R.id.listViewContact);
        lv.setAdapter(ma);
        lv.setOnItemClickListener(this);
        lv.setItemsCanFocus(false);
        lv.setTextFilterEnabled(true);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    private String numbergest(String number) {
        String[] char1 = number.split("");
        String char2 = "";
        int j;
        if(!char1[1].equals("+")){
            if(char1[1].equals("0")) {
                for (int i = 2; i < char1.length; i++) {
                    j = i - 2;
                    char2 += char1[i];
                }
                number=char2;
            }
        }else{
            spinnerprefix="";
        }
        return number;
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("JSVL", jsvl);
        jsvl.GLOBAL_CONTEXT.startActivity(intent);
        finish();
    }

    private void spinnergest(){
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayList<CharSequence> al=new ArrayList<CharSequence>();
        CharSequence[] choices = getResources().getStringArray(R.array.country);
        for (int i = 0; i < choices.length; i++) {
            al.add(choices[i]);
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1, al);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(this);
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String country = tm.getSimCountryIso();
        country=country.toUpperCase();
        for (int i = 0; i < spinner.getCount(); i++) {
            spinnercountry=spinner.getItemAtPosition(i).toString();
            spinnercountry=spinnercountry.substring(0,2);
            if(spinnercountry.equals(country)){
                spinner.setSelection(i);
                spinnerprefix=spinner.getItemAtPosition(i).toString();
                spinnerprefix = spinnerprefix.substring(5,spinnerprefix.length());
                spinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.redcolor));
        spinnerprefix=parent.getItemAtPosition(position).toString();
        spinnerprefix=spinnerprefix.substring(5,spinnerprefix.length());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
