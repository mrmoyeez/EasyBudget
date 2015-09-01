package com.benoitletondor.easybudgetapp.view.welcome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.benoitletondor.easybudgetapp.R;
import com.benoitletondor.easybudgetapp.helper.CurrencyHelper;
import com.benoitletondor.easybudgetapp.view.selectcurrency.SelectCurrencyFragment;

import java.util.Currency;

/**
 * Onboarding step 1 fragment
 *
 * @author Benoit LETONDOR
 */
public class Onboarding2Fragment extends OnboardingFragment
{
    private Currency selectedCurrency;
    private Button nextButton;

    private BroadcastReceiver receiver;

// ------------------------------------->

    /**
     * Required empty public constructor
     */
    public Onboarding2Fragment()
    {

    }

// ------------------------------------->

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_onboarding2, container, false);

        nextButton = (Button) v.findViewById(R.id.onboarding_screen2_next_button);
        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                next(v);
            }
        });

        selectedCurrency = CurrencyHelper.getUserCurrency(v.getContext());
        setNextButtonText();

        Fragment selectCurrencyFragment = new SelectCurrencyFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.expense_select_container, selectCurrencyFragment).commit();

        IntentFilter filter = new IntentFilter(SelectCurrencyFragment.CURRENCY_SELECTED_INTENT);
        receiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                selectedCurrency = Currency.getInstance(intent.getStringExtra(SelectCurrencyFragment.CURRENCY_ISO_EXTRA));
                setNextButtonText();
            }
        };

        LocalBroadcastManager.getInstance(v.getContext()).registerReceiver(receiver, filter);

        return v;
    }

    @Override
    public void onDestroyView()
    {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);

        super.onDestroyView();
    }

    @Override
    public int getStatusBarColor()
    {
        return R.color.onboarding_2_statusbar;
    }

    /**
     * Set the next button text according to the selected currency
     */
    private void setNextButtonText()
    {
        nextButton.setText("Go with "+selectedCurrency.getSymbol());
    }
}
