//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.1.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.taichang.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.taichang.R.layout;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class AtyCarSearch_
    extends AtyCarSearch
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.aty_car_search);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        requestWindowFeature(1);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static AtyCarSearch_.IntentBuilder_ intent(Context context) {
        return new AtyCarSearch_.IntentBuilder_(context);
    }

    public static AtyCarSearch_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new AtyCarSearch_.IntentBuilder_(fragment);
    }

    public static AtyCarSearch_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new AtyCarSearch_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        title = ((TextView) hasViews.findViewById(com.taichang.R.id.title));
        listviewCars = ((ListView) hasViews.findViewById(com.taichang.R.id.cars));
        searchET = ((EditText) hasViews.findViewById(com.taichang.R.id.search_car_no));
        isOnlineBT = ((Button) hasViews.findViewById(com.taichang.R.id.search_is_online));
        {
            View view = hasViews.findViewById(com.taichang.R.id.display_on_map);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        AtyCarSearch_.this.onDisplayButtonClicked();
                    }

                }
                );
            }
        }
        if (isOnlineBT!= null) {
            isOnlineBT.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    AtyCarSearch_.this.onIsOnlineChanged();
                }

            }
            );
        }
        {
            View view = hasViews.findViewById(com.taichang.R.id.back);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        AtyCarSearch_.this.onBack();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(com.taichang.R.id.search);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        AtyCarSearch_.this.onCarNoChanged();
                    }

                }
                );
            }
        }
        if (listviewCars!= null) {
            listviewCars.setOnItemClickListener(new OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AtyCarSearch_.this.onItemClicked(position);
                }

            }
            );
        }
        init();
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<AtyCarSearch_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, AtyCarSearch_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), AtyCarSearch_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), AtyCarSearch_.class);
            fragmentSupport_ = fragment;
        }

        @Override
        public void startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent, requestCode);
            } else {
                if (fragment_!= null) {
                    if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                        fragment_.startActivityForResult(intent, requestCode, lastOptions);
                    } else {
                        fragment_.startActivityForResult(intent, requestCode);
                    }
                } else {
                    if (context instanceof Activity) {
                        Activity activity = ((Activity) context);
                        ActivityCompat.startActivityForResult(activity, intent, requestCode, lastOptions);
                    } else {
                        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                            context.startActivity(intent, lastOptions);
                        } else {
                            context.startActivity(intent);
                        }
                    }
                }
            }
        }

    }

}
