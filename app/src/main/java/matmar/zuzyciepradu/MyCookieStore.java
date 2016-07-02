package matmar.zuzyciepradu;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

/**
 * Created by timo on 30.06.16.
 */
public class MyCookieStore implements CookieStore {
    @Override
    public void add(URI uri, HttpCookie httpCookie) {

    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return null;
    }

    @Override
    public List<HttpCookie> getCookies() {
        return null;
    }

    @Override
    public List<URI> getURIs() {
        return null;
    }

    @Override
    public boolean remove(URI uri, HttpCookie httpCookie) {
        return false;
    }

    @Override
    public boolean removeAll() {
        return false;
    }
}
