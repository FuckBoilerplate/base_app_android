package app.presentation.sections.user_demo.detail;


import javax.inject.Inject;

import app.data.foundation.UIUtils;
import app.data.sections.WireframeRepository;
import app.domain.user_demo.User;
import app.presentation.foundation.PresenterFragment;
import rx.Observable;

public class UserPresenter extends PresenterFragment {

    @Inject public UserPresenter(WireframeRepository wireframeRepository, UIUtils uiUtils) {
        super(wireframeRepository, uiUtils);
    }

    Observable<User> getCurrentUser() {
        return wireframeRepository.<User>getWireframeCurrentObject();
    }
}
