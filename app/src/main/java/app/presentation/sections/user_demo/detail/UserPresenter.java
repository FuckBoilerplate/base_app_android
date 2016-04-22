package app.presentation.sections.user_demo.detail;


import javax.inject.Inject;

import app.data.foundation.UIUtils;
import app.domain.user_demo.User;
import app.presentation.foundation.PresenterFragment;
import app.presentation.sections.Wireframe;
import rx.Observable;

public class UserPresenter extends PresenterFragment {

    @Inject public UserPresenter(Wireframe wireframe, UIUtils uiUtils) {
        super(wireframe, uiUtils);
    }

    Observable<User> getCurrentUser() {
        return wireframe.<User>getWireframeCurrentObject();
    }

    Observable<Void> goToSearchScreen() {
        return Observable.defer(() -> {
            wireframe.searchUserScreen();
            return Observable.just(null);
        });
    }
}
