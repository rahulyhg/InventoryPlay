package com.dell.inventoryplay.base;

/**
 * Created by sasikanta on 11/16/2017.
 * IPresenter
 */
/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the MvpView type that wants to be attached with.
 */
public interface IPresenter<V extends IView> {
    void onAttach(V IView);
}
