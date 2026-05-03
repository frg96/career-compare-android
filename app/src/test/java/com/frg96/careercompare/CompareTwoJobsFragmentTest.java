package com.frg96.careercompare;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.test.core.app.ActivityScenario;

import com.frg96.careercompare.data.entity.Job;
import com.frg96.careercompare.data.entity.Weights;
import com.frg96.careercompare.domain.util.JobRanker;
import com.frg96.careercompare.ui.comparison.CompareTwoJobsFragment;
import com.frg96.careercompare.ui.comparison.CompareTwoJobsFragmentArgs;
import com.frg96.careercompare.ui.main.MainActivity;
import com.frg96.careercompare.ui.viewmodel.JobViewModel;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;
import org.mockito.quality.Strictness;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.text.DecimalFormat;


@RunWith(RobolectricTestRunner.class)
@LooperMode(LooperMode.Mode.PAUSED)
@Config(sdk = 34)
public class CompareTwoJobsFragmentTest {

    private ActivityScenario<MainActivity> scenario;

    private CompareTwoJobsFragment frag;

    @After
    public void tearDown() {
        if (scenario != null) {
            scenario.close();
        }
    }

    @Test
    public void populates_table_when_data_arrives() {
        scenario = ActivityScenario.launch(MainActivity.class);

        scenario.onActivity(activity -> {
            NavHostFragment navHost = (NavHostFragment)
                    activity.getSupportFragmentManager().findFragmentById(R.id.nav_host);
            assertNotNull(navHost);

            // --- Prepare the Mockito-backed VM & LiveData wiring ---
            TestUtilities.MockJobViewModel wiring = new TestUtilities.MockJobViewModel();

            // --- Intercept any new ViewModelProvider inside the fragment ---
            try (MockedConstruction<ViewModelProvider> ignored =
                 mockConstruction(ViewModelProvider.class, (mock, context) -> {
                     ViewModelProvider delegate;
                     Object[] argsArr = context.arguments().toArray();
                     if (argsArr.length == 2 && argsArr[0] instanceof ViewModelStoreOwner && argsArr[1] instanceof ViewModelProvider.Factory) {
                         delegate = new ViewModelProvider((ViewModelStoreOwner) argsArr[0], (ViewModelProvider.Factory) argsArr[1]);
                     } else if (argsArr.length == 2 && argsArr[0] instanceof ViewModelStore && argsArr[1] instanceof ViewModelProvider.Factory) {
                         delegate = new ViewModelProvider((ViewModelStore) argsArr[0], (ViewModelProvider.Factory) argsArr[1]);
                     } else {
                         // Fallback so Navigation’s internal ViewModels still get created
                         delegate = new ViewModelProvider(new ViewModelStore(), new ViewModelProvider.NewInstanceFactory());
                     }

                     // Return our mock VM only for JobViewModel; delegate all other requests
                     when(mock.get(JobViewModel.class)).thenReturn(wiring.mockVm);
                     when(mock.get((Class) any(Class.class))).thenAnswer(inv -> {
                         Class<?> cls = (Class<?>) inv.getArgument(0);
                         if (cls == JobViewModel.class) return wiring.mockVm;
                         return delegate.get((Class) cls);
                     });
                     when(mock.get(anyString(), (Class) any(Class.class))).thenAnswer(inv -> {
                         String key = inv.getArgument(0);
                         Class<?> cls = (Class<?>) inv.getArgument(1);
                         if (cls == JobViewModel.class) return wiring.mockVm;
                         return delegate.get(key, (Class) cls);
                     });
                 })) {

                // Navigate to CompareTwoJobsFragment with args
                Bundle args = new CompareTwoJobsFragmentArgs.Builder(1L, 2L, "jobOfferFragment")
                        .build()
                        .toBundle();

                NavController nav = navHost.getNavController();
                nav.setGraph(R.navigation.nav_graph);
                nav.navigate(R.id.compareTwoJobsFragment, args);

                // Let fragment inflate
                activity.getSupportFragmentManager().executePendingTransactions();
                navHost.getChildFragmentManager().executePendingTransactions();
                Shadows.shadowOf(Looper.getMainLooper()).idle();

                Fragment cmp2JobsFrag = navHost.getChildFragmentManager().getPrimaryNavigationFragment();
                assertTrue(cmp2JobsFrag instanceof CompareTwoJobsFragment);
                frag = (CompareTwoJobsFragment) cmp2JobsFrag;

                View v = frag.requireView();

                // Push fake data AFTER fragment subscribed to LiveData
                Job left = new Job();
                left.jobId = 1L;
                left.title = "LeftTitle";
                left.company = "LeftCompany";
                left.city = "ATL";
                left.state = "GA";
                left.costOfLivingIndex = 125;
                left.yearlySalary = 125000.0;
                left.yearlyBonus = 1500.0;
                left.match401k = 5;
                left.internetStipend = 200.0;
                left.accidentInsurance = 30000.0;
                left.tuitionReimbursement = 4000.0;

                Job right = new Job();
                right.jobId = 2L;
                right.title = "RightTitle";
                right.company = "RightCompany";
                right.city = "NYC";
                right.state = "NY";
                right.costOfLivingIndex = 100;
                right.yearlySalary = 125000.0;
                right.yearlyBonus = 1000.0;
                right.match401k = 3;
                right.internetStipend = 100.0;
                right.accidentInsurance = 15000.0;
                right.tuitionReimbursement = 3000.0;

                Weights w = new Weights();
                w.yearlySalaryWeight = 1;
                w.yearlyBonusWeight = 1;
                w.match401kWeight = 1;
                w.internetStipendWeight = 1;
                w.accidentInsuranceWeight = 1;
                w.tuitionReimbursementWeight = 1;

                wiring.pushLeft(left);
                wiring.pushRight(right);
                wiring.pushWeights(w);

                // Flush observers -> populate UI
                Shadows.shadowOf(Looper.getMainLooper()).idle();

                // Assert headers based on sourceFragment="jobOfferFragment"
                TextView hdrLeft = v.findViewById(R.id.cmp_tbl_header_left);
                TextView hdrRight = v.findViewById(R.id.cmp_tbl_header_right);
                assertEquals("Current Job", hdrLeft.getText().toString());
                assertEquals("Last Saved Job Offer", hdrRight.getText().toString());

                DecimalFormat df = new DecimalFormat("#.##");

                // Assert titles/companies/locations copied
                assertEquals("LeftTitle", ((TextView) v.findViewById(R.id.cmp_tbl_title_left)).getText().toString());
                assertEquals("LeftCompany", ((TextView) v.findViewById(R.id.cmp_tbl_company_left)).getText().toString());
                assertEquals("ATL, GA", ((TextView) v.findViewById(R.id.cmp_tbl_location_left)).getText().toString());
                assertEquals(
                        df.format(JobRanker.adjustForCostOfLiving(left.yearlySalary, left.costOfLivingIndex)),
                        ((TextView) v.findViewById(R.id.cmp_tbl_ays_left)).getText().toString()
                );
                assertEquals(
                        df.format(JobRanker.adjustForCostOfLiving(left.yearlyBonus, left.costOfLivingIndex)),
                        ((TextView) v.findViewById(R.id.cmp_tbl_ayb_left)).getText().toString()
                );
                assertEquals("5", ((TextView) v.findViewById(R.id.cmp_tbl_401k_left)).getText().toString());
                assertEquals("200", ((TextView) v.findViewById(R.id.cmp_tbl_is_left)).getText().toString());
                assertEquals("30000", ((TextView) v.findViewById(R.id.cmp_tbl_ai_left)).getText().toString());
                assertEquals("4000", ((TextView) v.findViewById(R.id.cmp_tbl_tr_left)).getText().toString());
                assertEquals(df.format(JobRanker.getJobScore(left, w)), ((TextView) v.findViewById(R.id.cmp_tbl_js_left)).getText().toString());


                assertEquals("RightTitle", ((TextView) v.findViewById(R.id.cmp_tbl_title_right)).getText().toString());
                assertEquals("RightCompany", ((TextView) v.findViewById(R.id.cmp_tbl_company_right)).getText().toString());
                assertEquals("NYC, NY", ((TextView) v.findViewById(R.id.cmp_tbl_location_right)).getText().toString());
                assertEquals(
                        df.format(JobRanker.adjustForCostOfLiving(right.yearlySalary, right.costOfLivingIndex)),
                        ((TextView) v.findViewById(R.id.cmp_tbl_ays_right)).getText().toString()
                );
                assertEquals(
                        df.format(JobRanker.adjustForCostOfLiving(right.yearlyBonus, right.costOfLivingIndex)),
                        ((TextView) v.findViewById(R.id.cmp_tbl_ayb_right)).getText().toString()
                );
                assertEquals("3", ((TextView) v.findViewById(R.id.cmp_tbl_401k_right)).getText().toString());
                assertEquals("100", ((TextView) v.findViewById(R.id.cmp_tbl_is_right)).getText().toString());
                assertEquals("15000", ((TextView) v.findViewById(R.id.cmp_tbl_ai_right)).getText().toString());
                assertEquals("3000", ((TextView) v.findViewById(R.id.cmp_tbl_tr_right)).getText().toString());
                assertEquals(df.format(JobRanker.getJobScore(right, w)), ((TextView) v.findViewById(R.id.cmp_tbl_js_right)).getText().toString());


                // (Optional) If you want to assert numerical fields, either
                // compute expected via your JobRanker or just assert they're non-empty:
                assertFalse(((TextView) v.findViewById(R.id.cmp_tbl_js_left)).getText().toString().isEmpty());
                assertFalse(((TextView) v.findViewById(R.id.cmp_tbl_js_right)).getText().toString().isEmpty());
            }
        });
    }
}

class TestUtilities {
    /**
     * A small bundle that wires a Mockito mock of JobViewModel
     * to test-controlled LiveData streams you can push values into
     */
    public static class MockJobViewModel {
        public final MutableLiveData<Job> left = new MutableLiveData<>();
        public final MutableLiveData<Job> right = new MutableLiveData<>();
        public final MutableLiveData<Weights> weights = new MutableLiveData<>();
        public final JobViewModel mockVm;

        public MockJobViewModel() {
            mockVm = mock(JobViewModel.class, withSettings().strictness(Strictness.LENIENT));
            // Stub only what the fragment reads
            when(mockVm.getJobById(1L)).thenReturn(left);
            when(mockVm.getJobById(2L)).thenReturn(right);
            when(mockVm.getCurrentWeights()).thenReturn(weights);
        }

        public void pushLeft(Job j) {
            left.setValue(j);
        }

        public void pushRight(Job j) {
            right.setValue(j);
        }

        public void pushWeights(Weights w) {
            weights.setValue(w);
        }
    }
}



