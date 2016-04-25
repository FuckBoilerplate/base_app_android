package app.presentation.sections.user_demo.search;

import javax.inject.Inject;

import app.data.foundation.UIUtils;
import app.data.sections.WireframeRepository;
import app.data.sections.user_demo.UserRepository;
import app.domain.user_demo.User;
import app.presentation.foundation.PresenterFragment;
import rx.Observable;

/**
 * Created by victor on 08/04/16.
 */
public class SearchUserPresenter extends PresenterFragment {
    private final UserRepository userRepository;

    @Inject public SearchUserPresenter(WireframeRepository wireframeRepository, UserRepository userRepository, UIUtils uiUtils) {
        super(wireframeRepository, uiUtils);
        this.userRepository = userRepository;
    }

    Observable<User> getUserByUserName(String name) {
        return userRepository.searchByUserName(name);
    }

}
