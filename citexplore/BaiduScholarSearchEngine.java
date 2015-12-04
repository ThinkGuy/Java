package citexplore.system.scholarsearch;

import citexplore.foundation.util.Html;
import citexplore.foundation.util.Net;
import org.apache.commons.lang.NotImplementedException;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * 百度学术搜索引擎，
 *
 * @author Liu, Xinwei
 */
public class BaiduScholarSearchEngine extends ScholarSearchEngine implements
        SchedulableScholarSearchEngineCall {

    // *****************公开变量

    // *****************私有变量

    /**
     * 搜索结果变量
     */
    private ArrayList<ScholarSearchResult> results = null;

    /**
     * 查询变量
     */
    private ScholarSearchQuery query = null;

    /**
     * 执行搜索
     *
     * @param query 查询
     * @return 搜索结果列表
     */
    @Override
    public ArrayList<ScholarSearchResult> search(ScholarSearchQuery query) {
        // log(query);

        this.query = query;
        results = new ArrayList<>();

        ScholarSearchEngineCallScheduler scheduler = new
                ScholarSearchEngineCallScheduler();
        scheduler.start = query.start;
        scheduler.returns = query.returns;
        scheduler.searchEngineReturns = 10;
        scheduler.searchEngineCall = this;
        scheduler.startStartSchedule();

        return results;
    }

    /**
     * 搜索引擎调用调度器回调，用于执行搜索结果提取。
     *
     * @param searchEngineStart   要求搜索引擎从第几条结果开始返回。
     * @param searchEngineReturns 要求搜索引擎返回多少条结果。
     * @param searchResultStart   返回的搜索结果中从第几条开始为有效结果。
     * @param searchResultReturns 返回的搜索结果中应该取回多少条搜索结果。
     */
    //搜索v时Discute as metodologias d显示为di
    //result.preview中显示正常，这边代码感觉没问题。
    @Override
    public void scheduledStartCall(int searchEngineStart, int
            searchEngineReturns, int searchResultStart, int
            searchResultReturns) {
        String url;
        if (query.type == ScholarSearchType.KEYWORD) {
            url = "http://xueshu.baidu.com/s?wd=" + URLEncoder.encode(query
                    .query) + "&pn=" + searchEngineStart;

            if (query.since >= 0) {
                url = url + "&filter=sc_year%3D{" + query.since + "%2C%2B}";
            }

            if (query.order == ScholarSearchOrderBy.DATE) {
                url = url + "&sort=sc_time";
            }

            if (query.order == ScholarSearchOrderBy.CITED) {
                url = url + "&sort=sc_cited";
            }

        } else {
            url = "http://xueshu.baidu.com/s?" + query.citeQueryParameter +
                    "&sort=sc_cited";
        }

        String html = Net.fetchStringFromUrlSafely(url);

        for (int i = 0; i < 10; i++) {
            if (!html.contains("<div class=\"sc_content\">")) {
                break;
            }

            html = html.substring(html.indexOf("<div class=\"sc_content\">"));

            String directUrl = "";

            ScholarSearchResult result = new ScholarSearchResult();

            html = html.substring(html.indexOf("<h3 class=\"t c_font\">"));
            String tcFont = html.substring(0, html.indexOf("</h3>") + 5);
            if (tcFont.indexOf("<a") > 0) {
                tcFont = tcFont.substring(tcFont.indexOf("<a"));
                tcFont = tcFont.substring(tcFont.indexOf(">") + 1);
                tcFont = tcFont.substring(0, tcFont.indexOf("</a>"));
                result.title = tcFont.trim();
            } else if (tcFont.indexOf("<span class=\"untap\"") > 0) {
                tcFont = tcFont.substring(tcFont.indexOf("<span " +
                        "class=\"untap\""));
                tcFont = tcFont.substring(tcFont.indexOf(">") + 1);
                tcFont = tcFont.substring(0, tcFont.indexOf("</span>"));
                result.title = tcFont.trim();
            }

            html = html.substring(html.indexOf("</h3>") + 5);

            if (html.contains("<div class=\"sc_info\">") && (!html.contains
                    ("<h3 class=\"t c_font\">") || html.indexOf("<div class="
                    + "\"sc_info\">") < html.indexOf("<h3 class=\"t c" +
                    "_font\">"))) {
                html = html.substring(html.indexOf("<div class=\"sc_info\">"));
                String scInfo = Html.cleanHtml(html.substring(0, html.indexOf
                        ("</div>") + 6));

                String[] scInfoSplit = scInfo.split("-");

                if (scInfoSplit[scInfoSplit.length - 2].matches("\\d*")) {
                    result.year = Integer.parseInt(scInfoSplit[scInfoSplit.length - 2].trim());
                }

                if (scInfoSplit.length >= 3) {
                    String[] authors = scInfoSplit[0].split("，");
                    for (String author : authors) {
                        result.authors.add(author.trim());
                    }
                }

                if (scInfoSplit.length >= 4) {
                    result.source = scInfoSplit[1].trim();
                }

                String scCite = html.substring(html.indexOf("<span " +
                        "class=\"\">"), html.indexOf("</div>") + 6);

                // indexof有问题，/s之后的
                scCite = scCite.substring(scCite.indexOf("/s?") + 3);
                result.citeQueryParameter = scCite.substring(0, scCite.indexOf("&")).trim();
                String citeBy = "";

                if (scCite.contains("sc_cite_cont")) {
                    scCite = scCite.substring(scCite.indexOf("}\">") + 3);
                    citeBy = scCite.substring(0, scCite.indexOf("</a>"));

                } else {
                    citeBy = scCite.substring(scCite.indexOf("被引量") + 10,
                            scCite.indexOf("</span>"));
                }

                if (citeBy.contains("万")) {
                    citeBy = citeBy.substring(0, citeBy.indexOf("万"));

                    if (citeBy.contains(".")) {
                        citeBy = citeBy.substring(0, citeBy.indexOf(".")) +
                                citeBy.substring(citeBy.indexOf(".") + 1);
                        citeBy = citeBy + "000";
                    } else {
                        citeBy = citeBy + "0000";
                    }
                }
                result.citedBy = Integer.parseInt(citeBy.trim());

                html = html.substring(html.indexOf("</div>") + 6);
            }

            if (html.contains("<div class=\"c_abstract\">") && (!html
                    .contains("<h3 class=\"t c_font\">") || html.indexOf
                    ("<div" + " class=\"" + "c_abstract\">") < html.indexOf
                    ("<h3 " + "class=\"t c_font\">"))) {
                html = html.substring(html.indexOf("<div class=\"c_abstra" +
                        "ct\">"));
                result.preview = html.substring(24, html.indexOf("<div " + "class=\"sc_allversion\"")).trim();

                if (html.contains("<div class=\"sc_allversion\">") && (!html
                        .contains("<h3 class=\"t c_font\">") || html.indexOf
                        ("<div class=\"sc_allversion\">") < html.indexOf("<h3" +
                        " " +
                        "" + "class=\"t c_font\">"))) {
                    html = html.substring(html.indexOf("<div " +
                            "class=\"sc_allversion\">"));
                    String versionsHtml = html.substring(0, html.indexOf
                            ("</div>") + 6);

                    while (versionsHtml.contains("<span class=\"v_icon\">")) {
                        versionsHtml = versionsHtml.substring(versionsHtml
                                .indexOf("<span class=\"v_icon\">"));
                        String vIcon = versionsHtml.substring(0, versionsHtml
                                .indexOf("</a>") + 4);

                        if (vIcon.contains("<a href=\"javascript:")) {
                            versionsHtml = versionsHtml.substring
                                    (versionsHtml.indexOf("<p " +
                                            "class=\"saveurl\"") + 19);
                            directUrl = versionsHtml.substring(0,
                                    versionsHtml.indexOf("</p>"));
                        } else {
                            versionsHtml = versionsHtml.substring
                                    (versionsHtml.indexOf("href=\"") + 6);
                            directUrl = versionsHtml.substring(0,
                                    versionsHtml.indexOf("\""));
                        }

                        if (!"".equals(directUrl)) {
                            result.urls.add(directUrl);
                        }

                        versionsHtml = versionsHtml.substring(versionsHtml
                                .indexOf("</span>") + 7);
                    }

                }

                html = html.substring(html.indexOf("</div>") + 6);
            }

            if (i >= searchResultStart && i - searchResultStart <
                    searchResultReturns) {
                results.add(result);
            }
        }
    }

    /**
     * 搜索引擎调用调度器回调，用于执行搜索结果提取，基于起始分页。
     *
     * @param searchEnginePage    要求搜索引擎从第几页结果开始返回。
     * @param searchEngineReturns 要求搜索引擎返回多少条结果。
     * @param searchResultStart   返回的搜索结果中从第几条开始为有效结果。
     * @param searchResultReturns 返回的搜索结果中应该取回多少条搜索结果。
     */
    @Override
    public void scheduledPageCall(int searchEnginePage, int
            searchEngineReturns, int searchResultStart, int
            searchResultReturns) {
        throw new NotImplementedException();
    }

    public static void main(String[] args) {
        ScholarSearchQuery query = new ScholarSearchQuery();
<<<<<<< .mine
        query.query = "Java";
        query.order = ScholarSearchOrderBy.CITED;
        query.returns = 9;
        query.since = 1990;
        query.start = 0;
        query.to = 2015;
//        query.citeQueryParameter = "wd=refpaperuri:" + "" +
//                "(c6e5cb46ac0c57dae8c417f857315f48))";
=======
//        query.order = ScholarSearchOrderBy.CITED;
        query.query = "dfs";
        query.returns = 9;
        query.since = 1990;
        query.start = 0;
        query.to = 2015;
        // query.citeQueryParameter = "wd=refpaperuri:" + "" +
        // "(c6e5cb46ac0c57dae8c417f857315f48))";
>>>>>>> .r2430
        BaiduScholarSearchEngine engine = new BaiduScholarSearchEngine();
        ArrayList<ScholarSearchResult> results = engine.search(query);
        System.out.println(results.size());
    }

}
