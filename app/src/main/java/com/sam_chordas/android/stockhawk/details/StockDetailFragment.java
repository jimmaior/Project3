package com.sam_chordas.android.stockhawk.details;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.sam_chordas.android.stockhawk.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class StockDetailFragment extends Fragment {

    // private members
    private static final String TAG = StockDetailFragment.class.getSimpleName();

    private LineChart mLineChart;
    private Button mButtonD1;
    private Button mButtonW1;
    private Button mButtonM1;
    private Button mButtonY1;
    private Button mButtonY5;
    private Button mButtonMax;
    private OnChartButtonClickedListener mListener;

    // data fields
    private static String mCompanyName;
    private static StockPrice mStockPrice;
    private static ArrayList<StockPriceHistory> mStockPriceHistory;

    interface OnChartButtonClickedListener {
        void onChartButtonClicked(String range);
    }

    public StockDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        if (getArguments() != null) {
            mCompanyName = getArguments().getString(Constants.COMPANY_NAME);
            mStockPrice = getArguments().getParcelable(Constants.STOCK_PRICE);
            mStockPriceHistory = getArguments().getParcelableArrayList(Constants.STOCK_PRICE_HISTORY);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnChartButtonClickedListener) getActivity();
        } catch (ClassCastException cce) {
            Log.e(TAG, cce.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(TAG, "onCreateView");

        // Fragment////////////////////////////////////////////////////////////////////
        getActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().setTitle(mStockPrice.getSymbol() + ": " + mCompanyName);
        final View view =  inflater.inflate(R.layout.fragment_stock_detail, container, false);

        // TextViews//////////////////////////////////////////////////////////////////////
        TextView quoteTv = (TextView) view.findViewById(R.id.tv_quote);
        quoteTv.setText(mStockPrice.getQuote());
        quoteTv.setContentDescription(getString(R.string.a11y_quote, mCompanyName, quoteTv.getText()));

        TextView changeTv = (TextView) view.findViewById(R.id.tv_change);
        changeTv.setText(mStockPrice.getChange());
        changeTv.setContentDescription(getString(R.string.a11y_change));

        if (Float.parseFloat(mStockPrice.getChange()) < 0 ) {
            changeTv.setTextColor(Color.RED);
        } else {
            changeTv.setTextColor(Color.GREEN);
        }

        TextView dayHighTv = (TextView) view.findViewById(R.id.tv_day_hi_val);
        dayHighTv.setText(mStockPrice.getDayHighQuote());
        dayHighTv.setContentDescription(getString(R.string.a11y_day_hi, dayHighTv.getText()));

        TextView dayLowTv = (TextView) view.findViewById(R.id.tv_day_low_val);
        dayLowTv.setText(mStockPrice.getDayLowQuote());
        dayLowTv.setContentDescription(getString(R.string.a11y_day_lo, dayLowTv.getText()));

        TextView yearHighTv = (TextView) view.findViewById(R.id.tv_year_hi_val);
        yearHighTv.setText(mStockPrice.getYearHighQuote());
        yearHighTv.setContentDescription(getString(R.string.a11y_year_hi, yearHighTv.getText()));

        TextView yearLowTv = (TextView) view.findViewById(R.id.tv_year_low_val);
        yearLowTv.setText(mStockPrice.getYearLowQuote());
        yearLowTv.setContentDescription(getString(R.string.a11y_year_lo, yearLowTv.getText()));

        TextView dailyAvgVolumeTv = (TextView) view.findViewById(R.id.tv_day_vol_val);
        dailyAvgVolumeTv.setText(mStockPrice.getAvgDayVolume());
        dailyAvgVolumeTv.setContentDescription(getString(R.string.a11y_avg_day_volume, dailyAvgVolumeTv.getText()));

        TextView volumeTv = (TextView) view.findViewById(R.id.tv_vol_val);
        volumeTv.setText(mStockPrice.getVolume());
        volumeTv.setContentDescription(getString(R.string.a11y_volume, volumeTv.getText()));

        // Buttons////////////////////////////////////////////////////////////////////////
        mButtonD1 = (Button) view.findViewById(R.id.button_1d);
        mButtonD1.setTag("1d");
        mButtonD1.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                resetEveryButtonBackground(view);
                mButtonD1.setBackgroundColor(getResources().getColor(R.color.md_btn_selected));
                Log.d(TAG, "onClick view id:" + v.getId() );
                mListener.onChartButtonClicked(mButtonD1.getTag().toString());
            }
        });

        mButtonW1 = (Button) view.findViewById(R.id.button_1w);
        mButtonW1.setTag("5d");
        mButtonW1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "onClick view id:" + v.getId());
                resetEveryButtonBackground(view);
                mButtonW1.setBackgroundColor(getResources().getColor(R.color.md_btn_selected));
                mListener.onChartButtonClicked(mButtonW1.getTag().toString());
            }
        });

        mButtonM1 = (Button) view.findViewById(R.id.button_1m);
        mButtonM1.setTag("1m");
        mButtonM1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "onClick view id:" + v.getId());
                resetEveryButtonBackground(view);
                mButtonM1.setBackgroundColor(getResources().getColor(R.color.md_btn_selected));
                mListener.onChartButtonClicked(mButtonM1.getTag().toString());
            }
        });

        mButtonY1 = (Button) view.findViewById(R.id.button_1y);
        mButtonY1.setTag("1y");
        mButtonY1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "onClick view id:" + v.getId());
                resetEveryButtonBackground(view);
                mButtonY1.setBackgroundColor(getResources().getColor(R.color.md_btn_selected));
                mListener.onChartButtonClicked(mButtonY1.getTag().toString());
            }
        });

        mButtonY5 = (Button) view.findViewById(R.id.button_5y);
        mButtonY5.setTag("5y");
        mButtonY5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "onClick view id:" + v.getId());
                resetEveryButtonBackground(view);
                mButtonY5.setBackgroundColor(getResources().getColor(R.color.md_btn_selected));
                mListener.onChartButtonClicked(mButtonY5.getTag().toString());
            }
        });

        mButtonMax = (Button) view.findViewById(R.id.button_max);
        mButtonMax.setTag("my");
        mButtonMax.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "onClick view id:" + v.getId());
                resetEveryButtonBackground(view);
                mButtonMax.setBackgroundColor(getResources().getColor(R.color.md_btn_selected));
                mListener.onChartButtonClicked(mButtonMax.getTag().toString());
            }
        });

        // LineChart///////////////////////////////////////////////////////////////////
        mLineChart = (LineChart) view.findViewById(R.id.chart_valueLineChart);
        mLineChart.setDrawGridBackground(false);
        mLineChart.setBackgroundColor(Color.BLACK);
        mLineChart.setDescription("");
        mLineChart.setNoDataTextDescription("No data to display");
        mLineChart.animateX(1500, Easing.EasingOption.EaseInOutQuart);

        // RIGHT Y Axis
        mLineChart.getAxisRight().setEnabled(false);

        // LEFT Axis
        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setValueFormatter(new PriceYAxisValueFormatter());

        // X Axis
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);

        setData();

        return view;
    }

    private float getFloatForOpenPrice(String price) {
        float value = 0;
        if (isFloat(price)) {
            return Float.parseFloat(price);
        }
        return value;
    }

    private void setData() {
        Log.d(TAG, "setData");

        List<String> xValues = new ArrayList<>();
        List<Entry> yValues = new ArrayList<>();
        for (int i = 0; i < mStockPriceHistory.size(); i++) {
            xValues.add(mStockPriceHistory.get(i).getDateTime());
            yValues.add( new Entry(getFloatForOpenPrice(mStockPriceHistory.get(i).getOpen()), i));
        }
        renderChart(xValues, yValues);
        mLineChart.invalidate();
    }

    private void renderChart(List<String> xValues, List<Entry> yValues ) {

        // create a dataset and give it a type
        LineDataSet valueSet = new LineDataSet(yValues, "Quote");
        LineData stockValues = new LineData(xValues, valueSet);

        valueSet.setDrawFilled(true);
        // color below the line
        if(android.os.Build.VERSION.SDK_INT >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_blue);
            valueSet.setFillDrawable(drawable);
        } else {
            valueSet.setFillColor(Color.BLACK);
        }
        mLineChart.setData(stockValues);
    }

    public void updateChartData(Bundle data) {
        Log.d(TAG, "updateChartData");
        mStockPriceHistory = data.getParcelableArrayList(Constants.STOCK_PRICE_HISTORY);
        setData();
    }


    public class PriceYAxisValueFormatter implements YAxisValueFormatter {
        private DecimalFormat mFormat;

        public PriceYAxisValueFormatter() {
            mFormat = new DecimalFormat("###,###,###,##0.00");
        }

        @Override
        public String getFormattedValue(float value, YAxis yAxis) {
            return "$ " + mFormat.format(value);
        }
    }

    private boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void resetEveryButtonBackground(View parent) {
        Log.d(TAG, "resetButtonBackground");
        // for each button reset the background
        ViewGroup buttonParent = (LinearLayout) parent.findViewById(R.id.buttons_layout);
        for (int i = 0; i < buttonParent.getChildCount()-1; i++ ) {
            final View button = buttonParent.getChildAt(i);
            if (button instanceof Button) {
                button.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }
}
