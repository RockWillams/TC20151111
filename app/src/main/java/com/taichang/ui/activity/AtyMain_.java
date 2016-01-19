//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.1.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.taichang.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AutoCompleteTextView;
import com.baidu.mapapi.map.MapView;
import com.taichang.R.id;
import com.taichang.R.layout;
import com.taichang.dao.AccountDao_;
import com.taichang.dao.SettingsDao_;
import com.taichang.gps.MyLocationListener_;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class AtyMain_
    extends AtyMain
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.aty_main);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        settingsDao = SettingsDao_.getInstance_(this);
        mLocationListener = MyLocationListener_.getInstance_(this);
        dao = AccountDao_.getInstance_(this);
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

    public static AtyMain_.IntentBuilder_ intent(Context context) {
        return new AtyMain_.IntentBuilder_(context);
    }

    public static AtyMain_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new AtyMain_.IntentBuilder_(fragment);
    }

    public static AtyMain_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new AtyMain_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        searchTV = ((AutoCompleteTextView) hasViews.findViewById(id.search_content));
        mMapView = ((MapView) hasViews.findViewById(id.mapView));
        {
            View view = hasViews.findViewById(id.to_collection);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        AtyMain_.this.toCollection();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.search);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        AtyMain_.this.search();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.to_car_warning);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        AtyMain_.this.toCarWarning();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.area);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        AtyMain_.this.locationSelect();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.to_car_search);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        AtyMain_.this.toCarSearch();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.exit);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        AtyMain_.this.exit();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.to_settings);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        AtyMain_.this.toSettings();
                    }

                }
                );
            }
        }
        init();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case  0 :
                AtyMain_.this.onCarSearchResult(resultCode, data);
                break;
            case  4 :
                AtyMain_.this.onSettingsResult();
                break;
            case  3 :
                AtyMain_.this.onCarCollectionResult(resultCode, data);
                break;
        }
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<AtyMain_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, AtyMain_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), AtyMain_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), AtyMain_.class);
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
