package com.frg96.careercompare;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.test.core.app.ActivityScenario;

import com.frg96.careercompare.ui.joboffer.JobOfferFragment;
import com.frg96.careercompare.ui.main.MainActivity;

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
public class JobOfferFragmentTest {
    private ActivityScenario<MainActivity> scenario;
    private JobOfferFragment frag;

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
                nav.navigate(R.id.jobOfferFragment);
            } catch (IllegalArgumentException ignored) {
                nav.navigate(R.id.action_mainMenuFragment_to_jobOfferFragment);
            }

            activity.getSupportFragmentManager().executePendingTransactions();
            navHost.getChildFragmentManager().executePendingTransactions();
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            Fragment joboffer = navHost.getChildFragmentManager().getPrimaryNavigationFragment();
            assertNotNull(joboffer);
            assertTrue(joboffer instanceof JobOfferFragment);

            frag = (JobOfferFragment) joboffer;

            fillValidFields();
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
    public void test_title_nonEmpty(){
        EditText textView = frag.requireView().findViewById(R.id.jo_title_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("Company1");
        add.performClick();

        assertNull("Title is required", textView.getError());
    }

    @Test
    public void test_title_empty(){
        EditText textView = frag.requireView().findViewById(R.id.jo_title_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("");
        add.performClick();

        assertEquals("Title is required", textView.getError());
    }

    //col index
    @Test
    public void col_index_correctInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_col_index_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("50");
        add.performClick();

        assertNull("", textView.getError());
    }

    @Test
    public void col_index_negativeInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_col_index_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("-5");
        add.performClick();

        assertEquals("Cost of living index must be a positive integer (>= 1)", textView.getError());
    }

    @Test
    public void col_index_nanInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_col_index_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("Hello");
        add.performClick();

        assertEquals("Cost of living index must be a number", textView.getError().toString());
    }


    //yearly salary test
    @Test
    public void yearly_salary_correctInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_yearly_salary_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("100000");
        add.performClick();

        assertNull("", textView.getError());
    }

    @Test
    public void yearly_salary_negativeInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_yearly_salary_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("-50000");
        add.performClick();

        assertEquals("Yearly salary must be greater than 0", textView.getError());
    }

    @Test
    public void yearly_salary_nanInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_yearly_salary_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("Weezer");
        add.performClick();

        assertEquals("Yearly salary must be a number", textView.getError().toString());
    }

    //yearly bonus tests
    @Test
    public void yearly_bonus_correctInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_yearly_bonus_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("1000");
        add.performClick();

        assertNull("", textView.getError());
    }

    @Test
    public void yearly_bonus_negativeInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_yearly_bonus_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("-5000");
        add.performClick();

        assertEquals("Yearly bonus must be greater than or equal to 0", textView.getError());
    }

    @Test
    public void yearly_bonus_nanInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_yearly_bonus_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("Queen");
        add.performClick();

        assertEquals("Yearly bonus must be a number", textView.getError().toString());
    }

    //match401k tests
    @Test
    public void match401k_correctInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_401kmatch_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("2");
        add.performClick();

        assertNull("", textView.getError());
    }

    @Test
    public void match401k_negativeInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_401kmatch_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("-5");
        add.performClick();

        assertEquals("401K match must be between 0 and 6", textView.getError());
    }

    @Test
    public void match401k_largeInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_401kmatch_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("7");
        add.performClick();

        assertEquals("401K match must be between 0 and 6", textView.getError());
    }

    @Test
    public void match401k_nanInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_401kmatch_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("Abba");
        add.performClick();

        assertEquals("401K match must be a number", textView.getError().toString());
    }

    //internet stipend tests
    @Test
    public void internetStipend_correctInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_internet_stipend_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("100");
        add.performClick();

        assertNull("", textView.getError());
    }

    @Test
    public void internetStipend_negativeInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_internet_stipend_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("-300");
        add.performClick();

        assertEquals("Internet stipend must be between 0 and 360", textView.getError());
    }

    @Test
    public void internetStipend_nanInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_internet_stipend_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("Ace of Base");
        add.performClick();

        assertEquals("Internet stipend must be a number", textView.getError().toString());
    }

    @Test
    public void internetStipend_outOfRange(){
        EditText textView = frag.requireView().findViewById(R.id.jo_internet_stipend_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("365");
        add.performClick();

        assertEquals("Internet stipend must be between 0 and 360", textView.getError().toString());
    }

    //Accident insurance tests
    @Test
    public void accidentInsurance_correctInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_accident_insurance_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("10000");
        add.performClick();

        assertNull("", textView.getError());
    }

    @Test
    public void accidentInsurance_negativeInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_accident_insurance_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("-30000");
        add.performClick();

        assertEquals("Accident insurance must be between 0 and 50000", textView.getError());
    }

    @Test
    public void accidentInsurance_nanInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_accident_insurance_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("Pink Floyd");
        add.performClick();

        assertEquals("Accident insurance must be a number", textView.getError().toString());
    }

    @Test
    public void accidentInsurance_outOfRange(){
        EditText textView = frag.requireView().findViewById(R.id.jo_accident_insurance_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("60000");
        add.performClick();

        assertEquals("Accident insurance must be between 0 and 50000", textView.getError().toString());
    }

    //tuition reimbursement tests
    @Test
    public void tuitionReimbursement_correctInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_tuition_reimbursement_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("10000");
        add.performClick();

        assertNull("", textView.getError());
    }

    @Test
    public void tuitionReimbursement_negativeInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_tuition_reimbursement_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("-20000");
        add.performClick();

        assertEquals("Tuition reimbursement must be between 0 and 12000", textView.getError());
    }

    @Test
    public void tuitionReimbursement_nanInput(){
        EditText textView = frag.requireView().findViewById(R.id.jo_tuition_reimbursement_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("Pink Floyd");
        add.performClick();

        assertEquals("Tuition reimbursement must be a number", textView.getError().toString());
    }

    @Test
    public void tuitionReimbursement_outOfRange(){
        EditText textView = frag.requireView().findViewById(R.id.jo_tuition_reimbursement_edittext);
        Button add = frag.requireView().findViewById(R.id.jo_add_button);

        textView.setText("12500");
        add.performClick();

        assertEquals("Tuition reimbursement must be between 0 and 12000", textView.getError().toString());
    }

    //this is to automatically fill all the fields with valid inputs, so the test can run on
    // the field desired (and not return an error early). The field being tested will be replaced
    private void fillValidFields(){
        View view = frag.requireView();
        EditText title = view.findViewById(R.id.jo_title_edittext);
        title.setText("Software Engineer");
        EditText company = view.findViewById(R.id.jo_company_edittext);
        company.setText("Bank");
        EditText city = view.findViewById(R.id.jo_city_edittext);
        city.setText("Atlanta");
        EditText state = view.findViewById(R.id.jo_state_edittext);
        state.setText("Georgia");
        EditText col_index = view.findViewById(R.id.jo_col_index_edittext);
        col_index.setText("125");
        EditText yearly_salary = view.findViewById(R.id.jo_yearly_salary_edittext);
        yearly_salary.setText("125000");
        EditText yearly_bonus = view.findViewById(R.id.jo_yearly_bonus_edittext);
        yearly_bonus.setText("10000");
        EditText match_401k = view.findViewById(R.id.jo_401kmatch_edittext);
        match_401k.setText("5");
        EditText internet_stipend = view.findViewById(R.id.jo_internet_stipend_edittext);
        internet_stipend.setText("100");
        EditText accident_insurance = view.findViewById(R.id.jo_accident_insurance_edittext);
        accident_insurance.setText("500");
        EditText tuition_reimbursement = view.findViewById(R.id.jo_tuition_reimbursement_edittext);
        tuition_reimbursement.setText("400");
    }
}
