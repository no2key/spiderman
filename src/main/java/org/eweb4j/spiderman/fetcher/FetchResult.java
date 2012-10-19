package org.eweb4j.spiderman.fetcher;

public class FetchResult {

	protected int statusCode;
	protected String fetchedUrl = null;
	protected String movedToUrl = null;
	protected Page page = null;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getFetchedUrl() {
		return fetchedUrl;
	}

	public void setFetchedUrl(String fetchedUrl) {
		this.fetchedUrl = fetchedUrl;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getMovedToUrl() {
		return movedToUrl;
	}

	public void setMovedToUrl(String movedToUrl) {
		this.movedToUrl = movedToUrl;
	}

	@Override
	public String toString() {
		return "FetchResult [statusCode=" + statusCode +", fetchedUrl=" + fetchedUrl + ", movedToUrl=" + movedToUrl + "]";
	}

}
