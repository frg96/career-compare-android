package com.frg96.careercompare;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.os.Looper;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.test.core.app.ActivityScenario;

import com.frg96.careercompare.ui.main.MainActivity;
import com.frg96.careercompare.ui.weights.ComparisonWeightFragment;
import com.google.android.material.slider.Slider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;



@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
@LooperMode(LooperMode.Mode.PAUSED)
public class ComparisonWeightFragmentTest {

    private ActivityScenario<MainActivity> scenario;
    private ComparisonWeightFragment frag;

    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            NavHostFragment navHost = (NavHostFragment)
                    activity.getSupportFragmentManager().findFragmentById(R.id.nav_host);
            assertNotNull(navHost);

            NavController nav = navHost.getNavController();
            nav.setGraph(R.navigation.nav_graph);

            try {
                nav.navigate(R.id.comparisonWeightFragment);
            } catch (IllegalArgumentException ignored) {
                nav.navigate(R.id.action_mainMenuFragment_to_comparisonWeightFragment);
            }

            activity.getSupportFragmentManager().executePendingTransactions();
            navHost.getChildFragmentManager().executePendingTransactions();
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            Fragment cwf = navHost.getChildFragmentManager().getPrimaryNavigationFragment();
            assertNotNull(cwf);
            assertTrue(cwf instanceof ComparisonWeightFragment);

            frag = (ComparisonWeightFragment) cwf;
        });
    }

    @After
    public void tearDown() {
        if (scenario != null) {
            scenario.close();
        }
        frag = null;
    }

    @Test
    public void test_salaryWeight_input(){
        float input = 4.0f;
        Slider slider = frag.requireView().findViewById(R.id.cw_salary_weight_slider);
        slider.setValue(input);

        assertEquals("Slider value should match the input", input, slider.getValue(), 0.001f);

        assertTrue(slider.getValue() >= slider.getValueFrom());
        assertTrue(slider.getValue() <= slider.getValueTo());
    }

    @Test
    public void test_bonusWeight_input(){
        float input = 9.0f;
        Slider slider = frag.requireView().findViewById(R.id.cw_bonus_weight_slider);
        slider.setValue(input);

        assertEquals("Slider value should match the input", input, slider.getValue(), 0.001f);

        assertTrue(slider.getValue() >= slider.getValueFrom());
        assertTrue(slider.getValue() <= slider.getValueTo());
    }

    @Test
    public void test_match401kWeight_input(){
        float input = 0.0f;
        Slider slider = frag.requireView().findViewById(R.id.cw_match401k_weight_slider);
        slider.setValue(input);

        assertEquals("Slider value should match the input", input, slider.getValue(), 0.001f);

        assertTrue(slider.getValue() >= slider.getValueFrom());
        assertTrue(slider.getValue() <= slider.getValueTo());
    }

    @Test
    public void test_internetStipendWeight_input(){
        float input = 1.0f;
        Slider slider = frag.requireView().findViewById(R.id.cw_internet_stipend_weight_slider);
        slider.setValue(input);

        assertEquals("Slider value should match the input", input, slider.getValue(), 0.001f);

        assertTrue(slider.getValue() >= slider.getValueFrom());
        assertTrue(slider.getValue() <= slider.getValueTo());
    }

    @Test
    public void test_accidentInsuranceWeight_input(){
        float input = 2.0f;
        Slider slider = frag.requireView().findViewById(R.id.cw_accident_insurance_weight_slider);
        slider.setValue(input);

        assertEquals("Slider value should match the input", input, slider.getValue(), 0.001f);

        assertTrue(slider.getValue() >= slider.getValueFrom());
        assertTrue(slider.getValue() <= slider.getValueTo());
    }

    @Test
    public void test_tuitionReimbursementWeight_input(){
        float input = 6.0f;
        Slider slider = frag.requireView().findViewById(R.id.cw_tuition_reimbursement_weight_slider);
        slider.setValue(input);

        assertEquals("Slider value should match the input", input, slider.getValue(), 0.001f);

        assertTrue(slider.getValue() >= slider.getValueFrom());
        assertTrue(slider.getValue() <= slider.getValueTo());
    }
}

