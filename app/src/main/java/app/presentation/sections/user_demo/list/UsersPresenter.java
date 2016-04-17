package app.presentation.sections.user_demo.list;

import java.util.List;

import javax.inject.Inject;

import app.data.sections.user_demo.UserRepository;
import app.domain.user_demo.User;
import app.presentation.foundation.Presenter;
import app.presentation.sections.Wireframe;
import rx.Observable;

/**
 * Created by victor on 08/04/16.
 */
public class UsersPresenter extends Presenter<UsersFragment> {
    private final UserRepository repository;

    @Inject public UsersPresenter(Wireframe wireframe, UserRepository repository) {
        super(wireframe);
        this.repository = repository;
    }

    public void goToDetail(User user) {
        wireframe.setWireframeCurrentObject(user)
                .compose(safelyReportSnackbar())
                .subscribe(_I -> wireframe.userScreen());
    }

    public Observable<List<User>> nextPage(User user) {
        Integer id = null;
        if (user != null) id = user.getId();
        return repository.getUsers(id, false).compose(safelyReportSnackbar());
    }

    public Observable<List<User>> refreshList() {
        return repository.getUsers(0, true).compose(safelyReportSnackbar());
    }

}
