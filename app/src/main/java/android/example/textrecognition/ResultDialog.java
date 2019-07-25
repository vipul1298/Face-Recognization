package android.example.textrecognition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

class ResultDialog extends DialogFragment {
    private Button okButton;
    private TextView result;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view =inflater.inflate(R.layout.fragment_resultdialog,container,false);
                 String resultText = "";
         okButton=view.findViewById(R.id.btn);
         result=view.findViewById(R.id.text);

         Bundle bundle = getArguments();
         resultText = bundle.getString("face");

         result.setText(resultText);
         okButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dismiss();
             }
         });


        return view;
    }
}
