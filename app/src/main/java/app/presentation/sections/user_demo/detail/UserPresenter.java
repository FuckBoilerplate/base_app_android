package app.presentation.sections.user_demo.detail;


import javax.inject.Inject;

import app.data.sections.gcm_notifications.NotificationRepository;
import app.domain.user_demo.User;
import app.presentation.foundation.Presenter;
import app.presentation.sections.Wireframe;
import rx.Observable;

public class UserPresenter extends Presenter<UserFragment> {

    @Inject public UserPresenter(Wireframe wireframe, NotificationRepository notificationRepository) {
        super(wireframe, notificationRepository);
    }

    @Override public void onCreatedView() {
        super.onCreatedView();

        Observable<User> currentUser = wireframe.<User>getWireframeCurrentObject()
                .compose(safelyReportSnackbar())
                .compose(applyLoading());

        view.showUser(currentUser);
    }

    void goToSearchScreen() {
        wireframe.searchUserScreen();
    }
}
