package com.dell.inventoryplay.main;

import com.dell.inventoryplay.base.BasePresenter;
import com.dell.inventoryplay.base.IView;

/**
 * Created by sasikanta on 11/16/2017.
 * MainPresenter
 */

public class MainPresenter<V extends IView> extends BasePresenter<V>
        implements IMainPresenter<V> {

    public static MainPresenter newInstance() {
        return new MainPresenter();
    }

    @Override
    public void onNavMenuCreated() {

    }
}
