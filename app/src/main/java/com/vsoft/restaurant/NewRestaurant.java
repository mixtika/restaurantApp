package com.vsoft.restaurant;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;

public class NewRestaurant extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;
    private ImageView IMAGE_VIEW;
    private String sname = "";
    private EditText name,nit,property,street,phone;
    private MarkerOptions mko;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_restaurant);
        sname = Environment.getExternalStorageDirectory() + "/test.jpg";

        name = (EditText)findViewById(R.id.name);
        nit = (EditText)findViewById(R.id.nit);
        property = (EditText)findViewById(R.id.property);
        street = (EditText)findViewById(R.id.street);
        phone = (EditText)findViewById(R.id.phone);

        //name = Environment.getDataDirectory() + "/test.jpg";
        //Button btnAction = (Button)findViewById(R.id.btnPic);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng potosi = new LatLng (-19.5723589,-65.7617494);
        CameraPosition cameraPosition = CameraPosition.builder().target(potosi).zoom(16).build();
        mko = new MarkerOptions().position(potosi).title("Marcador en Potosi").draggable(true);
        mMap.addMarker(mko);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void btnSaveRestaurant(View view) {
        GetServer ob = new GetServer();
        String params="name="+name.getText()+"&nit="+nit.getText()+"&property="+property.getText()+"&street="+street.getText()+"&phone="+phone.getText()+"&lat="+ mko.getPosition().latitude+"&log="+ mko.getPosition().longitude;
        String cad=ob.stringQuery("http://10.0.2.2:3000/api/v1.0/save_restaurant?"+params,"POST");
        Toast.makeText(getApplicationContext(),"Registro correcto",Toast.LENGTH_LONG).show();
    }
    public void btnSearchPicture(View view) {
        IMAGE_VIEW = (ImageView) findViewById(R.id.logo);
        opciones(2);
    }
    public void btnSearchLocale(View view) {
        IMAGE_VIEW = (ImageView) findViewById(R.id.locale);
        opciones(2);//1
    }
    private void opciones(int op)
    {
        Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        int code = TAKE_PICTURE;
        if (op==1) {
            Uri output = Uri.fromFile(new File(sname));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        } else if (op==2){
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            code = SELECT_PICTURE;
        }
        startActivityForResult(intent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * Se revisa si la imagen viene de la c‡mara (TAKE_PICTURE) o de la galería (SELECT_PICTURE)
         */
        if (requestCode == TAKE_PICTURE) {
            /**
             * Si se reciben datos en el intent tenemos una vista previa (thumbnail)
             */
            if (data != null) {
                /**
                 * En el caso de una vista previa, obtenemos el extra data del intent y
                 * lo mostramos en el ImageView
                 */
                if (data.hasExtra("data")) {
                    //ImageView iv = (ImageView)findViewById(R.id.locale);
                    IMAGE_VIEW.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
                }
                /**
                 * De lo contrario es una imagen completa
                 */
            } else {
                /**
                 * A partir del nombre del archivo ya definido lo buscamos y creamos el bitmap
                 * para el ImageView
                 */
                //ImageView iv = (ImageView)findViewById(R.id.locale);
                IMAGE_VIEW.setImageBitmap(BitmapFactory.decodeFile(sname));
                /**
                 * Para guardar la imagen en la galería, utilizamos una conexión a un MediaScanner
                 */
                new MediaScannerConnectionClient() {
                    private MediaScannerConnection msc = null; {
                        msc = new MediaScannerConnection(getApplicationContext(), this); msc.connect();
                    }
                    public void onMediaScannerConnected() {
                        msc.scanFile(sname, null);
                    }
                    public void onScanCompleted(String path, Uri uri) {
                        msc.disconnect();
                    }
                };
            }
            /**
             * Recibimos el URI de la imagen y construimos un Bitmap a partir de un stream de Bytes
             */
        } else if (requestCode == SELECT_PICTURE){
            Uri selectedImage = data.getData();
            InputStream is;
            try {
                is = getContentResolver().openInputStream(selectedImage);
                BufferedInputStream bis = new BufferedInputStream(is);
                Bitmap bitmap = BitmapFactory.decodeStream(bis);
                //ImageView iv = (ImageView)findViewById(R.id.locale);
                IMAGE_VIEW.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {}
        }
    }
}
