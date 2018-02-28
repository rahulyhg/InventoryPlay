package com.dell.inventoryplay.base;

/**
 * Created by sasikanta on 11/16/2017.
 * BasePresenter
 */

public class BasePresenter <V extends IView> implements IPresenter<V> {
    private V mIView;
    @Override
    public void onAttach(V IView) {
        mIView = IView;
    }


}
