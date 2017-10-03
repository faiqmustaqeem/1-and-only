package com.codiansoft.oneandonly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.codiansoft.oneandonly.adapter.PropertiesListViewAdapter;
import com.codiansoft.oneandonly.dialog.RegisterDialog;
import com.codiansoft.oneandonly.model.PropertyListItemDataModel;

import java.util.ArrayList;

public class PropertiesActivity extends AppCompatActivity implements View.OnClickListener{
    Button bRegister,bAddPicture,bSettings;

    ListView listView;
    private static PropertiesListViewAdapter adapter;
    ArrayList<PropertyListItemDataModel> dataModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties);

        initUI();
        dataModels= new ArrayList<>();

/*
        dataModels.add(new PropertyListItemDataModel("Villa with 3 bedrooms","For Rent","Karachi","2:00","1","Its at 2nd floor . Only three rooms one kitchen n one washroom with open roof. U can use it as washing area . Separate electric n gas meters. Rent is negotiable.","03001234567","03112345678","xyz@abc.com","https://cdn.houseplans.com/product/o2d2ui14afb1sov3cnslpummre/w560x373.jpg?v=15","24.917785","67.097034"));
        dataModels.add(new PropertyListItemDataModel("House 200 sq. yards","For Rent","Lahore","6:45","1","E 11/3 one kanal 3 bed room with bath dd Living servant room store Meter separate Gate Separate V.Good Location ","03001234567","03112345678","xyz@abc.com","https://photos-a.propertyimages.ie/media/6/0/9/3778906/5a21c496-24d6-4c9c-b26b-0d2c44ac11f3_l.jpg","31.586486","74.311647"));
        dataModels.add(new PropertyListItemDataModel("Beautiful House for sale","For Sale","Islamabad","12:55","1","","03001234567","03112345678","xyz@abc.com","","33.726910","73.039066"));
        dataModels.add(new PropertyListItemDataModel(" 1 Kanal Bungalow","For Sale","Faisalabad","23:00","1","","03001234567","03112345678","xyz@abc.com","","31.407442","73.112550"));
        dataModels.add(new PropertyListItemDataModel("Ground portion","For Rent","Rawalpindi","21:54","1","","03001234567","03112345678","xyz@abc.com","","32.173942","-110.820111"));
        dataModels.add(new PropertyListItemDataModel(" New house at prime location","For Sale","Hyderabad","5:23","1","","03001234567","03112345678","xyz@abc.com","","32.173942","-110.820111"));
        dataModels.add(new PropertyListItemDataModel("Bungalow ideal for big family","For Rent","Peshawar","6:54","1","","03001234567","03112345678","xyz@abc.com","","32.173942","-110.820111"));
        dataModels.add(new PropertyListItemDataModel("Golf Villa","For Sale","Quetta","3:56","1","","03001234567","03112345678","xyz@abc.com","","32.173942","-110.820111"));
        dataModels.add(new PropertyListItemDataModel("Basement For Rent","For Rent","Sargodha","5:56","1","","03001234567","03112345678","xyz@abc.com","","32.173942","-110.820111"));
        dataModels.add(new PropertyListItemDataModel("Property #10","For Sale","Sialkot","5:34","1","","03001234567","03112345678","xyz@abc.com","","32.173942","-110.820111"));
        dataModels.add(new PropertyListItemDataModel("Property #11","For Sale","Karachi","21:21","1","","03001234567","03112345678","xyz@abc.com","","32.173942","-110.820111"));
        dataModels.add(new PropertyListItemDataModel("Property #12","For Sale","Lahore","12:01","1","","03001234567","03112345678","xyz@abc.com","","32.173942","-110.820111"));
        dataModels.add(new PropertyListItemDataModel("Property #13","For Sale","Lahore","23:07","1","","03001234567","03112345678","xyz@abc.com","","32.173942","-110.820111"));
*/

        adapter= new PropertiesListViewAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PropertyListItemDataModel dataModel= dataModels.get(position);

                GlobalClass.selectedPropertyName = dataModel.getName();
                GlobalClass.selectedPropertyCity = dataModel.getCity();
                GlobalClass.selectedPropertyUpdateTime = dataModel.getLastUpdateTime();
                GlobalClass.selectedPropertyID = dataModel.getID();
                GlobalClass.selectedPropertyDetails = dataModel.getDetails();
                GlobalClass.selectedPropertyContact1 = dataModel.getContact1();
                GlobalClass.selectedPropertyContact2 = dataModel.getContact2();
                GlobalClass.selectedPropertyEmail = dataModel.getEmail();
                GlobalClass.selectedPropertyImageURL = dataModel.getImageURL();
                GlobalClass.selectedPropertyCategory = dataModel.getCategory();
                GlobalClass.selectedPropertyLatitude = dataModel.getLatitude();
                GlobalClass.selectedPropertyLongitude = dataModel.getLongitude();

                Intent i = new Intent(PropertiesActivity.this, AdDetailsActivity.class);
                i.putExtra("restaurantName",dataModel.getName());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }

    private void initUI() {
        listView = (ListView) findViewById(R.id.list);
        bRegister = (Button) findViewById(R.id.bRegister);
        bAddPicture = (Button) findViewById(R.id.bAddPicture);
        bSettings = (Button) findViewById(R.id.bSettings);

        bRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bRegister:
                RegisterDialog registerDialog=new RegisterDialog(this);
                registerDialog.show();
                break;
            case R.id.bAddPicture:

                break;
            case R.id.bSettings:

                break;

        }
    }
}
