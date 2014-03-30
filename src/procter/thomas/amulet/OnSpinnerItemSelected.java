package procter.thomas.amulet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


public class OnSpinnerItemSelected implements OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent,
                View view, int pos, long id) {
            Toast.makeText(parent.getContext(), "Clicked : " +
                    parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();


        }

        @Override
        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }