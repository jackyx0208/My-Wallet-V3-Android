package piuk.blockchain.android.ui.base;

@Deprecated
public interface Presenter<VIEW extends View> {

    void onViewDestroyed();

    void onViewResumed();

    void onViewPaused();

    void initView(VIEW view);

    VIEW getView();

}
