package kg.auca.dice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static final int[] DIE_SIDES_IMAGE_RESOURCE = {
        R.drawable.ic_1,
        R.drawable.ic_2,
        R.drawable.ic_3,
        R.drawable.ic_4,
        R.drawable.ic_5,
        R.drawable.ic_6
    };
    public static final String FIRST_DIE_VALUE_KEY  = "FIRST_DIE_VALUE";
    public static final String SECOND_DIE_VALUE_KEY = "SECOND_DIE_VALUE";

    private ImageView firstDieImageView;
    private ImageView secondDieImageView;

    private Dice dice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstDieImageView = findViewById(R.id.firstDieImageView);
        secondDieImageView = findViewById(R.id.secondDieImageView);

        if (savedInstanceState != null) {
            int firstDieValue =
                savedInstanceState.getInt(FIRST_DIE_VALUE_KEY);
            int secondDieValue =
                savedInstanceState.getInt(SECOND_DIE_VALUE_KEY);

            dice = new Dice(firstDieValue, secondDieValue);
        } else {
            dice = new Dice();
        }

        updateImageViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(FIRST_DIE_VALUE_KEY, dice.getFirstDie());
        outState.putInt(SECOND_DIE_VALUE_KEY, dice.getSecondDie());
    }

    public void roll(View view) {
        dice.roll();
        updateImageViews();
    }

    private void updateImageViews() {
        int firstDie = dice.getFirstDie();
        int secondDie = dice.getSecondDie();

        int firstDieImageResource = DIE_SIDES_IMAGE_RESOURCE[firstDie - 1];
        int secondDieImageResource = DIE_SIDES_IMAGE_RESOURCE[secondDie - 1];

        firstDieImageView.setImageResource(firstDieImageResource);
        secondDieImageView.setImageResource(secondDieImageResource);
    }

}
