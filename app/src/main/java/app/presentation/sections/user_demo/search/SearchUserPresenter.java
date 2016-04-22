package app.presentation.sections.user_demo.search;

import android.os.Bundle;

import javax.inject.Inject;

import app.data.sections.user_demo.UserRepository;
import app.domain.user_demo.User;
import app.presentation.foundation.PresenterFragment;
import app.presentation.sections.Wireframe;
import rx.Observable;

/**
 * Created by victor on 08/04/16.
 */
public class SearchUserPresenter extends PresenterFragment<SearchUserFragment> {
    private final UserRepository userRepository;
    public static final String HELLO_FROM_BUNDLE_WIREFRAME_KEY = "hello_from_bundle_key";

    @Inject public SearchUserPresenter(Wireframe wireframe, UserRepository userRepository) {
        super(wireframe);
        this.userRepository = userRepository;
    }

    Observable<String> helloFromBundle() {
        Bundle bundle = view.getArguments();
        String helloFromBundle = bundle != null ? bundle.getString(HELLO_FROM_BUNDLE_WIREFRAME_KEY, "") : "";
        if (!helloFromBundle.isEmpty()) return Observable.just(helloFromBundle).compose(safely());
        return Observable.<String>empty().compose(safely());
    }

    Observable<User> getUserByUserName(String name) {
        return userRepository.searchByUserName(name)
                .compose(safelyReportSnackbar())
                .compose(applyLoading());
    }

}
