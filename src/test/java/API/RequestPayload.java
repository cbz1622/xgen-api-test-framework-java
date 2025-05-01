package API;

public class RequestPayload {
    private String url;
    private String pageSource;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPageSource() {
        return pageSource;
    }

    public void setPageSource(String pageSource) {
        this.pageSource = pageSource;
    }

    public RequestPayload(String url, String pageSource) {
        this.url = url;
        this.pageSource = pageSource;
    }
}
