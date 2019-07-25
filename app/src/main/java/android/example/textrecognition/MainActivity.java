package android.example.textrecognition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn;
    private final static int REQUEST_CAMERA =124;
    private FirebaseVisionTextRecognizer textRecognizer;
    FirebaseVisionImage image;
    FirebaseVisionFaceDetector detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=findViewById(R.id.recognize);
        FirebaseApp.initializeApp(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(takePictureIntent,REQUEST_CAMERA);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CAMERA && resultCode==RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
               detectFace(bitmap);

//            recognizeMyText(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    private void recognizeMyText(Bitmap bitmap) {
//        try{
//            image = FirebaseVisionImage.fromBitmap(bitmap);
//            textRecognizer = FirebaseVision
//                           .getInstance()
//                           .getOnDeviceTextRecognizer();
//        } catch(Exception e){
//            e.printStackTrace();
//        }
//        textRecognizer.processImage(image)
//                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
//                    @Override
//                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
//                        String resultText = firebaseVisionText.getText();
//                        if(resultText.isEmpty()){
//                            Toast.makeText(MainActivity.this, "NO TEXT RECOGNIZED", Toast.LENGTH_SHORT).show();
//                        }else{
//                            Intent intent =new Intent(MainActivity.this,ResultActivity.class);
//                            intent.putExtra("reco",resultText);
//                            startActivity(intent);
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
    private void detectFace(Bitmap bitmap){
        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                .setModeType(FirebaseVisionFaceDetectorOptions.ACCURATE_MODE)
                .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .setMinFaceSize(0.15f)
                .setTrackingEnabled(true)
                .build();
        try{
            image = FirebaseVisionImage.fromBitmap(bitmap);
            detector = FirebaseVision.getInstance()
                    .getVisionFaceDetector(options);
        } catch(Exception e){
            e.printStackTrace();
        }

        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                String resultText = "";
                int i=1;
                for(FirebaseVisionFace face: firebaseVisionFaces){
                    resultText=resultText.concat("\n"+i+".")
                            .concat("\nSmile: "+face.getSmilingProbability()*100+"%")
                            .concat("\nLeftEye"+face.getLeftEyeOpenProbability()*100+"%");

                    i++;
                }
                if(firebaseVisionFaces.size()==0){
                    Toast.makeText(MainActivity.this, "No Faces", Toast.LENGTH_SHORT).show();
                }
                else{
                    Bundle bundle = new Bundle();
                    bundle.putString("face",resultText);
                    DialogFragment resultDialog = new ResultDialog();
                    resultDialog.setArguments(bundle);
                    resultDialog.setCancelable(false);
                    resultDialog.show(getSupportFragmentManager(),"face");
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

}
