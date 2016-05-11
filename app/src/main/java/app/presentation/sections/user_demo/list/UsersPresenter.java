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

    public Observable<List<User>> nextPage(User user) {
        Integer id = null;
        if (user != null) id = user.getId();
        return repository.getUsers(id, false);
    }

    public Observable<List<User>> refreshList() {
        return repository.getUsers(0, true);
    }

    public Observable<List<User>> filter(String query) {
        if (TextUtils.isEmpty(query)) return refreshList();

        return refreshList()
                .flatMapIterable(users -> users)
                .filter(user -> user.getLogin().toLowerCase().contains(query.trim().toLowerCase()))
                .toList();
    }

}
