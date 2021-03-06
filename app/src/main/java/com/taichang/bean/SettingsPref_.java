//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.1.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.taichang.bean;

import android.content.Context;
import android.content.SharedPreferences;
import org.androidannotations.api.sharedpreferences.EditorHelper;
import org.androidannotations.api.sharedpreferences.LongPrefEditorField;
import org.androidannotations.api.sharedpreferences.LongPrefField;
import org.androidannotations.api.sharedpreferences.SharedPreferencesHelper;

public final class SettingsPref_
    extends SharedPreferencesHelper
{


    public SettingsPref_(Context context) {
        super(context.getSharedPreferences("SettingsPref", 0));
    }

    public SettingsPref_.SettingsPrefEditor_ edit() {
        return new SettingsPref_.SettingsPrefEditor_(getSharedPreferences());
    }

    public LongPrefField updateGap() {
        return longField("updateGap", 60000L);
    }

    public final static class SettingsPrefEditor_
        extends EditorHelper<SettingsPref_.SettingsPrefEditor_>
    {


        SettingsPrefEditor_(SharedPreferences sharedPreferences) {
            super(sharedPreferences);
        }

        public LongPrefEditorField<SettingsPref_.SettingsPrefEditor_> updateGap() {
            return longField("updateGap");
        }

    }

}
