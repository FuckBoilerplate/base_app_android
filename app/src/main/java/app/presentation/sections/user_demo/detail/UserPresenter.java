package app.presentation.sections.user_demo.detail;


import javax.inject.Inject;

import app.domain.user_demo.User;
import app.presentation.foundation.PresenterFragment;
import app.presentation.sections.Wireframe;
import rx.Observable;

public class UserPresenter extends PresenterFragment<UserFragment> {

    @Inject public UserPresenter(Wireframe wireframe) {
        super(wireframe);
    }

    Observable<User> getCurrentUser() {
        return wireframe.<User>getWireframeCurrentObject()
                .compose(safelyReportSnackbar())
                .compose(applyLoading());
    }

    void goToSearchScreen() {
        wireframe.searchUserScreen();
    }
}
