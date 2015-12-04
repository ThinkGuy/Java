package citexplore.foundation.util;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Http Cookie管理器。
 *
 * @author Liu, Xinwei; Zhang, Yin
 */
public class HttpCookieManager {

    // **************** 公开变量

    /**
     * 全局唯一的http Cookie管理器。
     */
    public static final HttpCookieManager instance = new HttpCookieManager();

    // **************** 私有变量

    /**
     * cookie表，键为cookie的域。
     */
    private Hashtable<String, Hashtable<String, Hashtable<String, Cookie>>>
            cookieTable = new Hashtable<>();

    /**
     * Cookie表锁。
     */
    private final Object cookieTableLock = new Object();

    // **************** 继承方法

    // **************** 公开方法

    /**
     * 从服务器上获取cookie。
     *
     * @param connection URLConnection。
     */
    public void receiveCookie(URLConnection connection) {
        Map<String, List<String>> headFieldMap = connection.getHeaderFields();
        List<String> cookies = headFieldMap.get("Set-Cookie");

        if (cookies == null) {
            return;
        }

        for (String cookieString : cookies) {
            Cookie cookie = new Cookie();

            cookieString = cookieString + ";";

            cookie.name = cookieString.substring(0, cookieString.indexOf("="));
            if (cookieString.contains("=\"")) {
                cookieString = cookieString.substring(cookieString.indexOf
                        ("=\"") + 2);
                cookie.value = cookieString.substring(0, cookieString.indexOf
                        ("\";"));
            } else {
                cookie.value = cookieString.substring(cookieString.indexOf
                        ("=") + 1, cookieString.indexOf(";"));
            }

            cookieString = cookieString.substring(cookieString.indexOf(";") +
                    1);

            Matcher expiresMather = Pattern.compile("(?i)expires=.+?;")
                    .matcher(cookieString);
            if (expiresMather.find()) {
                String expires = expiresMather.group();
                expires = expires.substring(0, expires.length() - 1).trim();
                expires = expires.substring(expires.indexOf("=") + 1, expires
                        .length());
                SimpleDateFormat format = new SimpleDateFormat("EEE, " +
                        "d-MMM-yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
                Date date;
                try {
                    date = format.parse(expires);
                } catch (Exception e) {
                    Log.log(getClass(), "Error parsing cookie expires: " + e
                            .getMessage(), Log.Type.LOG);
                    date = new Date(System.currentTimeMillis() + 60l * 20 *
                            1000);
                }
                cookie.expires = date.getTime();
            } else {
                Matcher maxAgeMather = Pattern.compile("(?i)max-age=.+?;")
                        .matcher(cookieString);
                if (maxAgeMather.find()) {
                    String maxAge = maxAgeMather.group();
                    maxAge = maxAge.substring(0, maxAge.length() - 1).trim();
                    maxAge = maxAge.substring(maxAge.indexOf("=") + 1, maxAge
                            .length());
                    cookie.expires = System.currentTimeMillis() + Long
                            .parseLong(maxAge) * 1000;
                } else {
                    cookie.expires = System.currentTimeMillis() + 60l * 20 *
                            1000;
                }
            }

            Matcher patcherMather = Pattern.compile("(?i)path=.+?;").matcher
                    (cookieString);
            if (patcherMather.find()) {
                String path = patcherMather.group();
                path = path.substring(0, path.length() - 1).trim();
                path = path.substring(path.indexOf("=") + 1, path.length());
                if (!path.endsWith("/")) {
                    path = path + "/";
                }
                cookie.path = path;
            }

            Matcher domainMatcher = Pattern.compile("(?i)domain=.+?;")
                    .matcher(cookieString);
            if (domainMatcher.find()) {
                cookie.domain = domainMatcher.group();
                cookie.domain = cookie.domain.substring(0, cookie.domain
                        .length() - 1).trim();
                cookie.domain = cookie.domain.substring(cookie.domain.indexOf
                        ("=") + 1, cookie.domain.length());
            } else {
                cookie.domain = Url.extractDomain(connection.getURL()
                        .toString());
                if (cookie.domain.contains(":")) {
                    cookie.domain = cookie.domain.substring(0, cookie.domain
                            .indexOf(":"));
                }
            }

            Hashtable<String, Hashtable<String, Cookie>> pathCookieTable;
            Hashtable<String, Cookie> nameCookieTable;
            if ((pathCookieTable = cookieTable.get(cookie.domain)) == null) {
                synchronized (cookieTableLock) {
                    if ((pathCookieTable = cookieTable.get(cookie.domain)) ==
                            null) {
                        pathCookieTable = new Hashtable<>();
                        cookieTable.put(cookie.domain, pathCookieTable);
                    }
                }
            }
            if ((nameCookieTable = pathCookieTable.get(cookie.path)) == null) {
                synchronized (cookieTableLock) {
                    if ((nameCookieTable = pathCookieTable.get(cookie.path))
                            == null) {
                        nameCookieTable = new Hashtable<>();
                        pathCookieTable.put(cookie.path, nameCookieTable);
                    }
                }
            }
            nameCookieTable.put(cookie.name, cookie);
        }
    }

    /**
     * 发送cookie给服务器。
     *
     * @param connection URLConnection。
     */
    public void sendCookie(URLConnection connection) {
        String cookie = "";
        URL url = connection.getURL();
        String urlString = url.toString();
        String domain = Url.extractDomain(urlString);

        if (cookieTable.containsKey(domain)) {
            cookie = cookieString(domain, url);
        }

        domain = domain.substring(domain.indexOf("."));
        if (cookieTable.containsKey(domain)) {
            cookie = cookie + cookieString(domain, url);
        }

        if (cookie.length() != 0) {
            cookie = cookie.substring(0, cookie.length() - 1);
        }
        connection.setRequestProperty("Cookie", cookie);
    }

    // **************** 私有方法

    /**
     * 获得cookie字符串。
     *
     * @param domain Domain。
     * @param url    Url。
     * @return cookie字符串。
     */
    private String cookieString(String domain, URL url) {
        StringBuffer cookieString = new StringBuffer();
        Hashtable<String, Hashtable<String, Cookie>> domainTable =
                cookieTable.get(domain);

        domainTable.keySet().stream().forEach(path -> {
            if (matchPath(url.getPath(), path)) {
                Hashtable<String, Cookie> pathTable = domainTable.get(path);
                pathTable.keySet().stream().forEach(name -> {
                    Cookie cookie = pathTable.get(name);
                    if (System.currentTimeMillis() - cookie.expires <= 0) {
                        cookieString.append(cookie.name);
                        cookieString.append("=\"");
                        cookieString.append(cookie.value);
                        cookieString.append("\";");
                    }
                });
            }
        });

        return cookieString.toString();
    }

    /**
     * 比较path。
     *
     * @param urlPath    url的path。
     * @param cookiePath cookie的path。
     * @return Path是否匹配。
     */
    private boolean matchPath(String urlPath, String cookiePath) {
        // TODO /a may match /abc which is wrong 以/结束应该不会 出现这种问题
        if ("/".equals(cookiePath)) {
            return true;
        } else if (urlPath.regionMatches(0, cookiePath, 0, cookiePath.length
                ())) {
            return true;
        }

        return false;
    }

    /**
     * 私有的构造函数。
     */
    private HttpCookieManager() {
    }

}

/**
 * Cookie实体类。
 *
 * @author Liu, Xinwei
 */
class Cookie {

    // **************** 公开变量

    // **************** 私有变量
    /**
     * cookie名。
     */
    public String name;
    /**
     * cookie值。
     */
    public String value;

    /**
     * cookie路径。
     */
    public String path = "/";

    /**
     * cookie域。
     */
    public String domain = "";

    /**
     * cookie过期时间点。
     */
    public long expires;

    // **************** 继承方法

    // **************** 公开方法

    /**
     * 默认构造参数。
     */
    public Cookie() {

    }

    /**
     * 带参数构造函数。
     *
     * @param name  cookie名。
     * @param value cookie值。
     */
    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Cookie [name=" + name + ", value=" + value + ", path=" + path
                + ", domain=" + domain + ", expires=" + expires + "]";
    }

    // **************** 私有方法

}
