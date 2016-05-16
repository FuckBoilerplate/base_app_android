package app.presentation.sections.user_demo.list;

import android.text.TextUtils;

import java.util.List;

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
public class UsersPresenter extends PresenterFragment {
    private final UserRepository repository;

    @Inject public UsersPresenter(WireframeRepository wireframeRepository, UserRepository repository, UIUtils uiUtils) {
        super(wireframeRepository, uiUtils);
        this.repository = repository;
    }

    public Observable<List<User>> nextPage(User user, String query) {
        Integer id = null;
        if (user != null) id = user.getId();

        return filter(repository.getUsers(id, false), query);
    }

    public Observable<List<User>> refreshList(String query) {
        return filter(repository.getUsers(0, true), query);
    }

    private Observable<List<User>> filter(Observable<List<User>> oUsers, String query) {
        if (TextUtils.isEmpty(query)) return oUsers;

        return oUsers
                .flatMapIterable(users -> users)
                .filter(user -> user.getLogin().toLowerCase().contains(query.trim().toLowerCase()))
                .toList();
    }

}
