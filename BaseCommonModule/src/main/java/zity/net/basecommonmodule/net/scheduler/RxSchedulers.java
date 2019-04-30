package zity.net.basecommonmodule.net.scheduler;

import androidx.appcompat.app.AppCompatActivity;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * author：hanshaokai
 * date： 2018/5/29 16:08
 * describe： 线程调度器
 */


public class RxSchedulers {
    //线程调度 从io 到主线程 通过 bindToLifecycle 放置RxJava 回调视图销毁后  仍持有视图造成内存泄漏 activity 下的请求
    public static <T> ObservableTransformer<T, T> io_main(final AppCompatActivity activity) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.compose(((RxAppCompatActivity) activity).<T>bindToLifecycle()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> MaybeTransformer<T, T> io_main_maybe(final AppCompatActivity activity) {
        return new MaybeTransformer<T, T>() {
            @Override
            public MaybeSource<T> apply(Maybe<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> MaybeTransformer<T, T> io_frg_maybe(final RxFragment rxFragment) {
        return new MaybeTransformer<T, T>() {
            @Override
            public MaybeSource<T> apply(Maybe<T> upstream) {
                return upstream.compose((rxFragment).<T>bindToLifecycle()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    //fragment中的线程调度器
    public static <T> ObservableTransformer<T, T> io_main_frag(final RxFragment fragment) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.compose(fragment.<T>bindToLifecycle()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}

