package app.data.foundation.net;

import java.util.ArrayList;
import java.util.List;

import app.domain.user_demo.User;
import retrofit2.Response;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by victor on 12/04/16.
 */
public class RestApiMock implements RestApi {

    @Override public Observable<Response<User>> getUserByName(@Path("username") String username) {
        User user = new User(1);
        user.setLogin("Mock name");
        user.setAvatar_url("https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png");
        return Observable.just(Response.success(user));
    }

    @Override public Observable<Response<List<User>>> getUsers(@Query("since") int lastIdQueried, @Query("per_page") int perPage) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            users.add(createUser(i));
        }

        return Observable.just(Response.success(users));
    }

    private User createUser(int id) {
        User user = new User(id);
        user.setLogin("Mock name");
        user.setAvatar_url("https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png");
        return user;
    }
}
